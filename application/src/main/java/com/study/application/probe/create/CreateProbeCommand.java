package com.study.application.probe.create;

public record CreateProbeCommand(
        String name,
        int cordX,
        int cordY,
        Long planetId
) {
    public static CreateProbeCommand with(
            final String name,
            final int cordX,
            final int cordY,
            final Long planetId
    ){
        return new CreateProbeCommand(name, cordX, cordY, planetId);
    }
}
