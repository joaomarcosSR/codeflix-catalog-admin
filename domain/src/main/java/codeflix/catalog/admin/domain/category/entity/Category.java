package codeflix.catalog.admin.domain.category.entity;

import codeflix.catalog.admin.domain._share.entity.AggregateRoot;
import codeflix.catalog.admin.domain._share.utils.InstantUtils;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain.category.validation.CategoryValidator;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> {

    private String name;
    private String description;
    private boolean active;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aDeleteDate
    ) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
        final CategoryID id = CategoryID.unique();
        final Instant now = InstantUtils.now();
        final Instant deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);
    }

    public static Category with(final Category aCategory) {
        return new Category(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.isActive(),
                aCategory.getCreatedAt(),
                aCategory.getUpdatedAt(),
                aCategory.getDeletedAt()
        );
    }

    public static Category with(
            final CategoryID anId,
            final String aName,
            final String aDescription,
            final boolean isActive,
            final Instant aCreationDate,
            final Instant aUpdateDate,
            final Instant aDeleteDate
    ) {
        return new Category(
                anId,
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
        this.active = true;
        this.updatedAt = InstantUtils.now();
        this.deletedAt = null;

        return this;
    }

    public Category deactivate() {
        if (this.getDeletedAt() == null) this.deletedAt = InstantUtils.now();
        this.active = false;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive) {
        if (isActive) this.activate();
        else this.deactivate();
        this.name = aName;
        this.description = aDescription;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isActive() {
        return this.active;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Instant getDeletedAt() {
        return this.deletedAt;
    }
}
