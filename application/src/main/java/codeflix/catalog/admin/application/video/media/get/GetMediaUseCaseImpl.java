package codeflix.catalog.admin.application.video.media.get;

import codeflix.catalog.admin.domain._share.exceptions.NotFoundException;
import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain.video.MediaResourceGateway;
import codeflix.catalog.admin.domain.video.VideoID;
import codeflix.catalog.admin.domain.video.VideoMediaType;

import java.util.Objects;

public class GetMediaUseCaseImpl extends GetMediaUseCase {
    private final MediaResourceGateway mediaResourceGateway;

    public GetMediaUseCaseImpl(final MediaResourceGateway mediaResourceGateway) {
        this.mediaResourceGateway = Objects.requireNonNull(mediaResourceGateway);
    }

    @Override
    public MediaOutput execute(final GetMediaCommand aCommand) {
        final VideoID anId = VideoID.from(aCommand.videoId());
        final VideoMediaType aType = VideoMediaType.of(aCommand.type())
                .orElseThrow(() -> this.typeNotFound(aCommand.type()));

        return this.mediaResourceGateway.getResource(anId, aType)
                .map(MediaOutput::with)
                .orElseThrow(() -> this.notFound(aCommand.videoId(), aCommand.type()));
    }

    private NotFoundException notFound(final String anId, final String aType) {
        return NotFoundException.with(new Error("Resource %s not found for video %s".formatted(aType, anId)));
    }

    private NotFoundException typeNotFound(final String aType) {
        return NotFoundException.with(new Error("Media type %s doesn't exists".formatted(aType)));
    }
}
