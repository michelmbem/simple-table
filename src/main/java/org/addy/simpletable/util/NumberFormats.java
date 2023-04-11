package org.addy.simpletable.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class NumberFormats {
    private NumberFormats() {}

    public static NumberFormat of(Object value) {
        if (value == null) return NumberFormat.getInstance();

        if (value instanceof NumberFormat) return (NumberFormat) value;

        switch (value.toString()) {
            case "g":
                return NumberFormat.getNumberInstance();
            case "$":
            case "c":
            case "currency":
                return NumberFormat.getCurrencyInstance();
            case "%":
            case "p":
            case "percent":
                return NumberFormat.getPercentInstance();
            default: {
                DecimalFormat decimalFormat = new DecimalFormat(value.toString());
                decimalFormat.setParseBigDecimal(true);
                return decimalFormat;
            }
        }
    }
}
