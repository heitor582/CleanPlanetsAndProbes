package com.study.application.probe.create;

import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.validation.Error;
import com.study.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultCreateProbeUseCase extends CreateProbeUseCase{
    private final PlanetGateway planetGateway;
    private final ProbeGateway probeGateway;

    public DefaultCreateProbeUseCase(
            final PlanetGateway planetGateway,
            final ProbeGateway probeGateway) {
        this.planetGateway = Objects.requireNonNull(planetGateway);
        this.probeGateway = Objects.requireNonNull(probeGateway);
    }

    @Override
    public CreateProbeOutput execute(final CreateProbeCommand command) {
        final String name = command.name();
        final int cordX = command.cordX();
        final int cordY = command.cordY();
        final PlanetID planetID = PlanetID.from(command.planetId());

        final Planet planet = this.planetGateway.findBy(planetID)
                .orElseThrow(() -> NotFoundException.with(Planet.class, planetID));

        final Notification notification = Notification.create();

        final Probe probe = notification.validate(() -> Probe.newProbe(name, cordX, cordY, planet.getId()));

        if(planet.getCordX() < Math.abs(probe.getCordX()) || planet.getCordY() < Math.abs(probe.getCordY())){
            notification.append(new Error("does not have this position to land the ship"));
        }

        if (notification.hasError()) {
            throw new NotificationException(
                    "Could not update Aggregate Probe", notification
            );
        }

        return CreateProbeOutput.from(this.probeGateway.create(probe));
    }
}
