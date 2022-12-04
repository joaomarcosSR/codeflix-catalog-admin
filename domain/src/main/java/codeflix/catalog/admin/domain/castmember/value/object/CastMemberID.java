package codeflix.catalog.admin.domain.castmember.value.object;

import codeflix.catalog.admin.domain._share.value.object.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {
    private final String value;

    private CastMemberID(final String anId) {
        Objects.requireNonNull(anId);
        this.value = anId;
    }

    public static CastMemberID unique() {
        return CastMemberID.from(UUID.randomUUID());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId);
    }

    public static CastMemberID from(final UUID anId) {
        return new CastMemberID(anId.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final CastMemberID that = (CastMemberID) o;
        return this.getValue().equals(that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getValue());
    }
}
