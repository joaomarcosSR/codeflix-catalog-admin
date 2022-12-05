package codeflix.catalog.admin.infrastructure.castmember.models;

import codeflix.catalog.admin.domain.castmember.enums.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}
