package org.addy.simpletable.util;

import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import static org.assertj.core.api.Assertions.assertThat;

public class NumberFormatsTest {
    @Test
    void from_returns_default_instance_when_pattern_is_g() {
        NumberFormat numberFormat = NumberFormats.of("g");
        assertThat(numberFormat).isEqualTo(NumberFormat.getInstance());
    }

    @Test
    void from_returns_currency_instance_when_pattern_is_c_or_currency() {
        NumberFormat numberFormat1 = NumberFormats.of("c");
        NumberFormat numberFormat2 = NumberFormats.of("currency");
        NumberFormat numberFormat3 = NumberFormats.of("$");
        assertThat(numberFormat1).isEqualTo(NumberFormat.getCurrencyInstance());
        assertThat(numberFormat2).isEqualTo(NumberFormat.getCurrencyInstance());
        assertThat(numberFormat3).isEqualTo(NumberFormat.getCurrencyInstance());
    }

    @Test
    void from_returns_percent_instance_when_pattern_is_p_or_percent() {
        NumberFormat numberFormat1 = NumberFormats.of("p");
        NumberFormat numberFormat2 = NumberFormats.of("percent");
        NumberFormat numberFormat3 = NumberFormats.of("%");
        assertThat(numberFormat1).isEqualTo(NumberFormat.getPercentInstance());
        assertThat(numberFormat2).isEqualTo(NumberFormat.getPercentInstance());
        assertThat(numberFormat3).isEqualTo(NumberFormat.getPercentInstance());
    }

    @Test
    void from_returns_decimal_format_when_pattern_unrecognized() {
        NumberFormat numberFormat = NumberFormats.of("###,###,##0.00");
        assertThat(numberFormat).isInstanceOf(DecimalFormat.class);
    }
}
