package codeflix.catalog.admin.application.category.retrieve.get;

import codeflix.catalog.admin.domain._share.exceptions.DomainException;
import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
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

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCategoryByIdUseCaseTest {

    @InjectMocks
    private GetCategoryByIdUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    void givenAValidId_WhenCallGetCategoryById_ThenReturnCategory() {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final CategoryID expectedId = aCategory.getId();

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.of(aCategory));

        final CategoryOutput actualCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    void givenAInvValidId_WhenCallGetCategoryById_ThenReturnNotFound() {
        final String expectedErrorMessage = "Category with ID 123 was not found";
        final CategoryID expectedId = CategoryID.from("123");

        when(categoryGateway.findById(expectedId)).thenReturn(Optional.empty());

        final DomainException actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    void givenAValidId_WhenGatewayThrowsException_ThenReturnAException() {
        final Category aCategory = Category.newCategory("Filme", "A categoria mais assistida", true);
        final String expectedErrorMessage = "Gateway error.";
        final CategoryID expectedId = aCategory.getId();

        when(categoryGateway.findById(expectedId)).thenThrow(new IllegalArgumentException(expectedErrorMessage));

        final IllegalArgumentException actualException =
                Assertions.assertThrows(IllegalArgumentException.class, () -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}
