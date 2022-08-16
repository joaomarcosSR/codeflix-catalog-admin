package codeflix.catalog.admin.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryRequest(
        String name,
        String description,
        @JsonProperty("is_active") Boolean active
) {

    public static CreateCategoryRequest with(
            final String aName,
            final String aDescription,
            final boolean isActive
    ) {
        return new CreateCategoryRequest(aName, aDescription, isActive);
    }
}
