package codeflix.catalog.admin.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {
    }

    public static <T> Specification<T> like(final String prop, final String term) {
        return (root, query, cb) -> cb.like(cb.upper(root.get(prop)), likePattern(term.toUpperCase()));
    }

    private static String likePattern(final String term) {
        return "%" + term + "%";
    }
}
