package codeflix.catalog.admin.domain.video;

import codeflix.catalog.admin.domain._share.utils.IdUtils;
import codeflix.catalog.admin.domain._share.value.object.ValueObject;

import java.util.Objects;

import static codeflix.catalog.admin.domain.video.MediaStatus.PENDING;

public class AudioVideoMedia extends ValueObject {
    private final String id;
    private final String checksum;
    private final String name;
    private final String rawLocation;
    private final String encodedLocation;
    private final MediaStatus status;

    private AudioVideoMedia(
            final String id,
            final String checksum,
            final String name,
            final String rawLocation,
            final String encodedLocation,
            final MediaStatus status
    ) {
        this.id = Objects.requireNonNull(id);
        this.checksum = Objects.requireNonNull(checksum);
        this.name = Objects.requireNonNull(name);
        this.rawLocation = Objects.requireNonNull(rawLocation);
        this.encodedLocation = Objects.requireNonNull(encodedLocation);
        this.status = Objects.requireNonNull(status);
    }

    public static AudioVideoMedia with(
            final String checksum,
            final String name,
            final String rawLocation
    ) {
        return new AudioVideoMedia(IdUtils.uuid(), checksum, name, rawLocation, "", PENDING);
    }

    public static AudioVideoMedia with(
            final String checksum,
            final String name,
            final String rawLocation,
            final String encodedLocation,
            final MediaStatus status
    ) {
        return new AudioVideoMedia(IdUtils.uuid(), checksum, name, rawLocation, encodedLocation, status);
    }

    public static AudioVideoMedia with(
            final String id,
            final String checksum,
            final String name,
            final String rawLocation,
            final String encodedLocation,
            final MediaStatus status
    ) {
        return new AudioVideoMedia(id, checksum, name, rawLocation, encodedLocation, status);
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

    public String rawLocation() {
        return this.rawLocation;
    }

    public String encodedLocation() {
        return this.encodedLocation;
    }

    public MediaStatus status() {
        return this.status;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final AudioVideoMedia that = (AudioVideoMedia) o;
        return this.checksum.equals(that.checksum) && this.rawLocation.equals(that.rawLocation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.checksum, this.rawLocation);
    }
}
