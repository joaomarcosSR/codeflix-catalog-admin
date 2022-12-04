package codeflix.catalog.admin.application.genre.retrieve.list;

import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.entity.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {
    public static GenreListOutput from(final Genre aGenre) {
        return new GenreListOutput(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.isActive(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt()
        );
    }
}
