package codeflix.catalog.admin.domain._share.entity;

import codeflix.catalog.admin.domain._share.value.object.Identifier;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

    protected AggregateRoot(final ID id) {
        super(id);
    }
}
