package com.study.domain.probe;

import com.study.domain.planet.Planet;
import com.study.domain.validation.Error;
import com.study.domain.validation.ValidationHandler;
import com.study.domain.validation.Validator;

public class ProbeValidator extends Validator {
    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;

    private final Probe probe;

    protected ProbeValidator(final ValidationHandler handler, final Probe probe) {
        super(handler);
        this.probe = probe;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkDirectionConstraints();
    }

    private void checkNameConstraints() {
        final String name = this.probe.getName();

        if (name == null) {
            this.validationHandler().append(new Error("name should not be null"));
            return;
        }

        if (name.isBlank()) {
            this.validationHandler().append(new Error("name should not be empty"));
            return;
        }

        final int nameLength = name.trim().length();

        if (nameLength < NAME_MIN_LENGTH || nameLength > NAME_MAX_LENGTH) {
            this.validationHandler().append(new Error("name must be between 3 and 255 characters"));
        }
    }

    private void checkDirectionConstraints() {
        if(this.probe.getDirection() == null){
            this.validationHandler().append(new Error("direction should not be null"));
        }
    }
}
