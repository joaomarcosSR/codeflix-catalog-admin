package codeflix.catalog.admin.application.category.delete;

import codeflix.catalog.admin.IntegrationTest;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static codeflix.catalog.admin.application.category.utils.CategoryITUtils.save;

@IntegrationTest
class DeleteCategoryUseCaseIT {
    @Autowired
    private DeleteCategoryUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidId_whenCallDeleteCategory_ThenReturnOk() {
        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        save(categoryRepository, aCategory);

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCategory.getId().getValue()));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    void givenAInvalidId_whenCallDeleteCategory_ThenReturnOk() {
        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final CategoryID expectedId = CategoryID.from("123");

        save(categoryRepository, aCategory);

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(1, categoryRepository.count());
    }
}
