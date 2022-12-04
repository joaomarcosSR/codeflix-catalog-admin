package codeflix.catalog.admin.domain.castmember.validation;

import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.validation.Validator;
import codeflix.catalog.admin.domain.castmember.entity.CastMember;

public class CastMemberValidator extends Validator {

    private static final int NAME_MIN_VALUE = 3;
    private static final int NAME_MAX_VALUE = 255;

    private final CastMember castMember;

    public CastMemberValidator(final CastMember castMember, final ValidationHandler aHandler) {
        super(aHandler);
        this.castMember = castMember;
    }

    @Override
    public void validate() {
        this.checkNameConstraints();
        this.checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final String name = this.castMember.getName();
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
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
    }

    private void checkTypeConstraints() {
        if (this.castMember.getType() == null) this.validationHandler().append(new Error("'type' should not be null"));
    }
}
