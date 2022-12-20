package codeflix.catalog.admin.infrastructure.video.persistence;

import codeflix.catalog.admin.domain.category.value.object.CategoryID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "VideoCategory")
@Table(name = "videos_categories")
public class VideoCategoryJpaEntity {
    @EmbeddedId
    private VideoCategoryID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCategoryJpaEntity() {
    }

    private VideoCategoryJpaEntity(final VideoCategoryID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCategoryJpaEntity from(final VideoJpaEntity video, final CategoryID category) {
        return new VideoCategoryJpaEntity(
                VideoCategoryID.from(video.getId(), category.getValue()),
                video
        );
    }

    public VideoCategoryID getId() {
        return this.id;
    }

    public void setId(final VideoCategoryID id) {
        this.id = id;
    }

    public VideoJpaEntity getVideo() {
        return this.video;
    }

    public void setVideo(final VideoJpaEntity video) {
        this.video = video;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final VideoCategoryJpaEntity that = (VideoCategoryJpaEntity) o;
        return this.getId().equals(that.getId()) && this.getVideo().equals(that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getVideo());
    }
}
