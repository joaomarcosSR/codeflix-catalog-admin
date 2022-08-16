package codeflix.catalog.admin.domain._share.validation.handler;

import codeflix.catalog.admin.domain._share.exceptions.DomainException;
import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;

import java.util.Collections;
import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {
    @Override
    public ValidationHandler append(Error anError) {
        throw DomainException.with(anError);
    }

    @Override
    public ValidationHandler append(ValidationHandler anHandler) {
        throw DomainException.with(anHandler.getErrors());
    }

    @Override
    public ValidationHandler validate(Validation aValidation) {
        try {
            aValidation.validate();
        } catch (final Exception ex) {
            throw DomainException.with(new Error(ex.getMessage()));
        }

        return this;
    }

    @Override
    public List<Error> getErrors() {
        return Collections.emptyList();
    }
}
