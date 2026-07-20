package de.vonrauchhaupt.picutils;

import java.util.HashMap;
import java.util.Map;

public enum MetadataDictionaryEnum {
    PNG_IHDR("PNG-IHDR"),
    PNG_ZTXT("PNG-zTXt"),
    ICC_PROFILE("ICC Profile"),
    PNG_ICCP("PNG-iCCP"),
    XMP("XMP"),
    PNG_BKGD("PNG-bKGD"),
    PNG_PHYS("PNG-pHYs"),
    PNG_TIME("PNG-tIME"),
    PNG_TEXT("PNG-tEXt"),
    FILE_TYPE("File Type"),
    FILE("File"),
    JPEG("JPEG"),
    JFIF("JFIF"),
    EXIF_IFD0("Exif IFD0"),
    EXIF_SUB_IFD("Exif SubIFD"),
    HUFFMAN("Huffman"),
    GPS("GPS"),
    PHOTOSHOP("Photoshop"),
    JPEG_COMPONENT("JpegComment"),
    DUCKY("Ducky"),
    EXIF_THUMBNAIL("Exif Thumbnail"),
    IPTC("IPTC");
    private static final Map<String, MetadataDictionaryEnum> byName = new HashMap<>();
    private final String name;

    MetadataDictionaryEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static MetadataDictionaryEnum getByName(String name) {
        if (byName.isEmpty()) {
            for (MetadataDictionaryEnum tag : values()) {
                byName.put(tag.name, tag);
            }
        }
        return byName.get(name);
    }
}
