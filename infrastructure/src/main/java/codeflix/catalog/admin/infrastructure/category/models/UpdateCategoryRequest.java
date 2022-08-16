package codeflix.catalog.admin.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateCategoryRequest(
        String name,
        String description,
        @JsonProperty("is_active") Boolean active
) {

    public static UpdateCategoryRequest with(
            final String aName,
            final String aDescription,
            final boolean isActive
    ) {
        return new UpdateCategoryRequest(aName, aDescription, isActive);
    }
}
