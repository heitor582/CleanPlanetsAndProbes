package com.study.application.probe.create;

import com.study.domain.probe.Probe;

public record CreateProbeOutput(Long id) {
    public static CreateProbeOutput from(final Long id){
        return new CreateProbeOutput(id);
    }

    public static CreateProbeOutput from(final Probe probe){
        return from(probe.getId().getValue());
    }
}
