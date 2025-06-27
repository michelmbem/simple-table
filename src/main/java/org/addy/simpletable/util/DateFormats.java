package org.addy.simpletable.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class DateFormats {
    private static final Pattern DATETIME_PATTERN =
            Pattern.compile("^(date|time|datetime|d|t|dt)(:(short|medium|long)(,(short|long))?)?$");

    private DateFormats() {}

    public static DateFormat of(Object value) {
        if (value == null)
            return DateFormat.getInstance();

        if (value instanceof DateFormat dateFormat)
            return dateFormat;

        String pattern = value.toString();
        int[] style = {DateFormat.DEFAULT, DateFormat.DEFAULT};

        Matcher matcher = DATETIME_PATTERN.matcher(pattern);
        if (matcher.find()) {
            pattern = matcher.group(1);

            if (matcher.group(3) != null)
                style[0] = parseDateStyle(matcher.group(3));

            if (matcher.group(5) != null)
                style[1] = parseDateStyle(matcher.group(5));
        }

        return switch (pattern) {
            case "g" -> DateFormat.getInstance();
            case "d", "date" -> DateFormat.getDateInstance(style[0]);
            case "t", "time" -> DateFormat.getTimeInstance(style[0]);
            case "dt", "datetime" -> DateFormat.getDateTimeInstance(style[0], style[1]);
            default -> new SimpleDateFormat(pattern);
        };
    }

    private static int parseDateStyle(String text) {
        return switch (text) {
            case "short" -> DateFormat.SHORT;
            case "medium" -> DateFormat.MEDIUM;
            case "long" -> DateFormat.LONG;
            default -> DateFormat.DEFAULT;
        };
    }
}
