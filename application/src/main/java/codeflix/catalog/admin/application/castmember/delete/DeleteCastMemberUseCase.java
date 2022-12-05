package codeflix.catalog.admin.application.castmember.delete;

import codeflix.catalog.admin.application._shared.base.UnitUseCase;

public abstract sealed class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DeleteCastMemberUseCaseImpl {
}
