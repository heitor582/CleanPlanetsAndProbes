package com.study.application.probe.retrieve.list;

import com.study.application.UseCase;
import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;

public abstract class ListProbeUseCase extends UseCase<SearchQuery, Pagination<ProbeListOutput>> {
}
