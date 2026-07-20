package de.vonrauchhaupt.picutils.model;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class XmpInfoDto {
    private static String NAMESPACE_LIGHTROOM = "http://ns.adobe.com/lightroom/1.0/";
    private static String NAMESPACE_RDF = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
    private final Path xmpFile;
    private final List<String> xmpPersonTags;
    private final List<String> xmpLocationTags;

    public XmpInfoDto(Path imageFile,
                      List<String> xmpPersonTags,
                      List<String> xmpLocationTags) {
        this.xmpFile = Paths.get(imageFile.toAbsolutePath() + ".xmp");
        this.xmpPersonTags = xmpPersonTags;
        this.xmpLocationTags = xmpLocationTags;
    }

    public XmpInfoDto(Path imageFile) {
        this.xmpFile = Paths.get(imageFile.toAbsolutePath() + ".xmp");
        this.xmpPersonTags = new ArrayList<>();
        this.xmpLocationTags = new ArrayList<>();
        if (Files.exists(xmpFile)) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                dbFactory.setNamespaceAware(true); // Crucial for XMP/RDF namespaces
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(xmpFile.toFile());
                doc.getDocumentElement().normalize();

                // Get all array items (<rdf:li>) for tags/subjects
                NodeList liList = doc.getElementsByTagNameNS(NAMESPACE_RDF, "li");
                for (int i = 0; i < liList.getLength(); i++) {
                    Node liNode = liList.item(i);
                    if (liNode.getTextContent() == null)
                        continue;
                    String tag = liNode.getTextContent().trim();
                    if (tag.startsWith("person|"))
                        xmpPersonTags.add(tag);
                    if (tag.startsWith("location|"))
                        xmpLocationTags.add(tag);
                }
            } catch (Exception e) {
                System.err.println("Could not read xmp metadata of file " + xmpFile.toAbsolutePath() + " because of " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public Path getXmpFile() {
        return xmpFile;
    }

    public List<String> getXmpPersonTags() {
        return xmpPersonTags;
    }

    public List<String> getXmpLocationTags() {
        return xmpLocationTags;
    }
}
