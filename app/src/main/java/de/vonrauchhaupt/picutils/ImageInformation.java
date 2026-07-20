package de.vonrauchhaupt.picutils;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import dev.brachtendorf.jimagehash.hash.Hash;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ImageInformation implements Comparable<ImageInformation> {

    private static final BigInteger ZERO = new BigInteger("0");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final Path path;
    private final int width;
    private final int height;
    private final Hash pHash;
    private final LocalDateTime lastModificationDate;
    private boolean exists;
    private final Map<MetadataDirectoryTagRecord, String> metadata = new HashMap<>();

    public ImageInformation(Path path) throws Exception {
        this.path = path;
        this.lastModificationDate = LocalDateTime.ofInstant(
                Files.getLastModifiedTime(path).toInstant(),
                ZoneId.systemDefault()
        );
        Metadata tmpMetadata = ImageMetadataReader.readMetadata(path.toFile());
        Set<String> missingMetadataEnums = new HashSet<>();
        for (Directory directory : tmpMetadata.getDirectories()) {
            MetadataDictionaryEnum dictionaryEnum = MetadataDictionaryEnum.getByName(directory.getName());
            if (dictionaryEnum == null)
                throw new IllegalArgumentException("Unknown metadata directory: " + directory.getName() + " in image " + path);
            for (Tag tag : directory.getTags()) {
                MetadataTagEnum tagEnum = MetadataTagEnum.getByName(tag.getTagName());
                if (tagEnum == null) {
                    missingMetadataEnums.add(tag.getTagName().toUpperCase().replace(' ', '_') + "(\"" + tag.getTagName() + "\"),");
                    continue;
                }
                if (!tagEnum.isIncludeInSummary())
                    continue;

                this.metadata.put(new MetadataDirectoryTagRecord(dictionaryEnum, tagEnum), tag.getDescription());
            }
        }
        if (!missingMetadataEnums.isEmpty()) {
            System.err.println("Missing MetadataTagEnum : \n" + String.join("\n", missingMetadataEnums));
        }
        BufferedImage image = ImageIO.read(path.toFile());
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pHash = Scanner.pHash.hash(image);
        this.exists = true;
    }

    public ImageInformation(Element imageInfoElement) throws Exception {
        if (imageInfoElement == null || !"image-info".equals(imageInfoElement.getTagName())) {
            throw new IllegalArgumentException("Invalid XML: expected root element <image-info>");
        }

        this.path = Path.of(getRequiredAttribute(imageInfoElement, "path"));
        if (!Files.exists(path)) {
            this.exists = false;
            this.lastModificationDate = LocalDateTime.MIN;
            this.width = 0;
            this.height = 0;
            this.pHash = new Hash(ZERO, Scanner.pHash.getKeyResolution(), Scanner.pHash.algorithmId());
            return;
        }
        this.exists = true;
        this.lastModificationDate = LocalDateTime.parse(getRequiredAttribute(imageInfoElement, "modified"), FORMATTER);
        String widthText = getRequiredAttribute(imageInfoElement, "width");
        this.width = Integer.parseInt(widthText);
        String heightText = getRequiredAttribute(imageInfoElement, "height");
        this.height = Integer.parseInt(heightText);
        String hashAlgorithm = getRequiredAttribute(imageInfoElement, "hash-algorithm");
        if (!hashAlgorithm.equals("" + Scanner.pHash.algorithmId()))
            throw new IllegalArgumentException("Invalid XML: expected hash-algorithm to be " + Scanner.pHash.algorithmId() + " but was " + hashAlgorithm + "");
        String hashLength = getRequiredAttribute(imageInfoElement, "hash-length");
        String hashValue = getRequiredAttribute(imageInfoElement, "hash-value");
        this.pHash = new Hash(new BigInteger(hashValue), Integer.parseInt(hashLength), Scanner.pHash.algorithmId());

        // deserialize metadata
        NodeList children = imageInfoElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue; // ignore whitespace/text nodes
            }

            Element childElement = (Element) child;
            if (!"metadata".equals(childElement.getTagName())) {
                throw new IllegalArgumentException(
                        "Invalid XML: expected child element <metadata>, but found <" + childElement.getTagName() + ">"
                );
            }

            String dictionaryName = getRequiredAttribute(childElement, "dictionary");
            String metadataName = getRequiredAttribute(childElement, "name");
            String metadataValue = getRequiredAttribute(childElement, "value");

            MetadataDictionaryEnum dictionaryEnum = MetadataDictionaryEnum.valueOf(dictionaryName);
            if (dictionaryEnum == null) {
                throw new IllegalArgumentException("Invalid XML: unknown dictionary: " + dictionaryName + "on metadata of " + path);
            }
            MetadataTagEnum tagEnum = MetadataTagEnum.valueOf(metadataName);
            if (tagEnum == null) {
                throw new IllegalArgumentException("Invalid XML: unknown tag: " + dictionaryName + "on metadata of " + path);
            }

            this.metadata.put(new MetadataDirectoryTagRecord(dictionaryEnum, tagEnum), metadataValue);
        }

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ImageInformation that = (ImageInformation) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return pHash.hashCode();
    }

    @Override
    public int compareTo(ImageInformation o) {
        return path.compareTo(o.path);
    }

    public Path getPath() {
        return path;
    }

    public String getMetadata(MetadataTagEnum tag) {
        return metadata.get(tag);
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Hash getpHash() {
        return pHash;
    }

    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public boolean exists() {
        return exists;
    }

    public String toXml() {
        StringBuilder xml = new StringBuilder(1024);
        xml.append("   <image-info ");

        xml.append("width =\"").append(width).append("\" ");
        xml.append("height =\"").append(height).append("\"\n");
        xml.append("          path =\"").append(xmlAttributeEscape(path.toString())).append("\"\n");
        xml.append("          modified=\"").append(xmlAttributeEscape(lastModificationDate.format(FORMATTER))).append("\"\n");
        xml.append("          hash-algorithm=\"").append(xmlAttributeEscape(String.valueOf(pHash.getAlgorithmId()))).append("\" ");
        xml.append("hash-length=\"").append(xmlAttributeEscape(String.valueOf(pHash.getHashValue().bitLength()))).append("\" ");
        xml.append("hash-value=\"").append(xmlAttributeEscape(String.valueOf(pHash.getHashValue()))).append("\">\n");
        for (Map.Entry<MetadataDirectoryTagRecord, String> metaDataEntry : metadata.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByKey())
                .toList()) {
            xml.append("         <metadata dictionary=\"").append(xmlAttributeEscape(metaDataEntry.getKey().directory().name())).append("\" ");
            xml.append("name=\"").append(xmlAttributeEscape(metaDataEntry.getKey().tag().name())).append("\" ");
            xml.append("value=\"").append(xmlAttributeEscape(metaDataEntry.getValue())).append("\" />\n");
        }

        xml.append("   </image-info>\n");
        return xml.toString();
    }

    private static String xmlAttributeEscape(String value) {
        return value.replace("&", "&amp;").replace("\"", "&quot;");
    }

    private static String getRequiredAttribute(Element parent, String attributeName) {
        String attribute = parent.getAttribute(attributeName);
        if (attribute == null || attribute.isBlank()) {
            throw new IllegalArgumentException("Missing required XML attribute: " + attributeName + " on '" + parent.getTagName() + "'");
        }
        return attribute;
    }
}
