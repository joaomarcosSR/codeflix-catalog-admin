package codeflix.catalog.admin.e2e.genre;

import codeflix.catalog.admin.E2ETest;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;
import codeflix.catalog.admin.e2e.MockDsl;
import codeflix.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import codeflix.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@E2ETest
@Testcontainers
public class GenreE2ETest implements MockDsl {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private GenreRepository genreRepository;

    @Container
    private static final MySQLContainer MYSQL_CONTAINER
            = new MySQLContainer("mysql:latest")
            .withPassword("123456")
            .withUsername("root")
            .withDatabaseName("adm_videos");

    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
    }

    @Override
    public MockMvc mvc() {
        return this.mvc;
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = this.givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var filmes = this.givenACategory("Filmes", null, true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = this.givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToNavigateThruAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        this.givenAGenre("Ação", true, List.of());
        this.givenAGenre("Esportes", true, List.of());
        this.givenAGenre("Drama", true, List.of());

        this.listGenres(0, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_pages", equalTo(3)))
                .andExpect(jsonPath("$.total_elements", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Ação")));

        this.listGenres(1, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(1)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_pages", equalTo(3)))
                .andExpect(jsonPath("$.total_elements", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));

        this.listGenres(2, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(2)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_pages", equalTo(3)))
                .andExpect(jsonPath("$.total_elements", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Esportes")));

        this.listGenres(3, 1)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(3)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_pages", equalTo(3)))
                .andExpect(jsonPath("$.total_elements", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(0)));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        this.givenAGenre("Ação", true, List.of());
        this.givenAGenre("Esportes", true, List.of());
        this.givenAGenre("Drama", true, List.of());

        this.listGenres(0, 1, "dra")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(1)))
                .andExpect(jsonPath("$.total_pages", equalTo(1)))
                .andExpect(jsonPath("$.total_elements", equalTo(1)))
                .andExpect(jsonPath("$.items", hasSize(1)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        this.givenAGenre("Ação", true, List.of());
        this.givenAGenre("Esportes", true, List.of());
        this.givenAGenre("Drama", true, List.of());

        this.listGenres(0, 3, "", "name", "desc")
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.current_page", equalTo(0)))
                .andExpect(jsonPath("$.per_page", equalTo(3)))
                .andExpect(jsonPath("$.total_pages", equalTo(1)))
                .andExpect(jsonPath("$.total_elements", equalTo(3)))
                .andExpect(jsonPath("$.items", hasSize(3)))
                .andExpect(jsonPath("$.items[0].name", equalTo("Esportes")))
                .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
                .andExpect(jsonPath("$.items[2].name", equalTo("Ação")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var filmes = this.givenACategory("Filmes", null, true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = this.givenAGenre(expectedName, expectedIsActive, expectedCategories);

        final var actualGenre = this.retrieveAGenre(actualId);

        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                        && this.mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var aRequest = get("/genres/123")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        this.mvc.perform(aRequest)
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", equalTo("Genre with ID 123 was not found")));
    }

    @Test
    void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var filmes = this.givenACategory("Filmes", null, true);

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes);

        final var actualId = this.givenAGenre("acao", expectedIsActive, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(
                expectedName,
                this.mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive
        );

        this.updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var filmes = this.givenACategory("Filmes", null, true);

        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(filmes);

        final var actualId = this.givenAGenre(expectedName, true, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(
                expectedName,
                this.mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive
        );

        this.updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoryIDs().size()
                        && expectedCategories.containsAll(actualGenre.getCategoryIDs())
        );
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToActivateAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var actualId = this.givenAGenre(expectedName, false, expectedCategories);

        final var aRequestBody = new UpdateGenreRequest(
                expectedName,
                this.mapTo(expectedCategories, CategoryID::getValue),
                expectedIsActive
        );

        this.updateAGenre(actualId, aRequestBody)
                .andExpect(status().isOk());

        final var actualGenre = this.genreRepository.findById(actualId.getValue()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategoryIDs());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        final var filmes = this.givenACategory("Filmes", null, true);

        final var actualId = this.givenAGenre("Ação", true, List.of(filmes));

        this.deleteAGenre(actualId)
                .andExpect(status().isNoContent());

        Assertions.assertFalse(this.genreRepository.existsById(actualId.getValue()));
        Assertions.assertEquals(0, this.genreRepository.count());
    }

    @Test
    void asACatalogAdminIShouldNotSeeAnErrorByDeletingANotExistentGenre() throws Exception {
        Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
        Assertions.assertEquals(0, this.genreRepository.count());

        this.deleteAGenre(GenreID.from("12313"))
                .andExpect(status().isNoContent());

        Assertions.assertEquals(0, this.genreRepository.count());
    }
}
