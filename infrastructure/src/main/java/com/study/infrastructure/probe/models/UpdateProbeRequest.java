package com.study.infrastructure.probe.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.domain.probe.Direction;

public record UpdateProbeRequest(
        @JsonProperty("name") String name,
        @JsonProperty("cord_x") int cordX,
        @JsonProperty("cord_y") int cordY,
        @JsonProperty("direction") Direction direction
) {
}
