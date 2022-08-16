package codeflix.catalog.admin.application.category.retrieve.list;

import codeflix.catalog.admin.application._shared.base.UseCase;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain.category.gateway.CategorySearchQuery;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}
