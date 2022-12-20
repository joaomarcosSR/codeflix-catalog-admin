package codeflix.catalog.admin.domain.video;

import codeflix.catalog.admin.domain._share.utils.IdUtils;
import codeflix.catalog.admin.domain._share.value.object.ValueObject;

import java.util.Objects;

public class ImageMedia extends ValueObject {

    private final String id;
    private final String checksum;
    private final String name;
    private final String location;

    private ImageMedia(final String id, final String checksum, final String name, final String location) {
        this.id = Objects.requireNonNull(id);
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.location = Objects.requireNonNull(location);
    }

    public static ImageMedia with(final String checksum, final String name, final String location) {
        return new ImageMedia(IdUtils.uuid(), checksum, name, location);
    }

    public static ImageMedia with(final String id, final String checksum, final String name, final String location) {
        return new ImageMedia(id, checksum, name, location);
    }

    public String id() {
        return this.id;
    }

    public String checksum() {
        return this.checksum;
    }

    public String name() {
        return this.name;
    }

    public String location() {
        return this.location;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final ImageMedia that = (ImageMedia) o;
        return this.checksum.equals(that.checksum) && this.location.equals(that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.checksum, this.location);
    }
}
