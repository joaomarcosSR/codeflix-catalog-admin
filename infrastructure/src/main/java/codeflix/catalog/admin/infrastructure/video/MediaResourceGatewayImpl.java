package codeflix.catalog.admin.infrastructure.video;

import codeflix.catalog.admin.domain.resource.Resource;
import codeflix.catalog.admin.domain.video.*;
import codeflix.catalog.admin.infrastructure.configuration.properties.storage.StorageProperties;
import codeflix.catalog.admin.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MediaResourceGatewayImpl implements MediaResourceGateway {

    private final String fileNamePattern;
    private final String locationPattern;
    private final StorageService storageService;

    public MediaResourceGatewayImpl(final StorageProperties props, final StorageService storageService) {
        this.fileNamePattern = props.getFileNamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final String filePath = this.getFilePath(anId, videoResource);
        final Resource aResource = videoResource.resource();
        this.store(filePath, aResource);
        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filePath);
    }

    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource videoResource) {
        final String filePath = this.getFilePath(anId, videoResource);
        final Resource aResource = videoResource.resource();
        this.store(filePath, aResource);
        return ImageMedia.with(aResource.checksum(), aResource.name(), filePath);
    }

    @Override
    public void clearResources(final VideoID anId) {
        final List<String> names = this.storageService.list(this.getFolder(anId));
        this.storageService.deleteAll(names);
    }

    @Override
    public Optional<VideoResource> getResource(final VideoID anId, final VideoMediaType aType) {
        final String filePath = this.getFilePath(anId, aType);
        return this.storageService.get(filePath)
                .map(it -> VideoResource.with(it, aType));
    }

    private void store(final String filePath, final Resource aResource) {
        this.storageService.store(filePath, aResource);
    }

    private String getFilePath(final VideoID anId, final VideoResource aResource) {
        return this.getFilePath(anId, aResource.type());
    }

    private String getFilePath(final VideoID anId, final VideoMediaType aType) {
        return this.getFolder(anId)
                .concat("/")
                .concat(this.getFileName(aType));
    }

    private String getFileName(final VideoMediaType aType) {
        return this.fileNamePattern.replace("{type}", aType.name());
    }

    private String getFolder(final VideoID anId) {
        return this.locationPattern.replace("{videoId}", anId.getValue());
    }
}
