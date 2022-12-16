package codeflix.catalog.admin.application.video.retrieve.list;

import codeflix.catalog.admin.application._shared.base.UseCase;
import codeflix.catalog.admin.domain._share.pagination.Pagination;
import codeflix.catalog.admin.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}
