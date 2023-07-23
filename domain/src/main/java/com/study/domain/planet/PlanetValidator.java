package com.study.domain.planet;

import com.study.domain.validation.Error;
import com.study.domain.validation.ValidationHandler;
import com.study.domain.validation.Validator;

public class PlanetValidator extends Validator {
    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;
    private static final int CORD_Y_X_MAX = 1000;
    private static final int CORD_Y_X_MIN = 1;
    private final Planet planet;

    protected PlanetValidator(final ValidationHandler handler, final Planet planet) {
        super(handler);
        this.planet = planet;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkCordConstraints();
    }

    private void checkNameConstraints() {
        final String name = this.planet.getName();
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

    private void checkCordConstraints() {
        final int cordX = this.planet.getCordX();
        final int cordY = this.planet.getCordY();

        if(cordY < CORD_Y_X_MIN || cordY > CORD_Y_X_MAX) {
            this.validationHandler().append(new Error("coordinate Y must be between 1 and 1000"));
            return;
        }

        if(cordX < CORD_Y_X_MIN || cordX > CORD_Y_X_MAX) {
            this.validationHandler().append(new Error("coordinate X must be between 1 and 1000"));
        }
    }
}
