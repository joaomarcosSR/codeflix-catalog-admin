package codeflix.catalog.admin.domain.video;

import codeflix.catalog.admin.domain._share.value.object.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {

    private final String value;

    public VideoID(final String value) {
        this.value = Objects.requireNonNull(value);
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
    }

    public static VideoID from(final UUID anId) {
        return new VideoID(anId.toString());
    }

    public static VideoID unique() {
        return VideoID.from(UUID.randomUUID());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final VideoID videoID = (VideoID) o;
        return this.getValue().equals(videoID.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getValue());
    }
}
