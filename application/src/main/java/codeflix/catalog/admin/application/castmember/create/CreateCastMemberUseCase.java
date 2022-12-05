package codeflix.catalog.admin.application.castmember.create;

import codeflix.catalog.admin.application._shared.base.UseCase;

public abstract sealed class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits CreateCastMemberUseCaseImpl {
}
