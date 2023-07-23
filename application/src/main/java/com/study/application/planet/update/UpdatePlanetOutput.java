package com.study.application.planet.update;

import com.study.domain.planet.Planet;

public record UpdatePlanetOutput(Long id) {
    public static UpdatePlanetOutput from(final Long id) {
        return new UpdatePlanetOutput(id);
    }

    public static UpdatePlanetOutput from(final Planet planet) {
        return from(planet.getId().getValue());
    }
}
