package com.study.application.probe.update;

import com.study.domain.probe.Probe;

public record UpdateProbeOutput(Long id) {
    public static UpdateProbeOutput from(final Long id){
        return new UpdateProbeOutput(id);
    }

    public static UpdateProbeOutput from(final Probe probe){
        return new UpdateProbeOutput(probe.getId().getValue());
    }
}
