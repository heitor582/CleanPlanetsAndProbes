package com.study.application.planet.retrieve.get;

import com.study.domain.planet.Planet;

import java.time.Instant;

public record PlanetOutput(
        Long id,
        String name,
        int cordX,
        int cordY,
        Instant createdAt,
        Instant updatedAt
) {
    public static PlanetOutput from(final Planet planet) {
        return new PlanetOutput(
                planet.getId().getValue(),
                planet.getName(),
                planet.getCordX(),
                planet.getCordY(),
                planet.getCreatedAt(),
                planet.getUpdatedAt()
        );
    }
}
