package org.addy.simpletable.util;

public final class HyperLink {
    private HyperLink() {}

    public static String format(Object value) {
        return String.format("<html><a href=''>%s</a></html>", value);
    }
}
