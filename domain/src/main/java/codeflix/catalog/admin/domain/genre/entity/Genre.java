package codeflix.catalog.admin.domain.genre.entity;

import codeflix.catalog.admin.domain._share.entity.AggregateRoot;
import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain._share.utils.InstantUtils;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.validation.GenreValidator;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreID> {

    private String name;
    private boolean active;
    private List<CategoryID> categories;
    private final Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;


    protected Genre(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        super(anId);
        this.name = aName;
        this.active = isActive;
        this.categories = categories;
        this.createdAt = aCreatedAt;
        this.updatedAt = aUpdatedAt;
        this.deletedAt = aDeletedAt;

        this.selfValidate();
    }

    public static Genre newGenre(final String aName, final boolean isActive) {
        final GenreID anId = GenreID.unique();
        final Instant now = InstantUtils.now();
        final Instant deletedAt = isActive ? null : now;
        return new Genre(anId, aName, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    public static Genre with(
            final GenreID anId,
            final String aName,
            final boolean isActive,
            final List<CategoryID> categories,
            final Instant aCreatedAt,
            final Instant aUpdatedAt,
            final Instant aDeletedAt
    ) {
        return new Genre(anId, aName, isActive, categories, aCreatedAt, aUpdatedAt, aDeletedAt);
    }

    public static Genre with(final Genre aGenre) {
        return new Genre(
                aGenre.id,
                aGenre.name,
                aGenre.active,
                new ArrayList<>(aGenre.categories),
                aGenre.createdAt,
                aGenre.updatedAt,
                aGenre.deletedAt
        );
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new GenreValidator(this, aHandler).validate();
    }

    public Genre update(final String aName, final boolean isActive, final List<CategoryID> categories) {
        if (isActive) this.activate();
        else this.deactivate();
        this.name = aName;
        this.categories = new ArrayList<>(categories != null ? categories : Collections.emptyList());

        this.selfValidate();

        return this;
    }

    public Genre activate() {
        this.active = true;
        this.updatedAt = InstantUtils.now();
        this.deletedAt = null;

        return this;
    }

    public Genre deactivate() {
        if (this.getDeletedAt() == null) this.deletedAt = InstantUtils.now();
        this.active = false;
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre addCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) return this;

        this.categories.add(aCategoryID);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre addCategories(final List<CategoryID> categories) {
        if (categories == null || categories.isEmpty()) return this;

        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Genre removeCategory(final CategoryID aCategoryID) {
        if (aCategoryID == null) return this;

        this.categories.remove(aCategoryID);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public String getName() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }

    public List<CategoryID> getCategories() {
        return Collections.unmodifiableList(this.categories);
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

    private void selfValidate() {
        final Notification notification = Notification.create();
        this.validate(notification);

        if (notification.hasError())
            throw new NotificationException("Failed to create a Aggregate Genre", notification);
    }
}
