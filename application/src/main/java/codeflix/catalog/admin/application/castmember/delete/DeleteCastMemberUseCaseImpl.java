package codeflix.catalog.admin.application.castmember.delete;

import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;

import java.util.Objects;

public non-sealed class DeleteCastMemberUseCaseImpl extends DeleteCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public DeleteCastMemberUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public void execute(final String anId) {
        this.castMemberGateway.deleteById(CastMemberID.from(anId));
    }
}
