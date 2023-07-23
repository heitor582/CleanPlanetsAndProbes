package com.study.application.planet.retrieve.list;

import com.study.application.UseCase;
import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;

public abstract class ListPlanetUseCase extends UseCase<SearchQuery, Pagination<PlanetListOutput>> {
}
