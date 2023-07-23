package com.study.domain.planet;

import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface PlanetGateway {
    Optional<Planet> findBy(final PlanetID id);
    void deleteBy(final PlanetID id);
    Planet create(final Planet planet);
    Planet update(final Planet planet);
    Pagination<Planet> findAll(final SearchQuery query);
}
