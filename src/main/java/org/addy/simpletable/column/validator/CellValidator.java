package org.addy.simpletable.column.validator;

@FunctionalInterface
public interface CellValidator {
    ValidationResult validate(Object cellValue, Object rowItem);
}
