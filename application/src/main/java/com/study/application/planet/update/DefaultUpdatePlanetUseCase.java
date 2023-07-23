package com.study.application.planet.update;

import com.study.domain.exceptions.NotFoundException;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.planet.PlanetID;
import com.study.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultUpdatePlanetUseCase extends UpdatePlanetUseCase{
    private final PlanetGateway gateway;

    public DefaultUpdatePlanetUseCase(final PlanetGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }
    @Override
    public UpdatePlanetOutput execute(UpdatePlanetCommand command) {
        final String name = command.name();
        final int cordX = command.cordX();
        final int cordY = command.cordY();
        final PlanetID id = PlanetID.from(command.id());

        final Planet planet = this.gateway.findBy(id)
                .orElseThrow(() -> NotFoundException.with(Planet.class, id));

        final var notification = Notification.create();

        notification.validate(() -> planet.update(cordY, cordX, name));

        if (notification.hasError()) {
            throw new NotificationException("Could not create Aggregate Planet %s".formatted(id), notification);
        }

        return UpdatePlanetOutput.from(this.gateway.update(planet));
    }
}
