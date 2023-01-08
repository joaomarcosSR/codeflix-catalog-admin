package codeflix.catalog.admin.domain.video;

import codeflix.catalog.admin.domain._share.entity.AggregateRoot;
import codeflix.catalog.admin.domain._share.utils.InstantUtils;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.events.DomainEvent;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;

import java.time.Instant;
import java.time.Year;
import java.util.*;

import static codeflix.catalog.admin.domain.video.VideoMediaType.TRAILER;
import static codeflix.catalog.admin.domain.video.VideoMediaType.VIDEO;
import static java.util.Collections.emptyList;

public class Video extends AggregateRoot<VideoID> {
    private String title;
    private String description;
    private Year launchedAt;
    private double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private final Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> castMembers;

    private Video(
            final VideoID anId,
            final List<DomainEvent> domainEvents,
            final String aTitle,
            final String aDescription,
            final Year aLaunchedYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Instant aCreateDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumbnail,
            final ImageMedia aThumbnailHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        super(anId, domainEvents);
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedYear;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.createdAt = aCreateDate;
        this.updatedAt = aUpdateDate;
        this.banner = aBanner;
        this.thumbnail = aThumbnail;
        this.thumbnailHalf = aThumbnailHalf;
        this.trailer = aTrailer;
        this.video = aVideo;
        this.categories = categories;
        this.genres = genres;
        this.castMembers = members;
    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        final Instant now = InstantUtils.now();
        final VideoID anId = VideoID.unique();
        return new Video(
                anId,
                emptyList(),
                aTitle,
                aDescription,
                aLaunchedYear,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                members
        );
    }

    public static Video with(final Video aVideo) {
        return new Video(
                aVideo.getId(),
                aVideo.getDomainEvents(),
                aVideo.getTitle(),
                aVideo.getDescription(),
                aVideo.getLaunchedAt(),
                aVideo.getDuration(),
                aVideo.isOpened(),
                aVideo.isPublished(),
                aVideo.getRating(),
                aVideo.getCreatedAt(),
                aVideo.getUpdatedAt(),
                aVideo.getBanner().orElse(null),
                aVideo.getThumbnail().orElse(null),
                aVideo.getThumbnailHalf().orElse(null),
                aVideo.getTrailer().orElse(null),
                aVideo.getVideo().orElse(null),
                new HashSet<>(aVideo.getCategories()),
                new HashSet<>(aVideo.getGenres()),
                new HashSet<>(aVideo.getCastMembers())
        );
    }

    public static Video with(
            final VideoID anId,
            final String aTitle,
            final String aDescription,
            final Year aLaunchedYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Instant aCreateDate,
            final Instant aUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumbnail,
            final ImageMedia aThumbnailHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        return new Video(
                anId,
                emptyList(),
                aTitle,
                aDescription,
                aLaunchedYear,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                aCreateDate,
                aUpdateDate,
                aBanner,
                aThumbnail,
                aThumbnailHalf,
                aTrailer,
                aVideo,
                categories,
                genres,
                members
        );
    }

    @Override
    public void validate(final ValidationHandler aHandler) {
        new VideoValidator(this, aHandler).validate();
    }


    public Video update(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedYear,
            final double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedYear;
        this.duration = aDuration;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.rating = aRating;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setCastMembers(members);
        this.updatedAt = InstantUtils.now();

        return this;
    }

    public Video updateBannerMedia(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateThumbnailMedia(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateThumbnailHalfMedia(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Video updateTrailerMedia(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
        this.onAudioVideoMediaUpdated(trailer);
        return this;
    }

    public Video updateVideoMedia(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = InstantUtils.now();
        this.onAudioVideoMediaUpdated(video);
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public Year getLaunchedAt() {
        return this.launchedAt;
    }

    public double getDuration() {
        return this.duration;
    }

    public Rating getRating() {
        return this.rating;
    }

    public boolean isOpened() {
        return this.opened;
    }

    public boolean isPublished() {
        return this.published;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(this.banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(this.thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(this.thumbnailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(this.trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(this.video);
    }

    public Set<CategoryID> getCategories() {
        return this.convertToUnmodifiableOrEmptySet(this.categories);
    }

    public Set<GenreID> getGenres() {
        return this.convertToUnmodifiableOrEmptySet(this.genres);
    }

    public Set<CastMemberID> getCastMembers() {
        return this.convertToUnmodifiableOrEmptySet(this.castMembers);
    }

    private void setCategories(final Set<CategoryID> categories) {
        this.categories = this.convertToModifiableSet(categories);
        this.updatedAt = InstantUtils.now();
    }

    private void setGenres(final Set<GenreID> genres) {
        this.genres = this.convertToModifiableSet(genres);
        this.updatedAt = InstantUtils.now();
    }

    private void setCastMembers(final Set<CastMemberID> castMembers) {
        this.castMembers = this.convertToModifiableSet(castMembers);
        this.updatedAt = InstantUtils.now();
    }

    private <T> Set<T> convertToModifiableSet(final Set<T> values) {
        return values != null ? new HashSet<>(values) : new HashSet<>();
    }

    private <T> Set<T> convertToUnmodifiableOrEmptySet(final Set<T> values) {
        return values != null ? Collections.unmodifiableSet(values) : Collections.emptySet();
    }

    public Video processing(final VideoMediaType aType) {
        if (VIDEO == aType) this.getVideo().ifPresent(media -> this.updateVideoMedia(media.processing()));
        else if (TRAILER == aType) this.getTrailer().ifPresent(media -> this.updateTrailerMedia(media.processing()));
        return this;
    }

    public Video completed(final VideoMediaType aType, final String encodedPath) {
        if (VIDEO == aType) this.getVideo().ifPresent(media -> this.updateVideoMedia(media.completed(encodedPath)));
        else if (TRAILER == aType)
            this.getTrailer().ifPresent(media -> this.updateTrailerMedia(media.completed(encodedPath)));
        return this;
    }

    private void onAudioVideoMediaUpdated(final AudioVideoMedia media) {
        if (media != null && media.isPendingEncode())
            this.registerEvent(VideoMediaCreated.with(this.getId().getValue(), media.rawLocation()));
    }
}
