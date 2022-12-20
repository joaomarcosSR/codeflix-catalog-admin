package codeflix.catalog.admin.application.video.media.upload;

import codeflix.catalog.admin.application.UseCaseTest;
import codeflix.catalog.admin.domain.Fixture;
import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.video.MediaResourceGateway;
import codeflix.catalog.admin.domain.video.VideoGateway;
import codeflix.catalog.admin.domain.video.VideoMediaType;
import codeflix.catalog.admin.domain.video.VideoResource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UploadMediaUseCaseTest extends UseCaseTest {

    @InjectMocks
    private UploadMediaUseCaseImpl useCase;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Mock
    private VideoGateway videoGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(this.mediaResourceGateway, this.videoGateway);
    }

    @Test
    void givenCmdToUpload_whenIsValid_shouldUpdateVideoMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.VIDEO;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(aVideo));

        when(this.mediaResourceGateway.storeAudioVideo(expectedId, expectedVideoResource))
                .thenReturn(expectedMedia);

        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(aCmd);

        // then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(this.videoGateway).update(argThat(actualVideo ->
                Objects.equals(expectedMedia, actualVideo.getVideo().get())
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenCmdToUpload_whenIsValid_shouldUpdateTrailerMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.TRAILER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(aVideo));

        when(this.mediaResourceGateway.storeAudioVideo(expectedId, expectedVideoResource))
                .thenReturn(expectedMedia);

        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(aCmd);

        // then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(this.videoGateway).update(argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getTrailer().get())
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenCmdToUpload_whenIsValid_shouldUpdateBannerMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.BANNER;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(aVideo));

        when(this.mediaResourceGateway.storeImage(expectedId, expectedVideoResource))
                .thenReturn(expectedMedia);

        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(aCmd);

        // then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(this.videoGateway).update(argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getBanner().get())
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(aVideo));

        when(this.mediaResourceGateway.storeImage(expectedId, expectedVideoResource))
                .thenReturn(expectedMedia);

        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(aCmd);

        // then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(this.videoGateway).update(argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getThumbnail().get())
                        && actualVideo.getThumbnailHalf().isEmpty()
        ));
    }

    @Test
    void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailHalfMediaAndPersistIt() {
        // given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);
        final var expectedMedia = Fixture.Videos.image(expectedType);

        when(this.videoGateway.findById(expectedId))
                .thenReturn(Optional.of(aVideo));

        when(this.mediaResourceGateway.storeImage(expectedId, expectedVideoResource))
                .thenReturn(expectedMedia);

        when(this.videoGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualOutput = this.useCase.execute(aCmd);

        // then
        Assertions.assertEquals(expectedType, actualOutput.mediaType());
        Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

        verify(this.videoGateway).update(argThat(actualVideo ->
                actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && Objects.equals(expectedMedia, actualVideo.getThumbnailHalf().get())
        ));
    }

    @Test
    void givenCmdToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
        // given
        final var aVideo = Fixture.Videos.systemDesign();
        final var expectedId = aVideo.getId();
        final var expectedType = VideoMediaType.THUMBNAIL_HALF;
        final var expectedResource = Fixture.Videos.resource(expectedType);
        final var expectedVideoResource = VideoResource.with(expectedResource, expectedType);

        final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

        when(this.videoGateway.findById(any()))
                .thenReturn(Optional.empty());

        final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

        // when
        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> this.useCase.execute(aCmd)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}