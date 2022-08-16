package codeflix.catalog.admin.application.category.create;

import codeflix.catalog.admin.domain.category.entity.Category;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput from(final String anId) {
        return new CreateCategoryOutput(anId);
    }

    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }
}
