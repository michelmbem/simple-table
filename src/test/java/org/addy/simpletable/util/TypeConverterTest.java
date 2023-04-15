package org.addy.simpletable.util;

import org.addy.simpletable.model.ListItem;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

class TypeConverterTest {
    @Test
    void toType_can_construct() {
        ListItem<Integer> li1 = new ListItem<>(10);
        ListItem<Integer> l2 = (ListItem<Integer>) TypeConverter.toType(10, ListItem.class);
        assertThat(l2).isEqualTo(li1);

        Fraction f1 = new Fraction(2);
        Fraction f2 = (Fraction) TypeConverter.toType(2, Fraction.class);
        assertThat(f2).isEqualTo(f1);
    }

    @Test
    void toType_can_factor() {
        String repr = "2023-04-14T21:56:30+06:00";
        OffsetDateTime odt1 = OffsetDateTime.parse(repr);
        OffsetDateTime odt2 = (OffsetDateTime) TypeConverter.toType(repr, OffsetDateTime.class);
        assertThat(odt2).isEqualTo(odt1);
    }

    @Test
    void convertByIntrospection_works_on_primitive_types() {
        Fraction f = new Fraction(5, 2);
        double d = TypeConverter.toDouble(f);
        int i = TypeConverter.toInt(f);
        assertThat(d).isEqualTo(f.toDouble());
        assertThat(i).isEqualTo(f.toInt());
    }

    static class Fraction {
        private final int numerator;
        private final int denominator;

        public Fraction(int numerator, int denominator) {
            if (denominator == 0)
                throw new IllegalArgumentException("denominator cannot be 0");

            this.numerator  = numerator;
            this.denominator = denominator;
        }

        public Fraction(int numerator) {
            this(numerator, 1);
        }

        public int getNumerator() {
            return numerator;
        }

        public int getDenominator() {
            return denominator;
        }

        public double toDouble() {
            return (double) numerator / denominator;
        }

        public int toInt() {
            return numerator / denominator;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Fraction fraction = (Fraction) o;
            return numerator == fraction.numerator && denominator == fraction.denominator;
        }

        @Override
        public int hashCode() {
            return Objects.hash(numerator, denominator);
        }
    }
}
