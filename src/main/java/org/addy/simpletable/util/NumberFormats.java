package org.addy.simpletable.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public final class NumberFormats {
    private NumberFormats() {}

    public static NumberFormat of(String pattern) {
        switch (pattern) {
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
            default:
                return new DecimalFormat(pattern);
        }
    }
}
