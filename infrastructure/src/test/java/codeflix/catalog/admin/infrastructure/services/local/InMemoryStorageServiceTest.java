package codeflix.catalog.admin.infrastructure.services.local;

import codeflix.catalog.admin.domain.Fixture;
import codeflix.catalog.admin.domain.video.VideoMediaType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class InMemoryStorageServiceTest {
    private final InMemoryStorageService target = new InMemoryStorageService();

    @BeforeEach
    public void setUp() {
        this.target.clear();
    }

    @Test
    void givenValidResource_whenCallsStore_shouldStoreIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "item";

        this.target.store(expectedId, expectedResource);

        final var actualContent = this.target.storage().get(expectedId);

        Assertions.assertEquals(expectedResource, actualContent);
    }

    @Test
    void givenResource_whenCallsGet_shouldRetrieveIt() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "item";

        this.target.store(expectedId, expectedResource);

        final var actualContent = this.target.get(expectedId).get();

        Assertions.assertEquals(expectedResource, actualContent);
    }

    @Test
    void givenInvalidResource_whenCallsGet_shouldRetrieveEmpty() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);
        final var expectedId = "jajaja";

        this.target.storage().put("item", expectedResource);

        final var actualContent = this.target.get(expectedId);

        Assertions.assertTrue(actualContent.isEmpty());
    }

    @Test
    void givenPrefix_whenCallsList_shouldRetrieveAll() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.store("item1", expectedResource);
        this.target.store("item2", expectedResource);

        final var actualContent = this.target.list("it");

        Assertions.assertTrue(
                expectedIds.size() == actualContent.size()
                        && expectedIds.containsAll(actualContent)
        );
    }

    @Test
    void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
        final var expectedResource = Fixture.Videos.resource(VideoMediaType.THUMBNAIL);

        final var expectedIds = List.of("item1", "item2");

        this.target.storage().put("item1", expectedResource);
        this.target.storage().put("item2", expectedResource);

        this.target.deleteAll(expectedIds);

        Assertions.assertTrue(this.target.storage().isEmpty());
    }
}