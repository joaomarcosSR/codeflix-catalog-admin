package codeflix.catalog.admin.application.genre.retrieve.get;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.genre.entity.Genre;
import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;

import java.util.Objects;

public class GetGenreByIdUseCaseImpl extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    public GetGenreByIdUseCaseImpl(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(final String anId) {
        final GenreID aGenreId = GenreID.from(anId);
        return this.genreGateway.findById(aGenreId)
                .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, aGenreId));
    }
}
