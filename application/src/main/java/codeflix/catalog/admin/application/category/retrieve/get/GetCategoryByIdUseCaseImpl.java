package codeflix.catalog.admin.application.category.retrieve.get;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;

import java.util.Objects;
import java.util.function.Supplier;

public class GetCategoryByIdUseCaseImpl extends GetCategoryByIdUseCase {

    private final CategoryGateway categoryGateway;

    public GetCategoryByIdUseCaseImpl(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CategoryOutput execute(final String anId) {
        CategoryID anCategoryId = CategoryID.from(anId);
        return categoryGateway.findById(anCategoryId)
                .map(CategoryOutput::from)
                .orElseThrow(notFound(anCategoryId));
    }

    private Supplier<NotFoundException> notFound(final CategoryID anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }
}
