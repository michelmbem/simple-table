package org.addy.simpletable.model;

public class Range {
    private int minimum;
    private int maximum;

    public Range() {}

    public Range(int minimum, int maximum) {
        this.minimum = Math.min(minimum, maximum);
        this.maximum = Math.max(minimum, maximum);
    }

    public int getMinimum() {
        return minimum;
    }

    public void setMinimum(int minimum) {
        this.minimum = minimum;
    }

    public int getMaximum() {
        return maximum;
    }

    public void setMaximum(int maximum) {
        this.maximum = maximum;
    }
}
