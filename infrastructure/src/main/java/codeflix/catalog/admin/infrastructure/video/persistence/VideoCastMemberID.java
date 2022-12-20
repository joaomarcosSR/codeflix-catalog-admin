package codeflix.catalog.admin.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoCastMemberID implements Serializable {
    @Column(name = "video_id", nullable = false)
    private String videoId;
    @Column(name = "cast_member_id", nullable = false)
    private String castMemberId;

    public VideoCastMemberID() {
    }

    private VideoCastMemberID(final String videoId, final String castMemberId) {
        this.videoId = videoId;
        this.castMemberId = castMemberId;
    }

    public static VideoCastMemberID from(final String videoId, final String castMemberId) {
        return new VideoCastMemberID(videoId, castMemberId);
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public String getCastMemberId() {
        return this.castMemberId;
    }

    public void setCastMemberId(final String castMemberId) {
        this.castMemberId = castMemberId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final VideoCastMemberID that = (VideoCastMemberID) o;
        return this.getVideoId().equals(that.getVideoId()) && this.getCastMemberId().equals(that.getCastMemberId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getVideoId(), this.getCastMemberId());
    }
}
