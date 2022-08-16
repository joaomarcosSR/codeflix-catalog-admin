package codeflix.catalog.admin.application.category.update;

import codeflix.catalog.admin.IntegrationTest;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static codeflix.catalog.admin.application.category.utils.CategoryITUtils.save;

@IntegrationTest
class UpdateCategoryUseCaseIT {
    @Autowired
    private UpdateCategoryUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAValidCommand_WhenCallUpdateCategory_ThenReturnCategoryId() {
        final Category aCategory = Category.newCategory("Fim", null, false);
        save(categoryRepository, aCategory);

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final CategoryID expectedId = aCategory.getId();

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final UpdateCategoryOutput actualOutput = useCase.execute(aCommand).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final CategoryJpaEntity actualCategory = categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    }

    @Test
    void givenAnInvalidCommand_WhenCallUpdateCategory_ThenReturnDomainException() {
        final Category aCategory = Category.newCategory("Fim", "A categoria mais assistida", true);
        save(categoryRepository, aCategory);

        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final CategoryID expectedId = aCategory.getId();
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be null";

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final Notification actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());
    }
}
