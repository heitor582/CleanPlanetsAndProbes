package com.study.application.planet.delete;

import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;

import java.util.Objects;

public class DefaultDeletePlanetUseCase extends DeletePlanetUseCase{
    private final PlanetGateway gateway;

    public DefaultDeletePlanetUseCase(final PlanetGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }

    @Override
    public void execute(final Long id) {
        this.gateway.deleteBy(PlanetID.from(id));
    }
}
