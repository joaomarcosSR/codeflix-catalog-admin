package codeflix.catalog.admin.application.video.update;

import codeflix.catalog.admin.domain.resource.Resource;

import java.util.Optional;
import java.util.Set;

public record UpdateVideoCommand(
        String id,
        String title,
        String description,
        Integer launchedAt,
        Double duration,
        Boolean opened,
        Boolean published,
        String rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> members,
        Resource video,
        Resource trailer,
        Resource banner,
        Resource thumbnail,
        Resource thumbnailHalf
) {

    public static UpdateVideoCommand with(
            final String anId,
            final String aTitle,
            final String aDescription,
            final Integer aLaunchDate,
            final Double aDuration,
            final Boolean wasOpened,
            final Boolean wasPublished,
            final String rating,
            final Set<String> categories,
            final Set<String> genres,
            final Set<String> members,
            final Resource aVideo,
            final Resource aTrailer,
            final Resource aBanner,
            final Resource aThumbnail,
            final Resource aThumbnailHalf
    ) {
        return new UpdateVideoCommand(
                anId,
                aTitle,
                aDescription,
                aLaunchDate,
                aDuration,
                wasOpened,
                wasPublished,
                rating,
                categories,
                genres,
                members,
                aVideo,
                aTrailer,
                aBanner,
                aThumbnail,
                aThumbnailHalf
        );
    }

    public Optional<Resource> getVideo() {
        return Optional.ofNullable(this.video);
    }

    public Optional<Resource> getTrailer() {
        return Optional.ofNullable(this.trailer);
    }

    public Optional<Resource> getBanner() {
        return Optional.ofNullable(this.banner);
    }

    public Optional<Resource> getThumbnail() {
        return Optional.ofNullable(this.thumbnail);
    }

    public Optional<Resource> getThumbnailHalf() {
        return Optional.ofNullable(this.thumbnailHalf);
    }
}