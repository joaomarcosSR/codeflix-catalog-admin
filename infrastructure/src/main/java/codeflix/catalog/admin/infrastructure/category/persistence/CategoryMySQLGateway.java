package codeflix.catalog.admin.infrastructure.category.persistence;

import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.category.gateway.CategorySearchQuery;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static codeflix.catalog.admin.infrastructure.utils.SpecificationUtils.like;

@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    public CategoryMySQLGateway(CategoryRepository aRepository) {
        repository = aRepository;
    }

    @Override
    public Category create(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public void deleteById(final CategoryID anId) {
        final String anIdValue = anId.getValue();
        if (repository.existsById(anIdValue))
            repository.deleteById(anIdValue);
    }

    @Override
    public Optional<Category> findById(final CategoryID anId) {
        return repository.findById(anId.getValue())
                .map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public Category update(final Category aCategory) {
        return save(aCategory);
    }

    @Override
    public Pagination<Category> findAll(final CategorySearchQuery aQuery) {
        final PageRequest page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final Specification<CategoryJpaEntity> specification = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(term ->
                        SpecificationUtils.<CategoryJpaEntity>
                                        like("name", term)
                                .or(like("description", term)))
                .orElse(null);

        final Page<CategoryJpaEntity> pageResult = repository.findAll(Specification.where(specification), page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalPages(),
                pageResult.getTotalElements(),
                pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    private Category save(Category aCategory) {
        return repository.save(CategoryJpaEntity.from(aCategory))
                .toAggregate();
    }
}
