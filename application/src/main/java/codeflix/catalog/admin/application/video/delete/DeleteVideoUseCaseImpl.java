package codeflix.catalog.admin.application.video.delete;

import codeflix.catalog.admin.domain.video.MediaResourceGateway;
import codeflix.catalog.admin.domain.video.VideoGateway;
import codeflix.catalog.admin.domain.video.VideoID;

import java.util.Objects;

public class DeleteVideoUseCaseImpl extends DeleteVideoUseCase {
    private final VideoGateway videoGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public DeleteVideoUseCaseImpl(final VideoGateway videoGateway, final MediaResourceGateway mediaResourceGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public void execute(final String anId) {
        final VideoID videoID = VideoID.from(anId);
        this.videoGateway.deleteById(videoID);
        this.mediaResourceGateway.clearResources(videoID);
    }
}
