package com.study.application.probe.update;

import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import com.study.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultUpdateProbeUseCase extends UpdateProbeUseCase{
    private final ProbeGateway probeGateway;

    public DefaultUpdateProbeUseCase(final ProbeGateway probeGateway) {
        this.probeGateway = Objects.requireNonNull(probeGateway);
    }

    @Override
    public UpdateProbeOutput execute(final UpdateProbeCommand updateProbeCommand) {
        final ProbeID probeID= ProbeID.from(updateProbeCommand.id());
        final String name = updateProbeCommand.name();
        final int cordX = updateProbeCommand.cordX();
        final int cordY = updateProbeCommand.cordY();
        final Direction direction = updateProbeCommand.direction();

        final Probe probe = this.probeGateway.findBy(probeID)
                .orElseThrow(() -> NotFoundException.with(Probe.class, probeID));

        final var notification = Notification.create();
        notification.validate(() -> probe.update(name, cordX, cordY, direction));

        if (notification.hasError()) {
            throw new NotificationException(
                    "Could not update Aggregate Probe %s".formatted(probeID.getValue()), notification
            );
        }


        return UpdateProbeOutput.from(this.probeGateway.update(probe));
    }
}
