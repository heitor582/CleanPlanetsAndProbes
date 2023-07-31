package com.study.application.probe.move;

import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetID;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeGateway;
import com.study.domain.probe.ProbeID;
import com.study.domain.validation.Error;
import com.study.domain.validation.handler.Notification;

import java.util.List;
import java.util.Objects;

public class DefaultMoveProbeUseCase extends MoveProbeUseCase{
    private final ProbeGateway probeGateway;

    public DefaultMoveProbeUseCase(final ProbeGateway probeGateway) {
        this.probeGateway = Objects.requireNonNull(probeGateway);
    }

    @Override
    public MoveProbeOutput execute(final MoveProbeCommand input) {
        final ProbeID probeID = ProbeID.from(input.id());
        final Probe probe = this.probeGateway
                .findBy(probeID).orElseThrow(() -> NotFoundException.with(Probe.class, probeID));

        final PlanetID planetID = probe.getPlanetId();
        final Planet planet = this.planetGateway
                .findBy(planetID).orElseThrow(() -> NotFoundException.with(Planet.class, planetID));

        final List<Probe> probesByPlanetId = this.probeGateway.findAllByPlanetId(planetID);

        final var notification = Notification.create();

        if(!input.command().matches("(L|M|R)*")) {
            notification.append(new Error("The command passed did not work"));
        }

        for (final String command: input.command().split("")) {
            if(command.equals("L")) {
                notification.validate(probe::turnLeft);
            } else if (command.equals("R")) {
                notification.validate(probe::turnRight);
            } else {
                notification.validate(() ->
                        probe.move(
                            planet.getCordX(),
                            planet.getCordY(),
                            probesByPlanetId.stream().map(Probe::getCordX).toList(),
                            probesByPlanetId.stream().map(Probe::getCordY).toList()
                ));
            }
        }

        if (notification.hasError()) {
            throw new NotificationException("Could not update Aggregate Probe %s".formatted(probeID.getValue()), notification);
        }

        return MoveProbeOutput.from(this.probeGateway.update(probe));
    }
}
