package codeflix.catalog.admin.infrastructure.api;

import codeflix.catalog.admin.ControllerTest;
import codeflix.catalog.admin.application.genre.create.CreateGenreOutput;
import codeflix.catalog.admin.application.genre.create.CreateGenreUseCase;
import codeflix.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import codeflix.catalog.admin.application.genre.retrieve.get.GenreOutput;
import codeflix.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import codeflix.catalog.admin.application.genre.retrieve.list.GenreListOutput;
import codeflix.catalog.admin.application.genre.retrieve.list.ListGenreUseCase;
import codeflix.catalog.admin.application.genre.update.UpdateGenreOutput;
import codeflix.catalog.admin.application.genre.update.UpdateGenreUseCase;
import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.entity.Genre;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;
import codeflix.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import codeflix.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
class GenreAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateGenreUseCase createGenreUseCase;

    @MockBean
    private GetGenreByIdUseCase getGenreByIdUseCase;

    @MockBean
    private UpdateGenreUseCase updateGenreUseCase;

    @MockBean
    private DeleteGenreUseCase deleteGenreUseCase;

    @MockBean
    private ListGenreUseCase listGenreUseCase;

    @Test
    void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
        // given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedId = "123";

        final var aCommand =
                new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(this.createGenreUseCase.execute(any()))
                .thenReturn(CreateGenreOutput.from(expectedId));

        // when
        final var aRequest = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/genres/" + expectedId))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(this.createGenreUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand =
                new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(this.createGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        // when
        final var aRequest = post("/genres")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Location", nullValue()))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(this.createGenreUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAValidId_whenCallsGetGenreById_shouldReturnGenre() throws Exception {
        // given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = false;

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(
                        expectedCategories.stream()
                                .map(CategoryID::from)
                                .toList()
                );

        final var expectedId = aGenre.getId().getValue();

        when(this.getGenreByIdUseCase.execute(any()))
                .thenReturn(GenreOutput.from(aGenre));

        // when
        final var aRequest = get("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.categories_id", equalTo(expectedCategories)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aGenre.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aGenre.getDeletedAt().toString())));

        verify(this.getGenreByIdUseCase).execute(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsGetGenreById_shouldReturnNotFound() throws Exception {
        // given
        final var expectedErrorMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreID.from("123");

        when(this.getGenreByIdUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Genre.class, expectedId));

        // when
        final var aRequest = get("/genres/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(this.getGenreByIdUseCase).execute(expectedId.getValue());
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() throws Exception {
        // given
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId().getValue();

        final var aCommand =
                new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(this.updateGenreUseCase.execute(any()))
                .thenReturn(UpdateGenreOutput.from(aGenre));

        // when
        final var aRequest = put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)));

        verify(this.updateGenreUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedCategories = List.of("123", "456");
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";

        final var aGenre = Genre.newGenre("Ação", expectedIsActive);
        final var expectedId = aGenre.getId().getValue();

        final var aCommand =
                new UpdateGenreRequest(expectedName, expectedCategories, expectedIsActive);

        when(this.updateGenreUseCase.execute(any()))
                .thenThrow(new NotificationException("Error", Notification.create(new Error(expectedErrorMessage))));

        // when
        final var aRequest = put("/genres/{id}", expectedId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(aCommand));

        final var response = this.mvc.perform(aRequest)
                .andDo(print());

        // then
        response.andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(1)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

        verify(this.updateGenreUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAValidId_whenCallsDeleteGenre_shouldBeOK() throws Exception {
        // given
        final var expectedId = "123";

        doNothing()
                .when(this.deleteGenreUseCase).execute(any());

        // when
        final var aRequest = delete("/genres/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON);

        final var result = this.mvc.perform(aRequest);

        // then
        result.andExpect(status().isNoContent());

        verify(this.deleteGenreUseCase).execute(expectedId);
    }

    @Test
    void givenValidParams_whenCallsListGenres_shouldReturnGenres() throws Exception {
        // given
        final var aGenre = Genre.newGenre("Ação", false);

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "ac";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var expectedItemsCount = 1;
        final int expectedTotalPages = 1;
        final int expectedTotalElements = 1;

        final var expectedItems = List.of(GenreListOutput.from(aGenre));

        when(this.listGenreUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotalPages, expectedTotalElements, expectedItems));

        // when
        final var aRequest = get("/genres")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(aRequest);

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total_pages", equalTo(expectedTotalPages)))
                .andExpect(jsonPath("$.total_elements", equalTo(expectedTotalElements)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aGenre.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aGenre.getName())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aGenre.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aGenre.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aGenre.getDeletedAt().toString())));

        verify(this.listGenreUseCase).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
