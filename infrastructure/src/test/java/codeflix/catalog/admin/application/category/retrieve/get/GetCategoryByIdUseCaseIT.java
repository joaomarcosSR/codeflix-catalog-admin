package codeflix.catalog.admin.application.category.retrieve.get;

import codeflix.catalog.admin.IntegrationTest;
import codeflix.catalog.admin.domain._share.exceptions.DomainException;
import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static codeflix.catalog.admin.application.category.utils.CategoryITUtils.save;

@IntegrationTest
class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidId_WhenCallGetCategoryById_ThenReturnCategory() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        save(categoryRepository, aCategory);

        final CategoryOutput actualCategory = useCase.execute(aCategory.getId().getValue());

        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    }

    @Test
    void givenAInvValidId_WhenCallGetCategoryById_ThenReturnNotFound() {
        final String expectedErrorMessage = "Category with ID 123 was not found";
        final CategoryID expectedId = CategoryID.from("123");

        final DomainException actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
