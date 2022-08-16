package codeflix.catalog.admin.application.category.utils;

import codeflix.catalog.admin.domain.category.entity.Category;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;

import java.util.Arrays;

public final class CategoryITUtils {
    private CategoryITUtils() {
    }

    public static void save(final CategoryRepository categoryRepository, final Category... categories) {
        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAllAndFlush(Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList());

        Assertions.assertEquals(categories.length, categoryRepository.count());
    }
}
