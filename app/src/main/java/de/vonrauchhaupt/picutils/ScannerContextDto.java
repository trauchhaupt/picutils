package de.vonrauchhaupt.picutils;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
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
    private final Map<Path, ImageInformation> imageInformationList = new HashMap<>();

    public ScannerContextDto(Path rootPath) {
        if (!Files.isDirectory(rootPath)) {
            throw new IllegalArgumentException("The root path is not a directory:" + rootPath.toAbsolutePath());
        }
        this.rootPath = rootPath;
    }

    public void scanRootDirectory() throws Exception {
        Path fileInfosXml = Paths.get(rootPath.toString(), "fileInfos.xml");
        if (Files.exists(fileInfosXml)) {
            List<ImageInformation> imageInformations = fromXml(fileInfosXml);
            imageInformationList.putAll(imageInformations
                    .stream()
                    .collect(Collectors.toMap(ImageInformation::getPath, Function.identity())));
        }

        try (Stream<Path> paths = Files.walk(rootPath)) {
            paths.filter(Files::isRegularFile)
                    .filter(path -> path.toString().toLowerCase().endsWith(".jpg") || path.toString().toLowerCase().endsWith(".jpeg"))
                    .forEach(path -> {
                        try {
                            ImageInformation existingInfo = imageInformationList.get(path);

                            if (existingInfo == null)
                                imageInformationList.put(path, new ImageInformation(path));
                            else {
                                LocalDateTime modificationDateTime = LocalDateTime.ofInstant(
                                        Files.getLastModifiedTime(path).toInstant(),
                                        ZoneId.systemDefault()
                                );
                                if (existingInfo.getLastModificationDate().isBefore(modificationDateTime)) {
                                    imageInformationList.put(path, new ImageInformation(path));
                                }
                            }

                        } catch (Exception exception) {
                            System.err.println("Error scanning file:" + path.toAbsolutePath() + " -> " + exception.getMessage());
                        }
                    });
        } catch (Exception e) {
            System.err.println("Error scanning root directory:" + e.getMessage());
        }

        StringBuilder xml = new StringBuilder(1024);
        xml.append("<image-info-list>");
        imageInformationList.values().stream()
                .sorted(Comparator.naturalOrder())
                .forEach(info -> {
                    xml.append(info.toXml());
                    xml.append("\n");
                });
        xml.append("</image-info-list>");

        Files.writeString(fileInfosXml, xml.toString());
    }

    public static List<ImageInformation> fromXml(Path inputXml) throws Exception {
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

        List<ImageInformation> result = new ArrayList<>();
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

            result.add(new ImageInformation(imageInfoElement));
        }

        return result;


    }


}
