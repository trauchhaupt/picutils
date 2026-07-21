package de.vonrauchhaupt.picutils.model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScannerContextDto {
    private final Path rootPath;
    private final boolean cleanScan;
    private final Map<Path, ImageInformationDto> imageInformationList = new HashMap<>();

    public ScannerContextDto(Path rootPath, boolean cleanScan) {
        if (!Files.isDirectory(rootPath)) {
            throw new IllegalArgumentException("The root path is not a directory:" + rootPath.toAbsolutePath());
        }
        this.rootPath = rootPath;
        this.cleanScan = cleanScan;
    }

    public static List<ImageInformationDto> fromXml(Path inputXml) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        factory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        factory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        factory.setExpandEntityReferences(false);

        Document document = factory.newDocumentBuilder().parse(inputXml.toFile());
        Element root = document.getDocumentElement();

        if (root == null || !"image-info-list".equals(root.getTagName())) {
            throw new IllegalArgumentException("Invalid XML: expected root element <image-info-list>");
        }

        List<ImageInformationDto> result = new ArrayList<>();
        NodeList children = root.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue; // ignore whitespace/text nodes
            }

            Element imageInfoElement = (Element) child;
            if (!"image-info".equals(imageInfoElement.getTagName())) {
                throw new IllegalArgumentException(
                        "Invalid XML: expected child element <image-info>, but found <" + imageInfoElement.getTagName() + ">"
                );
            }

            result.add(new ImageInformationDto(imageInfoElement));
        }

        return result;
    }

    public void loadFromFile() {
        Path fileInfosXml = Paths.get(rootPath.toString(), "fileInfos.xml");
        try {
            if (Files.exists(fileInfosXml)) {
                System.out.println("Loading " + fileInfosXml.toAbsolutePath());
                List<ImageInformationDto> imageInformations = fromXml(fileInfosXml);
                imageInformationList.putAll(imageInformations
                        .stream()
                        .collect(Collectors.toMap(ImageInformationDto::getPath, Function.identity())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not load " + fileInfosXml.toAbsolutePath());
        }
    }

    public void scanRootDirectory() throws Exception {
        if (!cleanScan) {
            loadFromFile();
        }
        System.out.println("Scanning for JPG files");
        List<Path> allJpgFiles;
        try (Stream<Path> paths = Files.walk(rootPath)) {
            allJpgFiles = paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".jpg") || path.toString().toLowerCase().endsWith(".jpeg"))
                    .sorted()
                    .toList();
        }
        int amountJpgFiles = allJpgFiles.size();
        System.out.println("Indexing " + amountJpgFiles + " JPG files.");

        Set<String> directoryEnumsNotKnown = new HashSet<>();
        Set<String> tagEnumsNotKnown = new HashSet<>();
        for (int i = 0; i < amountJpgFiles; i++) {
            Path path = allJpgFiles.get(i);
            if ((i + 1) % 100 == 0) {
                System.out.println("Scanning '" + path + "' with file " + (i + 1) + "/" + amountJpgFiles);
            }
            if ((i + 1) % 300 == 0)
                writeFileInfoXml();
            try {
                ImageInformationDto existingInfo = imageInformationList.get(path);

                if (existingInfo == null)
                    imageInformationList.put(path, new ImageInformationDto(path, directoryEnumsNotKnown, tagEnumsNotKnown));
                else {
                    LocalDateTime modificationDateTime = LocalDateTime.ofInstant(
                            Files.getLastModifiedTime(path).toInstant(),
                            ZoneId.systemDefault()
                    );
                    if (existingInfo.getLastModificationDate().isBefore(modificationDateTime)) {
                        imageInformationList.put(path, new ImageInformationDto(path, directoryEnumsNotKnown, tagEnumsNotKnown));
                    }
                }

            } catch (Exception exception) {
                System.err.println("Error scanning file:" + path.toAbsolutePath() + " -> " + exception.getMessage());
            }
        }

        if (!directoryEnumsNotKnown.isEmpty()) {
            System.err.println("Missing MetadataDictionaryEnum : \n" + String.join("\n", directoryEnumsNotKnown));
        }

        if (!tagEnumsNotKnown.isEmpty()) {
            System.err.println("Missing MetadataTagEnum : \n" + String.join("\n", tagEnumsNotKnown));
        }

        writeFileInfoXml();
        System.out.println("Finished");
    }

    void writeFileInfoXml() {
        Path fileInfosXml = Paths.get(rootPath.toString(), "fileInfos.xml");
        System.out.println("Writing " + fileInfosXml.toAbsolutePath());
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(fileInfosXml, StandardCharsets.UTF_8)) {
            bufferedWriter.write("<image-info-list>\n");
            imageInformationList.values().stream()
                    .sorted(Comparator.naturalOrder())
                    .forEach(info -> {
                        try {
                            bufferedWriter.write(info.toXml());
                            bufferedWriter.write("\n");
                        } catch (IOException e) {
                            throw new RuntimeException("Could not write line to " + fileInfosXml.toAbsolutePath(), e);
                        }
                    });
            bufferedWriter.write("</image-info-list>");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Could not write to file " + fileInfosXml.toAbsolutePath());
        }
    }

    public Set<ImageInformationDto> getImageInformationSet() {
        return new HashSet<>(imageInformationList.values());
    }
}
