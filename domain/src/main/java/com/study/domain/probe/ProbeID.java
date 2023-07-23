package com.study.domain.probe;

import com.study.domain.Identifier;

import java.util.Objects;

public class ProbeID extends Identifier {
    private final Long value;
    protected ProbeID(Long value) {
        this.value = value;
    }

    @Override
    public Long getValue() {
        return value;
    }

    public static ProbeID from(Long id) {
        return new ProbeID(id);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ProbeID that = (ProbeID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getValue());
    }
}
