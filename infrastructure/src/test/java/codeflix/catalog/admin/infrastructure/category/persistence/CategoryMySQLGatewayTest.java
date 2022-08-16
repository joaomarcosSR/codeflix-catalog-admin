package codeflix.catalog.admin.infrastructure.category.persistence;

import codeflix.catalog.admin.MySQLGatewayTest;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@MySQLGatewayTest
class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCategory_WhenCallsCreate_ThenReturnANewCategory() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, this.categoryRepository.count());

        final Category actualCategory = this.categoryGateway.create(aCategory);

        Assertions.assertEquals(1, this.categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());

        final CategoryJpaEntity actualEntity = this.categoryRepository.getById(aCategory.getId().getValue());

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAValidCategory_WhenCallsUpdate_ThenReturnAUpdatedCategory() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory("Other Name", null, expectedIsActive);

        Assertions.assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.save(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, this.categoryRepository.count());

        aCategory.update(expectedName, expectedDescription, expectedIsActive);

        final Category actualCategory = this.categoryGateway.update(aCategory);

        Assertions.assertEquals(1, this.categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertTrue(actualCategory.getUpdatedAt().isAfter(actualCategory.getCreatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());

        final CategoryJpaEntity actualEntity = this.categoryRepository.getById(aCategory.getId().getValue());

        Assertions.assertEquals(aCategory.getId().getValue(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertTrue(actualEntity.getUpdatedAt().isAfter(actualEntity.getCreatedAt()));
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryId_WhenTryDeleteIt_ThenDeleteCategory() {
        final Category aCategory = Category.newCategory("Filmes", null, true);

        Assertions.assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.save(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, this.categoryRepository.count());

        this.categoryGateway.deleteById(aCategory.getId());

        Assertions.assertEquals(0, this.categoryRepository.count());
    }

    @Test
    void givenAPrePersistedCategoryAndInvalidCategoryId_WhenTryDeleteIt_ThenDeleteCategory() {
        final Category aCategory = Category.newCategory("Filmes", null, true);

        Assertions.assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.save(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, this.categoryRepository.count());

        this.categoryGateway.deleteById(CategoryID.from("123"));

        Assertions.assertEquals(1, this.categoryRepository.count());
    }

    @Test
    void givenAPrePersistedCategoryAndValidCategoryId_WhenCallsFindById_ThenReturnACategory() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.save(CategoryJpaEntity.from(aCategory));

        Assertions.assertEquals(1, this.categoryRepository.count());

        final Category actualCategory = this.categoryGateway.findById(aCategory.getId()).get();

        Assertions.assertEquals(1, this.categoryRepository.count());

        Assertions.assertEquals(aCategory.getId(), actualCategory.getId());
        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCategoryNotPersisted_WhenCallsCreate_ThenReturnANNewCategory() {
        Assertions.assertEquals(0, this.categoryRepository.count());

        final Optional<Category> actualCategoryOp = this.categoryGateway.findById(CategoryID.unique());

        Assertions.assertEquals(0, this.categoryRepository.count());

        Assertions.assertTrue(actualCategoryOp.isEmpty());
    }

    @Test
    void givenPrePersistedCategories_WhenCallsFindAll_ThenReturnPaginated() {
        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotalPages = 3;
        final int expectedTotalElements = 3;

        final Category filmes = Category.newCategory("Filmes", null, true);
        final Category series = Category.newCategory("Series", null, true);
        final Category documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.save(CategoryJpaEntity.from(filmes));
        this.categoryRepository.save(CategoryJpaEntity.from(series));
        this.categoryRepository.save(CategoryJpaEntity.from(documentarios));

        Assertions.assertEquals(3, this.categoryRepository.count());

        final SearchQuery query =
                new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");

        final Pagination<Category> actualResult = this.categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenEmptyCategoryTable_WhenCallsFindAll_ThenReturnEmptyPage() {
        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotalPages = 0;
        final int expectedTotalElements = 0;

        Assertions.assertEquals(0, this.categoryRepository.count());

        final SearchQuery query =
                new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");

        final Pagination<Category> actualResult = this.categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
        Assertions.assertEquals(0, actualResult.items().size());
    }

    @Test
    void givenFollowPagination_WhenCallsFindAllWithPage1_ThenReturnPaginated() {
        int expectedPage = 0;
        final int expectedPerPage = 2;
        final int expectedTotalPages = 2;
        final int expectedTotalElements = 3;

        final Category filmes = Category.newCategory("Filmes", null, true);
        final Category series = Category.newCategory("Series", null, true);
        final Category documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.save(CategoryJpaEntity.from(filmes));
        this.categoryRepository.save(CategoryJpaEntity.from(series));
        this.categoryRepository.save(CategoryJpaEntity.from(documentarios));

        Assertions.assertEquals(3, this.categoryRepository.count());

        SearchQuery query =
                new SearchQuery(expectedPage, expectedPerPage, "", "name", "asc");

        Pagination<Category> actualResult = this.categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
        Assertions.assertEquals(filmes.getId(), actualResult.items().get(1).getId());

        expectedPage = 1;

        query =
                new SearchQuery(1, expectedPerPage, "", "name", "asc");

        actualResult = this.categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
        Assertions.assertEquals(1, actualResult.items().size());
        Assertions.assertEquals(series.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedCategoriesAndFilAsTerms_WhenCallsFindAllAndTermsMatchCategoryName_ThenReturnPaginated() {
        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotalPages = 1;
        final int expectedTotalElements = 1;
        final String aTerms = "fil";

        final Category filmes = Category.newCategory("Filmes", null, true);
        final Category series = Category.newCategory("Series", null, true);
        final Category documentarios = Category.newCategory("Documentarios", null, true);

        Assertions.assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.save(CategoryJpaEntity.from(filmes));
        this.categoryRepository.save(CategoryJpaEntity.from(series));
        this.categoryRepository.save(CategoryJpaEntity.from(documentarios));

        Assertions.assertEquals(3, this.categoryRepository.count());

        final SearchQuery query =
                new SearchQuery(expectedPage, expectedPerPage, aTerms, "name", "asc");

        final Pagination<Category> actualResult = this.categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(filmes.getId(), actualResult.items().get(0).getId());
    }

    @Test
    void givenPrePersistedCategoriesAndCategoriaMaisAsTerms_WhenCallsFindAllAndTermsMatchCategoryDescription_ThenReturnPaginated() {
        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedTotalPages = 2;
        final int expectedTotalElements = 2;
        final String aTerms = "categoria MAIS";

        final Category filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final Category series = Category.newCategory("Series", "Todos assistem", true);
        final Category documentarios = Category.newCategory("Documentarios", "A categoria mais visualizada", true);

        Assertions.assertEquals(0, this.categoryRepository.count());

        this.categoryRepository.save(CategoryJpaEntity.from(filmes));
        this.categoryRepository.save(CategoryJpaEntity.from(series));
        this.categoryRepository.save(CategoryJpaEntity.from(documentarios));

        Assertions.assertEquals(3, this.categoryRepository.count());

        final SearchQuery query =
                new SearchQuery(expectedPage, expectedPerPage, aTerms, "name", "asc");

        final Pagination<Category> actualResult = this.categoryGateway.findAll(query);

        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotalPages, actualResult.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualResult.totalElements());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
    }
}