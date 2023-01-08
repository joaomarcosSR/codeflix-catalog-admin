package codeflix.catalog.admin.application.video.media.upload;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.video.*;

import java.util.Objects;

public class UploadMediaUseCaseImpl extends UploadMediaUseCase {
    private final MediaResourceGateway mediaResourceGateway;
    private final VideoGateway videoGateway;

    public UploadMediaUseCaseImpl(final MediaResourceGateway mediaResourceGateway, final VideoGateway videoGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public UploadMediaOutput execute(final UploadMediaCommand aCommand) {
        final VideoID anId = VideoID.from(aCommand.videoId());
        final VideoResource videoResource = aCommand.videoResource();
        final Video aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> this.notFound(anId));

        switch (videoResource.type()) {
            case VIDEO -> aVideo.updateVideoMedia(this.mediaResourceGateway.storeAudioVideo(anId, videoResource));
            case TRAILER -> aVideo.updateTrailerMedia(this.mediaResourceGateway.storeAudioVideo(anId, videoResource));
            case BANNER -> aVideo.updateBannerMedia(this.mediaResourceGateway.storeImage(anId, videoResource));
            case THUMBNAIL -> aVideo.updateThumbnailMedia(this.mediaResourceGateway.storeImage(anId, videoResource));
            case THUMBNAIL_HALF ->
                    aVideo.updateThumbnailHalfMedia(this.mediaResourceGateway.storeImage(anId, videoResource));
        }

        return UploadMediaOutput.with(this.videoGateway.update(aVideo), videoResource.type());
    }

    private NotFoundException notFound(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
