package com.study.application.planet.retrieve.list;

import com.study.domain.planet.Planet;

import java.time.Instant;

public record PlanetListOutput(
        Long id,
        String name,
        int cordX,
        int cordY,
        Instant createdAt
) {
    public static PlanetListOutput from(final Planet planet) {
        return new PlanetListOutput(
                planet.getId().getValue(),
                planet.getName(),
                planet.getCordX(),
                planet.getCordY(),
                planet.getCreatedAt()
        );
    }
}
