package de.vonrauchhaupt.picutils.model;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.PerceptiveHash;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class ImageInformationDto implements Comparable<ImageInformationDto> {

    public static final BigInteger ZERO = new BigInteger("0");
    public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    public static PerceptiveHash perceptiveHash = new PerceptiveHash(32);
    private final Path path;
    private final int width;
    private final int height;
    private final Hash pHash;
    private final LocalDateTime lastModificationDate;
    private final Map<MetadataDirectoryTagRecord, String> metadata = new HashMap<>();
    private final XmpInfoDto xmpInfo;
    private boolean exists;

    public ImageInformationDto(Path path, Set<String> directoryEnumsNotKnown, Set<String> tagEnumsNotKnown) throws Exception {
        this.path = path;
        this.lastModificationDate = LocalDateTime.ofInstant(
                Files.getLastModifiedTime(path).toInstant(),
                ZoneId.systemDefault()
        );
        Metadata tmpMetadata = ImageMetadataReader.readMetadata(path.toFile());
        for (Directory directory : tmpMetadata.getDirectories()) {
            MetadataDictionaryEnum dictionaryEnum = MetadataDictionaryEnum.getByName(directory.getName());
            if (dictionaryEnum == null) {
                directoryEnumsNotKnown.add(directory.getName().toUpperCase().replace(' ', '_') + "(\"" + directory.getName() + "\"),");
                continue;
            }
            for (Tag tag : directory.getTags()) {
                if (tag.getTagName().toLowerCase().startsWith("unknown "))
                    continue;
                MetadataTagEnum tagEnum = MetadataTagEnum.getByName(tag.getTagName());
                if (tagEnum == null) {
                    tagEnumsNotKnown.add(tag.getTagName().toUpperCase().replace(' ', '_') + "(\"" + tag.getTagName() + "\"),");
                    continue;
                }
                if (!tagEnum.isIncludeInSummary())
                    continue;

                String tagValue = tag == null ? "" : tag.getDescription();
                tagValue = tagValue.trim();
                tagValue = tagValue.replaceAll("\\s+", " ");
                tagValue = tagValue.trim();

                this.metadata.put(new MetadataDirectoryTagRecord(dictionaryEnum, tagEnum), tagValue);
            }
        }

        xmpInfo = new XmpInfoDto(path);

        BufferedImage image = ImageIO.read(path.toFile());
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.pHash = perceptiveHash.hash(image);
        this.exists = true;
    }

    public ImageInformationDto(Element imageInfoElement) throws Exception {
        if (imageInfoElement == null || !"image-info".equals(imageInfoElement.getTagName())) {
            throw new IllegalArgumentException("Invalid XML: expected root element <image-info>");
        }

        this.path = Path.of(getRequiredAttribute(imageInfoElement, "path", "<image-info path=\"\" />"));
        if (!Files.exists(path)) {
            this.exists = false;
            this.lastModificationDate = LocalDateTime.MIN;
            this.width = 0;
            this.height = 0;
            this.pHash = new Hash(ZERO, perceptiveHash.getKeyResolution(), perceptiveHash.algorithmId());
            this.xmpInfo = new XmpInfoDto(path);
            return;
        }
        String imageInfoPath = "<image-info path=\"" + path.toAbsolutePath() + "\">";
        this.exists = true;
        this.lastModificationDate = LocalDateTime.parse(getRequiredAttribute(imageInfoElement, "modified", imageInfoPath), FORMATTER);
        String widthText = getRequiredAttribute(imageInfoElement, "width", imageInfoPath);
        this.width = Integer.parseInt(widthText);
        String heightText = getRequiredAttribute(imageInfoElement, "height", imageInfoPath);
        this.height = Integer.parseInt(heightText);
        String hashAlgorithm = getRequiredAttribute(imageInfoElement, "hash-algorithm", imageInfoPath);
        if (!hashAlgorithm.equals("" + perceptiveHash.algorithmId()))
            throw new IllegalArgumentException("Invalid XML: expected hash-algorithm to be " + perceptiveHash.algorithmId() + " but was " + hashAlgorithm);
        String hashLength = getRequiredAttribute(imageInfoElement, "hash-length", imageInfoPath);
        String hashValue = getRequiredAttribute(imageInfoElement, "hash-value", imageInfoPath);
        this.pHash = new Hash(new BigInteger(hashValue), Integer.parseInt(hashLength), perceptiveHash.algorithmId());

        List<String> xmpPersonTags = new ArrayList<>();
        List<String> xmpLocationTags = new ArrayList<>();
        // deserialize metadata
        NodeList children = imageInfoElement.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);

            if (child.getNodeType() != Node.ELEMENT_NODE) {
                continue; // ignore whitespace/text nodes
            }

            Element childElement = (Element) child;
            if ("metadata".equals(childElement.getTagName())) {

                String dictionaryName = getRequiredAttribute(childElement, "dictionary", imageInfoPath + "<metadata dictionary=\"\">");
                String metadatagPath = imageInfoPath + "<metadata dictionary=\"" + dictionaryName + "\">";
                String metadataName = getRequiredAttribute(childElement, "name", metadatagPath);
                String metadataValue = getOptionalAttribute(childElement, "value", metadatagPath);

                MetadataDictionaryEnum dictionaryEnum = MetadataDictionaryEnum.valueOf(dictionaryName);
                if (dictionaryEnum == null) {
                    throw new IllegalArgumentException("Invalid XML: unknown dictionary: " + dictionaryName + "on metadata of " + path);
                }
                MetadataTagEnum tagEnum = MetadataTagEnum.valueOf(metadataName);
                if (tagEnum == null) {
                    throw new IllegalArgumentException("Invalid XML: unknown tag: " + dictionaryName + "on metadata of " + path);
                }

                this.metadata.put(new MetadataDirectoryTagRecord(dictionaryEnum, tagEnum), metadataValue);
            } else if ("xmp-tag".equals(childElement.getTagName())) {
                String tagName = getRequiredAttribute(childElement, "name", imageInfoPath + "<xmp-tag name=\"\">").trim();
                if (tagName.startsWith("person|"))
                    xmpPersonTags.add(tagName);
                else if (tagName.startsWith("location|"))
                    xmpLocationTags.add(tagName);
                else
                    System.err.println("Unknown XMP tag category for " + imageInfoPath + "<xmp-tag name=\"" + tagName + "\", it is neither location, nor person.>");

            } else {
                throw new IllegalArgumentException(
                        "Invalid XML: expected child element <metadata> or <xmp-tag>, but found <" + childElement.getTagName() + ">"
                );
            }
        }
        this.xmpInfo = new XmpInfoDto(path, xmpPersonTags, xmpLocationTags);
    }

    private static String xmlAttributeEscape(String value) {
        return value.replace("&", "&amp;").replace("\"", "&quot;");
    }

    private static String getRequiredAttribute(Element parent, String attributeName, String path) {
        String attribute = parent.getAttribute(attributeName);
        if (attribute == null || attribute.isBlank()) {
            throw new IllegalArgumentException("Missing required XML attribute: " + path + ".attributeName");
        }
        return attribute.trim();
    }

    private static String getOptionalAttribute(Element parent, String attributeName, String path) {
        String attribute = parent.getAttribute(attributeName);
        if (attribute == null || attribute.isBlank()) {
            return "";
        }
        return attribute.trim();
    }

    public LocalDateTime getMinDateFromPath() {
        Path curPath = path;
        while (curPath != null) {
            if (!Files.exists(curPath)) {
                return null;
            }
            String directoryName = curPath.getFileName().toString();

            if (directoryName.matches("\\d{4}_\\d{2}_\\d{2}_.*")) {
                String[] parts = directoryName.split("_");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);

                int day = Integer.parseInt(parts[2]);
                if (month < 1) {
                    month = 1;
                    day = 1;
                } else if (day < 1) {
                    day = 1;
                }
                return LocalDateTime.of(year, month, day, 0, 0, 0, 0);
            }
            if (directoryName.matches("\\d{4}")) {
                return LocalDateTime.of(Integer.parseInt(directoryName), 1, 1, 0, 0, 0, 0);
            }
            curPath = curPath.getParent();
        }
        return null;
    }

    public LocalDateTime getDateFromExif()
    {
        LocalDateTime returnValue = getDateFrom(MetadataTagEnum.DATE_TIME);
        if (returnValue != null)
            return returnValue;
        returnValue = getDateFrom(MetadataTagEnum.DATE_TIME_ORIGINAL);
        if (returnValue != null)
            return returnValue;
        String dateTimeString = getMetadata(MetadataTagEnum.DATE_TIME);
        if ( dateTimeString == null || dateTimeString.isBlank())
            return null;
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }

    public LocalDateTime getDateFrom(MetadataTagEnum tagEnum)
    {
        String dateTimeString = getMetadata(tagEnum);
        if ( dateTimeString == null || dateTimeString.isBlank())
            return null;
        return LocalDateTime.parse(dateTimeString, FORMATTER);
    }

    public LocalDateTime getMaxDateFromPath() {
        Path curPath = path;
        while (curPath != null) {
            if (!Files.exists(curPath)) {
                return null;
            }
            String directoryName = curPath.getFileName().toString();

            if (directoryName.matches("\\d{4}_\\d{2}_\\d{2}_.*")) {
                String[] parts = directoryName.split("_");
                int year = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int day = Integer.parseInt(parts[2]);
                if (month < 1) {
                    month = 12;
                    day = 31;
                } else if (day < 1) {
                    month = month + 1;
                    day = 1;
                    LocalDateTime nextDay = LocalDateTime.of(year, month, day, 23, 59, 59);
                    return nextDay.minusDays(1);
                }
                return LocalDateTime.of(year, month, day, 23, 59, 59);
            }
            if (directoryName.matches("\\d{4}")) {
                return LocalDateTime.of(Integer.parseInt(directoryName), 1, 1, 0, 0, 0, 0);
            }
            curPath = curPath.getParent();
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ImageInformationDto that = (ImageInformationDto) o;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return pHash.hashCode();
    }

    @Override
    public int compareTo(ImageInformationDto o) {
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
        for (String xmpPersonTag : xmpInfo.getXmpPersonTags()) {
            xml.append("         <xmp-tag name=\"").append(xmpPersonTag + "\" />\n");
        }
        for (String xmpPersonTag : xmpInfo.getXmpLocationTags()) {
            xml.append("         <xmp-tag name=\"").append(xmpPersonTag + "\" />\n");
        }

        xml.append("   </image-info>\n");
        return xml.toString();
    }
}
