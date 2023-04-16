package org.addy.simpletable.util;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static org.assertj.core.api.Assertions.assertThat;

class NumberFormatsTest {
    @Test
    void of_returns_default_instance_when_value_is_g_or_null() {
        NumberFormat numberFormat1 = NumberFormats.of("g");
        NumberFormat numberFormat2 = NumberFormats.of(null);
        assertThat(numberFormat1).isEqualTo(NumberFormat.getInstance());
        assertThat(numberFormat2).isEqualTo(NumberFormat.getInstance());
    }

    @Test
    void of_returns_currency_instance_when_value_is_c_or_currency() {
        NumberFormat numberFormat1 = NumberFormats.of("c");
        NumberFormat numberFormat2 = NumberFormats.of("currency");
        NumberFormat numberFormat3 = NumberFormats.of("$");
        assertThat(numberFormat1).isEqualTo(NumberFormat.getCurrencyInstance());
        assertThat(numberFormat2).isEqualTo(NumberFormat.getCurrencyInstance());
        assertThat(numberFormat3).isEqualTo(NumberFormat.getCurrencyInstance());
    }

    @Test
    void of_returns_percent_instance_when_value_is_p_or_percent() {
        NumberFormat numberFormat1 = NumberFormats.of("p");
        NumberFormat numberFormat2 = NumberFormats.of("percent");
        NumberFormat numberFormat3 = NumberFormats.of("%");
        assertThat(numberFormat1).isEqualTo(NumberFormat.getPercentInstance());
        assertThat(numberFormat2).isEqualTo(NumberFormat.getPercentInstance());
        assertThat(numberFormat3).isEqualTo(NumberFormat.getPercentInstance());
    }

    @Test
    void of_returns_decimal_format_when_value_unrecognized() {
        NumberFormat numberFormat = NumberFormats.of("###,###,##0.00");
        assertThat(numberFormat).isInstanceOf(DecimalFormat.class);
    }

    @Test
    void of_returns_the_given_object_when_value_is_a_number_format() {
        Object value = new DecimalFormat("###,###,##0.00");
        NumberFormat numberFormat = NumberFormats.of(value);
        assertThat(numberFormat).isSameAs(value);
    }
}
