package codeflix.catalog.admin.domain.category.entity;

import codeflix.catalog.admin.domain._share.entity.AggregateRoot;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain.category.validation.CategoryValidator;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID aId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aDeleteDate
    ) {
        super(aId);
        name = aName;
        description = aDescription;
        active = isActive;
        createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
        deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
        final CategoryID id = CategoryID.unique();
        Instant now = Instant.now();
        Instant deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);
    }

    public static Category with(
            final CategoryID aId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aDeleteDate
    ) {
        return new Category(
                aId,
                aName,
                aDescription,
                isActive,
                aCreationDate,
                aUpdateDate,
                aDeleteDate
        );
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new CategoryValidator(this, aHandler).validate();
    }

    public Category activate() {
        active = true;
        updatedAt = Instant.now();
        deletedAt = null;

        return this;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            deletedAt = Instant.now();
        }
        active = false;
        updatedAt = Instant.now();

        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive) {
        if (isActive) activate();
        else deactivate();
        name = aName;
        description = aDescription;
        updatedAt = Instant.now();

        return this;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
