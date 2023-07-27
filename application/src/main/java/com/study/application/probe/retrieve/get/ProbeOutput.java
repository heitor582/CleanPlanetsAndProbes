package com.study.application.probe.retrieve.get;

import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;

import java.time.Instant;

public record ProbeOutput(
        Long id,
        String name,
        int cordX,
        int cordY,
        Direction direction,
        Long planetId,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProbeOutput from(final Probe probe) {
        return new ProbeOutput(
                probe.getId().getValue(),
                probe.getName(),
                probe.getCordX(),
                probe.getCordY(),
                probe.getDirection(),
                probe.getPlanet().getId().getValue(),
                probe.getCreatedAt(),
                probe.getUpdatedAt()
        );
    }
}
