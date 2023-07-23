package com.study.domain.probe;

import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.planet.PlanetID;

import java.util.List;
import java.util.Optional;

public interface ProbeGateway {
    Optional<Probe> findBy(final ProbeID id);
    Probe create(final Probe probe);
    void deleteBy(final ProbeID id);
    Probe update(final Probe probe);
    Pagination<Probe> findAll(final SearchQuery query);
    List<Probe> findAllByPlanetId(final PlanetID planetID);
}
