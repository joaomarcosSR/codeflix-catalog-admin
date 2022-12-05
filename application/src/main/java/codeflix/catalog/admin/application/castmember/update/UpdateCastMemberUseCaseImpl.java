package codeflix.catalog.admin.application.castmember.update;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.castmember.entity.CastMember;
import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;

import java.util.Objects;
import java.util.function.Supplier;

public non-sealed class UpdateCastMemberUseCaseImpl extends UpdateCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public UpdateCastMemberUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand aCommand) {
        final CastMemberID anId = CastMemberID.from(aCommand.id());

        final CastMember aMember = this.castMemberGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final Notification notification = Notification.create();
        final CastMember updatedMember = notification.validate(() -> aMember.update(aCommand.name(), aCommand.type()));

        if (notification.hasError())
            throw new NotificationException("Could not update Aggregate CastMember %s".formatted(anId.getValue()), notification);

        return UpdateCastMemberOutput.from(this.castMemberGateway.update(updatedMember));
    }

    private static Supplier<NotFoundException> notFound(final CastMemberID anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }
}
