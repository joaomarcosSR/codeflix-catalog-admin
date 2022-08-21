package codeflix.catalog.admin.application.genre.retrieve.list;

import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;

import java.util.Objects;

public class ListGenreUseCaseImpl extends ListGenreUseCase {

    private final GenreGateway genreGateway;

    public ListGenreUseCaseImpl(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery)
                .map(GenreListOutput::from);
    }
}
