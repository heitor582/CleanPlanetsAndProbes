package com.study.application.probe.update;

import com.study.domain.probe.Direction;

public record UpdateProbeCommand(
        Long id,
        String name,
        int cordX,
        int cordY,
        Direction direction
) {
    public static UpdateProbeCommand with(
            final Long id,
            final String name,
            final int cordX,
            final int cordY,
            final Direction direction
    ){
        return new UpdateProbeCommand(id, name, cordX, cordY, direction);
    }
}
