package codeflix.catalog.admin.domain.video;

import codeflix.catalog.admin.domain._share.utils.InstantUtils;
import codeflix.catalog.admin.domain.events.DomainEvent;

import java.time.Instant;

public record VideoMediaCreated(
        String resourceId,
        String filePath,
        Instant occurredOn
) implements DomainEvent {
    public static VideoMediaCreated with(final String recourseId, final String filePath) {
        return new VideoMediaCreated(recourseId, filePath, InstantUtils.now());
    }
}
