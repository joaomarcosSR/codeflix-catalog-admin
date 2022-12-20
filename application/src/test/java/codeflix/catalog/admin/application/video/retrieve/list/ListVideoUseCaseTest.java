package codeflix.catalog.admin.application.video.retrieve.list;

import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain.Fixture;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain.video.VideoGateway;
import codeflix.catalog.admin.domain.video.VideoPreview;
import codeflix.catalog.admin.domain.video.VideoSearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class ListVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private ListVideosUseCaseImpl useCase;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.videoGateway);
    }

    @Test
    void givenAValidQuery_whenCallsListVideos_shouldReturnVideos() {
        // given
        final var videos = List.of(
                VideoPreview.from(Fixture.video()),
                VideoPreview.from(Fixture.video())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotalPages = 1;
        final var expectedTotalElements = 2;

        final var expectedItems = videos.stream()
                .map(VideoListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotalPages,
                expectedTotalElements,
                videos
        );

        when(this.videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                null,
                null,
                null
        );

        // when
        final var actualOutput = this.useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotalPages, actualOutput.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualOutput.totalElements());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(this.videoGateway).findAll(aQuery);
    }

    @Test
    void givenAValidQuery_whenCallsListVideosAndResultIsEmpty_shouldReturnGenres() {
        // given
        final var videos = List.<VideoPreview>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotalPages = 0;
        final var expectedTotalElements = 0;

        final var expectedItems = List.<VideoListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotalPages,
                expectedTotalElements,
                videos
        );

        when(this.videoGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                null,
                null,
                null
        );

        // when
        final var actualOutput = this.useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotalPages, actualOutput.totalPages());
        Assertions.assertEquals(expectedTotalElements, actualOutput.totalElements());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        Mockito.verify(this.videoGateway).findAll(aQuery);
    }

    @Test
    void givenAValidQuery_whenCallsListVideosAndGatewayThrowsRandomError_shouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(this.videoGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery = new VideoSearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection,
                null,
                null,
                null
        );

        // when
        final var actualOutput = Assertions.assertThrows(
                IllegalStateException.class,
                () -> this.useCase.execute(aQuery)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        Mockito.verify(this.videoGateway).findAll(aQuery);
    }
}