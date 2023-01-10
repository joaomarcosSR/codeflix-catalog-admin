package codeflix.catalog.admin.infrastructure.video.presenters;

import codeflix.catalog.admin.application.video.retrieve.get.VideoOutput;
import codeflix.catalog.admin.domain.video.AudioVideoMedia;
import codeflix.catalog.admin.domain.video.ImageMedia;
import codeflix.catalog.admin.infrastructure.video.models.AudioVideoMediaResponse;
import codeflix.catalog.admin.infrastructure.video.models.ImageMediaResponse;
import codeflix.catalog.admin.infrastructure.video.models.VideoResponse;

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
        return new AudioVideoMediaResponse(
                media.id(),
                media.checksum(),
                media.name(),
                media.rawLocation(),
                media.encodedLocation(),
                media.status().name()
        );
    }
}
