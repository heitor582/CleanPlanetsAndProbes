package com.study.application.probe.move;

public record MoveProbeCommand(
        Long id,
        String command
) {
    public static MoveProbeCommand with(
            final Long id,
            final String command
    ){
        return new MoveProbeCommand(id, command);
    }
}
