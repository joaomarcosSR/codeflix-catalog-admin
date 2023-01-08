package codeflix.catalog.admin.domain._share.entity;

import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.value.object.Identifier;
import codeflix.catalog.admin.domain.events.DomainEvent;
import codeflix.catalog.admin.domain.events.DomainEventPublisher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class Entity<ID extends Identifier> {

    protected final ID id;
    private final List<DomainEvent> domainEvents;

    protected Entity(final ID id, final List<DomainEvent> domainEvents) {
        this.id = Objects.requireNonNull(id, "'id' should not be null");
        this.domainEvents = new ArrayList<>(domainEvents == null ? new ArrayList<>() : new ArrayList<>(domainEvents));
    }

    public abstract void validate(ValidationHandler aHandler);

    public ID getId() {
        return this.id;
    }

    public List<DomainEvent> getDomainEvents() {
        return Collections.unmodifiableList(this.domainEvents);
    }

    public void publishDomainEvents(final DomainEventPublisher publisher) {
        if (publisher == null) return;

        this.getDomainEvents().forEach(publisher::publishEvent);
        this.domainEvents.clear();
    }

    public void registerEvent(final DomainEvent event) {
        if (event == null) return;

        this.domainEvents.add(event);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        final Entity<?> entity = (Entity<?>) o;
        return this.getId().equals(entity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
