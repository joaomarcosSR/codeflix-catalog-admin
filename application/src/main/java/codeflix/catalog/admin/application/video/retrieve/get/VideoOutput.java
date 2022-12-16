package codeflix.catalog.admin.application.video.retrieve.get;

import codeflix.catalog.admin.domain._share.utils.CollectionUtils;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;
import codeflix.catalog.admin.domain.video.AudioVideoMedia;
import codeflix.catalog.admin.domain.video.ImageMedia;
import codeflix.catalog.admin.domain.video.Rating;
import codeflix.catalog.admin.domain.video.Video;

import java.time.Instant;
import java.util.Set;

public record VideoOutput(
        String id,
        Instant createdAt,
        Instant updatedAt,
        String title,
        String description,
        int launchedAt,
        double duration,
        boolean opened,
        boolean published,
        Rating rating,
        Set<String> categories,
        Set<String> genres,
        Set<String> castMembers,
        AudioVideoMedia video,
        AudioVideoMedia trailer,
        ImageMedia banner,
        ImageMedia thumbnail,
        ImageMedia thumbnailHalf
) {

    public static VideoOutput from(final Video aVideo) {
        return new VideoOutput(
                aVideo.getId().getValue(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt().getValue(),
                aVideo.getDuration(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getRating(),
                CollectionUtils.mapTo(aVideo.getCategories(), CategoryID::getValue),
                CollectionUtils.mapTo(aVideo.getGenres(), GenreID::getValue),
                CollectionUtils.mapTo(aVideo.getCastMembers(), CastMemberID::getValue),
                aVideo.getVideo().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null)
        );
    }
}