package codeflix.catalog.admin.infrastructure.video.presenters;

import codeflix.catalog.admin.application.video.media.upload.UploadMediaOutput;
import codeflix.catalog.admin.application.video.retrieve.get.VideoOutput;
import codeflix.catalog.admin.application.video.retrieve.list.VideoListOutput;
import codeflix.catalog.admin.application.video.update.UpdateVideoOutput;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain.video.AudioVideoMedia;
import codeflix.catalog.admin.domain.video.ImageMedia;
import codeflix.catalog.admin.infrastructure.video.models.*;

public interface VideoApiPresenter {
    static VideoResponse present(final VideoOutput output) {
        return new VideoResponse(
                output.id(),
                output.title(),
                output.description(),
                output.launchedAt(),
                output.duration(),
                output.opened(),
                output.published(),
                output.rating().getName(),
                output.createdAt(),
                output.updatedAt(),
                present(output.video()),
                present(output.trailer()),
                present(output.banner()),
                present(output.thumbnail()),
                present(output.thumbnailHalf()),
                output.categories(),
                output.genres(),
                output.castMembers()
        );
    }

    static ImageMediaResponse present(final ImageMedia image) {
        if (image == null) return null;

        return new ImageMediaResponse(
                image.id(),
                image.checksum(),
                image.name(),
                image.location()
        );
    }

    static AudioVideoMediaResponse present(final AudioVideoMedia media) {
        if (media == null) return null;

        return new AudioVideoMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }

    static UpdateVideoResponse present(final UpdateVideoOutput output) {
        return UpdateVideoResponse.with(output.id());
    }

    static VideoListResponse present(final VideoListOutput output) {
        return new VideoListResponse(
                output.id(),
                output.title(),
                output.description(),
                output.createdAt(),
                output.updatedAt()
        );
    }

    static Pagination<VideoListResponse> present(final Pagination<VideoListOutput> page) {
        return page.map(VideoApiPresenter::present);
    }

    static UploadMediaResponse present(final UploadMediaOutput output) {
        return new UploadMediaResponse(output.videoId(), output.mediaType());
    }
}
