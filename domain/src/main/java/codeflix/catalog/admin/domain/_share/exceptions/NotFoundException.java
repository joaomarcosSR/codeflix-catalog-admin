package codeflix.catalog.admin.domain._share.exceptions;

import codeflix.catalog.admin.domain._share.entity.AggregateRoot;
import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.value.object.Identifier;

import java.util.List;

import static java.util.Collections.emptyList;

public class NotFoundException extends DomainException {
    protected NotFoundException(final String aMessage, final List<Error> errors) {
        super(aMessage, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> anAggregate,
            final Identifier id
    ) {
        final String anError = "%s with ID %s was not found"
                .formatted(anAggregate.getSimpleName(), id.getValue());

        return new NotFoundException(anError, emptyList());
    }
}
