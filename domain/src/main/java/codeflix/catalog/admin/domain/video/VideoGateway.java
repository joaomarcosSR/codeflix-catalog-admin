package codeflix.catalog.admin.domain.video;

import codeflix.catalog.admin.domain._share.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {
    Video create(Video aVideo);

    Video update(Video aVideo);

    void deleteById(VideoID anId);

    Optional<Video> findById(VideoID anId);

    Pagination<Video> findAll(VideoSearchQuery aQuery);
}
