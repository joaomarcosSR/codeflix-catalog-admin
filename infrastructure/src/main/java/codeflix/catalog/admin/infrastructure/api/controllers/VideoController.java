package codeflix.catalog.admin.infrastructure.api.controllers;

import codeflix.catalog.admin.application.video.create.CreateVideoCommand;
import codeflix.catalog.admin.application.video.create.CreateVideoOutput;
import codeflix.catalog.admin.application.video.create.CreateVideoUseCase;
import codeflix.catalog.admin.application.video.delete.DeleteVideoUseCase;
import codeflix.catalog.admin.application.video.media.get.GetMediaCommand;
import codeflix.catalog.admin.application.video.media.get.GetMediaUseCase;
import codeflix.catalog.admin.application.video.media.get.MediaOutput;
import codeflix.catalog.admin.application.video.media.upload.UploadMediaCommand;
import codeflix.catalog.admin.application.video.media.upload.UploadMediaOutput;
import codeflix.catalog.admin.application.video.media.upload.UploadMediaUseCase;
import codeflix.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import codeflix.catalog.admin.application.video.retrieve.list.ListVideosUseCase;
import codeflix.catalog.admin.application.video.update.UpdateVideoCommand;
import codeflix.catalog.admin.application.video.update.UpdateVideoOutput;
import codeflix.catalog.admin.application.video.update.UpdateVideoUseCase;
import codeflix.catalog.admin.domain._share.exceptions.NotificationException;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;
import codeflix.catalog.admin.domain.category.value.object.CategoryID;
import codeflix.catalog.admin.domain.genre.value.object.GenreID;
import codeflix.catalog.admin.domain.resource.Resource;
import codeflix.catalog.admin.domain.video.VideoMediaType;
import codeflix.catalog.admin.domain.video.VideoResource;
import codeflix.catalog.admin.domain.video.VideoSearchQuery;
import codeflix.catalog.admin.infrastructure.api.VideoAPI;
import codeflix.catalog.admin.infrastructure.utils.HashingUtils;
import codeflix.catalog.admin.infrastructure.video.models.CreateVideoRequest;
import codeflix.catalog.admin.infrastructure.video.models.UpdateVideoRequest;
import codeflix.catalog.admin.infrastructure.video.models.VideoListResponse;
import codeflix.catalog.admin.infrastructure.video.models.VideoResponse;
import codeflix.catalog.admin.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import static codeflix.catalog.admin.domain._share.utils.CollectionUtils.mapTo;

//TODO: Remover
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class VideoController implements VideoAPI {
    private static final String VIDEOS_BASE_ENDPOINT = "/videos/";
    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;

    private final UpdateVideoUseCase updateVideoUseCase;
    private final DeleteVideoUseCase deleteVideoUseCase;
    private final ListVideosUseCase listVideosUseCase;
    private final GetMediaUseCase getMediaUseCase;
    private final UploadMediaUseCase uploadMediaUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase,
            final UpdateVideoUseCase updateVideoUseCase,
            final DeleteVideoUseCase deleteVideoUseCase,
            final ListVideosUseCase listVideosUseCase,
            final GetMediaUseCase getMediaUseCase,
            final UploadMediaUseCase uploadMediaUseCase
    ) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
        this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
        this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
        this.listVideosUseCase = Objects.requireNonNull(listVideosUseCase);
        this.getMediaUseCase = Objects.requireNonNull(getMediaUseCase);
        this.uploadMediaUseCase = Objects.requireNonNull(uploadMediaUseCase);
    }

    @Override
    public ResponseEntity<?> createFull(
            final String aTitle,
            final String aDescription,
            final Integer launchedAt,
            final Double aDuration,
            final Boolean wasOpened,
            final Boolean wasPublished,
            final String aRating,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres,
            final MultipartFile videoFile,
            final MultipartFile trailerFile,
            final MultipartFile bannerFile,
            final MultipartFile thumbFile,
            final MultipartFile thumbHalfFile
    ) {
        final CreateVideoCommand aCommand = CreateVideoCommand.with(
                aTitle,
                aDescription,
                launchedAt,
                aDuration,
                wasOpened,
                wasPublished,
                aRating,
                categories,
                genres,
                castMembers,
                this.resourceOf(videoFile),
                this.resourceOf(trailerFile),
                this.resourceOf(bannerFile),
                this.resourceOf(thumbFile),
                this.resourceOf(thumbHalfFile)
        );

        final CreateVideoOutput output = this.createVideoUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create(VIDEOS_BASE_ENDPOINT + output.id()))
                .body(output);
    }

    @Override
    public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
        final CreateVideoCommand aCommand = CreateVideoCommand.with(
                payload.title(),
                payload.description(),
                payload.yearLaunch(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final CreateVideoOutput output = this.createVideoUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create(VIDEOS_BASE_ENDPOINT + output.id()))
                .body(output);
    }

    @Override
    public VideoResponse getById(final String anId) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<?> update(final String anId, final UpdateVideoRequest payload) {
        final UpdateVideoCommand aCommand = UpdateVideoCommand.with(
                anId,
                payload.title(),
                payload.description(),
                payload.yearLaunch(),
                payload.duration(),
                payload.opened(),
                payload.published(),
                payload.rating(),
                payload.categories(),
                payload.genres(),
                payload.castMembers()
        );

        final UpdateVideoOutput output = this.updateVideoUseCase.execute(aCommand);

        return ResponseEntity.ok()
                .location(URI.create(VIDEOS_BASE_ENDPOINT + output.id()))
                .body(VideoApiPresenter.present(output));
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteVideoUseCase.execute(anId);
    }

    @Override
    public Pagination<VideoListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres
    ) {
        final VideoSearchQuery aQuery = new VideoSearchQuery(
                page,
                perPage,
                search,
                sort,
                direction,
                mapTo(castMembers, CastMemberID::from),
                mapTo(categories, CategoryID::from),
                mapTo(genres, GenreID::from)
        );

        return VideoApiPresenter.present(this.listVideosUseCase.execute(aQuery));
    }

    @Override
    public ResponseEntity<byte[]> getMediaType(final String id, final String type) {
        final MediaOutput aMedia = this.getMediaUseCase.execute(GetMediaCommand.with(id, type));
        return ResponseEntity.ok()
                .contentType(MediaType.valueOf(aMedia.contentType()))
                .contentLength(aMedia.content().length)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=%s".formatted(aMedia.name()))
                .body(aMedia.content());
    }

    @Override
    public ResponseEntity<?> uploadMediaType(final String id, final String type, final MultipartFile media) {
        final VideoMediaType mediaType = VideoMediaType.of(type)
                .orElseThrow(() -> NotificationException.with(new Error("Invalid %s for VideoMediaType.".formatted(type))));
        final UploadMediaCommand aCommand =
                UploadMediaCommand.with(id, VideoResource.with(this.resourceOf(media), mediaType));

        final UploadMediaOutput output = this.uploadMediaUseCase.execute(aCommand);

        return ResponseEntity
                .created(URI.create("%s%s/medias/%s".formatted(VIDEOS_BASE_ENDPOINT, id, type)))
                .body(VideoApiPresenter.present(output));
    }

    private Resource resourceOf(final MultipartFile part) {
        if (part == null) return null;
        try {
            return Resource.with(
                    HashingUtils.checksum(part.getBytes()),
                    part.getBytes(),
                    part.getContentType(),
                    part.getOriginalFilename()
            );
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
