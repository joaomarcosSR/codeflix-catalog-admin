package codeflix.catalog.admin.application.category.retrieve.list;

import codeflix.catalog.admin.application._shared.base.UseCase;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;

public abstract class ListCategoriesUseCase
        extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}
