package codeflix.catalog.admin.infrastructure.api;

import codeflix.catalog.admin.ControllerTest;
import codeflix.catalog.admin.application.category.create.CreateCategoryOutput;
import codeflix.catalog.admin.application.category.create.CreateCategoryUseCase;
import codeflix.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import codeflix.catalog.admin.application.category.retrieve.get.CategoryOutput;
import codeflix.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import codeflix.catalog.admin.application.category.retrieve.list.CategoryListOutput;
import codeflix.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import codeflix.catalog.admin.application.category.update.UpdateCategoryOutput;
import codeflix.catalog.admin.application.category.update.UpdateCategoryUseCase;
import codeflix.catalog.admin.domain._share.exceptions.DomainException;
import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import codeflix.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.Objects;

import static io.vavr.API.Left;
import static io.vavr.API.Right;
import static java.util.Collections.singletonList;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = CategoryAPI.class)
class CategoryAPITest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private CreateCategoryUseCase createCategoryUseCase;
    @MockBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;
    @MockBean
    private UpdateCategoryUseCase updateCategoryUseCase;
    @MockBean
    private DeleteCategoryUseCase deleteCategoryUseCase;
    @MockBean
    ListCategoriesUseCase listCategoriesUseCase;

    @Test
    void givenAValidCommand_WhenCallCreateCategory_ThenReturnCategoryId() throws Exception {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedId = "123";

        final CreateCategoryRequest anInput =
                CreateCategoryRequest.with(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Right(CreateCategoryOutput.from(expectedId)));

        final MockHttpServletRequestBuilder request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isCreated(),
                        header().string("Location", "/categories/" + expectedId),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.id", equalTo(expectedId))
                );

        verify(createCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenACommandWithInvalidName_WhenCallCreateCategory_ThenNotification() throws Exception {
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedMessage = "'name' should not be null";

        final CreateCategoryRequest anInput =
                CreateCategoryRequest.with(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final MockHttpServletRequestBuilder request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.errors", hasSize(1)),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage))
                );

        verify(createCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAInvalidCommand_WhenCallCreateCategory_ThenDomainExcpetion() throws Exception {
        final String expectedName = null;
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final String expectedMessage = "'name' should not be null";

        final CreateCategoryRequest anInput =
                CreateCategoryRequest.with(expectedName, expectedDescription, expectedIsActive);

        when(createCategoryUseCase.execute(any()))
                .thenThrow(DomainException.with(new Error(expectedMessage)));

        final MockHttpServletRequestBuilder request = post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(anInput));

        mvc.perform(request)
                .andDo(print())
                .andExpectAll(
                        status().isUnprocessableEntity(),
                        header().string("Location", nullValue()),
                        header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE),
                        jsonPath("$.message", equalTo(expectedMessage)),
                        jsonPath("$.errors", hasSize(1)),
                        jsonPath("$.errors[0].message", equalTo(expectedMessage))
                );

        verify(createCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAValidId_WhenCallGetCategory_ThenReturnCategory() throws Exception {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final Category aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final String expectedId = aCategory.getId().getValue();

        when(getCategoryByIdUseCase.execute(expectedId))
                .thenReturn(CategoryOutput.from(aCategory));

        final MockHttpServletRequestBuilder request = get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(jsonPath("$.name", equalTo(expectedName)))
                .andExpect(jsonPath("$.description", equalTo(expectedDescription)))
                .andExpect(jsonPath("$.is_active", equalTo(expectedIsActive)))
                .andExpect(jsonPath("$.created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.updated_at", equalTo(aCategory.getUpdatedAt().toString())))
                .andExpect(jsonPath("$.deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(getCategoryByIdUseCase).execute(expectedId);
    }

    @Test
    void givenAInvValidId_WhenCallGetCategoryById_ThenReturnNotFound() throws Exception {
        final String expectedErrorMessage = "Category with ID 123 was not found";
        final CategoryID anId = CategoryID.from("123");

        when(getCategoryByIdUseCase.execute(anId.getValue()))
                .thenThrow(NotFoundException.with(Category.class, anId));

        final MockHttpServletRequestBuilder request = get("/categories/{id}", anId.getValue());

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));
    }

    @Test
    void givenAValidCommand_WhenCallUpdateCategory_ThenReturnCategoryId() throws Exception {
        final String expectedId = "123";
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Right(UpdateCategoryOutput.from(expectedId)));

        final UpdateCategoryRequest aCommand =
                UpdateCategoryRequest.with(expectedName, expectedDescription, expectedIsActive);

        final MockHttpServletRequestBuilder request = put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(expectedId)))
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE));

        verify(updateCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenACommandWithInvalidId_WhenCallUpdateCategory_ThenReturnNotFound() throws Exception {
        final String anId = "not-found";
        final String expectedName = "Filme";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final String expectedErrorMessage = "Category with ID not-found was not found";

        when(updateCategoryUseCase.execute(any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(anId)));

        final UpdateCategoryRequest aCommand =
                UpdateCategoryRequest.with(expectedName, expectedDescription, expectedIsActive);

        final MockHttpServletRequestBuilder request = put("/categories/{id}", anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.message", equalTo(expectedErrorMessage)));

        verify(updateCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAnInvalidCommand_WhenCallUpdateCategory_ThenReturnDomainException() throws Exception {
        final String anId = "123";
        final String expectedName = "invalid-name";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final int expectedErrorCount = 1;
        final String expectedMessage = "'name' should not be null";

        when(updateCategoryUseCase.execute(any()))
                .thenReturn(Left(Notification.create(new Error(expectedMessage))));

        final UpdateCategoryRequest aCommand =
                UpdateCategoryRequest.with(expectedName, expectedDescription, expectedIsActive);

        final MockHttpServletRequestBuilder request = put("/categories/{id}", anId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(aCommand));

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.errors", hasSize(expectedErrorCount)))
                .andExpect(jsonPath("$.errors[0].message", equalTo(expectedMessage)));

        verify(updateCategoryUseCase).execute(argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.isActive())
        ));
    }

    @Test
    void givenAValidId_WhenCallDeleteCategory_ThenReturnNoContent() throws Exception {
        final String expectedId = "123";

        doNothing()
                .when(deleteCategoryUseCase)
                .execute(any());

        final MockHttpServletRequestBuilder request = delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(deleteCategoryUseCase).execute(expectedId);
    }

    @Test
    void givenAValid_WhenCallsListCategories_ThenReturnCategories() throws Exception {
        final Category aCategory = Category.newCategory("Movies", null, true);
        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final String expectedTerms = "movies";
        final String expectedSort = "description";
        final String expectedDirection = "asc";
        final List<CategoryListOutput> expectedItems = singletonList(CategoryListOutput.from(aCategory));
        final int expectedItemsCount = 1;
        final int expectedTotalPages = 1;
        final int expectedTotalElements = 1;

        when(listCategoriesUseCase.execute(any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotalPages, expectedTotalElements,
                        expectedItems));

        final MockHttpServletRequestBuilder request = get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON_VALUE);

        mvc.perform(request)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(expectedPage)))
                .andExpect(jsonPath("$.per_page", equalTo(expectedPerPage)))
                .andExpect(jsonPath("$.total_elements", equalTo(expectedTotalElements)))
                .andExpect(jsonPath("$.total_pages", equalTo(expectedTotalPages)))
                .andExpect(jsonPath("$.items", hasSize(expectedItemsCount)))
                .andExpect(jsonPath("$.items[0].id", equalTo(aCategory.getId().getValue())))
                .andExpect(jsonPath("$.items[0].name", equalTo(aCategory.getName())))
                .andExpect(jsonPath("$.items[0].description", equalTo(aCategory.getDescription())))
                .andExpect(jsonPath("$.items[0].is_active", equalTo(aCategory.isActive())))
                .andExpect(jsonPath("$.items[0].created_at", equalTo(aCategory.getCreatedAt().toString())))
                .andExpect(jsonPath("$.items[0].deleted_at", equalTo(aCategory.getDeletedAt())));

        verify(listCategoriesUseCase).execute(argThat(query ->
                Objects.equals(expectedPage, query.page())
                        && Objects.equals(expectedPerPage, query.perPage())
                        && Objects.equals(expectedDirection, query.direction())
                        && Objects.equals(expectedSort, query.sort())
                        && Objects.equals(expectedTerms, query.terms())
        ));
    }
}
