package codeflix.catalog.admin.application.video.media.update;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.video.*;

import java.util.Objects;

import static codeflix.catalog.admin.domain.video.VideoMediaType.TRAILER;
import static codeflix.catalog.admin.domain.video.VideoMediaType.VIDEO;

public class UpdateMediaStatusUseCaseImpl extends UpdateMediaStatusUseCase {
    private final VideoGateway videoGateway;

    public UpdateMediaStatusUseCaseImpl(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public void execute(final UpdateMediaStatusCommand aCommand) {
        final VideoID anId = VideoID.from(aCommand.videoId());
        final String aResourceId = aCommand.resourceId();
        final String folder = aCommand.folder();
        final String filename = aCommand.fileName();

        final Video aVideo = this.videoGateway.findById(anId)
                .orElseThrow(() -> this.notFound(anId));

        final var encodedPath = "%s/%s".formatted(folder, filename);

        if (this.matches(aResourceId, aVideo.getVideo().orElse(null)))
            this.updateVideo(VIDEO, aCommand.status(), aVideo, encodedPath);
        else if (this.matches(aResourceId, aVideo.getTrailer().orElse(null)))
            this.updateVideo(TRAILER, aCommand.status(), aVideo, encodedPath);
    }

    private void updateVideo(final VideoMediaType aType, final MediaStatus aStatus, final Video aVideo, final String encodedPath) {
        switch (aStatus) {
            case PENDING -> {
            }
            case PROCESSING -> aVideo.processing(aType);
            case COMPLETED -> aVideo.completed(aType, encodedPath);
        }

        this.videoGateway.update(aVideo);
    }

    private boolean matches(final String anId, final AudioVideoMedia aMedia) {
        if (aMedia == null) return false;
        return aMedia.id().equals(anId);
    }

    private NotFoundException notFound(final VideoID anId) {
        return NotFoundException.with(Video.class, anId);
    }
}
