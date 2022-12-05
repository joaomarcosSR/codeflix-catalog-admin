package codeflix.catalog.admin.application.castmember.retrieve.get;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.castmember.entity.CastMember;
import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;

import java.util.Objects;

public non-sealed class GetCastMemberByIdUseCaseImpl extends GetCastMemberByIdUseCase {
    private final CastMemberGateway castMemberGateway;

    public GetCastMemberByIdUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(final String anId) {
        final CastMemberID castMemberID = CastMemberID.from(anId);
        return this.castMemberGateway.findById(castMemberID)
                .map(CastMemberOutput::from)
                .orElseThrow(() -> NotFoundException.with(CastMember.class, castMemberID));
    }
}
