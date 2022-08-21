package codeflix.catalog.admin.application.genre.create;

import codeflix.catalog.admin.domain.genre.entity.Genre;

public record CreateGenreOutput(
        String id
) {
    public static CreateGenreOutput from(final String anId) {
        return new CreateGenreOutput(anId);
    }

    public static CreateGenreOutput from(final Genre aGenre) {
        return new CreateGenreOutput(aGenre.getId().getValue());
    }
}
