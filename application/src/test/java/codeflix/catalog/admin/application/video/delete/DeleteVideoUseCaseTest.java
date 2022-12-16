package codeflix.catalog.admin.application.video.delete;

import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain._share.exceptions.InternalErrorException;
import codeflix.catalog.admin.domain.video.MediaResourceGateway;
import codeflix.catalog.admin.domain.video.VideoGateway;
import codeflix.catalog.admin.domain.video.VideoID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DeleteVideoUseCaseImpl useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.videoGateway, this.mediaResourceGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        // given
        final var expectedId = VideoID.unique();

        doNothing()
                .when(this.videoGateway).deleteById(expectedId);

        doNothing()
                .when(this.mediaResourceGateway).clearResources(expectedId);

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        // given
        final var expectedId = VideoID.from("1231");

        doNothing()
                .when(this.videoGateway).deleteById(expectedId);

        // when
        Assertions.assertDoesNotThrow(() -> this.useCase.execute(expectedId.getValue()));
    }

    @Test
    void givenAValidId_whenCallsDeleteVideoAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var expectedId = VideoID.from("1231");

        doThrow(InternalErrorException.with("Error on delete video", new RuntimeException()))
                .when(this.videoGateway).deleteById(expectedId);

        // when
        Assertions.assertThrows(
                InternalErrorException.class,
                () -> this.useCase.execute(expectedId.getValue())
        );
    }
}