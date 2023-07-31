package com.study.application.probe.update;

import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import com.study.domain.validation.Error;
import com.study.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultUpdateProbeUseCase extends UpdateProbeUseCase{
    private final ProbeGateway probeGateway;
    private final PlanetGateway planetGateway;

    public DefaultUpdateProbeUseCase(final ProbeGateway probeGateway, final PlanetGateway planetGateway) {
        this.probeGateway = Objects.requireNonNull(probeGateway);
        this.planetGateway = Objects.requireNonNull(planetGateway);
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

        final Planet planet = this.planetGateway.findBy(probe.getPlanetId())
                .orElseThrow(() -> NotFoundException.with(Planet.class, probe.getPlanetId()));

        final var notification = Notification.create();

        notification.validate(() -> probe.update(name, cordX, cordY, direction));

        if(planet.getCordX() < Math.abs(probe.getCordX()) || planet.getCordY() < Math.abs(probe.getCordY())){
            notification.append(new Error("does not have this position to land the ship"));
        }

        if (notification.hasError()) {
            throw new NotificationException(
                    "Could not update Aggregate Probe %s".formatted(probeID.getValue()), notification
            );
        }


        return UpdateProbeOutput.from(this.probeGateway.update(probe));
    }
}
