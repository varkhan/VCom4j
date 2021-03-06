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
public abstract class AggregateFunctional<A,C> implements Functional<A,C> {

    protected final Functional<A, C>[] funcs;

    public AggregateFunctional(Functional<A, C>... funcs) {
        this.funcs = funcs;
    }

    public Functional<A, C>[] components() {
        return funcs.clone();
    }

    public abstract double invoke(A arg, C ctx);

    public static <A,C> AggregateFunctional<A,C> sum(Functional<A, C>... funcs) {
        return new AggregateFunctional<A,C>(funcs) {
            @Override
            public double invoke(A arg, C ctx) {
                double val = 0;
                for(Functional<A, C> f: funcs) {
                    val += f.invoke(arg, ctx);
                }
                return val;
            }
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder();
                boolean f = true;
                for(Functional<A,C> m: funcs) {
                    if(f) f = false;
                    else buf.append('+');
                    buf.append(m.toString());
                }
                return buf.toString();
            }
        };
    }

    public static <A,C> AggregateFunctional<A,C> prod(Functional<A, C>... funcs) {
        return new AggregateFunctional<A,C>(funcs) {
            @Override
            public double invoke(A arg, C ctx) {
                double val = 1;
                for(Functional<A, C> f: funcs) {
                    val *= f.invoke(arg, ctx);
                }
                return val;
            }
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder();
                boolean f = true;
                for(Functional<A,C> m: funcs) {
                    if(f) f = false;
                    else buf.append('*');
                    buf.append(m.toString());
                }
                return buf.toString();
            }
        };
    }

    public static <A,C> AggregateFunctional<A,C> min(Functional<A, C>... funcs) {
        return new AggregateFunctional<A,C>(funcs) {
            @Override
            public double invoke(A arg, C ctx) {
                double val = Double.MAX_VALUE;
                for(Functional<A, C> f: funcs) {
                    double v = f.invoke(arg, ctx);
                    if(val>v) val = v;
                }
                return val;
            }
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder("min");
                buf.append('(');
                boolean f = true;
                for(Functional<A,C> m: funcs) {
                    if(f) f = false;
                    else buf.append(',');
                    buf.append(m.toString());
                }
                buf.append(')');
                return buf.toString();
            }
        };
    }

    public static <A,C> AggregateFunctional<A,C> max(Functional<A, C>... funcs) {
        return new AggregateFunctional<A,C>(funcs) {
            @Override
            public double invoke(A arg, C ctx) {
                double val = -Double.MAX_VALUE;
                for(Functional<A, C> f: funcs) {
                    double v = f.invoke(arg, ctx);
                    if(val<v) val = v;
                }
                return val;
            }
            @Override
            public String toString() {
                StringBuilder buf = new StringBuilder("max");
                buf.append('(');
                boolean f = true;
                for(Functional<A,C> m: funcs) {
                    if(f) f = false;
                    else buf.append(',');
                    buf.append(m.toString());
                }
                buf.append(')');
                return buf.toString();
            }
        };
    }

    protected String toString(String op) {
        StringBuilder buf = new StringBuilder(op);
        buf.append('(');
        boolean f = true;
        for(Functional<A,C> m: funcs) {
            if(f) f = false;
            else buf.append(',');
            buf.append(m.toString());
        }
        buf.append(')');
        return buf.toString();
    }

    @Override
    public String toString() {
        return toString(this.getClass().getSimpleName());
    }

}
