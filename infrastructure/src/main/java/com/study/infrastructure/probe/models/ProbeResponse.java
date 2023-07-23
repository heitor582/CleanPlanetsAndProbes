package com.study.infrastructure.probe.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.study.domain.probe.Direction;

import java.time.Instant;

public record ProbeResponse(
        @JsonProperty("id") Long id,
        @JsonProperty("name") String name,
        @JsonProperty("cord_x") int cordX,
        @JsonProperty("cord_y") int cordY,
        @JsonProperty("direction") Direction direction,
        @JsonProperty("planet_id") Long planetId,
        @JsonProperty("created_at") Instant createdAt,
        @JsonProperty("updated_at") Instant updatedAt
) {
}
