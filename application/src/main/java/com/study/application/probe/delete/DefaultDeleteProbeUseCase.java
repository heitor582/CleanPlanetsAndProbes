package com.study.application.probe.delete;

import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;

import java.util.Objects;

public class DefaultDeleteProbeUseCase extends DeleteProbeUseCase{
    private final ProbeGateway gateway;

    public DefaultDeleteProbeUseCase(final ProbeGateway probeGateway) {
        this.gateway = Objects.requireNonNull(probeGateway);
    }

    @Override
    public void execute(final Long id) {
        this.gateway.deleteBy(ProbeID.from(id));
    }
}
