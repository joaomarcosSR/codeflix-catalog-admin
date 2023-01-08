package codeflix.catalog.admin.infrastructure.api.controllers;

import codeflix.catalog.admin.application.genre.create.CreateGenreCommand;
import codeflix.catalog.admin.application.genre.create.CreateGenreOutput;
import codeflix.catalog.admin.application.genre.create.CreateGenreUseCase;
import codeflix.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import codeflix.catalog.admin.application.genre.retrieve.get.GetGenreByIdUseCase;
import codeflix.catalog.admin.application.genre.retrieve.list.ListGenreUseCase;
import codeflix.catalog.admin.application.genre.update.UpdateGenreCommand;
import codeflix.catalog.admin.application.genre.update.UpdateGenreOutput;
import codeflix.catalog.admin.application.genre.update.UpdateGenreUseCase;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.infrastructure.api.GenreAPI;
import codeflix.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import codeflix.catalog.admin.infrastructure.genre.models.GenreListResponse;
import codeflix.catalog.admin.infrastructure.genre.models.GenreResponse;
import codeflix.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import codeflix.catalog.admin.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

//TODO: Remover
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final ListGenreUseCase listGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase, final DeleteGenreUseCase deleteGenreUseCase, final ListGenreUseCase listGenreUseCase) {
        this.createGenreUseCase = createGenreUseCase;
        this.getGenreByIdUseCase = getGenreByIdUseCase;
        this.updateGenreUseCase = updateGenreUseCase;
        this.deleteGenreUseCase = deleteGenreUseCase;
        this.listGenreUseCase = listGenreUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest anInput) {
        final CreateGenreCommand aCommand = CreateGenreCommand.with(
                anInput.name(),
                anInput.active(),
                anInput.categories()
        );

        final CreateGenreOutput output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest anInput) {
        final UpdateGenreCommand aCommand = UpdateGenreCommand.with(
                id,
                anInput.name(),
                anInput.active(),
                anInput.categories()
        );

        final UpdateGenreOutput output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteGenreUseCase.execute(anId);
    }

    @Override
    public Pagination<GenreListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {

        return this.listGenreUseCase
                .execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreApiPresenter::present);
    }
}
