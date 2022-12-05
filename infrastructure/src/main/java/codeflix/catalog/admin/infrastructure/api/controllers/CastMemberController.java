package codeflix.catalog.admin.infrastructure.api.controllers;

import codeflix.catalog.admin.application.castmember.create.CreateCastMemberCommand;
import codeflix.catalog.admin.application.castmember.create.CreateCastMemberOutput;
import codeflix.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import codeflix.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import codeflix.catalog.admin.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import codeflix.catalog.admin.application.castmember.retrieve.list.ListCastMembersUseCase;
import codeflix.catalog.admin.application.castmember.update.UpdateCastMemberCommand;
import codeflix.catalog.admin.application.castmember.update.UpdateCastMemberOutput;
import codeflix.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.infrastructure.api.CastMemberAPI;
import codeflix.catalog.admin.infrastructure.castmember.models.CastMemberListResponse;
import codeflix.catalog.admin.infrastructure.castmember.models.CastMemberResponse;
import codeflix.catalog.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import codeflix.catalog.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import codeflix.catalog.admin.infrastructure.castmember.presenters.CastMemberApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase,
            final ListCastMembersUseCase listCastMembersUseCase) {
        this.createCastMemberUseCase = createCastMemberUseCase;
        this.getCastMemberByIdUseCase = getCastMemberByIdUseCase;
        this.updateCastMemberUseCase = updateCastMemberUseCase;
        this.deleteCastMemberUseCase = deleteCastMemberUseCase;
        this.listCastMembersUseCase = listCastMembersUseCase;
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest anInput) {
        final CreateCastMemberCommand aCommand =
                CreateCastMemberCommand.with(anInput.name(), anInput.type());

        final CreateCastMemberOutput output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberApiPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest anInput) {
        final UpdateCastMemberCommand aCommand = UpdateCastMemberCommand.with(id, anInput.name(), anInput.type());

        final UpdateCastMemberOutput output = this.updateCastMemberUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteCastMemberUseCase.execute(anId);
    }

    @Override
    public Pagination<CastMemberListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction) {

        return this.listCastMembersUseCase
                .execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberApiPresenter::present);
    }
}
