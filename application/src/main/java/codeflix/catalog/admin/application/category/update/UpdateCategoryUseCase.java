package codeflix.catalog.admin.application.category.update;

import codeflix.catalog.admin.application._shared.base.UseCase;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}
