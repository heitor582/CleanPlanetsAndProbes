package com.study.domain.planet;

import com.study.domain.Identifier;

import java.util.Objects;

public class PlanetID extends Identifier {
    private final Long value;
    private PlanetID(final Long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    public static PlanetID from(Long id) {
        return new PlanetID(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final PlanetID that = (PlanetID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
