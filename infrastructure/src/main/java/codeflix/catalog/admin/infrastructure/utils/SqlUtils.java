package codeflix.catalog.admin.infrastructure.utils;

import static org.apache.commons.lang3.StringUtils.isBlank;

public final class SqlUtils {
    private SqlUtils() {
    }

    public static String upper(final String term) {
        if (isBlank(term)) return null;
        return term.toUpperCase();
    }

    public static String like(final String term) {
        if (isBlank(term)) return null;
        return "%" + term + "%";
    }

}
