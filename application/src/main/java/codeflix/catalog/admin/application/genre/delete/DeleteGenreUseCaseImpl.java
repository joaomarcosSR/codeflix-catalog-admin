package codeflix.catalog.admin.application.genre.delete;

import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;

import java.util.Objects;

public class DeleteGenreUseCaseImpl extends DeleteGenreUseCase {
    private final GenreGateway genreGateway;

    public DeleteGenreUseCaseImpl(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public void execute(final String anIn) {
        this.genreGateway.deleteById(GenreID.from(anIn));
    }
}
