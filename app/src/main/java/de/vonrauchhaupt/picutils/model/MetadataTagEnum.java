package de.vonrauchhaupt.picutils.model;

import java.util.HashMap;
import java.util.Map;

public enum MetadataTagEnum {
    IMAGE_WIDTH("Image Width"), // Pixel width of the image.
    IMAGE_HEIGHT("Image Height"), // Pixel height of the image.
    EXIF_IMAGE_WIDTH("Exif Image Width"), // Width from EXIF dimension fields.
    EXIF_IMAGE_HEIGHT("Exif Image Height"), // Height from EXIF dimension fields.
    PROFILE_DATE_TIME("Profile Date/Time", true), // ICC profile creation timestamp.
    LAST_MODIFICATION_TIME("Last Modification Time", true), // Last modification time recorded in metadata.
    FILE_NAME("File Name"), // Source file name.
    FILE_SIZE("File Size"), // File size as reported by metadata extractor.
    FILE_MODIFIED_DATE("File Modified Date", true), // Filesystem modified timestamp string.
    UNIT_SPECIFIER("Unit Specifier"), // Unit for density values (e.g., metres).
    PIXELS_PER_UNIT_X("Pixels Per Unit X"), // Horizontal pixel density.
    PIXELS_PER_UNIT_Y("Pixels Per Unit Y"), // Vertical pixel density.
    RESOLUTION_UNITS("Resolution Units"), // JFIF resolution unit.
    RESOLUTION_UNIT("Resolution Unit"), // EXIF resolution unit.
    X_RESOLUTION("X Resolution"), // Horizontal resolution value.
    Y_RESOLUTION("Y Resolution"), // Vertical resolution value.
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
    JPEG_QUALITY("JPEG Quality"),
    URL_LIST("URL List",true),
    TIME_CREATED("Time Created", true),

    AEB_BRACKET_VALUE("AEB Bracket Value"),
    AE_SETTING("AE Setting"),
    AF_INFO_ARRAY_2("AF Info Array 2"),
    AF_POINTS_IN_FOCUS("AF Points in Focus"),
    AF_POINT_SELECTED("AF Point Selected"),
    APERTURE_VALUE("Aperture Value"),
    APPLICATION_RECORD_VERSION("Application Record Version"), // IPTC application record version.
    ARTIST("Artist"),
    ASPECT_INFORMATION_ARRAY("Aspect Information Array"),
    AUTO_EXPOSURE_BRACKETING("Auto Exposure Bracketing"),
    AUTO_ISO("Auto ISO"),
    AUTO_ROTATE("Auto Rotate"),
    BACKGROUND_COLOR("Background Color"), // Suggested background color.
    BASE_ISO("Base ISO"),
    BITS_PER_SAMPLE("Bits Per Sample"), // Bit depth per channel/sample.
    BLUE_COLORANT("Blue Colorant"), // Blue primary colorant coordinates.
    BLUE_TRC("Blue TRC"), // Blue tone response curve definition.
    BRIGHTNESS_VALUE("Brightness Value"),
    BULB_DURATION("Bulb Duration"),
    BY_LINE("By-line"), // Creator/author credit line.
    BY_LINE_TITLE("By-line Title"), // Creator role/title.
    CAMERA_INFO("Camera Info"),
    CAMERA_INFO_ARRAY("Camera Info Array"),
    CAMERA_OWNER_NAME("Camera Owner Name"),
    CAMERA_SETTINGS("Camera Settings"),
    CAMERA_TEMPERATURE("Camera Temperature"),
    CAMERA_TYPE("Camera Type"),
    CANON_MODEL_ID("Canon Model ID"),
    CAPTION_ABSTRACT("Caption/Abstract"), // IPTC caption or abstract text.
    CAPTION_DIGEST("Caption Digest"),
    CAPTION_WRITER_EDITOR("Caption Writer/Editor"),
    CATEGORIES("Categories"),
    CATEGORY("Category"), // IPTC category code.
    CHROMATICITY("Chromaticity"), // Chromaticity tag data.
    CHROMATIC_ADAPTATION("Chromatic Adaptation"), // Chromatic adaptation matrix/metadata.
    CLASS("Class"), // ICC profile class (display, input, etc.).
    CMM_TYPE("CMM Type"), // Color Management Module signature/type.
    CODED_CHARACTER_SET("Coded Character Set"),
    COLOR_COMPENSATION_FILTER("Color Compensation Filter"),
    COLOR_HALFTONING_INFORMATION("Color Halftoning Information"),
    COLOR_MODE("Color Mode"),
    COLOR_SPACE1("Color space"), // Declared color space used by the image/profile.
    COLOR_SPACE2("Color Space"), // Declared color space used by the image/profile.
    COLOR_TEMPERATURE("Color Temperature"),
    COLOR_TONE("Color Tone"),
    COLOR_TRANSFER_FUNCTIONS("Color Transfer Functions"),
    COLOR_TRANSFORM("Color Transform"),
    COLOR_TYPE("Color Type"), // PNG/JPEG color model/type information.
    COMPONENTS_CONFIGURATION("Components Configuration"), // EXIF component layout/configuration.
    COMPONENT_1("Component 1"), // Details for first component/channel.
    COMPONENT_2("Component 2"), // Details for second component/channel.
    COMPONENT_3("Component 3"), // Details for third component/channel.
    COMPRESSED_BITS_PER_PIXEL("Compressed Bits Per Pixel"),
    COMPRESSION("Compression"),
    COMPRESSION_TYPE("Compression Type"), // Compression algorithm/mode used.
    CONTINUOUS_DRIVE_MODE("Continuous Drive Mode"),
    CONTRAST("Contrast"),
    CONTROL_MODE("Control Mode"),
    COPYRIGHT("Copyright"),
    COPYRIGHT_FLAG("Copyright Flag"),
    COPYRIGHT_NOTICE("Copyright Notice"), // IPTC copyright statement.
    COUNTRY_PRIMARY_LOCATION_CODE("Country/Primary Location Code"), // IPTC country code.
    COUNTRY_PRIMARY_LOCATION_NAME("Country/Primary Location Name"), // IPTC country name.
    CREDIT("Credit"), // Credit provider line.
    CUSTOM_RENDERED("Custom Rendered"),
    DATA_PRECISION("Data Precision"), // JPEG sample precision.
    DATE_CREATED("Date Created"), // IPTC creation date.
    DATE_STAMP_MODE("Date Stamp Mode"),
    DATE_TIME_DIGITIZED("Date/Time Digitized"),
    DCT_ENCODE_VERSION("DCT Encode Version"),
    DETECTED_FILE_TYPE_LONG_NAME("Detected File Type Long Name"), // Full detected file type name.
    DETECTED_FILE_TYPE_NAME("Detected File Type Name"), // Short detected file type (e.g., PNG).
    DETECTED_MIME_TYPE("Detected MIME Type"), // Detected MIME type.
    DEVICE_MANUFACTURER("Device manufacturer"),
    DEVICE_MFG_DESCRIPTION("Device Mfg Description"), // Device manufacturer description.
    DEVICE_MODEL_DESCRIPTION("Device Model Description"), // Device model description.
    DIGITAL_ZOOM("Digital Zoom"),
    DIGITAL_ZOOM_RATIO("Digital Zoom Ratio"),
    GAIN_CONTROL("Gain Control"),
    DATA_DUMP("Data Dump"),
    IMAGE_ADJUSTMENT("Image Adjustment"),
    ADAPTER("Adapter"),
    NOISE_REDUCTION("Noise Reduction"),
    PREVIEW_IFD("Preview IFD"),
    SHARPENING("Sharpening"),
    AF_TYPE("AF Type"),
    ISO1("ISO"),
    ISO2("Iso"),
    MANUAL_FOCUS_DISTANCE("Manual Focus Distance"),
    QUALITY_FILE_FORMAT("Quality & File Format"),
    AF_FOCUS_POSITION("AF Focus Position"),
    ISO_MODE("ISO Mode"),
    FLASH_SYNC_MODE("Flash Sync Mode"),
    SUBJECT_DISTANCE_RANGE("Subject Distance Range"),
    DISPLAY_APERTURE("Display Aperture"),
    DYNAMIC_RANGE_OPTIMIZER("Dynamic Range Optimizer"),
    EASY_SHOOTING_MODE("Easy Shooting Mode"),
    EXIF_VERSION("Exif Version"), // EXIF specification version.
    EXPECTED_FILE_NAME_EXTENSION("Expected File Name Extension"), // Expected extension for detected type.
    EXPOSURE_BIAS_VALUE("Exposure Bias Value"),
    EXPOSURE_COMPENSATION("Exposure Compensation"),
    EXPOSURE_MODE("Exposure Mode"),
    EXPOSURE_PROGRAM("Exposure Program"),
    EXPOSURE_TIME("Exposure Time"),
    EXTRA_INFO("Extra Info"),
    FILE_SOURCE("File Source"),
    FILTER_METHOD("Filter Method"), // PNG filter method before compression.
    FIRMWARE_REVISION("Firmware Revision"),
    FIRMWARE_VERSION("Firmware Version"),
    FLAGS_0("Flags 0"),
    FLAGS_1("Flags 1"),
    FLASH("Flash"),
    FLASH_ACTIVITY("Flash Activity"),
    FLASH_DETAILS("Flash Details"),
    FLASH_EXPOSURE_COMPENSATION("Flash Exposure Compensation"),
    FLASH_GUIDE_NUMBER("Flash Guide Number"),
    FLASH_MODE("Flash Mode"),
    FLASH_OUTPUT("Flash Output"),
    FLASH_PIX_VERSION("FlashPix Version"), // FlashPix compatibility version.
    FOCAL_LENGTH("Focal Length"),
    FOCAL_LENGTH_35("Focal Length 35"),
    FOCAL_PLANE_RESOLUTION_UNIT("Focal Plane Resolution Unit"),
    FOCAL_PLANE_X_RESOLUTION("Focal Plane X Resolution"),
    FOCAL_PLANE_Y_RESOLUTION("Focal Plane Y Resolution"),
    FOCAL_UNITS_PER_MM("Focal Units per mm"),
    FOCUS_CONTINUOUS("Focus Continuous"),
    FOCUS_DISTANCE_LOWER("Focus Distance Lower"),
    FOCUS_DISTANCE_UPPER("Focus Distance Upper"),
    FOCUS_INFO("Focus Info"),
    FOCUS_MODE("Focus Mode"),
    FOCUS_TYPE("Focus Type"),
    F_NUMBER1("F Number"),
    F_NUMBER2("F-Number"),
    GLOBAL_ALTITUDE("Global Altitude"),
    GLOBAL_ANGLE("Global Angle"),
    GPS_VERSION_ID("GPS Version ID"),
    GREEN_COLORANT("Green Colorant"), // Green primary colorant coordinates.
    GREEN_TRC("Green TRC"), // Green tone response curve definition.
    GRID_AND_GUIDES_INFORMATION("Grid and Guides Information"),
    HEADLINE("Headline"), // IPTC headline/title.
    HIGH_ISO_NOISE_REDUCTION("High ISO Noise Reduction"),
    ICC_PROFILE_NAME("ICC Profile Name"), // Name/label of embedded ICC profile.
    ICC_UNTAGGED_PROFILE("ICC Untagged Profile"),
    IMAGE_NUMBER("Image Number"),
    IMAGE_QUALITY("Image Quality"),
    IMAGE_SIZE("Image Size"),
    IMAGE_STABILISATION("Image Stabilisation"),
    IMAGE_TYPE("Image Type"),
    IMAGE_UNIQUE_ID("Image Unique ID"),
    INTERLACE_METHOD("Interlace Method"), // Interlacing strategy (e.g., none/Adam7).
    INTEROPERABILITY_INDEX("Interoperability Index"),
    INTEROPERABILITY_VERSION("Interoperability Version"),
    ISO_SPEED_RATINGS("ISO Speed Ratings"),
    LAYERS_GROUP_INFORMATION("Layers Group Information"),
    LAYER_GROUPS_ENABLED_ID("Layer Groups Enabled ID"),
    LAYER_SELECTION_IDS("Layer Selection IDs"),
    LAYER_STATE_INFORMATION("Layer State Information"),
    LENS_ID("Lens ID"),
    LENS_SPEC("Lens Spec"),
    LENS_TYPE("Lens Type"),
    LONG_EXPOSURE_NOISE_REDUCTION("Long Exposure Noise Reduction"),
    LONG_FOCAL_LENGTH("Long Focal Length"),
    MACRO_MODE("Macro Mode"),
    MAKE("Make"), // Camera/scanner manufacturer.
    MANUAL_FLASH_OUTPUT("Manual Flash Output"),
    MAX_APERTURE("Max Aperture"),
    MAX_APERTURE_VALUE("Max Aperture Value"),
    MEASURED_EV("Measured EV"),
    MEASURED_EV_2("Measured EV 2"),
    MEDIA_BLACK_POINT("Media Black Point"),
    MEDIA_WHITE_POINT("Media White Point"), // White point chromaticity in profile.
    METERING_MODE("Metering Mode"),
    MIN_APERTURE("Min Aperture"),
    MODEL("Model"), // Camera/scanner model.
    MY_COLORS("My Colors"),
    ND_FILTER("ND Filter"),
    NUMBER_OF_COMPONENTS("Number of Components"), // Number of image color components/channels.
    NUMBER_OF_TABLES("Number of Tables"), // Number of Huffman tables present.
    OBJECT_NAME("Object Name"), // IPTC object name/slug.
    OPTICAL_ZOOM_CODE("Optical Zoom Code"),
    PADDING("Padding"), // Padding bytes/data block.
    PHOTOMETRIC_INTERPRETATION("Photometric Interpretation"),
    PHOTO_EFFECT("Photo Effect"),
    PIXEL_ASPECT_RATIO("Pixel Aspect Ratio"),
    PRIMARY_PLATFORM("Primary Platform"), // Platform target recorded in ICC profile.
    PRINTIM_VERSION("PrintIM Version"),
    PRINT_FLAGS("Print Flags"),
    PRINT_FLAGS_INFORMATION("Print Flags Information"),
    PRINT_INFO_2("Print Info 2"),
    PRINT_SCALE("Print Scale"),
    PRINT_STYLE("Print Style"),
    PROFILE_CONNECTION_SPACE("Profile Connection Space"), // PCS used by ICC profile (e.g., XYZ/Lab).
    PROFILE_COPYRIGHT("Profile Copyright"), // Copyright notice for profile.
    PROFILE_DESCRIPTION("Profile Description"), // Human-readable profile description.
    PROFILE_SIZE("Profile Size"), // ICC profile size in bytes.
    QUALITY("Quality"),
    RATING("Rating"),
    RECORD_MODE("Record Mode"),
    RED_COLORANT("Red Colorant"), // Red primary colorant coordinates.
    RED_TRC("Red TRC"), // Red tone response curve definition.
    RELATED_IMAGE_HEIGHT("Related Image Height"),
    RELATED_IMAGE_WIDTH("Related Image Width"),
    RENDERING_INTENT("Rendering Intent"),
    RESOLUTION_INFO("Resolution Info"),
    SAMPLES_PER_PIXEL("Samples Per Pixel"),
    SATURATION("Saturation"),
    SCENE_CAPTURE_TYPE("Scene Capture Type"),
    SCENE_MODE("Scene Mode"),
    SCENE_TYPE("Scene Type"),
    SEED_NUMBER("Seed Number"),
    SELF_TIMER_2("Self Timer 2"),
    SELF_TIMER_DELAY("Self Timer Delay"),
    SENSING_METHOD("Sensing Method"),
    SENSITIVITY_TYPE("Sensitivity Type"),
    SEQUENCE_NUMBER("Sequence Number"),
    SHARPNESS("Sharpness"),
    SHORT_FOCAL_LENGTH("Short Focal Length"),
    SHUTTER_SPEED_VALUE("Shutter Speed Value"),
    SIGNATURE("Signature"), // ICC profile signature (e.g., acsp).
    SLICES("Slices"),
    SLOW_SHUTTER("Slow Shutter"),
    SOFTWARE("Software"), // Software used to create/edit image.
    SOURCE("Source"), // Source/origin information.
    SPECIAL_INSTRUCTIONS("Special Instructions"), // Rights/use instructions.
    SPOT_METERING_MODE("Spot Metering Mode"),
    SRAW_QUALITY("SRAW Quality"),
    SUB_SEC_TIME_ORIGINAL("Sub-Sec Time Original"), // Sub-second precision for original timestamp.
    SUPPLEMENTAL_CATEGORY_S("Supplemental Category(s)"), // Additional IPTC categories.
    TAG_COUNT("Tag Count"), // Number of ICC tags in profile.
    TARGET_APERTURE("Target Aperture"),
    TARGET_EXPOSURE_TIME("Target Exposure Time"),
    TELECONVERTER_MODEL("Teleconverter Model"),
    TEXTUAL_DATA("Textual Data"), // Embedded textual payload/comments/raw text blocks.
    THUMBNAIL_DATA("Thumbnail Data"),
    THUMBNAIL_HEIGHT_PIXELS("Thumbnail Height Pixels"), // Embedded thumbnail height.
    THUMBNAIL_IMAGE_VALID_AREA("Thumbnail Image Valid Area"),
    THUMBNAIL_LENGTH("Thumbnail Length"),
    THUMBNAIL_OFFSET("Thumbnail Offset"),
    THUMBNAIL_WIDTH_PIXELS("Thumbnail Width Pixels"), // Embedded thumbnail width.
    UNKNOWN_CAMERA_SETTING_2("Unknown Camera Setting 2"),
    UNKNOWN_CAMERA_SETTING_3("Unknown Camera Setting 3"),
    UNKNOWN_CAMERA_SETTING_7("Unknown Camera Setting 7"),
    USER_COMMENT("User Comment"),
    VERSION("Version"), // Metadata schema/profile version.
    VERSION_INFO("Version Info"),
    VRD_OFFSET("VRD Offset"),
    WHITE_BALANCE("White Balance"),
    WHITE_BALANCE_FINE_TUNE_VALUE("White Balance Fine Tune Value"),
    WHITE_BALANCE_MODE("White Balance Mode"),
    XMP_VALUE_COUNT("XMP Value Count"), // Number of extracted XMP values.
    XYZ_VALUES("XYZ values"), // Profile XYZ tristimulus reference values.
    YCBCR_POSITIONING("YCbCr Positioning"), // YCbCr sampling positioning.
    ZONE_MATCHING("Zone Matching"),
    ZOOM_SOURCE_WIDTH("Zoom Source Width"),
    ZOOM_TARGET_WIDTH("Zoom Target Width")
    ;

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
