package com.study.application.probe.retrieve.get;

import com.study.domain.exceptions.NotFoundException;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;

import java.util.Objects;

public class DefaultGetProbeByIdUseCase extends GetProbeByIdUseCase{
    private final ProbeGateway gateway;

    public DefaultGetProbeByIdUseCase(final ProbeGateway probeGateway) {
        this.gateway = Objects.requireNonNull(probeGateway);
    }
    @Override
    public ProbeOutput execute(final Long id) {
        final ProbeID probeID = ProbeID.from(id);
        return this.gateway.findBy(probeID).map(ProbeOutput::from)
                .orElseThrow(() -> NotFoundException.with(Probe.class, probeID));
    }
}
