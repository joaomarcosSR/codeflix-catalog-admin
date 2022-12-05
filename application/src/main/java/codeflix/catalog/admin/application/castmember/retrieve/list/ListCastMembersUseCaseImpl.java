package codeflix.catalog.admin.application.castmember.retrieve.list;

import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain.castmember.gateway.CastMemberGateway;

import java.util.Objects;

public non-sealed class ListCastMembersUseCaseImpl extends ListCastMembersUseCase {
    private final CastMemberGateway castMemberGateway;

    public ListCastMembersUseCaseImpl(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public Pagination<CastMemberListOutput> execute(final SearchQuery aQuery) {
        return this.castMemberGateway.findAll(aQuery)
                .map(CastMemberListOutput::from);
    }
}
