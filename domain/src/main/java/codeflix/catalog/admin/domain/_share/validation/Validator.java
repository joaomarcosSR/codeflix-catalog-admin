package codeflix.catalog.admin.domain._share.validation;

public abstract class Validator {

    private final ValidationHandler handler;

    protected Validator(final ValidationHandler aHandler) {
        handler = aHandler;
    }

    public abstract void validate();

    protected ValidationHandler validationHandler() {
        return handler;
    }
}
