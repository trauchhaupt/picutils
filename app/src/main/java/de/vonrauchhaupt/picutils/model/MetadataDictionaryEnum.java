package de.vonrauchhaupt.picutils.model;

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
    NIKON_MAKERNOTE("Nikon Makernote"),
    KODAK_MAKERNOTE("Kodak Makernote"),
    OLYMPUS_MAKERNOTE("Olympus Makernote"),
    APPLE_RUN_TIME("Apple Run Time"),
    SAMSUNG_MAKERNOTE("Samsung Makernote"),
    GIF_HEADER("GIF Header"),
    FUJIFILM_MAKERNOTE("Fujifilm Makernote"),
    PANASONIC_MAKERNOTE("Panasonic Makernote"),
    CASIO_MAKERNOTE("Casio Makernote"),
    NIKON_PICTURECONTROL_1("Nikon PictureControl 1"),
    APPLE_MAKERNOTE("Apple Makernote"),
    OLYMPUS_FOCUS_INFO("Olympus Focus Info"),
    OLYMPUS_CAMERA_SETTINGS("Olympus Camera Settings"),
    OLYMPUS_RAW_DEVELOPMENT("Olympus Raw Development"),
    OLYMPUS_EQUIPMENT("Olympus Equipment"),
    SONY_9050B("Sony 9050B"),
    BMP_HEADER("BMP Header"),
    OLYMPUS_IMAGE_PROCESSING("Olympus Image Processing"),
    NIKON_PICTURECONTROL_2("Nikon PictureControl 2"),
    GIF_CONTROL("GIF Control"),
    GIF_IMAGE("GIF Image"),
    ADOBE_JPEG("Adobe JPEG"),
    EXIF_THUMBNAIL("Exif Thumbnail"),
    INTEROPERABILITY("Interoperability"),
    SONY_MAKERNOTE("Sony Makernote"),
    PRINTIM("PrintIM"),
    CANON_MAKERNOTE("Canon Makernote"),
    IPTC("IPTC");
    private static final Map<String, MetadataDictionaryEnum> byName = new HashMap<>();
    private final String name;

    MetadataDictionaryEnum(String name) {
        this.name = name;
    }

    public static MetadataDictionaryEnum getByName(String name) {
        if (byName.isEmpty()) {
            for (MetadataDictionaryEnum tag : values()) {
                byName.put(tag.name, tag);
            }
        }
        return byName.get(name);
    }

    public String getName() {
        return name;
    }
}
