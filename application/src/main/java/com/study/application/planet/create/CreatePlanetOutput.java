package com.study.application.planet.create;

import com.study.domain.planet.Planet;

public record CreatePlanetOutput(
        Long id
) {
    public static CreatePlanetOutput from(final Long id){
        return new CreatePlanetOutput(id);
    }

    public static CreatePlanetOutput from(final Planet planet){
        return from(planet.getId().getValue());
    }
}
