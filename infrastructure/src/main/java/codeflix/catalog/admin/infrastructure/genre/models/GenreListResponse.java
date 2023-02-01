package codeflix.catalog.admin.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.List;

public record GenreListResponse(
        String id,
        String name,
        @JsonProperty("is_active") Boolean active,
        @JsonProperty("categories_id") List<String> categories,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("deleted_at") Instant deletedAt
) {
}
