package models.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class ValidationError extends StandardError {

    private List<FieldError> errors;

    public void addError(final String fieldName, final String message) {
        errors.add(new FieldError(fieldName, message));
    }

    @Getter
    @AllArgsConstructor
    private static class FieldError {
        private String fieldName;
        private String message;
    }

}
