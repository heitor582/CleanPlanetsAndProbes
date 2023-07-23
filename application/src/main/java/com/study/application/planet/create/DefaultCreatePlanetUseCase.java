package com.study.application.planet.create;

import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetGateway;
import com.study.domain.validation.handler.Notification;

import java.util.Objects;

public class DefaultCreatePlanetUseCase extends CreatePlanetUseCase{
    private final PlanetGateway gateway;

    public DefaultCreatePlanetUseCase(final PlanetGateway gateway) {
        this.gateway = Objects.requireNonNull(gateway);
    }
    @Override
    public CreatePlanetOutput execute(CreatePlanetCommand command) {
        final String name = command.name();
        final int cordX = command.cordX();
        final int cordY = command.cordY();

        final Notification notification = Notification.create();

        final Planet planet = notification.validate(() -> Planet.newPlanet(cordY, cordX, name));

        if (notification.hasError()) {
            throw new NotificationException("Could not create Aggregate Planet", notification);
        }

        return CreatePlanetOutput.from(this.gateway.create(planet));
    }
}
