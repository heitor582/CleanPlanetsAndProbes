package com.study.application.planet.retrieve.list;

import com.study.domain.pagination.Pagination;
import com.study.domain.pagination.SearchQuery;
import com.study.domain.planet.PlanetGateway;

import java.util.Objects;

public class DefaultListPlanetUseCase extends ListPlanetUseCase{
    private final PlanetGateway gateway;

    public DefaultListPlanetUseCase(final PlanetGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public Pagination<PlanetListOutput> execute(final SearchQuery query) {
        return this.gateway.findAll(query).map(PlanetListOutput::from);
    }
}
