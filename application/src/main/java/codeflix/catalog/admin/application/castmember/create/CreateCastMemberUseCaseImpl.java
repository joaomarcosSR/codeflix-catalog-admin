package codeflix.catalog.admin.application.castmember.create;

import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.castmember.entity.CastMember;
import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;

import java.util.Objects;

public non-sealed class CreateCastMemberUseCaseImpl extends CreateCastMemberUseCase {
    private final CastMemberGateway castMemberGateway;

    public CreateCastMemberUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final Notification notification = Notification.create();
        final CastMember aMember = notification.validate(() -> CastMember.newMember(aCommand.name(), aCommand.type()));

        if (notification.hasError())
            this.notify(notification);

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }

    private void notify(final Notification notification) {
        throw new NotificationException("Could nor create Aggregate CastMember", notification);
    }
}
