package codeflix.catalog.admin.application.video.media.get;

import codeflix.catalog.admin.domain.resource.Resource;
import codeflix.catalog.admin.domain.video.VideoResource;

public record MediaOutput(
        byte[] content,
        String contentType,
        String name
) {
    public static MediaOutput with(final VideoResource videoResource) {
        final Resource aResource = videoResource.resource();
        return new MediaOutput(
                aResource.content(),
                aResource.contentType(),
                aResource.name()
        );
    }
}
