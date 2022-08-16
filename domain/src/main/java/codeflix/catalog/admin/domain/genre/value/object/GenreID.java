package codeflix.catalog.admin.domain.genre.value.object;

import codeflix.catalog.admin.domain._share.value.object.Identifier;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {
    private final String value;

    private GenreID(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static GenreID unique() {
        return GenreID.from(UUID.randomUUID());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId);
    }

    public static GenreID from(final UUID anId) {
        return new GenreID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final GenreID that = (GenreID) o;
        return this.value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.value);
    }
}
