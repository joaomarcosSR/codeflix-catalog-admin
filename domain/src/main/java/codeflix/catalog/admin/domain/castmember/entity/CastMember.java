package codeflix.catalog.admin.domain.castmember.entity;

import codeflix.catalog.admin.domain._share.entity.AggregateRoot;
import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain._share.utils.InstantUtils;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.castmember.enums.CastMemberType;
import codeflix.catalog.admin.domain.castmember.validation.CastMemberValidator;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;

import java.time.Instant;

public class CastMember extends AggregateRoot<CastMemberID> {
    private String name;
    private CastMemberType type;
    private final Instant createdAt;
    private Instant updatedAt;

    protected CastMember(
            final CastMemberID anId,
            final String aName,
            final CastMemberType aType,
            final Instant aCreationDate,
            final Instant aUpdateDate) {
        super(anId);
        this.name = aName;
        this.type = aType;
        this.createdAt = aCreationDate;
        this.updatedAt = aUpdateDate;
        this.selfValidate();
    }

    public static CastMember newMember(final String aName, final CastMemberType aType) {
        final CastMemberID anId = CastMemberID.unique();
        final Instant now = InstantUtils.now();
        return new CastMember(anId, aName, aType, now, now);
    }

    public static CastMember with(final CastMember aMember) {
        return new CastMember(
                aMember.id,
                aMember.name,
                aMember.type,
                aMember.createdAt,
                aMember.updatedAt
        );
    }

    public CastMember update(final String aName, final CastMemberType aType) {
        this.name = aName;
        this.type = aType;
        this.updatedAt = InstantUtils.now();
        this.selfValidate();
        return this;
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new CastMemberValidator(this, aHandler).validate();
    }

    public String getName() {
        return this.name;
    }

    public CastMemberType getType() {
        return this.type;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    private void selfValidate() {
        final Notification notification = Notification.create();
        this.validate(notification);

        if (notification.hasError())
            throw new NotificationException("Failed to create a Aggregate CastMember", notification);
    }
}
