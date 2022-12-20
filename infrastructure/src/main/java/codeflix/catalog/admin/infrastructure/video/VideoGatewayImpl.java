package codeflix.catalog.admin.infrastructure.video;

import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.value.object.Identifier;
import codeflix.catalog.admin.domain.video.*;
import codeflix.catalog.admin.infrastructure.utils.SqlUtils;
import codeflix.catalog.admin.infrastructure.video.persistence.VideoJpaEntity;
import codeflix.catalog.admin.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import static codeflix.catalog.admin.domain._share.utils.CollectionUtils.mapTo;
import static codeflix.catalog.admin.domain._share.utils.CollectionUtils.nullIfEmpty;
import static codeflix.catalog.admin.infrastructure.utils.SqlUtils.upper;

@Component
public class VideoGatewayImpl implements VideoGateway {

    private final VideoRepository videoRepository;

    public VideoGatewayImpl(final VideoRepository videoRepository) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return this.save(aVideo);
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return this.save(aVideo);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final String aVideoId = anId.getValue();
        if (this.videoRepository.existsById(aVideoId)) this.videoRepository.deleteById(aVideoId);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final PageRequest page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final Page<VideoPreview> actualPage = this.videoRepository.findAll(
                SqlUtils.like(upper(aQuery.terms())),
                this.toString(aQuery.castMembers()),
                this.toString(aQuery.categories()),
                this.toString(aQuery.genres()),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalPages(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    private Video save(final Video aVideo) {
        return this.videoRepository.save(VideoJpaEntity.from(aVideo))
                .toAggregate();
    }

    private Set<String> toString(final Set<? extends Identifier> values) {
        return nullIfEmpty(mapTo(values, Identifier::getValue));
    }
}
