package codeflix.catalog.admin.application.category.create;

import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import io.vavr.control.Either;

import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Try;

public class CreateCategoryUseCaseImpl extends CreateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public CreateCategoryUseCaseImpl(CategoryGateway aGateway) {
        categoryGateway = Objects.requireNonNull(aGateway);
    }

    @Override
    public Either<Notification, CreateCategoryOutput> execute(final CreateCategoryCommand aCommand) {
        Category aCategory = Category.newCategory(
                aCommand.name(),
                aCommand.description(),
                aCommand.isActive()
        );

        final Notification notification = Notification.create();
        aCategory.validate(notification);

        return notification.hasError() ? Left(notification) : create(aCategory);
    }

    private Either<Notification, CreateCategoryOutput> create(Category aCategory) {
        return Try(() -> categoryGateway.create(aCategory))
                .toEither()
                .bimap(Notification::create, CreateCategoryOutput::from);
    }
}
