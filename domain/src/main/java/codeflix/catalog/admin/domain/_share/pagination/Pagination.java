package codeflix.catalog.admin.domain._share.pagination;

import java.util.List;
import java.util.function.Function;

public record Pagination<T>(
        int currentPage,
        int perPage,
        long totalPages,
        long totalElements,
        List<T> items
) {

    public <R> Pagination<R> map(final Function<T, R> mapper) {
        final List<R> aMappedList = items.stream()
                .map(mapper)
                .toList();

        return new Pagination<>(currentPage(), perPage(), totalPages(), totalElements(), aMappedList);
    }

}
