package codeflix.catalog.admin.application.castmember.retrieve.list;

import codeflix.catalog.admin.domain.castmember.entity.CastMember;
import codeflix.catalog.admin.domain.castmember.enums.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt
) {
    public static CastMemberListOutput from(final CastMember aMember) {
        return new CastMemberListOutput(
                aMember.getId().getValue(),
                aMember.getName(),
                aMember.getType(),
                aMember.getCreatedAt()
        );
    }
}
