package codeflix.catalog.admin.application.video.retrieve.get;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain.video.Video;
import codeflix.catalog.admin.domain.video.VideoGateway;
import codeflix.catalog.admin.domain.video.VideoID;

import java.util.Objects;

public class GetVideoByIdUseCaseImpl extends GetVideoByIdUseCase {
    private final VideoGateway videoGateway;

    public GetVideoByIdUseCaseImpl(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public VideoOutput execute(final String anId) {
        final VideoID aVideoId = VideoID.from(anId);
        return this.videoGateway.findById(aVideoId)
                .map(VideoOutput::from)
                .orElseThrow(() -> NotFoundException.with(Video.class, aVideoId));
    }
}
