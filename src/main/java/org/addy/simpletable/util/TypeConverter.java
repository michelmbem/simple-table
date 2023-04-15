package org.addy.simpletable.util;

import org.addy.util.DateUtil;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.stream.Stream;

public final class TypeConverter {
    private TypeConverter() {}

    public static boolean toBoolean(Object value) {
        if (value == null) return false;
        if (value instanceof Boolean) return (boolean) value;
        if (value instanceof Number) return ((Number) value).intValue() != 0;
        if (value instanceof CharSequence) return Boolean.parseBoolean(value.toString());
        return (boolean) convertByIntrospection(value, boolean.class);
    }

    public static char toChar(Object value) {
        if (value == null) return '\0';
        if (value instanceof Boolean) return (boolean) value ? '1' : '0';
        if (value instanceof Number) return (char) ((Number) value).intValue();

        if (value instanceof CharSequence) {
            String str = value.toString();
            if (str.length() == 1) return  str.charAt(0);
            throw new IllegalArgumentException("The given character sequence is either empty or has more then one character");
        }

        return (char) convertByIntrospection(value, char.class);
    }

    public static byte toByte(Object value) {
        if (value == null) return 0;
        if (value instanceof Boolean) return (byte) ((boolean) value ? 1 : 0);
        if (value instanceof Number) return ((Number) value).byteValue();
        if (value instanceof CharSequence) return Byte.parseByte(value.toString());
        return (byte) convertByIntrospection(value, byte.class);
    }

    public static short toShort(Object value) {
        if (value == null) return 0;
        if (value instanceof Boolean) return (short) ((boolean) value ? 1 : 0);
        if (value instanceof Number) return ((Number) value).shortValue();
        if (value instanceof CharSequence) return Short.parseShort(value.toString());
        return (short) convertByIntrospection(value, short.class);
    }

    public static int toInt(Object value) {
        if (value == null) return 0;
        if (value instanceof Boolean) return (boolean) value ? 1 : 0;
        if (value instanceof Number) return ((Number) value).intValue();
        if (value instanceof CharSequence) return Integer.parseInt(value.toString());
        return (int) convertByIntrospection(value, int.class);
    }

    public static long toLong(Object value) {
        if (value == null) return 0L;
        if (value instanceof Boolean) return (boolean) value ? 1L : 0L;
        if (value instanceof Number) return ((Number) value).longValue();
        if (value instanceof CharSequence) return Long.parseLong(value.toString());
        return (long) convertByIntrospection(value, long.class);
    }

    public static float toFloat(Object value) {
        if (value == null) return 0F;
        if (value instanceof Boolean) return (boolean) value ? 1F : 0F;
        if (value instanceof Number) return ((Number) value).floatValue();
        if (value instanceof CharSequence) return Float.parseFloat(value.toString());
        return (float) convertByIntrospection(value, float.class);
    }

    public static double toDouble(Object value) {
        if (value == null) return 0.0;
        if (value instanceof Boolean) return (boolean) value ? 1.0 : 0.0;
        if (value instanceof Number) return ((Number) value).doubleValue();
        if (value instanceof CharSequence) return Double.parseDouble(value.toString());
        return (double) convertByIntrospection(value, double.class);
    }

    public static BigInteger toBigInteger(Object value) {
        if (value == null) return BigInteger.ZERO;
        if (value instanceof Boolean) return (boolean) value ? BigInteger.ONE : BigInteger.ZERO;
        if (value instanceof BigInteger) return (BigInteger) value;
        if (value instanceof BigDecimal) return ((BigDecimal) value).toBigInteger();
        if (value instanceof Number) return BigInteger.valueOf(((Number) value).longValue());
        if (value instanceof CharSequence) return new BigInteger(value.toString());
        return (BigInteger) convertByIntrospection(value, BigInteger.class);
    }

    public static BigDecimal toBigDecimal(Object value) {
        if (value == null) return BigDecimal.ZERO;
        if (value instanceof Boolean) return (boolean) value ? BigDecimal.ONE : BigDecimal.ZERO;
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof BigInteger) return new BigDecimal((BigInteger) value);
        if (value instanceof Number || value instanceof CharSequence) return new BigDecimal(value.toString());
        return (BigDecimal) convertByIntrospection(value, BigDecimal.class);
    }

    public static Date toDate(Object value) {
        if (value == null) return null;
        if (value instanceof Date) return (Date) value;
        if (value instanceof LocalDate) return DateUtil.toDate((LocalDate) value);
        if (value instanceof LocalTime) return DateUtil.toDate((LocalTime) value);
        if (value instanceof LocalDateTime) return DateUtil.toDate((LocalDateTime) value);

        if (value instanceof CharSequence) {
            try {
                return DateUtil.parseDate(value.toString());
            } catch (ParseException e) {
                throw new ClassCastException();
            }
        }

        return (Date) convertByIntrospection(value, Date.class);
    }

    public static LocalDate toLocalDate(Object value) {
        if (value == null) return null;
        if (value instanceof Date) return DateUtil.toLocalDate((Date) value);
        if (value instanceof LocalDate) return (LocalDate) value;
        if (value instanceof LocalDateTime) return ((LocalDateTime) value).toLocalDate();

        if (value instanceof CharSequence) {
            try {
                return LocalDate.parse(value.toString());
            } catch (DateTimeParseException e) {
                throw new ClassCastException();
            }
        }

        return (LocalDate) convertByIntrospection(value, LocalDate.class);
    }

    public static LocalTime toLocalTime(Object value) {
        if (value == null) return null;
        if (value instanceof Date) return DateUtil.toLocalTime((Date) value);
        if (value instanceof LocalTime) return (LocalTime) value;
        if (value instanceof LocalDateTime) return ((LocalDateTime) value).toLocalTime();

        if (value instanceof CharSequence) {
            try {
                return LocalTime.parse(value.toString());
            } catch (DateTimeParseException e) {
                throw new ClassCastException();
            }
        }

        return (LocalTime) convertByIntrospection(value, LocalTime.class);
    }

    public static LocalDateTime toLocalDateTime(Object value) {
        if (value == null) return null;
        if (value instanceof Date) return DateUtil.toLocalDateTime((Date) value);
        if (value instanceof LocalDate) return ((LocalDate) value).atStartOfDay();
        if (value instanceof LocalTime) return ((LocalTime) value).atDate(LocalDate.now());
        if (value instanceof LocalDateTime) return (LocalDateTime) value;

        if (value instanceof CharSequence) {
            try {
                return LocalDateTime.parse(value.toString());
            } catch (DateTimeParseException e) {
                throw new ClassCastException();
            }
        }

        return (LocalDateTime) convertByIntrospection(value, LocalDateTime.class);
    }

    public static Object toType(Object value, Class<?> targetType) {
        if (targetType == Boolean.class) return toBoolean(value);
        if (targetType == Byte.class) return toByte(value);
        if (targetType == Short.class) return toShort(value);
        if (targetType == Integer.class) return toInt(value);
        if (targetType == Long.class) return toLong(value);
        if (targetType == Float.class) return toFloat(value);
        if (targetType == Double.class) return toDouble(value);
        if (targetType == BigInteger.class) return toBigInteger(value);
        if (targetType == BigDecimal.class) return toBigDecimal(value);
        if (targetType == Date.class) return toDate(value);
        if (targetType == LocalDate.class) return toLocalDate(value);
        if (targetType == LocalTime.class) return toLocalTime(value);
        if (targetType == LocalDateTime.class) return toLocalDateTime(value);
        if (targetType == String.class) return String.valueOf(value);
        if (value == null) return null;
        if (targetType.isAssignableFrom(value.getClass())) return value;
        return convertByIntrospection(value, targetType);
    }

    private static Object convertByIntrospection(Object value, Class<?> targetType) {
        Object[] reference = new Object[1];

        if (constructed(targetType, value, reference) ||
                factored(targetType, value, reference) ||
                parsed(targetType, value, reference) ||
                converted(targetType, value, reference)) return reference[0];

        throw new ClassCastException("Could not cast " + value + " to " + targetType);
    }

    private static boolean constructed(Class<?> targetType, Object value, Object[] reference) {
        Constructor<?> constructor = Stream.of(targetType.getConstructors())
                .filter(c -> Modifier.isPublic(c.getModifiers()) &&
                        c.getParameters().length == 1 &&
                        ClassUtils.isAssignable(value.getClass(), c.getParameterTypes()[0], true))
                .findFirst()
                .orElse(null);

        if (constructor != null) {
            try {
                reference[0] = constructor.newInstance(value);
                return true;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                return false;
            }
        }

        return false;
    }

    private static boolean factored(Class<?> targetType, Object value, Object[] reference) {
        Method factoryMethod = Stream.of(targetType.getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()) &&
                        Modifier.isStatic(m.getModifiers()) &&
                        ClassUtils.isAssignable(m.getReturnType(), targetType, true) &&
                        m.getParameters().length == 1 &&
                        ClassUtils.isAssignable(value.getClass(), m.getParameterTypes()[0], true))
                .findFirst()
                .orElse(null);

        if (factoryMethod != null) {
            try {
                reference[0] = factoryMethod.invoke(null, value);
                return true;
            } catch (IllegalAccessException | InvocationTargetException ignored) {
                return false;
            }
        }

        return false;
    }

    private static boolean parsed(Class<?> targetType, Object value, Object[] reference) {
        return (value instanceof CharSequence) && factored(targetType, value.toString(), reference);
    }

    private static boolean converted(Class<?> targetType, Object value, Object[] reference) {
        Method converterMethod = Stream.of(value.getClass().getMethods())
                .filter(m -> Modifier.isPublic(m.getModifiers()) &&
                        !Modifier.isStatic(m.getModifiers()) &&
                        m.getName().equalsIgnoreCase("to" + targetType.getSimpleName()) &&
                        m.getReturnType() == targetType &&
                        m.getParameters().length == 0)
                .findFirst()
                .orElse(null);

        if (converterMethod != null) {
            try {
                reference[0] = converterMethod.invoke(value);
                return true;
            } catch (IllegalAccessException | InvocationTargetException ignored) {
                return false;
            }
        }

        return false;
    }
}
