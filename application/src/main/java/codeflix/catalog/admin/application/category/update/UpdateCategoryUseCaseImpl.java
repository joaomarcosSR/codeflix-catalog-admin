package codeflix.catalog.admin.application.category.update;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import io.vavr.control.Either;

import java.util.Objects;
import java.util.function.Supplier;

import static io.vavr.API.Try;
import static io.vavr.control.Either.left;

public class UpdateCategoryUseCaseImpl extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public UpdateCategoryUseCaseImpl(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand aCommand) {
        CategoryID anId = CategoryID.from(aCommand.id());
        Category aCategory = categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final Notification notification = Notification.create();

        aCategory
                .update(
                        aCommand.name(),
                        aCommand.description(),
                        aCommand.isActive()
                )
                .validate(notification);

        return notification.hasError() ? left(notification) : update(aCategory);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return Try(() -> categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private Supplier<NotFoundException> notFound(final CategoryID anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }
}
