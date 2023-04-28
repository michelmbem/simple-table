package org.addy.simpletable.util;

import de.odysseus.el.util.SimpleContext;
import org.addy.util.CollectionUtil;

import javax.el.ExpressionFactory;
import java.util.LinkedList;
import java.util.List;

public final class ELEngine {
    private static final List<ContextInitializer> contextInitializers = new LinkedList<>();

    static {
        addContextInitializer(new DefaultContextInitializer());
    }

    private ELEngine() {}

    public static void addContextInitializer(ContextInitializer contextInitializer) {
        contextInitializers.add(contextInitializer);
    }

    public static void removeContextInitializer(ContextInitializer contextInitializer) {
        contextInitializers.remove(contextInitializer);
    }

    public static void initializeContext(SimpleContext context, ExpressionFactory factory) {
        for (ContextInitializer contextInitializer : contextInitializers) {
            contextInitializer.apply(context, factory);
        }
    }


    @FunctionalInterface
    public interface ContextInitializer {
        void apply(SimpleContext context, ExpressionFactory factory);
    }

    public static class DefaultContextInitializer implements ContextInitializer {
        @Override
        public void apply(SimpleContext context, ExpressionFactory factory) {
            context.setVariable("MIN_SHORT", factory.createValueExpression(Short.MIN_VALUE, short.class));
            context.setVariable("MAX_SHORT", factory.createValueExpression(Short.MAX_VALUE, short.class));
            context.setVariable("MIN_INT", factory.createValueExpression(Integer.MIN_VALUE, int.class));
            context.setVariable("MAX_INT", factory.createValueExpression(Integer.MAX_VALUE, int.class));
            context.setVariable("MIN_LONG", factory.createValueExpression(Long.MIN_VALUE, long.class));
            context.setVariable("MAX_LONG", factory.createValueExpression(Long.MAX_VALUE, long.class));
            context.setVariable("E", factory.createValueExpression(Math.E, double.class));
            context.setVariable("PI", factory.createValueExpression(Math.PI, double.class));

            try {
                context.setFunction("", "abs", Math.class.getMethod("abs", int.class));
                context.setFunction("", "fabs", Math.class.getMethod("abs", double.class));
                context.setFunction("", "floor", Math.class.getMethod("floor", double.class));
                context.setFunction("", "ceil", Math.class.getMethod("ceil", double.class));
                context.setFunction("", "round", Math.class.getMethod("round", double.class));
                context.setFunction("", "sin", Math.class.getMethod("sin", double.class));
                context.setFunction("", "cos", Math.class.getMethod("cos", double.class));
                context.setFunction("", "tan", Math.class.getMethod("tan", double.class));
                context.setFunction("", "atan", Math.class.getMethod("atan", double.class));
                context.setFunction("", "ln", Math.class.getMethod("log", double.class));
                context.setFunction("", "log", Math.class.getMethod("log10", double.class));
                context.setFunction("", "exp", Math.class.getMethod("exp", double.class));
                context.setFunction("", "pow", Math.class.getMethod("pow", double.class, double.class));
                context.setFunction("", "sqrt", Math.class.getMethod("sqrt", double.class));
                context.setFunction("", "cbrt", Math.class.getMethod("cbrt", double.class));

                context.setFunction("", "format", String.class.getMethod("format", String.class, Object[].class));
                context.setFunction("", "join", String.class.getMethod("join", CharSequence.class, CharSequence[].class));

                context.setFunction("", "min", HelperMethods.class.getMethod("min", Comparable.class, Comparable[].class));
                context.setFunction("", "max", HelperMethods.class.getMethod("max", Comparable.class, Comparable[].class));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public static final class HelperMethods {
        private HelperMethods() {}

        @SafeVarargs
        public static <T extends Comparable<T>> T min(T first, T... next) {
            if (CollectionUtil.isEmpty(next)) return first;

            T minimum = first;

            for (T other : next) {
                if (other.compareTo(minimum) < 0) {
                    minimum = other;
                }
            }

            return minimum;
        }

        @SafeVarargs
        public static <T extends Comparable<T>> T max(T first, T... next) {
            if (CollectionUtil.isEmpty(next)) return first;

            T maximum = first;

            for (T other : next) {
                if (other.compareTo(maximum) > 0) {
                    maximum = other;
                }
            }

            return maximum;
        }
    }
}
