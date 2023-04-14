package org.addy.simpletable.model;

import org.addy.simpletable.util.NumberFormats;

import java.text.NumberFormat;

public class ProgressModel {
    private Range range;
    private boolean stringPainted;
    private NumberFormat numberFormat;

    public ProgressModel() {}

    public ProgressModel(int minimum, int maximum, boolean stringPainted, Object stringFormat) {
        this.range = new Range(minimum, maximum);
        this.stringPainted = stringPainted;
        this.numberFormat = NumberFormats.of(stringFormat);
    }

    public Range getRange() {
        return range;
    }

    public void setRange(Range range) {
        this.range = range;
    }

    public boolean isStringPainted() {
        return stringPainted;
    }

    public void setStringPainted(boolean stringPainted) {
        this.stringPainted = stringPainted;
    }

    public NumberFormat getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(NumberFormat numberFormat) {
        this.numberFormat = numberFormat;
    }
}
