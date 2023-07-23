package com.study.infrastructure.planet.persistence;

import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "planets")
public class PlanetJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "cord_y", nullable = false)
    private int cordY;
    @Column(name = "cord_x", nullable = false)
    private int cordX;
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    public PlanetJpaEntity() {
    }

    private PlanetJpaEntity(
            final Long id,
            final String name,
            final int cordY,
            final int cordX,
            final Instant createdAt,
            final Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.cordY = cordY;
        this.cordX = cordX;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static PlanetJpaEntity from(final Planet planet){
        return new PlanetJpaEntity(
                planet.getId().getValue(),
                planet.getName(),
                planet.getCordY(),
                planet.getCordX(),
                planet.getCreatedAt(),
                planet.getUpdatedAt()
        );
    }

    public Planet toAggregate(){
        return Planet.with(
                PlanetID.from(getId()),
                getCordY(),
                getCordX(),
                getName(),
                getCreatedAt(),
                getUpdatedAt()
        );
    }

    public Long getId() {
        return id;
    }

    public PlanetJpaEntity setId(final Long id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlanetJpaEntity setName(final String name) {
        this.name = name;
        return this;
    }

    public int getCordY() {
        return cordY;
    }

    public PlanetJpaEntity setCordY(final int cordY) {
        this.cordY = cordY;
        return this;
    }

    public int getCordX() {
        return cordX;
    }

    public PlanetJpaEntity setCordX(final int cordX) {
        this.cordX = cordX;
        return this;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public PlanetJpaEntity setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
        return this;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public PlanetJpaEntity setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
        return this;
    }
}
