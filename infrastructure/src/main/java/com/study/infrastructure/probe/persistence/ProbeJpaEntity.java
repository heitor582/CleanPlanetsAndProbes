package com.study.infrastructure.probe.persistence;

import com.study.domain.probe.Direction;
import com.study.domain.probe.Probe;
import com.study.domain.probe.ProbeID;
import com.study.infrastructure.configuration.GeneratedJpaOnly;
import com.study.infrastructure.planet.persistence.PlanetJpaEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "probes")
public class ProbeJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "cord_y", nullable = false)
    private int cordY;
    @Column(name = "cord_x", nullable = false)
    private int cordX;
    @Column(name="direction", nullable = false)
    @Enumerated(EnumType.STRING)
    private Direction direction;
    @ManyToOne
    @JoinColumn(name = "planet_id", nullable=false)
    private PlanetJpaEntity planet;
    @Column(name = "created_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant createdAt;
    @Column(name = "updated_at", nullable = false, columnDefinition = "TIMESTAMP(6)")
    private Instant updatedAt;

    @GeneratedJpaOnly
    public ProbeJpaEntity() {
    }

    private ProbeJpaEntity(
            final Long id,
            final String name,
            final int cordY,
            final int cordX,
            final Direction direction,
            final PlanetJpaEntity planet,
            final Instant createdAt,
            final Instant updatedAt

    ) {
        this.id = id;
        this.name = name;
        this.cordY = cordY;
        this.cordX = cordX;
        this.direction = direction;
        this.planet = planet;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProbeJpaEntity from(final Probe probe) {
        return new ProbeJpaEntity(
          probe.getId().getValue(),
                probe.getName(),
                probe.getCordY(),
                probe.getCordX(),
                probe.getDirection(),
                PlanetJpaEntity.from(probe.getPlanet()),
                probe.getCreatedAt(),
                probe.getUpdatedAt()
        );
    }

    public Probe toAggregate() {
        return Probe.with(
                ProbeID.from(this.id),
                this.cordX,
                this.cordY,
                this.name,
                this.direction,
                this.planet.toAggregate(),
                this.createdAt,
                this.updatedAt
        );
    }
}
