package codeflix.catalog.admin.infrastructure.video.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVideoResponse(
        @JsonProperty("id") String id
) {
    public static UpdateVideoResponse with(
            final String anId
    ) {
        return new UpdateVideoResponse(anId);
    }
}
