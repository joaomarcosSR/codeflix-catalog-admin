package codeflix.catalog.admin.domain.video;

import java.util.Optional;

public interface MediaResourceGateway {
    AudioVideoMedia storeAudioVideo(VideoID anId, VideoResource videoResource);

    ImageMedia storeImage(VideoID anId, VideoResource videoResource);

    void clearResources(VideoID anId);

    Optional<VideoResource> getResource(VideoID anId, VideoMediaType aType);
}
