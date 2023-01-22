package codeflix.catalog.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Set;

public record UpdateVideoRequest(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("duration") Double duration,
        @JsonProperty("year_launched") Integer yearLaunch,
        @JsonProperty("opened") Boolean opened,
        @JsonProperty("published") Boolean published,
        @JsonProperty("rating") String rating,
        @JsonProperty("cast_members") Set<String> castMembers,
        @JsonProperty("categories") Set<String> categories,
        @JsonProperty("genres") Set<String> genres
) {
    public static UpdateVideoRequest with(
            final String aTitle,
            final String aDescription,
            final Double aDuration,
            final Integer aYearLaunch,
            final Boolean wasOpened,
            final Boolean wasPublished,
            final String aRating,
            final Set<String> castMembers,
            final Set<String> categories,
            final Set<String> genres
    ) {
        return new UpdateVideoRequest(
                aTitle,
                aDescription,
                aDuration,
                aYearLaunch,
                wasOpened,
                wasPublished,
                aRating,
                castMembers,
                categories,
                genres
        );
    }
}
