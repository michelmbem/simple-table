package org.addy.simpletable.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class NumberFormats {
    private NumberFormats() {}

    public static NumberFormat of(Object value) {
        if (value == null)
            return NumberFormat.getInstance();

        if (value instanceof NumberFormat numberFormat)
            return numberFormat;

        return switch (value.toString()) {
            case "g" -> NumberFormat.getNumberInstance();
            case "$", "c", "currency" -> NumberFormat.getCurrencyInstance();
            case "%", "p", "percent" -> NumberFormat.getPercentInstance();
            default -> {
                var decimalFormat = new DecimalFormat(value.toString());
                decimalFormat.setParseBigDecimal(true);
                yield decimalFormat;
            }
        };
    }
}
