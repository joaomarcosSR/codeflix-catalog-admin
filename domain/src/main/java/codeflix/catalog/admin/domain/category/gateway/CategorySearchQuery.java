package codeflix.catalog.admin.domain.category.gateway;

public record CategorySearchQuery(
        int page,
        int perPage,
        String terms,
        String sort,
        String direction
) {
}
