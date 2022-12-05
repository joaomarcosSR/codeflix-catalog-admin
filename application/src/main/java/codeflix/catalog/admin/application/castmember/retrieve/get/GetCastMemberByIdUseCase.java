package codeflix.catalog.admin.application.castmember.retrieve.get;

import codeflix.catalog.admin.application._shared.base.UseCase;

public abstract sealed class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits GetCastMemberByIdUseCaseImpl {
}
