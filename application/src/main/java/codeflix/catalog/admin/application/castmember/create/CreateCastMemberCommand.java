package codeflix.catalog.admin.application.castmember.create;

import codeflix.catalog.admin.domain.castmember.enums.CastMemberType;

public record CreateCastMemberCommand(
        String name,
        CastMemberType type
) {
    public static CreateCastMemberCommand with(final String aName, final CastMemberType aType) {
        return new CreateCastMemberCommand(aName, aType);
    }
}
