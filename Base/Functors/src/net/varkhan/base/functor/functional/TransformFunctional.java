package net.varkhan.base.functor.functional;

import net.varkhan.base.functor.Functional;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 3:23 PM
 */
public abstract class TransformFunctional<A,C> implements Functional<A,C> {

    protected final Functional<A, C> func;

    public TransformFunctional(Functional<A,C> func) {
        this.func = func;
    }

    public Functional<A, C> component() {
        return func;
    }

    public abstract double invoke(A arg, C ctx);


    public static <A, C> TransformFunctional<A, C> log(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.log(func.invoke(arg, ctx)); }
            public String toString() { return toString("log"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> exp(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.exp(func.invoke(arg, ctx)); }
            public String toString() { return toString("exp"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> sin(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.sin(func.invoke(arg, ctx)); }
            public String toString() { return toString("sin"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> cos(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.cos(func.invoke(arg, ctx)); }
            public String toString() { return toString("cos"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> tan(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.tan(func.invoke(arg, ctx)); }
            public String toString() { return toString("tan"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> abs(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.abs(func.invoke(arg, ctx)); }
            public String toString() { return toString("abs"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> asin(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.asin(func.invoke(arg, ctx)); }
            public String toString() { return toString("asin"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> acos(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.acos(func.invoke(arg, ctx)); }
            public String toString() { return toString("acos"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> atan(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.atan(func.invoke(arg, ctx)); }
            public String toString() { return toString("atan"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> sinh(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.sinh(func.invoke(arg, ctx)); }
            public String toString() { return toString("sinh"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> cosh(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.cosh(func.invoke(arg, ctx)); }
            public String toString() { return toString("cosh"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> tanh(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.tanh(func.invoke(arg, ctx)); }
            public String toString() { return toString("tanh"); }
        };
    }

    public static <A, C> TransformFunctional<A, C> sqrt(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.sqrt(func.invoke(arg, ctx)); }
            public String toString() { return toString("sqrt"); }
        };
    }

    protected String toString(String op) {
        StringBuilder buf = new StringBuilder(op);
        buf.append('(');
        buf.append(component().toString());
        buf.append(')');
        return buf.toString();
    }

    @Override
    public String toString() {
        return toString(this.getClass().getSimpleName());
    }

}
