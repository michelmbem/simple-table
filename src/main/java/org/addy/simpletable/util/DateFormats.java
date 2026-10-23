package org.addy.simpletable.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public final class DateFormats {
    private DateFormats() {}

    public static DateFormat of(Object value) {
        if (value == null || value.equals("g"))
            return DateFormat.getDateInstance();

        if (value instanceof DateFormat dateFormat)
            return dateFormat;

        return switch (value.toString().toLowerCase(Locale.ROOT)) {
            case "s", "short" -> DateFormat.getDateInstance(DateFormat.SHORT);
            case "m", "medium" -> DateFormat.getDateInstance(DateFormat.MEDIUM);
            case "l", "long" -> DateFormat.getDateInstance(DateFormat.LONG);
            default -> new SimpleDateFormat(value.toString());
        };
    }
}
