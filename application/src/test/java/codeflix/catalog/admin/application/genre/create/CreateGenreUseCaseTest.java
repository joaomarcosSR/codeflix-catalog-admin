package codeflix.catalog.admin.application.genre.create;

import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;
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
class CreateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private CreateGenreUseCaseImpl useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.categoryGateway, this.genreGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
                CreateGenreCommand.with(expectName, expectedIsActive, this.asString(expectedCategories));

        when(this.genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(this.genreGateway).create(argThat(aGenre ->
                Objects.equals(expectName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aCommand =
                CreateGenreCommand.with(expectName, expectedIsActive, this.asString(expectedCategories));

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(this.genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(this.categoryGateway).existsByIds(expectedCategories);

        Mockito.verify(this.genreGateway).create(argThat(aGenre ->
                Objects.equals(expectName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
        // given
        final var expectName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aCommand =
                CreateGenreCommand.with(expectName, expectedIsActive, this.asString(expectedCategories));

        when(this.genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = this.useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(this.genreGateway).create(argThat(aGenre ->
                Objects.equals(expectName, aGenre.getName())
                        && Objects.equals(expectedIsActive, aGenre.isActive())
                        && Objects.equals(expectedCategories, aGenre.getCategories())
                        && Objects.nonNull(aGenre.getId())
                        && Objects.nonNull(aGenre.getCreatedAt())
                        && Objects.nonNull(aGenre.getUpdatedAt())
                        && Objects.nonNull(aGenre.getDeletedAt())
        ));
    }

    @Test
    void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        final var expectName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(expectName, expectedIsActive, this.asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(this.categoryGateway, never()).existsByIds(any());
        Mockito.verify(this.genreGateway, never()).create(any());
    }

    @Test
    void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
        // given
        final String expectName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                CreateGenreCommand.with(expectName, expectedIsActive, this.asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(this.categoryGateway, never()).existsByIds(any());
        Mockito.verify(this.genreGateway, never()).create(any());
    }

    @Test
    void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var filmes = CategoryID.from("456");
        final var series = CategoryID.from("123");
        final var documentarios = CategoryID.from("789");

        final var expectName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorMessage = "Some categories could not be found: 456, 789";
        final var expectedErrorCount = 1;

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(List.of(series));

        final var aCommand =
                CreateGenreCommand.with(expectName, expectedIsActive, this.asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        Mockito.verify(this.categoryGateway).existsByIds(any());
        Mockito.verify(this.genreGateway, never()).create(any());
    }

    @Test
    void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
        // given
        final var filmes = CategoryID.from("456");
        final var series = CategoryID.from("123");
        final var documentarios = CategoryID.from("789");

        final var expectName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes, series, documentarios);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        when(this.categoryGateway.existsByIds(any()))
                .thenReturn(List.of(series));

        final var aCommand =
                CreateGenreCommand.with(expectName, expectedIsActive, this.asString(expectedCategories));

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            this.useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

        Mockito.verify(this.categoryGateway).existsByIds(any());
        Mockito.verify(this.genreGateway, never()).create(any());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }
}
