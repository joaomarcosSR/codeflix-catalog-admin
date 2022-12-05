package codeflix.catalog.admin.application.castmember.update;

import codeflix.catalog.admin.application._shared.base.UseCase;

public abstract sealed class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits UpdateCastMemberUseCaseImpl {
}
