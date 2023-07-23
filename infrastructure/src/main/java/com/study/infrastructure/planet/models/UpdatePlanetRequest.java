package com.study.infrastructure.planet.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record UpdatePlanetRequest(
        @JsonProperty("name") String name,
        @JsonProperty("cord_x") int cordX,
        @JsonProperty("cord_y") int cordY
) {
}
