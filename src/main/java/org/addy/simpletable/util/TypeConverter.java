package org.addy.simpletable.util;

import org.addy.util.DateUtil;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;

public final class TypeConverter {
    private TypeConverter() {}
    
    public static boolean toBoolean(Object value) {
        if (value instanceof Boolean) return (boolean) value;
        if (value instanceof Number) return ((Number) value).intValue() != 0;
        if (value == null) return false;
        return Boolean.parseBoolean(value.toString());
    }

    public static byte toByte(Object value) {
        if (value instanceof Number) return ((Number) value).byteValue();
        if (value instanceof Boolean) return (byte) ((boolean) value ? 1 : 0);
        if (value == null) return 0;
        return Byte.parseByte(value.toString());
    }

    public static short toShort(Object value) {
        if (value instanceof Number) return ((Number) value).shortValue();
        if (value instanceof Boolean) return (short) ((boolean) value ? 1 : 0);
        if (value == null) return 0;
        return Short.parseShort(value.toString());
    }

    public static int toInt(Object value) {
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof Boolean) return (boolean) value ? 1 : 0;
        if (value == null) return 0;
        return Integer.parseInt(value.toString());
    }

    public static long toLong(Object value) {
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof Boolean) return (boolean) value ? 1L : 0L;
        if (value == null) return 0;
        return Long.parseLong(value.toString());
    }

    public static float toFloat(Object value) {
        if (value instanceof Number) return ((Number) value).floatValue();
        if (value instanceof Boolean) return (boolean) value ? 1F : 0F;
        if (value == null) return 0F;
        return Float.parseFloat(value.toString());
    }

    public static double toDouble(Object value) {
        if (value instanceof Number) return ((Number) value).doubleValue();
        if (value instanceof Boolean) return (boolean) value ? 1.0 : 0.0;
        if (value == null) return 0.0;
        return Double.parseDouble(value.toString());
    }

    public static BigInteger toBigInteger(Object value) {
        if (value instanceof BigInteger) return (BigInteger) value;
        if (value instanceof Boolean) return (boolean) value ? BigInteger.ONE : BigInteger.ZERO;
        if (value == null) return BigInteger.ZERO;
        return new BigInteger(value.toString());
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof BigInteger) return new BigDecimal((BigInteger) value);
        if (value instanceof Long) return new BigDecimal((long) value);
        if ((value instanceof Integer)) return new BigDecimal((int) value);
        if ((value instanceof Short)) return new BigDecimal((int) value);
        if ((value instanceof Byte)) return new BigDecimal((int) value);
        if (value instanceof Boolean) return (boolean) value ? BigDecimal.ONE : BigDecimal.ZERO;
        if (value == null) return BigDecimal.ZERO;
        return new BigDecimal(value.toString());
    }

    public static Date toDate(Object value) {
        if (value  == null) return null;
        if (value instanceof Date) return (Date) value;
        if (value instanceof LocalDate) return DateUtil.toDate((LocalDate) value);
        if (value instanceof LocalTime) return DateUtil.toDate((LocalTime) value);
        if (value instanceof LocalDateTime) return DateUtil.toDate((LocalDateTime) value);

        try {
            return DateUtil.parseDate(value.toString());
        } catch (ParseException e) {
            throw new ClassCastException();
        }
    }

    public static LocalDate toLocalDate(Object value) {
        if (value  == null) return null;
        if (value instanceof LocalDate) return (LocalDate) value;
        if (value instanceof LocalDateTime) return ((LocalDateTime) value).toLocalDate();
        if (value instanceof Date) return DateUtil.toLocalDate((Date) value);

        try {
            return LocalDate.parse(value.toString());
        } catch (DateTimeParseException e) {
            throw new ClassCastException();
        }
    }

    public static LocalTime toLocalTime(Object value) {
        if (value  == null) return null;
        if (value instanceof LocalTime) return (LocalTime) value;
        if (value instanceof LocalDateTime) return ((LocalDateTime) value).toLocalTime();
        if (value instanceof Date) return DateUtil.toLocalTime((Date) value);

        try {
            return LocalTime.parse(value.toString());
        } catch (DateTimeParseException e) {
            throw new ClassCastException();
        }
    }

    public static LocalDateTime toLocalDateTime(Object value) {
        if (value  == null) return null;
        if (value instanceof LocalDateTime) return (LocalDateTime) value;
        if (value instanceof LocalDate) return ((LocalDate) value).atStartOfDay();
        if (value instanceof LocalTime) return ((LocalTime) value).atDate(LocalDate.now());
        if (value instanceof Date) return DateUtil.toLocalDateTime((Date) value);

        try {
            return LocalDateTime.parse(value.toString());
        } catch (DateTimeParseException e) {
            throw new ClassCastException();
        }
    }

    public static Object toType(Object value, Class<?> targetType) {
        if (targetType == String.class) return String.valueOf(value);
        if (Boolean.class.isAssignableFrom(targetType)) return toBoolean(value);
        if (Byte.class.isAssignableFrom(targetType)) return toByte(value);
        if (Short.class.isAssignableFrom(targetType)) return toShort(value);
        if (Integer.class.isAssignableFrom(targetType)) return toInt(value);
        if (Long.class.isAssignableFrom(targetType)) return toLong(value);
        if (Float.class.isAssignableFrom(targetType)) return toFloat(value);
        if (Double.class.isAssignableFrom(targetType)) return toDouble(value);
        if (BigInteger.class.isAssignableFrom(targetType)) return toBigInteger(value);
        if (BigDecimal.class.isAssignableFrom(targetType)) return toBigDecimal(value);
        if (Date.class.isAssignableFrom(targetType)) return toDate(value);
        if (LocalDate.class.isAssignableFrom(targetType)) return toLocalDate(value);
        if (LocalTime.class.isAssignableFrom(targetType)) return toLocalTime(value);
        if (LocalDateTime.class.isAssignableFrom(targetType)) return toLocalDateTime(value);

        return value;
    }
}
