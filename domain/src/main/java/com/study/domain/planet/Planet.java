package com.study.domain.planet;

import com.study.domain.AggregateRoot;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.utils.InstantUtils;
import com.study.domain.validation.ValidationHandler;
import com.study.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.Objects;

public class Planet extends AggregateRoot<PlanetID> {
    private int cordY;
    private int cordX;
    private String name;
    private final Instant createdAt;
    private Instant updatedAt;

    private Planet(final PlanetID planetID,
                   final int maxY,
                   final int maxX,
                   final String name,
                   final Instant createdAt,
                   final Instant updatedAt) {
        super(planetID);
        this.cordY = maxY;
        this.cordX = maxX;
        this.name = name;
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);

        selfValidate();
    }

    public static Planet with(
            final PlanetID id, final int cordY, final int cordX,
            final String name, final Instant createdAt, final Instant updatedAt
    ) {
        return new Planet(
                id,
                cordY,
                cordX,
                name,
                createdAt,
                updatedAt
        );
    }

    public static Planet newPlanet(final int cordY, final int cordX, final String name) {
        final Instant now = Instant.now();
        return Planet.with(PlanetID.from(0L), cordY, cordX, name, now, now);
    }

    public int getCordY() {
        return cordY;
    }

    public int getCordX() {
        return cordX;
    }

    public String getName() {
        return name;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Planet update(
            final int maxY,
            final int maxX,
            final String name
    ) {
       this.cordX = maxX;
       this.cordY = maxY;
       this.name = name;

       this.updatedAt = InstantUtils.now();
       selfValidate();
       return this;
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new PlanetValidator(handler, this).validate();
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate Planet", notification);
        }
    }
}
