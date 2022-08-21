package codeflix.catalog.admin.application.category.delete;

import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class DeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteCategoryUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.categoryGateway);
    }

    @Test
    void givenAValidId_whenCallDeleteCategory_ThenReturnOk() {
        final Category aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final CategoryID expectedId = aCategory.getId();

        doNothing()
                .when(this.categoryGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        Mockito.verify(this.categoryGateway).deleteById(expectedId);
    }

    @Test
    void givenAInvalidId_whenCallDeleteCategory_ThenReturnOk() {
        final CategoryID expectedId = CategoryID.from("123");

        doNothing()
                .when(this.categoryGateway).deleteById(expectedId);

        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));

        Mockito.verify(this.categoryGateway).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenGatewayThrowsError_ThenReturnException() {
        final CategoryID expectedId = CategoryID.from("123");

        doThrow(new IllegalArgumentException("Gateway error."))
                .when(this.categoryGateway).deleteById(expectedId);

        Assertions.assertThrows(IllegalArgumentException.class, () -> this.useCase.execute(expectedId.getValue()));

        Mockito.verify(this.categoryGateway).deleteById(expectedId);
    }
}
