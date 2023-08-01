package com.study.application.probe.retrieve.list;

import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;

import java.time.Instant;

public record ProbeListOutput(
        Long id,
        String name,
        int cordX,
        int cordY,
        Direction direction,
        Long planetId,
        Instant createdAt,
        Instant updatedAt
) {
    public static ProbeListOutput from(final Probe probe) {
        return new ProbeListOutput(
                probe.getId().getValue(),
                probe.getName(),
                probe.getCordX(),
                probe.getCordY(),
                probe.getDirection(),
                probe.getPlanetId().getValue(),
                probe.getCreatedAt(),
                probe.getUpdatedAt()
        );
    }
}
