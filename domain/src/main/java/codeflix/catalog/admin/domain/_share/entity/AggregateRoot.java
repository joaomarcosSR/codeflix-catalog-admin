package codeflix.catalog.admin.domain._share.entity;

import codeflix.catalog.admin.domain._share.value.object.Identifier;
import codeflix.catalog.admin.domain.events.DomainEvent;

import java.util.Collections;
import java.util.List;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        super(id, Collections.emptyList());
    }

    protected AggregateRoot(final ID id, final List<DomainEvent> domainEvents) {
        super(id, domainEvents);
    }
}
