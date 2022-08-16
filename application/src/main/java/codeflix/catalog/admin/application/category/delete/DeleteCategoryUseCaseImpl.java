package codeflix.catalog.admin.application.category.delete;

import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;

import java.util.Objects;

public class DeleteCategoryUseCaseImpl extends DeleteCategoryUseCase {

    private CategoryGateway categoryGateway;

    public DeleteCategoryUseCaseImpl(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public void execute(final String anId) {
        categoryGateway.deleteById(CategoryID.from(anId));
    }
}
