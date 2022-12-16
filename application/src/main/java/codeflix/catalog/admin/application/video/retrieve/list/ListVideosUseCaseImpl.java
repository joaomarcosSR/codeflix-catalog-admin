package codeflix.catalog.admin.application.video.retrieve.list;

import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain.video.VideoGateway;
import codeflix.catalog.admin.domain.video.VideoSearchQuery;

import java.util.Objects;

public class ListVideosUseCaseImpl extends ListVideosUseCase {
    private final VideoGateway videoGateway;

    public ListVideosUseCaseImpl(final VideoGateway videoGateway) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
    }

    @Override
    public Pagination<VideoListOutput> execute(final VideoSearchQuery aQuery) {
        return this.videoGateway.findAll(aQuery)
                .map(VideoListOutput::from);
    }
}
