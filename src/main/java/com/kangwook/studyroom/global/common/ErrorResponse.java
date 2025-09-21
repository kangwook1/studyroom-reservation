package com.kangwook.studyroom.global.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private final String message;
    private final List<ValidationError> validationErrors;

    @Builder
    private ErrorResponse(String message, List<ValidationError> validationErrors) {
        this.message = message;
        this.validationErrors = validationErrors;
    }

    @Getter
    public static class ValidationError {

        private final String field;
        private final String value;
        private final String reason;

        private ValidationError(String field, String value, String reason) {
            this.field = field;
            this.value = value;
            this.reason = reason;
        }

        public static List<ValidationError> from(BindingResult bindingResult) {
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            return fieldErrors.stream()
                    .map(error -> new ValidationError(
                            error.getField(),
                            (error.getRejectedValue() == null) ? null : error.getRejectedValue().toString(),
                            error.getDefaultMessage()))
                    .collect(Collectors.toList());
        }
    }
}
