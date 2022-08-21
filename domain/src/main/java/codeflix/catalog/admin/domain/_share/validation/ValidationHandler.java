package codeflix.catalog.admin.domain._share.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    <T> T validate(Validation<T> aValidation);

    List<Error> getErrors();

    default boolean hasError() {
        return this.getErrors() != null && !this.getErrors().isEmpty();
    }

    default Error firstError() {
        return this.hasError() ? this.getErrors().get(0) : null;
    }

    interface Validation<T> {
        T validate();
    }
}
