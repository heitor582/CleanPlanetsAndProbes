package com.study.application.probe.move;

import com.study.domain.probe.Probe;

public record MoveProbeOutput(Long id) {
    public static MoveProbeOutput from(final Long id){
        return new MoveProbeOutput(id);
    }
    public static MoveProbeOutput from(final Probe probe){
        return new MoveProbeOutput(probe.getId().getValue());
    }
}
