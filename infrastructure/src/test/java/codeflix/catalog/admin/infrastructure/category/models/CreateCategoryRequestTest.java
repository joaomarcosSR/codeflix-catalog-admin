package codeflix.catalog.admin.infrastructure.category.models;

import codeflix.catalog.admin.JacksonTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

@JacksonTest
class CreateCategoryRequestTest {

    @Autowired
    private JacksonTester<CreateCategoryRequest> json;

    @Test
    void testMarshall() throws IOException {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final CreateCategoryRequest aRequest =
                new CreateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

        final JsonContent<CreateCategoryRequest> actualJson = json.write(aRequest);

        Assertions.assertThat(actualJson)
                .hasJsonPath("$.name", expectedName)
                .hasJsonPath("$.description", expectedDescription)
                .hasJsonPath("$.is_active", expectedIsActive);
    }

    @Test
    void testUnmarshall() throws IOException {
        final String expectedName = "Filmes";
        final String expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;

        final String json = """
                {
                    "name" : "%s",
                    "description" : "%s",
                    "is_active" : "%s"
                }
                """.formatted(expectedName, expectedDescription, expectedIsActive
        );

        final ObjectContent<CreateCategoryRequest> actualJson = this.json.parse(json);

        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}
