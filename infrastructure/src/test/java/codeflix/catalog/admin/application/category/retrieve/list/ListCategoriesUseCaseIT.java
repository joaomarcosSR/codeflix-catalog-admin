package codeflix.catalog.admin.application.category.retrieve.list;

import codeflix.catalog.admin.IntegrationTest;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;

import static codeflix.catalog.admin.application.category.utils.CategoryITUtils.save;

@IntegrationTest
class ListCategoriesUseCaseIT {
    @Autowired
    private ListCategoriesUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        save(this.categoryRepository,
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Netflix Originals", "Titulos de autoria da Netflix", true),
                Category.newCategory("Amazon Originals", "Titulos de autoria da Amazon", true),
                Category.newCategory("Documentarios", null, true),
                Category.newCategory("Sports", null, true),
                Category.newCategory("Kids", "Categoria para criancas", true),
                Category.newCategory("Series", null, true)
        );
    }

    @Test
    void givenAValidTerm_WhenDoesNotMatchesPrePersisted_ThenReturnEmptyPage() {
        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final int expectedTotalPages = 0;
        final int expectedItemsCount = 0;
        final int expectedTotalElements = 0;
        final String expectedTerms = "snhdfhsdf dsfbsdnjfk";
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final SearchQuery aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<CategoryListOutput> actualResult = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,1,Filmes",
            "net,0,10,1,1,1,Netflix Originals",
            "ZON,0,10,1,1,1,Amazon Originals",
            "KI,0,10,1,1,1,Kids",
            "criancas,0,10,1,1,1,Kids",
            "da Amazon,0,10,1,1,1,Amazon Originals",
    })
    void givenAValidTerm_WhenCallsListCategories_ThenReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotalPages,
            final int expectedTotalElements,
            final String expectedCategoryName
    ) {
        final String expectedSort = "name";
        final String expectedDirection = "asc";

        final SearchQuery aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<CategoryListOutput> actualResult = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,1,7,Amazon Originals",
            "name,desc,0,10,7,1,7,Sports",
            "createdAt,asc,0,10,7,1,7,Filmes",
            "createdAt,desc,0,10,7,1,7,Series"
    })
    void givenAValidSortAndDirection_WhenCallsListCategories_ThenReturnCategoriesFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotalPages,
            final int expectedTotalElements,
            final String expectedCategoryName
    ) {
        final String expectedTerms = "";

        final SearchQuery aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<CategoryListOutput> actualResult = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,4,7,Amazon Originals;Documentarios",
            "1,2,2,4,7,Filmes;Kids",
            "2,2,2,4,7,Netflix Originals;Series",
            "3,2,1,4,7,Sports",
    })
    void givenAValidPage_WhenCallsListCategories_ThenReturnCategoriesFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final int expectedTotalPages,
            final int expectedTotalElements,
            final String expectedCategoriesName
    ) {
        final String expectedTerms = "";
        final String expectedSort = "name";
        final String expectedDirection = "asc";


        final SearchQuery aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final Pagination<CategoryListOutput> actualResult = this.useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());

        int index = 0;
        for (final String expectedName : expectedCategoriesName.split(";")) {
            final String actualName = actualResult.items().get(index++).name();
            Assertions.assertEquals(expectedName, actualName);
        }

    }
}
