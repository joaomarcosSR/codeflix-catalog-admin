package codeflix.catalog.admin.domain.video;

import codeflix.catalog.admin.domain._share.validation.handler.ThrowsValidationHandler;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Year;
import java.util.Set;

class VideoTest {

    @Test
    void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
        //given
        final String expectedTitle = "System Design Interviews";
        final String expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final Year expectedLaunchedAt = Year.of(2022);
        final double expectedDuration = 120.10;
        final boolean expectedOpened = false;
        final boolean expectedPublished = false;
        final Rating expectedRating = Rating.L;
        final Set<CategoryID> expectedCategories = Set.of(CategoryID.unique());
        final Set<GenreID> expectedGenres = Set.of(GenreID.unique());
        final Set<CastMemberID> expectedMembers = Set.of(CastMemberID.unique());

        //when
        final Video actualVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        //then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                "Test title",
                "Lalala description",
                Year.of(1888),
                0.0,
                true,
                true,
                Rating.AGE_10,
                Set.of(),
                Set.of(),
                Set.of()
        );

        // when
        final var actualVideo = Video.with(aVideo).update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsSetVideo_shouldReturnUpdated() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aVideoMedia =
                AudioVideoMedia.with("abc", "Video.mp4", "/123/videos");

        // when
        final var actualVideo = Video.with(aVideo).setVideo(aVideoMedia);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertEquals(aVideoMedia, actualVideo.getVideo().get());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsSetTrailer_shouldReturnUpdated() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aTrailerMedia =
                AudioVideoMedia.with("abc", "Trailer.mp4", "/123/videos");

        // when
        final var actualVideo = Video.with(aVideo).setTrailer(aTrailerMedia);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertEquals(aTrailerMedia, actualVideo.getTrailer().get());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsSetBanner_shouldReturnUpdated() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aBannerMedia =
                ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        // when
        final var actualVideo = Video.with(aVideo).setBanner(aBannerMedia);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertEquals(aBannerMedia, actualVideo.getBanner().get());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsSetThumbnail_shouldReturnUpdated() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aThumbMedia =
                ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        // when
        final var actualVideo = Video.with(aVideo).setThumbnail(aThumbMedia);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertEquals(aThumbMedia, actualVideo.getThumbnail().get());
        Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }

    @Test
    void givenValidVideo_whenCallsSetThumbnailHalf_shouldReturnUpdated() {
        // given
        final var expectedTitle = "System Design Interviews";
        final var expectedDescription = """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
        final var expectedLaunchedAt = Year.of(2022);
        final var expectedDuration = 120.10;
        final var expectedOpened = false;
        final var expectedPublished = false;
        final var expectedRating = Rating.L;
        final var expectedCategories = Set.of(CategoryID.unique());
        final var expectedGenres = Set.of(GenreID.unique());
        final var expectedMembers = Set.of(CastMemberID.unique());

        final var aVideo = Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
        );

        final var aThumbMedia =
                ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

        // when
        final var actualVideo = Video.with(aVideo).setThumbnailHalf(aThumbMedia);

        // then
        Assertions.assertNotNull(actualVideo);
        Assertions.assertNotNull(actualVideo.getId());
        Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
        Assertions.assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
        Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
        Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
        Assertions.assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
        Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
        Assertions.assertEquals(expectedOpened, actualVideo.isOpened());
        Assertions.assertEquals(expectedPublished, actualVideo.isPublished());
        Assertions.assertEquals(expectedRating, actualVideo.getRating());
        Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
        Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
        Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
        Assertions.assertTrue(actualVideo.getVideo().isEmpty());
        Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
        Assertions.assertTrue(actualVideo.getBanner().isEmpty());
        Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
        Assertions.assertEquals(aThumbMedia, actualVideo.getThumbnailHalf().get());

        Assertions.assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
    }
}