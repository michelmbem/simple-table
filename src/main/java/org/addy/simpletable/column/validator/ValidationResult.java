package org.addy.simpletable.column.validator;

public class ValidationResult {
    private final boolean valid;
    private final String message;

    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }

    public ValidationResult() {
        this(true, null);
    }

    public boolean isValid() {
        return valid;
    }

    public String getMessage() {
        return message;
    }
}
