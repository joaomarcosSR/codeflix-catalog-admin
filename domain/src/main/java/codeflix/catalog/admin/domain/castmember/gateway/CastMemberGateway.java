package codeflix.catalog.admin.domain.castmember.gateway;

import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain._share.pagination.SearchQuery;
import codeflix.catalog.admin.domain.castmember.entity.CastMember;
import codeflix.catalog.admin.domain.castmember.value.object.CastMemberID;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {
    CastMember create(CastMember aCastMember);

    CastMember update(CastMember aCastMember);

    void deleteById(CastMemberID anId);

    Optional<CastMember> findById(CastMemberID anId);

    Pagination<CastMember> findAll(SearchQuery aQuery);

    List<CastMemberID> existsByIds(Iterable<CastMemberID> members);
}
