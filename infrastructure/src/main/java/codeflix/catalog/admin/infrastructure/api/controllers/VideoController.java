package codeflix.catalog.admin.infrastructure.api.controllers;

import codeflix.catalog.admin.application.video.create.CreateVideoCommand;
import codeflix.catalog.admin.application.video.create.CreateVideoOutput;
import codeflix.catalog.admin.application.video.create.CreateVideoUseCase;
import codeflix.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import codeflix.catalog.admin.domain.resource.Resource;
import codeflix.catalog.admin.infrastructure.api.VideoAPI;
import codeflix.catalog.admin.infrastructure.utils.HashingUtils;
import codeflix.catalog.admin.infrastructure.video.models.CreateVideoRequest;
import codeflix.catalog.admin.infrastructure.video.models.VideoResponse;
import codeflix.catalog.admin.infrastructure.video.presenters.VideoApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

//TODO: Remover
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
public class VideoController implements VideoAPI {
    private final CreateVideoUseCase createVideoUseCase;
    private final GetVideoByIdUseCase getVideoByIdUseCase;

    public VideoController(
            final CreateVideoUseCase createVideoUseCase,
            final GetVideoByIdUseCase getVideoByIdUseCase
    ) {
        this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
        this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
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

        return ResponseEntity.created(URI.create("/videos/" + output.id()))
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

        return ResponseEntity.created(URI.create("/videos/" + output.id()))
                .body(output);
    }

    @Override
    public VideoResponse getById(final String anId) {
        return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
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
