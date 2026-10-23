package org.addy.simpletable.util;

import org.junit.jupiter.api.Test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static org.assertj.core.api.Assertions.assertThat;

class DateFormatsTest {
    @Test
    void of_returns_date_instance_when_value_is_g_or_null() {
        DateFormat dateFormat1 = DateFormats.of("g");
        DateFormat dateFormat2 = DateFormats.of(null);
        assertThat(dateFormat1).isEqualTo(DateFormat.getDateInstance());
        assertThat(dateFormat2).isEqualTo(DateFormat.getDateInstance());
    }

    @Test
    void of_returns_short_date_instance_when_value_is_s_or_short() {
        DateFormat dateFormat1 = DateFormats.of("s");
        DateFormat dateFormat2 = DateFormats.of("short");
        assertThat(dateFormat1).isEqualTo(DateFormat.getDateInstance(DateFormat.SHORT));
        assertThat(dateFormat2).isEqualTo(DateFormat.getDateInstance(DateFormat.SHORT));
    }

    @Test
    void of_returns_medium_date_instance_when_value_is_m_or_medium() {
        DateFormat dateFormat1 = DateFormats.of("m");
        DateFormat dateFormat2 = DateFormats.of("medium");
        assertThat(dateFormat1).isEqualTo(DateFormat.getDateInstance(DateFormat.MEDIUM));
        assertThat(dateFormat2).isEqualTo(DateFormat.getDateInstance(DateFormat.MEDIUM));
    }

    @Test
    void of_returns_long_date_instance_when_value_is_l_or_long() {
        DateFormat dateFormat1 = DateFormats.of("l");
        DateFormat dateFormat2 = DateFormats.of("long");
        assertThat(dateFormat1).isEqualTo(DateFormat.getDateInstance(DateFormat.LONG));
        assertThat(dateFormat2).isEqualTo(DateFormat.getDateInstance(DateFormat.LONG));
    }

    @Test
    void of_returns_simple_date_format_when_value_is_a_pattern() {
        DateFormat dateFormat = DateFormats.of("dd/MM/yyyy");
        assertThat(dateFormat).isInstanceOf(SimpleDateFormat.class);
    }

    @Test
    void of_returns_the_given_object_when_value_is_a_date_format() {
        Object value = new SimpleDateFormat("dd.MM.yyyy");
        DateFormat dateFormat = DateFormats.of(value);
        assertThat(dateFormat).isSameAs(value);
    }
}
