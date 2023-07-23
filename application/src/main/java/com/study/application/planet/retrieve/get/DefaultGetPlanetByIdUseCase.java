package com.study.application.planet.retrieve.get;

import com.study.domain.exceptions.NotFoundException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;

import java.util.Objects;

public class DefaultGetPlanetByIdUseCase extends GetPlanetByIdUseCase{
    private final PlanetGateway gateway;

    public DefaultGetPlanetByIdUseCase(final PlanetGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }
    @Override
    public PlanetOutput execute(final Long id) {
        final PlanetID planetId = PlanetID.from(id);

        return this.gateway.findBy(planetId).map(PlanetOutput::from)
                .orElseThrow(() -> NotFoundException.with(Planet.class, planetId));
    }
}
