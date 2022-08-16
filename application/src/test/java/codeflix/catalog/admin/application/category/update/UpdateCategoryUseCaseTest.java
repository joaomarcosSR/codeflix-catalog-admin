package codeflix.catalog.admin.application.category.update;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateCategoryUseCaseTest {
    @InjectMocks
    private UpdateCategoryUseCaseImpl useCase;
    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidCommand_WhenCallUpdateCategory_ThenReturnCategoryId() {
        final Category aCategory = Category.newCategory("Fim", null, false);

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

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(aCategory));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        final UpdateCategoryOutput actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway).findById(expectedId);

        Mockito.verify(categoryGateway).update(argThat(aUpdatedCategory ->
                Objects.equals(expectedId, aUpdatedCategory.getId())
                        && Objects.equals(expectedName, aUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())));
    }

    @Test
    void givenAnInvalidCommand_WhenCallUpdateCategory_ThenReturnDomainException() {
        final Category aCategory = Category.newCategory("Fim", "A categoria mais assistida", true);

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

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(aCategory));

        final Notification actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());

        Mockito.verify(categoryGateway, never()).update(any());
    }

    @Test
    void givenAValidInactivateCommand_WhenCallUpdateCategory_ThenReturnCategoryId() {
        final Category aCategory = Category.newCategory("Fim", "A categoria", true);

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;
        final CategoryID expectedId = aCategory.getId();

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(aCategory));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aCategory.isActive());

        final UpdateCategoryOutput actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway).findById(expectedId);

        Mockito.verify(categoryGateway).update(argThat(aUpdatedCategory ->
                Objects.equals(expectedId, aUpdatedCategory.getId())
                        && Objects.equals(expectedName, aUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())));
    }

    @Test
    void givenAValidCommand_WhenGatewayThrowsRandomException_ThenReturnAException() {
        final Category aCategory = Category.newCategory("Fim", "A categoria", true);

        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final CategoryID expectedId = aCategory.getId();
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "Gateway error.";

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(aCategory));
        when(categoryGateway.update(any())).thenThrow(new IllegalArgumentException("Gateway error."));

        final Notification actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());

        Mockito.verify(categoryGateway).update(argThat(aUpdatedCategory ->
                Objects.equals(expectedId, aUpdatedCategory.getId())
                        && Objects.equals(expectedName, aUpdatedCategory.getName())
                        && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                        && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())));
    }

    @Test
    void givenACommandWithInvalidID_WhenCallUpdateCategory_ThenReturnNotFoundException() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedId = "123";
        final int expectedErrorCount = 1;
        final String expectedErrorMessage = "Category with ID 123 was not found";

        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        when(categoryGateway.findById(CategoryID.from(expectedId))).thenReturn(Optional.empty());

        NotFoundException actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, never()).update(any());
    }
}
