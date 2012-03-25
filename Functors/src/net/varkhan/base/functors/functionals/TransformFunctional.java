package net.varkhan.base.functors.functionals;

import net.varkhan.base.functors.Functional;


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
        };
    }

    public static <A, C> TransformFunctional<A, C> exp(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.exp(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> sin(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.sin(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> cos(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.cos(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> tan(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.tan(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> abs(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.abs(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> asin(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.asin(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> acos(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.acos(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> atan(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.atan(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> sinh(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.sinh(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> cosh(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.cosh(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> tanh(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.tanh(func.invoke(arg, ctx)); }
        };
    }

    public static <A, C> TransformFunctional<A, C> sqrt(Functional<A,C> func) {
        return new TransformFunctional<A,C>(func) {
            public double invoke(A arg, C ctx) { return Math.sqrt(func.invoke(arg, ctx)); }
        };
    }

}
