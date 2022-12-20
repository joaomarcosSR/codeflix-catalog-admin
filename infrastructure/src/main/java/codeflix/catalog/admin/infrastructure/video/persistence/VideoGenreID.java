package codeflix.catalog.admin.infrastructure.video.persistence;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class VideoGenreID implements Serializable {
    @Column(name = "video_id", nullable = false)
    private String videoId;

    @Column(name = "genre_id", nullable = false)
    private String genreId;

    public VideoGenreID() {
    }

    private VideoGenreID(final String videoId, final String genreId) {
        this.videoId = videoId;
        this.genreId = genreId;
    }

    public static VideoGenreID from(final String videoId, final String genreId) {
        return new VideoGenreID(videoId, genreId);
    }

    public String getVideoId() {
        return this.videoId;
    }

    public void setVideoId(final String videoId) {
        this.videoId = videoId;
    }

    public String getGenreId() {
        return this.genreId;
    }

    public void setGenreId(final String genreId) {
        this.genreId = genreId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final VideoGenreID that = (VideoGenreID) o;
        return this.getVideoId().equals(that.getVideoId()) && this.getGenreId().equals(that.getGenreId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getVideoId(), this.getGenreId());
    }
}
