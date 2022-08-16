package codeflix.catalog.admin.application.category.retrieve.list;

import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.gateway.CategorySearchQuery;

import java.util.Objects;

public class ListCategoriesUseCaseImpl extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public ListCategoriesUseCaseImpl(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final CategorySearchQuery aQuery) {
        return categoryGateway.findAll(aQuery)
                .map(CategoryListOutput::from);
    }
}
