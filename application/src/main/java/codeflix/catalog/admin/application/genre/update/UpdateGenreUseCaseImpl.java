package codeflix.catalog.admin.application.genre.update;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.entity.Genre;
import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class UpdateGenreUseCaseImpl extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public UpdateGenreUseCaseImpl(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final GenreID anId = GenreID.from(aCommand.id());
        final String aName = aCommand.name();
        final boolean isActive = aCommand.isActive();
        final List<CategoryID> categories = this.toCategoryId(aCommand.categories());

        final Genre aGenre = this.genreGateway.findById(anId)
                .orElseThrow(this.notFound(anId));

        final Notification notification = Notification.create();
        notification.append(this.validateCategories(categories));
        notification.validate(() -> aGenre.update(aName, isActive, categories));

        if (notification.hasError())
            throw new NotificationException("Could not update Aggregate Genre %s".formatted(anId.getValue()), notification);

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    private ValidationHandler validateCategories(final List<CategoryID> ids) {
        final Notification notification = Notification.create();

        if (ids == null || ids.isEmpty()) return notification;

        final List<CategoryID> retrievedIds = this.categoryGateway.existsByIds(ids);

        if (ids.size() != retrievedIds.size()) {
            final List<CategoryID> missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrievedIds);

            final String missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private Supplier<NotFoundException> notFound(final GenreID anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

    private List<CategoryID> toCategoryId(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
