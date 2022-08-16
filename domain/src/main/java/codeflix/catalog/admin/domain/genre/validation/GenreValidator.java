package codeflix.catalog.admin.domain.genre.validation;

import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.validation.Validator;
import codeflix.catalog.admin.domain.genre.entity.Genre;

public class GenreValidator extends Validator {

    public static final int NAME_MIN_VALUE = 1;
    public static final int NAME_MAX_VALUE = 255;
    private final Genre genre;

    public GenreValidator(final Genre aGenre, final ValidationHandler aHandler) {
        super(aHandler);
        this.genre = aGenre;
    }

    @Override
    public void validate() {
        this.checkNameConstraints();
    }

    private void checkNameConstraints() {
        final String name = this.genre.getName();
        if (name == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final int length = name.trim().length();
        if (length < NAME_MIN_VALUE || length > NAME_MAX_VALUE)
            this.validationHandler().append(
                    new Error("'name' must be between %d and %d characters".formatted(NAME_MIN_VALUE, NAME_MAX_VALUE))
            );
    }
}
