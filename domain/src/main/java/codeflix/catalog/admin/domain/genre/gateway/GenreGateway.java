package codeflix.catalog.admin.domain.genre.gateway;

import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain.genre.entity.Genre;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;

import java.util.List;
import java.util.Optional;

public interface GenreGateway {
    Genre create(Genre aGenre);

    Genre update(Genre aGenre);

    void deleteById(GenreID anId);

    Optional<Genre> findById(GenreID anId);

    List<Genre> findAll(SearchQuery aQuery);
}
