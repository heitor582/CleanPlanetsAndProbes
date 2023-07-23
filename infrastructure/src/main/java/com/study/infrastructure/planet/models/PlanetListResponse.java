package com.study.infrastructure.planet.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record PlanetListResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("cord_x") int cordX,
        @JsonProperty("cord_y") int cordY,
        @JsonProperty("created_at") Instant createdAt
) {
}
