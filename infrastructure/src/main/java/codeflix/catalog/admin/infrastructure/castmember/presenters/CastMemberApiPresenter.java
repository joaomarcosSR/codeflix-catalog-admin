package codeflix.catalog.admin.infrastructure.castmember.presenters;

import codeflix.catalog.admin.application.castmember.retrieve.get.CastMemberOutput;
import codeflix.catalog.admin.application.castmember.retrieve.list.CastMemberListOutput;
import codeflix.catalog.admin.infrastructure.castmember.models.CastMemberListResponse;
import codeflix.catalog.admin.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberApiPresenter {

    static CastMemberResponse present(final CastMemberOutput output) {
        return new CastMemberResponse(
                output.id(),
                output.name(),
                output.type().name(),
                output.createdAt().toString(),
                output.updatedAt().toString()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput output) {
        return new CastMemberListResponse(
                output.id(),
                output.name(),
                output.type().name(),
                output.createdAt().toString()
        );
    }
}
