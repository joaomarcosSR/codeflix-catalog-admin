package codeflix.catalog.admin.infrastructure.configuration.usecases;

import codeflix.catalog.admin.application.video.create.CreateVideoUseCase;
import codeflix.catalog.admin.application.video.create.CreateVideoUseCaseImpl;
import codeflix.catalog.admin.application.video.delete.DeleteVideoUseCase;
import codeflix.catalog.admin.application.video.delete.DeleteVideoUseCaseImpl;
import codeflix.catalog.admin.application.video.media.get.GetMediaUseCase;
import codeflix.catalog.admin.application.video.media.get.GetMediaUseCaseImpl;
import codeflix.catalog.admin.application.video.media.update.UpdateMediaStatusUseCase;
import codeflix.catalog.admin.application.video.media.update.UpdateMediaStatusUseCaseImpl;
import codeflix.catalog.admin.application.video.media.upload.UploadMediaUseCase;
import codeflix.catalog.admin.application.video.media.upload.UploadMediaUseCaseImpl;
import codeflix.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCase;
import codeflix.catalog.admin.application.video.retrieve.get.GetVideoByIdUseCaseImpl;
import codeflix.catalog.admin.application.video.retrieve.list.ListVideosUseCase;
import codeflix.catalog.admin.application.video.retrieve.list.ListVideosUseCaseImpl;
import codeflix.catalog.admin.application.video.update.UpdateVideoUseCase;
import codeflix.catalog.admin.application.video.update.UpdateVideoUseCaseImpl;
import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;
import codeflix.catalog.admin.domain.category.gateway.CategoryGateway;
import codeflix.catalog.admin.domain.genre.gateway.GenreGateway;
import codeflix.catalog.admin.domain.video.MediaResourceGateway;
import codeflix.catalog.admin.domain.video.VideoGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class VideoUseCaseConfig {
    private final VideoGateway videoGateway;
    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;
    private final CastMemberGateway castMemberGateway;
    private final MediaResourceGateway mediaResourceGateway;

    public VideoUseCaseConfig(
            final VideoGateway videoGateway,
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway,
            final CastMemberGateway castMemberGateway,
            final MediaResourceGateway mediaResourceGateway
    ) {
        this.videoGateway = Objects.requireNonNull(videoGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Bean
    CreateVideoUseCase createVideoUseCase() {
        return new CreateVideoUseCaseImpl(
                this.videoGateway,
                this.categoryGateway,
                this.genreGateway,
                this.castMemberGateway,
                this.mediaResourceGateway
        );
    }

    @Bean
    GetVideoByIdUseCase getVideoByIdUseCase() {
        return new GetVideoByIdUseCaseImpl(this.videoGateway);
    }

    @Bean
    UpdateVideoUseCase updateVideoUseCase() {
        return new UpdateVideoUseCaseImpl(
                this.videoGateway,
                this.categoryGateway,
                this.genreGateway,
                this.castMemberGateway,
                this.mediaResourceGateway
        );
    }

    @Bean
    DeleteVideoUseCase deleteVideoUseCase() {
        return new DeleteVideoUseCaseImpl(this.videoGateway, this.mediaResourceGateway);
    }

    @Bean
    ListVideosUseCase listVideosUseCase() {
        return new ListVideosUseCaseImpl(this.videoGateway);
    }

    @Bean
    GetMediaUseCase getMediaUseCase() {
        return new GetMediaUseCaseImpl(this.mediaResourceGateway);
    }

    @Bean
    UploadMediaUseCase uploadMediaUseCase() {
        return new UploadMediaUseCaseImpl(this.mediaResourceGateway, this.videoGateway);
    }

    @Bean
    UpdateMediaStatusUseCase updateMediaStatusUseCase() {
        return new UpdateMediaStatusUseCaseImpl(this.videoGateway);
    }

}
