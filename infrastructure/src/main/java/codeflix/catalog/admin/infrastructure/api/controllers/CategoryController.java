package codeflix.catalog.admin.infrastructure.api.controllers;

import codeflix.catalog.admin.application.category.create.CreateCategoryCommand;
import codeflix.catalog.admin.application.category.create.CreateCategoryOutput;
import codeflix.catalog.admin.application.category.create.CreateCategoryUseCase;
import codeflix.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import codeflix.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import codeflix.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import codeflix.catalog.admin.application.category.update.UpdateCategoryCommand;
import codeflix.catalog.admin.application.category.update.UpdateCategoryOutput;
import codeflix.catalog.admin.application.category.update.UpdateCategoryUseCase;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain._share.validation.handler.Notification;
import codeflix.catalog.admin.infrastructure.api.CategoryAPI;
import codeflix.catalog.admin.infrastructure.category.models.CategoryListResponse;
import codeflix.catalog.admin.infrastructure.category.models.CategoryResponse;
import codeflix.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import codeflix.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import codeflix.catalog.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase, final ListCategoriesUseCase listCategoriesUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCategoryRequest anInput) {
        final CreateCategoryCommand aCommand = CreateCategoryCommand.with(
                anInput.name(),
                anInput.description(),
                defaultIfNull(anInput.active(), true)
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);//.build instead of .body

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public CategoryResponse getById(final String id) {
        return CategoryApiPresenter.present
                .compose(this.getCategoryByIdUseCase::execute)
                .apply(id);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest anInput) {
        final UpdateCategoryCommand aCommand = UpdateCategoryCommand.with(
                id,
                anInput.name(),
                anInput.description(),
                defaultIfNull(anInput.active(), true)
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }

    @Override
    public Pagination<CategoryListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {
        return this.listCategoriesUseCase
                .execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CategoryApiPresenter::present);
    }
}
