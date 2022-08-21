package codeflix.catalog.admin.infrastructure.category.models;

import codeflix.catalog.admin.JacksonTest;
import codeflix.catalog.admin.domain._share.utils.InstantUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;

import java.io.IOException;
import java.time.Instant;

@JacksonTest
class CategoryListResponseTest {

    @Autowired
    private JacksonTester<CategoryListResponse> json;

    @Test
    void testMarshall() throws IOException {
        final String expectedId = "123";
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;
        final Instant expectedCreatedAt = InstantUtils.now();
        final Instant expectedDeletedAt = InstantUtils.now();

        final CategoryListResponse response = new CategoryListResponse(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive,
                expectedCreatedAt,
                expectedDeletedAt
        );

        final JsonContent<CategoryListResponse> actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPath("$.id", expectedId)
                .hasJsonPath("$.name", expectedName)
                .hasJsonPath("$.description", expectedDescription)
                .hasJsonPath("$.is_active", expectedIsActive)
                .hasJsonPath("$.created_at", expectedCreatedAt.toString())
                .hasJsonPath("$.deleted_at", expectedDeletedAt.toString());
    }
}
