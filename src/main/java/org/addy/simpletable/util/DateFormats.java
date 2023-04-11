package org.addy.simpletable.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateFormats {
    private static final Pattern DATETIME_PATTERN = Pattern.compile("^(date|time|datetime|d|t|dt)(:(short|medium|long)(,(short|long))?)?$");

    private DateFormats() {}

    public static DateFormat of(Object value) {
        if (value == null) return DateFormat.getInstance();

        if (value instanceof DateFormat) return (DateFormat) value;

        String component = value.toString();
        int[] style = {DateFormat.DEFAULT, DateFormat.DEFAULT};

        Matcher matcher = DATETIME_PATTERN.matcher(component);
        if (matcher.find()) {
            component = matcher.group(1);

            if (matcher.group(3) != null)
                style[0] = parseDateStyle(matcher.group(3));

            if (matcher.group(5) != null)
                style[1] = parseDateStyle(matcher.group(5));
        }

        switch (component) {
            case "g":
                return DateFormat.getInstance();
            case "d":
            case "date":
                return DateFormat.getDateInstance(style[0]);
            case "t":
            case "time":
                return DateFormat.getTimeInstance(style[0]);
            case "dt":
            case "datetime":
                return DateFormat.getDateTimeInstance(style[0], style[1]);
            default:
                return new SimpleDateFormat(component);
        }
    }

    private static int parseDateStyle(String text) {
        switch (text) {
            case "short":
                return DateFormat.SHORT;
            case "medium":
                return DateFormat.MEDIUM;
            case "long":
                return DateFormat.LONG;
            default:
                return DateFormat.DEFAULT;
        }
    }
}
