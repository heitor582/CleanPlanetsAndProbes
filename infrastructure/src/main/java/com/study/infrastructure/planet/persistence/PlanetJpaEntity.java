package com.study.infrastructure.planet.persistence;

import com.study.domain.planet.Planet;
import com.study.domain.planet.PlanetID;
import com.study.infrastructure.configuration.GeneratedJpaOnly;

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
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant updatedAt;

    @GeneratedJpaOnly
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

    public String getName() {
        return name;
    }

    public int getCordY() {
        return cordY;
    }

    public int getCordX() {
        return cordX;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
