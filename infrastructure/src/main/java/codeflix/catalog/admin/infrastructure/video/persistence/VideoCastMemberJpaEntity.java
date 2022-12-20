package codeflix.catalog.admin.infrastructure.video.persistence;

import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "VideoCastMember")
@Table(name = "videos_cast_members")
public class VideoCastMemberJpaEntity {
    @EmbeddedId
    private VideoCastMemberID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("videoId")
    private VideoJpaEntity video;

    public VideoCastMemberJpaEntity() {
    }

    private VideoCastMemberJpaEntity(final VideoCastMemberID id, final VideoJpaEntity video) {
        this.id = id;
        this.video = video;
    }

    public static VideoCastMemberJpaEntity from(final VideoJpaEntity video, final CastMemberID castMember) {
        return new VideoCastMemberJpaEntity(
                VideoCastMemberID.from(video.getId(), castMember.getValue()),
                video
        );
    }

    public VideoCastMemberID getId() {
        return this.id;
    }

    public void setId(final VideoCastMemberID id) {
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
        final VideoCastMemberJpaEntity that = (VideoCastMemberJpaEntity) o;
        return this.getId().equals(that.getId()) && this.getVideo().equals(that.getVideo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId(), this.getVideo());
    }
}
