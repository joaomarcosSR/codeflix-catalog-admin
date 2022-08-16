package codeflix.catalog.admin.domain._share.exceptions;

import codeflix.catalog.admin.domain._share.validation.Error;

import java.util.Collections;
import java.util.List;

public class DomainException extends NoStackTraceException {

    protected final List<Error> errors;

    protected DomainException(final String aMessage, final List<Error> errors) {
        super(aMessage);
        this.errors = errors;
    }

    public static DomainException with(final Error anError) {
        return new DomainException(anError.message(), Collections.singletonList(anError));
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }

    public List<Error> getErrors() {
        return errors;
    }
}
