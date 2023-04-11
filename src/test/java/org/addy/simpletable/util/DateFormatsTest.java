package org.addy.simpletable.util;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

public class DateFormatsTest {
    @Test
    void from_returns_default_instance_when_pattern_is_g() {
        DateFormat dateFormat = DateFormats.of("g");
        assertThat(dateFormat).isEqualTo(DateFormat.getInstance());
    }

    @Test
    void from_returns_date_instance_when_pattern_is_d_or_date() {
        DateFormat dateFormat1 = DateFormats.of("d");
        DateFormat dateFormat2 = DateFormats.of("date");
        assertThat(dateFormat1).isEqualTo(DateFormat.getDateInstance());
        assertThat(dateFormat2).isEqualTo(DateFormat.getDateInstance());
    }

    @Test
    void from_returns_time_instance_when_pattern_is_t_or_time() {
        DateFormat dateFormat1 = DateFormats.of("t");
        DateFormat dateFormat2 = DateFormats.of("time");
        assertThat(dateFormat1).isEqualTo(DateFormat.getTimeInstance());
        assertThat(dateFormat2).isEqualTo(DateFormat.getTimeInstance());
    }

    @Test
    void from_returns_datetime_instance_when_pattern_is_dt_or_datetime() {
        DateFormat dateFormat1 = DateFormats.of("dt");
        DateFormat dateFormat2 = DateFormats.of("datetime");
        assertThat(dateFormat1).isEqualTo(DateFormat.getDateTimeInstance());
        assertThat(dateFormat2).isEqualTo(DateFormat.getDateTimeInstance());
    }

    @Test
    void from_returns_correct_instance_with_correct_style_when_pattern_and_style_are_set() {
        DateFormat dateFormat1 = DateFormats.of("d:short");
        DateFormat dateFormat2 = DateFormats.of("time:long");
        DateFormat dateFormat3 = DateFormats.of("dt:medium,short");
        assertThat(dateFormat1).isEqualTo(DateFormat.getDateInstance(DateFormat.SHORT));
        assertThat(dateFormat2).isEqualTo(DateFormat.getTimeInstance(DateFormat.LONG));
        assertThat(dateFormat3).isEqualTo(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT));
    }

    @Test
    void from_returns_simple_date_format_when_pattern_unrecognized() {
        DateFormat dateFormat = DateFormats.of("dd/MM/yyyy HH:mm:ss");
        assertThat(dateFormat).isInstanceOf(SimpleDateFormat.class);
    }
}
