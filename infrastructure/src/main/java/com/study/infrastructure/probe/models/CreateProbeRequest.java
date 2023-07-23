package com.study.infrastructure.probe.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.domain.probe.Direction;

public record CreateProbeRequest(
        @JsonProperty("name") String name,
        @JsonProperty("cord_x") int cordX,
        @JsonProperty("cord_y") int cordY,
        @JsonProperty("planet_id") Long planetId
) {
}
