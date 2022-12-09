package codeflix.catalog.admin.domain.video;

import codeflix.catalog.admin.domain._share.validation.Error;
import codeflix.catalog.admin.domain._share.validation.ValidationHandler;
import codeflix.catalog.admin.domain._share.validation.Validator;

import java.time.Year;

public class VideoValidator extends Validator {
    private static final int TITLE_MAX_LENGTH = 255;
    private static final int DESCRIPTION_MAX_LENGTH = 4000;
    private final Video video;

    protected VideoValidator(final Video video, final ValidationHandler aHandler) {
        super(aHandler);
        this.video = video;
    }

    @Override
    public void validate() {
        this.checkNameConstraints();
        this.checkDescriptionConstraints();
        this.checkLaunchedAtConstraints();
        this.checkRattingConstraints();
    }

    private void checkNameConstraints() {
        final String title = this.video.getTitle();
        if (title == null) {
            this.validationHandler().append(new Error("'title' should not be null"));
            return;
        }

        if (title.isBlank()) {
            this.validationHandler().append(new Error("'title' should not be empty"));
            return;
        }

        final int length = title.trim().length();
        if (length > TITLE_MAX_LENGTH)
            this.validationHandler().append(
                    new Error("'title' must be between %d and %d characters".formatted(1, TITLE_MAX_LENGTH))
            );
    }

    private void checkDescriptionConstraints() {
        final String description = this.video.getDescription();
        if (description == null) {
            this.validationHandler().append(new Error("'description' should not be null"));
            return;
        }

        if (description.isBlank()) {
            this.validationHandler().append(new Error("'description' should not be empty"));
            return;
        }

        final int length = description.trim().length();
        if (length > DESCRIPTION_MAX_LENGTH)
            this.validationHandler().append(
                    new Error("'description' must be between %d and %d characters".formatted(1, DESCRIPTION_MAX_LENGTH))
            );
    }

    private void checkLaunchedAtConstraints() {
        final Year launchedAt = this.video.getLaunchedAt();
        if (launchedAt == null) this.validationHandler().append(new Error("'launchedAt' should not be null"));
    }

    private void checkRattingConstraints() {
        final Rating rating = this.video.getRating();
        if (rating == null) this.validationHandler().append(new Error("'rating' should not be null"));
    }
}
