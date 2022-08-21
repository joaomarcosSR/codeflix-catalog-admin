package codeflix.catalog.admin.application.genre.create;

import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.entity.Genre;
import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CreateGenreUseCaseImpl extends CreateGenreUseCase {
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public CreateGenreUseCaseImpl(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }
 
    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
        final String aName = aCommand.name();
        final boolean isActive = aCommand.isActive();
        final List<CategoryID> categories = this.toCategoryID(aCommand.categories());

        final Notification notification = Notification.create();
        notification.append(this.validateCategories(categories));

        final Genre aGenre = notification.validate(() -> Genre.newGenre(aName, isActive));

        if (notification.hasError()) throw new NotificationException("Could not create Aggregate Genre", notification);

        aGenre.addCategories(categories);

        return CreateGenreOutput.from(this.genreGateway.create(aGenre));
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

    private List<CategoryID> toCategoryID(final List<String> categories) {
        return categories.stream()
                .map(CategoryID::from)
                .toList();
    }
}
