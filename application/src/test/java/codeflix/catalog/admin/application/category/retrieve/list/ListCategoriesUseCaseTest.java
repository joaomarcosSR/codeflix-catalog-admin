package codeflix.catalog.admin.application.category.retrieve.list;

import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListCategoriesUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListCategoriesUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.categoryGateway);
    }

    @Test
    void givenAValidQuery_WhenCallsListCategories_ThenReturnCategories() {
        final List<Category> categories = List.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Series", null, true)
        );

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final int expectedTotalPages = 1;
        final int expectedTotalElements = 2;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";

        final SearchQuery aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<Category> expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotalPages, expectedTotalElements, categories);

        final int expectedItemCount = 2;
        final Pagination<CategoryListOutput> expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(this.categoryGateway.findAll(aQuery)).thenReturn(expectedPagination);

        final Pagination<CategoryListOutput> actualResult = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
    }

    @Test
    void givenAValidQuery_WhenHasNoResult_ThenReturnEmptyCategories() {
        final List<Category> categories = List.of();

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final int expectedTotalPages = 1;
        final int expectedTotalElements = 2;
        final String expectedTerms = "";
        final String expectedSort = "createdAt";
        final String expectedDirection = "asc";

        final SearchQuery aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<Category> expectedPagination = new Pagination<>(expectedPage, expectedPerPage, expectedTotalPages, expectedTotalElements, categories);

        final int expectedItemCount = 0;
        final Pagination<CategoryListOutput> expectedResult = expectedPagination.map(CategoryListOutput::from);

        when(this.categoryGateway.findAll(aQuery)).thenReturn(expectedPagination);

        final Pagination<CategoryListOutput> actualResult = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemCount, actualResult.items().size());
        Assertions.assertEquals(expectedResult, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
    }

    @Test
    void givenAValidQuery_WhenGatewayThrowsException_ThenReturnException() {
        final String expectedErrorMessage = "Gateway error.";

        final SearchQuery aQuery =
                new SearchQuery(0, 10, "", "createdAt", "asc");

        when(this.categoryGateway.findAll(aQuery)).thenThrow(new IllegalStateException(expectedErrorMessage));

        final IllegalStateException actualException =
                Assertions.assertThrows(IllegalStateException.class, () -> this.useCase.execute(aQuery));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
