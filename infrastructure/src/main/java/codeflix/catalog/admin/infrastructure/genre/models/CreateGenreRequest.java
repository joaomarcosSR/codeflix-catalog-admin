package codeflix.catalog.admin.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

public record CreateGenreRequest(
        String name,
        @JsonProperty("categories_id") List<String> categories,
        @JsonProperty("is_active") Boolean active
) {

    @Override
    public Boolean active() {
        return this.active == null || this.active;
    }

    @Override
    public List<String> categories() {
        return isNotEmpty(this.categories) ? this.categories : emptyList();
    }
}
