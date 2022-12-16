package codeflix.catalog.admin.infrastructure.genre.persistence;

import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain.genre.entity.Genre;
import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;
import codeflix.catalog.admin.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository repository;

    public GenreMySQLGateway(final GenreRepository repository) {
        this.repository = Objects.requireNonNull(repository);
    }

    @Override
    public Genre create(final Genre aGenre) {
        return this.save(aGenre);
    }

    @Override
    public Genre update(final Genre aGenre) {
        return this.save(aGenre);
    }

    @Override
    public void deleteById(final GenreID anId) {
        final String anIdValue = anId.getValue();
        if (this.repository.existsById(anIdValue))
            this.repository.deleteById(anIdValue);
    }

    @Override
    public Optional<Genre> findById(final GenreID anId) {
        return this.repository.findById(anId.getValue())
                .map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Genre> findAll(final SearchQuery aQuery) {
        final PageRequest page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final Specification<GenreJpaEntity> specification = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final Page<GenreJpaEntity> pageResult = this.repository.findAll(Specification.where(specification), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    private Genre save(final Genre aGenre) {
        return this.repository.save(GenreJpaEntity.from(aGenre)).toAggregate();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

    @Override
    public List<GenreID> existsByIds(final Iterable<GenreID> genres) {
        final List<String> ids = StreamSupport.stream(genres.spliterator(), false)
                .map(GenreID::getValue)
                .toList();

        return this.repository.existsByIds(ids).stream()
                .map(GenreID::from)
                .toList();
    }
}
