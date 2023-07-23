package com.study.application.planet.create;

public record CreatePlanetCommand(
        String name,
        int cordX,
        int cordY
) {
    public static CreatePlanetCommand with(final String name, final int cordX, final int cordY){
        return new CreatePlanetCommand(name, cordX, cordY);
    }
}
