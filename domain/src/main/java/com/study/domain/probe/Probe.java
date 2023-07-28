package com.study.domain.probe;

import com.study.domain.AggregateRoot;
import com.study.domain.exceptions.NotificationException;
import com.study.domain.planet.PlanetID;
import com.study.domain.utils.InstantUtils;
import com.study.domain.validation.ValidationHandler;
import com.study.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class Probe extends AggregateRoot<ProbeID> {
    private int cordX;
    private int cordY;
    private String name;
    private Direction direction;
    private final PlanetID planetId;
    private final Instant createdAt;
    private Instant updatedAt;

    private Probe(
            final ProbeID probeID,
            final int cordX,
            final int cordY,
            final String name,
            final Direction direction,
            final PlanetID planetId,
            final Instant createdAt,
            final Instant updatedAt) {
        super(probeID);
        this.cordX = cordX;
        this.cordY = cordY;
        this.name = name;
        this.direction = direction;
        this.planetId = Objects.requireNonNull(planetId);
        this.createdAt = Objects.requireNonNull(createdAt);
        this.updatedAt = Objects.requireNonNull(updatedAt);

        selfValidate();
    }

    public static Probe with(
            final ProbeID probeID,
            final int cordX,
            final int cordY,
            final String name,
            final Direction direction,
            final PlanetID planetId,
            final Instant createdAt,
            final Instant updatedAt) {
        return new Probe(probeID, cordX, cordY, name, direction, planetId, createdAt, updatedAt);
    }

    public static Probe newProbe(
            final String name,
            final int cordX,
            final int cordY,
            final PlanetID planetId
    ) {
        final Instant now = InstantUtils.now();
        return Probe.with(ProbeID.from(0L), cordX, cordY, name, Direction.UP, planetId, now, now);
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new ProbeValidator(handler, this).validate();
    }

    public int getCordX() {
        return cordX;
    }

    public int getCordY() {
        return cordY;
    }

    public String getName() {
        return name;
    }

    public Direction getDirection() {
        return direction;
    }

    public PlanetID getPlanetId() {
        return planetId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Probe update(
            final String name,
            final int cordX,
            final int cordY,
            final Direction direction
    ) {
        this.name = name;
        this.cordX = cordX;
        this.cordY = cordY;
        this.direction = direction;

        return this.update();
    }

    public Probe turnLeft(){
        this.direction = this.direction.turnLeft();

        return this.update();
    }

    public Probe turnRight(){
        this.direction = this.direction.turnRight();

        return this.update();
    }

    public Probe move(final int planetCordX, final int planetCordY, final List<Integer> xAxes, final List<Integer> yAxes) {
        return this.direction.isUp()|| this.direction.isDown() ?  this.moveY(planetCordY, yAxes) : this.moveX(planetCordX, xAxes);
    }

    private Probe moveX(final int planetCordX, final List<Integer> xAxes) {
        int tempX = 0;
        if (this.direction.isLeft()) {
            tempX =  getCordX() > -planetCordX ? getCordX() - 1 : getCordX() * -1;
        } else {
            tempX =  getCordX() < planetCordX ? getCordX() + 1 : getCordX() * -1 ;
        }

        this.cordX = xAxes.contains(tempX) ? getCordX() : tempX;

        return this.update();
    }

    private Probe moveY(final int planetCordY, final List<Integer> yAxes) {
        int tempY = 0;
        if(this.direction.isUp()){
            tempY = getCordY() < planetCordY ? getCordY() + 1 : getCordY() * -1;
        } else {
            tempY =  getCordY() > -planetCordY ? getCordY() - 1 : getCordY() * -1;
        }

        this.cordY = yAxes.contains(tempY) ? getCordY() : tempY;

        return this.update();
    }

    private Probe update(){
        this.updatedAt = InstantUtils.now();
        selfValidate();
        return this;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        validate(notification);
        if (notification.hasError()) {
            throw new NotificationException("Failed to create a Aggregate Probe", notification);
        }
    }
}
