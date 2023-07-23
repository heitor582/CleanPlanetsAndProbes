package com.study.domain.validation;


import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error error);

    ValidationHandler append(ValidationHandler handler);

    <T> T validate(Validation<T> validation);

    List<Error> getErrors();

    default boolean hasError() {
        return getErrors() != null &&  !getErrors().isEmpty();
    }

    default Error firstError() {
        if(hasError()) {
            return getErrors().get(0);
        }
        return null;
    }

    interface Validation<T> {
        T validate();
    }

}
