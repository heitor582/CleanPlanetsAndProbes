package com.study.infrastructure.planet.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreatePlanetRequest(
        @JsonProperty("name") String name,
        @JsonProperty("cord_x") int cordX,
        @JsonProperty("cord_y") int cordY
) {
}
