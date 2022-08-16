package codeflix.catalog.admin.application.category.create;

import codeflix.catalog.admin.IntegrationTest;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import static org.mockito.ArgumentMatchers.any;

@IntegrationTest
class CreateCategoryUseCaseIT {
    @Autowired
    private CreateCategoryUseCase useCase;
    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_WhenCallCreateCategory_ThenReturnCategoryId() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final CreateCategoryCommand aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final CreateCategoryOutput actualOutput = useCase.execute(aCommand).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final CategoryJpaEntity actualCategoryEntity =
                categoryRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategoryEntity.getName());
        Assertions.assertEquals(expectedDescription, actualCategoryEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategoryEntity.isActive());
        Assertions.assertNotNull(actualCategoryEntity.getCreatedAt());
        Assertions.assertNotNull(actualCategoryEntity.getUpdatedAt());
        Assertions.assertNull(actualCategoryEntity.getDeletedAt());
    }

    @Test
    void givenAnInvalidCommand_WhenCallCreateCategory_ThenReturnDomainException() {
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be null";

        final CreateCategoryCommand aCommand =
                CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final Notification actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(0, categoryRepository.count());

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());

        Mockito.verify(categoryGateway, Mockito.never()).create(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_WhenCallCreateCategory_ThenReturnInactiveCategoryId() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        final CreateCategoryOutput actualOutput = useCase.execute(aCommand).get();

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final CategoryJpaEntity actualCategoryEntity =
                categoryRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategoryEntity.getName());
        Assertions.assertEquals(expectedDescription, actualCategoryEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategoryEntity.isActive());
        Assertions.assertNotNull(actualCategoryEntity.getCreatedAt());
        Assertions.assertNotNull(actualCategoryEntity.getUpdatedAt());
        Assertions.assertNotNull(actualCategoryEntity.getDeletedAt());
    }

    @Test
    void givenAValidCommand_WhenGatewayThrowsRandomException_ThenReturnAException() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "Gateway error.";

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        Mockito.doThrow(new IllegalArgumentException("Gateway error."))
                .when(categoryGateway)
                .create(any());

        final Notification actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());
    }
}
