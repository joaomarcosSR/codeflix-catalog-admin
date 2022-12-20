package codeflix.catalog.admin.application.video.media.get;

import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain.Fixture;
import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.video.MediaResourceGateway;
import codeflix.catalog.admin.domain.video.VideoID;
import codeflix.catalog.admin.domain.video.VideoResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

class GetMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private GetMediaUseCaseImpl useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.mediaResourceGateway);
    }

    @Test
    void givenVideoIdAndType_whenIsValidCmd_shouldReturnResource() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();
        final var expectedResource = Fixture.Videos.resource(expectedType);

        when(this.mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.of(VideoResource.with(expectedResource, expectedType)));

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        // when
        final var actualResult = this.useCase.execute(aCmd);

        // then
        Assertions.assertEquals(expectedResource.name(), actualResult.name());
        Assertions.assertEquals(expectedResource.content(), actualResult.content());
        Assertions.assertEquals(expectedResource.contentType(), actualResult.contentType());
    }

    @Test
    void givenVideoIdAndType_whenIsNotFound_shouldReturnNotFoundException() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedType = Fixture.Videos.mediaType();

        when(this.mediaResourceGateway.getResource(expectedId, expectedType))
                .thenReturn(Optional.empty());

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), expectedType.name());

        // when
        Assertions.assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(aCmd);
        });
    }

    @Test
    void givenVideoIdAndType_whenTypeDoesNotExists_shouldReturnNotFoundException() {
        // given
        final var expectedId = VideoID.unique();
        final var expectedErrorMessage = "Media type QUALQUER doesn't exists";

        final var aCmd = GetMediaCommand.with(expectedId.getValue(), "QUALQUER");

        // when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
            this.useCase.execute(aCmd);
        });

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}