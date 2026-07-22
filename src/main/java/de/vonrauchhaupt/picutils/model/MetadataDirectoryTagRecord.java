package de.vonrauchhaupt.picutils.model;

import java.util.Comparator;
import java.util.Objects;

public record MetadataDirectoryTagRecord(MetadataDictionaryEnum directory,
                                         MetadataTagEnum tag) implements Comparable<MetadataDirectoryTagRecord> {

    public static Comparator<MetadataDirectoryTagRecord> COMPARATOR =
            Comparator.comparing(MetadataDirectoryTagRecord::directory).
                    thenComparing(MetadataDirectoryTagRecord::tag);

    public MetadataDirectoryTagRecord {
        directory = Objects.requireNonNull(directory, "directory must not be null");
        tag = Objects.requireNonNull(tag, "tag must not be null");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MetadataDirectoryTagRecord that = (MetadataDirectoryTagRecord) o;
        return tag == that.tag && directory == that.directory;
    }

    @Override
    public int hashCode() {
        return Objects.hash(directory, tag);
    }

    @Override
    public int compareTo(MetadataDirectoryTagRecord o) {
        if (o == null)
            return 1;
        return COMPARATOR.compare(this, o);
    }

}
