package codeflix.catalog.admin.application.genre.update;

import codeflix.catalog.admin.domain.genre.entity.Genre;

public record UpdateGenreOutput(
        String id
) {
    public static UpdateGenreOutput from(final String anId) {
        return new UpdateGenreOutput(anId);
    }

    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }
}
