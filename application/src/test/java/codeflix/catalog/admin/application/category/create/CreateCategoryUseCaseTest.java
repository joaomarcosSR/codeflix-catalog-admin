package codeflix.catalog.admin.application.category.create;

import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCategoryUseCaseTest extends UseCaseTest {
    @InjectMocks
    private CreateCategoryUseCaseImpl useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.categoryGateway);
    }

    @Test
    void givenAValidCommand_WhenCallCreateCategory_ThenReturnCategoryId() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final CreateCategoryOutput actualOutput = this.useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(this.categoryGateway).create(argThat(aCreatedCategory ->
                Objects.nonNull(aCreatedCategory.getId())
                        && Objects.equals(expectedName, aCreatedCategory.getName())
                        && Objects.equals(expectedDescription, aCreatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCreatedCategory.isActive())));
    }

    @Test
    void givenAnInvalidCommand_WhenCallCreateCategory_ThenReturnDomainException() {
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "'name' should not be null";

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        final Notification actualNotification = this.useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());

        Mockito.verify(this.categoryGateway, never()).create(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_WhenCallCreateCategory_ThenReturnInactiveCategoryId() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGateway.create(any())).thenAnswer(returnsFirstArg());

        final CreateCategoryOutput actualOutput = this.useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(this.categoryGateway).create(argThat(aCreatedCategory ->
                Objects.nonNull(aCreatedCategory.getId())
                        && Objects.equals(expectedName, aCreatedCategory.getName())
                        && Objects.equals(expectedDescription, aCreatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCreatedCategory.isActive())));
    }

    @Test
    void givenAValidCommand_WhenGatewayThrowsRandomException_ThenReturnAException() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "Gateway error.";

        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGateway.create(any())).thenThrow(new IllegalArgumentException("Gateway error."));

        final Notification actualNotification = this.useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());

        Mockito.verify(this.categoryGateway).create(argThat(aCreatedCategory ->
                Objects.nonNull(aCreatedCategory.getId())
                        && Objects.equals(expectedName, aCreatedCategory.getName())
                        && Objects.equals(expectedDescription, aCreatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, aCreatedCategory.isActive())));
    }
}
