package de.vonrauchhaupt.picutils;

import java.util.HashMap;
import java.util.Map;

public enum MetadataTagEnum {
    IMAGE_WIDTH("Image Width", true), // Pixel width of the image.
    IMAGE_HEIGHT("Image Height", true), // Pixel height of the image.
    EXIF_IMAGE_WIDTH("Exif Image Width", true), // Width from EXIF dimension fields.
    EXIF_IMAGE_HEIGHT("Exif Image Height", true), // Height from EXIF dimension fields.
    PROFILE_DATE_TIME("Profile Date/Time", true), // ICC profile creation timestamp.
    LAST_MODIFICATION_TIME("Last Modification Time", true), // Last modification time recorded in metadata.
    FILE_NAME("File Name", true), // Source file name.
    FILE_SIZE("File Size", true), // File size as reported by metadata extractor.
    FILE_MODIFIED_DATE("File Modified Date", true), // Filesystem modified timestamp string.
    UNIT_SPECIFIER("Unit Specifier", true), // Unit for density values (e.g., metres).
    PIXELS_PER_UNIT_X("Pixels Per Unit X", true), // Horizontal pixel density.
    PIXELS_PER_UNIT_Y("Pixels Per Unit Y", true), // Vertical pixel density.
    RESOLUTION_UNITS("Resolution Units", true), // JFIF resolution unit.
    RESOLUTION_UNIT("Resolution Unit", true), // EXIF resolution unit.
    X_RESOLUTION("X Resolution", true), // Horizontal resolution value.
    Y_RESOLUTION("Y Resolution", true), // Vertical resolution value.
    ORIENTATION("Orientation", true), // EXIF orientation flag.
    DATE_TIME("Date/Time", true), // Generic EXIF date/time field.
    DATE_TIME_ORIGINAL("Date/Time Original", true), // Original capture/create timestamp.
    GPS_LATITUDE_REF("GPS Latitude Ref", true), // Latitude hemisphere reference (N/S).
    GPS_LATITUDE("GPS Latitude", true), // Latitude coordinate.
    GPS_LONGITUDE_REF("GPS Longitude Ref", true), // Longitude hemisphere reference (E/W).
    GPS_LONGITUDE("GPS Longitude", true), // Longitude coordinate.
    KEYWORDS("Keywords", true), // IPTC keywords/tags.
    IMAGE_DESCRIPTION("Image Description", true),
    JPEG_COMMENT("JPEG Comment", true),
    JPEG_QUALITY("JPEG Quality", true),
    URL_LIST("URL List",true),
    TIME_CREATED("Time Created", true),

    VERSION("Version"), // Metadata schema/profile version.
    COLOR_SPACE1("Color space"), // Declared color space used by the image/profile.
    COLOR_SPACE2("Color Space"), // Declared color space used by the image/profile.
    XYZ_VALUES("XYZ values"), // Profile XYZ tristimulus reference values.
    BITS_PER_SAMPLE("Bits Per Sample"), // Bit depth per channel/sample.
    COLOR_TYPE("Color Type"), // PNG/JPEG color model/type information.
    COMPRESSION_TYPE("Compression Type"), // Compression algorithm/mode used.
    FILTER_METHOD("Filter Method"), // PNG filter method before compression.
    INTERLACE_METHOD("Interlace Method"), // Interlacing strategy (e.g., none/Adam7).
    PROFILE_SIZE("Profile Size"), // ICC profile size in bytes.
    CMM_TYPE("CMM Type"), // Color Management Module signature/type.
    CLASS("Class"), // ICC profile class (display, input, etc.).
    PROFILE_CONNECTION_SPACE("Profile Connection Space"), // PCS used by ICC profile (e.g., XYZ/Lab).
    SIGNATURE("Signature"), // ICC profile signature (e.g., acsp).
    PRIMARY_PLATFORM("Primary Platform"), // Platform target recorded in ICC profile.
    TAG_COUNT("Tag Count"), // Number of ICC tags in profile.
    PROFILE_DESCRIPTION("Profile Description"), // Human-readable profile description.
    PROFILE_COPYRIGHT("Profile Copyright"), // Copyright notice for profile.
    MEDIA_WHITE_POINT("Media White Point"), // White point chromaticity in profile.
    CHROMATIC_ADAPTATION("Chromatic Adaptation"), // Chromatic adaptation matrix/metadata.
    RED_COLORANT("Red Colorant"), // Red primary colorant coordinates.
    BLUE_COLORANT("Blue Colorant"), // Blue primary colorant coordinates.
    GREEN_COLORANT("Green Colorant"), // Green primary colorant coordinates.
    RED_TRC("Red TRC"), // Red tone response curve definition.
    GREEN_TRC("Green TRC"), // Green tone response curve definition.
    BLUE_TRC("Blue TRC"), // Blue tone response curve definition.
    CHROMATICITY("Chromaticity"), // Chromaticity tag data.
    DEVICE_MFG_DESCRIPTION("Device Mfg Description"), // Device manufacturer description.
    DEVICE_MODEL_DESCRIPTION("Device Model Description"), // Device model description.
    ICC_PROFILE_NAME("ICC Profile Name"), // Name/label of embedded ICC profile.
    XMP_VALUE_COUNT("XMP Value Count"), // Number of extracted XMP values.
    BACKGROUND_COLOR("Background Color"), // Suggested background color.
    DETECTED_FILE_TYPE_NAME("Detected File Type Name"), // Short detected file type (e.g., PNG).
    DETECTED_FILE_TYPE_LONG_NAME("Detected File Type Long Name"), // Full detected file type name.
    DETECTED_MIME_TYPE("Detected MIME Type"), // Detected MIME type.
    EXPECTED_FILE_NAME_EXTENSION("Expected File Name Extension"), // Expected extension for detected type.
    DATA_PRECISION("Data Precision"), // JPEG sample precision.
    NUMBER_OF_COMPONENTS("Number of Components"), // Number of image color components/channels.
    COMPONENT_1("Component 1"), // Details for first component/channel.
    COMPONENT_2("Component 2"), // Details for second component/channel.
    COMPONENT_3("Component 3"), // Details for third component/channel.
    THUMBNAIL_WIDTH_PIXELS("Thumbnail Width Pixels"), // Embedded thumbnail width.
    THUMBNAIL_HEIGHT_PIXELS("Thumbnail Height Pixels"), // Embedded thumbnail height.
    SOFTWARE("Software"), // Software used to create/edit image.
    NUMBER_OF_TABLES("Number of Tables"), // Number of Huffman tables present.
    PADDING("Padding"), // Padding bytes/data block.
    SUB_SEC_TIME_ORIGINAL("Sub-Sec Time Original"), // Sub-second precision for original timestamp.
    YCBCR_POSITIONING("YCbCr Positioning"), // YCbCr sampling positioning.
    EXIF_VERSION("Exif Version"), // EXIF specification version.
    COMPONENTS_CONFIGURATION("Components Configuration"), // EXIF component layout/configuration.
    FLASH_PIX_VERSION("FlashPix Version"), // FlashPix compatibility version.
    MAKE("Make"), // Camera/scanner manufacturer.
    MODEL("Model"), // Camera/scanner model.
    BY_LINE("By-line"), // Creator/author credit line.
    BY_LINE_TITLE("By-line Title"), // Creator role/title.
    CAPTION_ABSTRACT("Caption/Abstract"), // IPTC caption or abstract text.
    CATEGORY("Category"), // IPTC category code.
    COPYRIGHT_NOTICE("Copyright Notice"), // IPTC copyright statement.
    COUNTRY_PRIMARY_LOCATION_CODE("Country/Primary Location Code"), // IPTC country code.
    COUNTRY_PRIMARY_LOCATION_NAME("Country/Primary Location Name"), // IPTC country name.
    CREDIT("Credit"), // Credit provider line.
    DATE_CREATED("Date Created"), // IPTC creation date.
    HEADLINE("Headline"), // IPTC headline/title.
    OBJECT_NAME("Object Name"), // IPTC object name/slug.
    APPLICATION_RECORD_VERSION("Application Record Version"), // IPTC application record version.
    TEXTUAL_DATA("Textual Data"), // Embedded textual payload/comments/raw text blocks.
    SOURCE("Source"), // Source/origin information.
    COPYRIGHT("Copyright"),
    RENDERING_INTENT("Rendering Intent"),
    DEVICE_MANUFACTURER("Device manufacturer"),
    MEDIA_BLACK_POINT("Media Black Point"),
    FLAGS_1("Flags 1"),
    GRID_AND_GUIDES_INFORMATION("Grid and Guides Information"),
    SEED_NUMBER("Seed Number"),
    COLOR_TRANSFORM("Color Transform"),
    COLOR_HALFTONING_INFORMATION("Color Halftoning Information"),
    LAYER_SELECTION_IDS("Layer Selection IDs"),
    PRINT_FLAGS("Print Flags"),
    RESOLUTION_INFO("Resolution Info"),
    CAPTION_DIGEST("Caption Digest"),
    PRINT_INFO_2("Print Info 2"),
    COLOR_TRANSFER_FUNCTIONS("Color Transfer Functions"),
    COMPRESSION("Compression"),
    SLICES("Slices"),
    DCT_ENCODE_VERSION("DCT Encode Version"),
    FLAGS_0("Flags 0"),
    PHOTOMETRIC_INTERPRETATION("Photometric Interpretation"),
    VERSION_INFO("Version Info"),
    THUMBNAIL_LENGTH("Thumbnail Length"),
    THUMBNAIL_DATA("Thumbnail Data"),
    THUMBNAIL_OFFSET("Thumbnail Offset"),
    GLOBAL_ANGLE("Global Angle"),
    PIXEL_ASPECT_RATIO("Pixel Aspect Ratio"),
    ICC_UNTAGGED_PROFILE("ICC Untagged Profile"),
    GLOBAL_ALTITUDE("Global Altitude"),
    PRINT_FLAGS_INFORMATION("Print Flags Information"),
    LAYER_GROUPS_ENABLED_ID("Layer Groups Enabled ID"),
    PRINT_STYLE("Print Style"),
    CODED_CHARACTER_SET("Coded Character Set"),
    LAYERS_GROUP_INFORMATION("Layers Group Information"),
    SAMPLES_PER_PIXEL("Samples Per Pixel"),
    LAYER_STATE_INFORMATION("Layer State Information"),
    PRINT_SCALE("Print Scale"),
    COPYRIGHT_FLAG("Copyright Flag"),
    QUALITY("Quality"),
    UNKNOWN_TAG("Unknown tag (0x000b)"),
    SPECIAL_INSTRUCTIONS("Special Instructions"), // Rights/use instructions.
    SUPPLEMENTAL_CATEGORY_S("Supplemental Category(s)"), // Additional IPTC categories.
    CAPTION_WRITER_EDITOR("Caption Writer/Editor"); // Caption editor/writer credit.

    private final String name;
    private final boolean includeInSummary;
    private static final Map<String, MetadataTagEnum> byName = new HashMap<>();

    MetadataTagEnum(String name) {
        this(name, false);
    }

    MetadataTagEnum(String name, boolean includeInSummary) {
        this.name = name;
        this.includeInSummary = includeInSummary;
    }

    public String getName() {
        return name;
    }

    public boolean isIncludeInSummary() {
        return includeInSummary;
    }

    public static MetadataTagEnum getByName(String name) {
        if (byName.isEmpty()) {
            for (MetadataTagEnum tag : values()) {
                byName.put(tag.name, tag);
            }
        }
        return byName.get(name);
    }
}
