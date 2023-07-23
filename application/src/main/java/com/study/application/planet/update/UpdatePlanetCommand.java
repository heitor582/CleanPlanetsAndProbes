package com.study.application.planet.update;

public record UpdatePlanetCommand(
        Long id,
        String name,
        int cordX,
        int cordY
) {
    public static UpdatePlanetCommand with(
            final Long id,
            final String name,
            final int cordX,
            final int cordY
    ){
        return new UpdatePlanetCommand(id, name, cordX, cordY);
    }
}
