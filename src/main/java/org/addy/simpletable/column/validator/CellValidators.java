package org.addy.simpletable.column.validator;

import org.addy.util.StringUtil;
import org.addy.util.TypeConverter;
import org.apache.commons.lang3.ObjectUtils;

public final class CellValidators {
    private static final String EMAIL_PATTERN = "^[\\\\w!#$%&’*+/=?`{|}~^-]+(?:\\\\.[\\\\w!#$%&’*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\\\.)+[a-zA-Z]{2,6}$";

    private CellValidators() {}

    public static CellValidator notNull() {
        return (cellValue, rowItem) -> cellValue != null
                ? new ValidationResult()
                : new ValidationResult(false, "This member is required");
    }

    public static CellValidator notEmpty() {
        return (cellValue, rowItem) -> !ObjectUtils.isEmpty(cellValue)
                ? new ValidationResult()
                : new ValidationResult(false, "This member should not be empty");
    }

    public static <T> CellValidator min(Comparable<T> minValue) {
        return (cellValue, rowItem) -> minValue.compareTo((T) TypeConverter.toType(cellValue, minValue.getClass())) <= 0
                ? new ValidationResult()
                : new ValidationResult(false, "This member should be greater than or equal to " + minValue);
    }

    public static <T> CellValidator max(Comparable<T> maxValue) {
        return (cellValue, rowItem) -> maxValue.compareTo((T) TypeConverter.toType(cellValue, maxValue.getClass())) >= 0
                ? new ValidationResult()
                : new ValidationResult(false, "This member should be less than or equal to " + maxValue);
    }

    public static <T> CellValidator range(Comparable<T> minValue, Comparable<T> maxValue) {
        return (cellValue, rowItem) -> {
            T convertedValue = (T) TypeConverter.toType(cellValue, minValue.getClass());
            return (minValue.compareTo(convertedValue) <= 0) && (maxValue.compareTo(convertedValue) >= 0)
                    ? new ValidationResult()
                    : new ValidationResult(false, "This member should be between " + minValue + " and " + maxValue);
        };
    }

    public static CellValidator length(int len) {
        return (cellValue, rowItem) -> String.valueOf(cellValue).length() == len
                ? new ValidationResult()
                : new ValidationResult(false, "This member should be exactly " + len + " characters long");
    }

    public static CellValidator minLength(int min) {
        return (cellValue, rowItem) -> String.valueOf(cellValue).length() >= min
                ? new ValidationResult()
                : new ValidationResult(false, "This member should be at least " + min + " characters long");
    }

    public static CellValidator maxLength(int max) {
        return (cellValue, rowItem) -> String.valueOf(cellValue).length() <= max
                ? new ValidationResult()
                : new ValidationResult(false, "This member should be at most " + max + " characters long");
    }

    public static CellValidator lengthRange(int min, int max) {
        return (cellValue, rowItem) -> {
            int length = String.valueOf(cellValue).length();
            return length >= min && length <= max
                ? new ValidationResult()
                : new ValidationResult(false, "The length of this member should be between " + min + " and " + max);
        };
    }

    public static CellValidator pattern(String regex, String message) {
        String effectiveMessage = StringUtil.isEmpty(message)
                ? "This member should match the pattern " + regex
                : message;

        return (cellValue, rowItem) -> String.valueOf(cellValue).matches(regex)
                ? new ValidationResult()
                : new ValidationResult(false, effectiveMessage);
    }

    public static CellValidator email() {
        return pattern(EMAIL_PATTERN, "This member should be a valid email address");
    }

    public static CellValidator combine(CellValidator... validators) {
        return (cellValue, rowItem) -> {
            for (CellValidator validator : validators) {
                ValidationResult result = validator.validate(cellValue, rowItem);
                if (!result.isValid()) return result;
            }

            return new ValidationResult();
        };
    }
}
