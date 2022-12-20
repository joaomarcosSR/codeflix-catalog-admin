package codeflix.catalog.admin.infrastructure.video.persistence;

import codeflix.catalog.admin.domain.genre.value.object.GenreID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "VideoGenre")
@Table(name = "videos_genres")
public class VideoGenreJpaEntity {
    @EmbeddedId
    private VideoGenreID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoGenreJpaEntity() {
    }

    private VideoGenreJpaEntity(final VideoGenreID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoGenreJpaEntity from(final VideoJpaEntity video, final GenreID genre) {
        return new VideoGenreJpaEntity(
                VideoGenreID.from(video.getId(), genre.getValue()),
                video
        );
    }

    public VideoGenreID getId() {
        return this.id;
    }

    public void setId(final VideoGenreID id) {
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
        final VideoGenreJpaEntity that = (VideoGenreJpaEntity) o;
        return this.getId().equals(that.getId()) && this.getVideo().equals(that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getVideo());
    }
}
