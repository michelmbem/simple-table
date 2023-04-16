package org.addy.simpletable.util;

import de.odysseus.el.util.SimpleContext;

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
            context.setVariable("MIN_INT", factory.createValueExpression(Integer.MIN_VALUE, int.class));
            context.setVariable("MAX_INT", factory.createValueExpression(Integer.MAX_VALUE, int.class));
            context.setVariable("E", factory.createValueExpression(Math.E, double.class));
            context.setVariable("PI", factory.createValueExpression(Math.PI, double.class));

            try {
                context.setFunction("", "sin", Math.class.getMethod("sin", double.class));
                context.setFunction("", "cos", Math.class.getMethod("cos", double.class));
                context.setFunction("", "tan", Math.class.getMethod("tan", double.class));
                context.setFunction("", "log", Math.class.getMethod("log", double.class));
                context.setFunction("", "exp", Math.class.getMethod("exp", double.class));
                context.setFunction("", "floor", Math.class.getMethod("floor", double.class));
                context.setFunction("", "ceil", Math.class.getMethod("ceil", double.class));
                context.setFunction("", "round", Math.class.getMethod("round", double.class));
                context.setFunction("", "min", Math.class.getMethod("min", double.class, double.class));
                context.setFunction("", "max", Math.class.getMethod("max", double.class, double.class));

                context.setFunction("", "format", String.class.getMethod("format", String.class, Object[].class));
                context.setFunction("", "join", String.class.getMethod("join", CharSequence.class, CharSequence[].class));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }
}
