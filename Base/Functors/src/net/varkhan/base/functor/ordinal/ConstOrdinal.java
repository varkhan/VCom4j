package net.varkhan.base.functor.ordinal;

import net.varkhan.base.functor.Ordinal;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 5:12 PM
 */
public class ConstOrdinal<A,C> implements Ordinal<A,C> {

    protected final long car;
    protected final long val;

    public ConstOrdinal(long car, long val) {
        this.car=car;
        this.val=val;
    }

    public long cardinal() { return car; }
    public long invoke(A arg, C ctx) { return val; }

    protected static final Ordinal<?,?> EMPTY=new ConstOrdinal(0, 0);
    protected static final Ordinal<?,?> UNITY=new ConstOrdinal(1, 0);

    @SuppressWarnings({ "unchecked" })
    public static <A,C> Ordinal<A,C> EMPTY() { return (Ordinal<A,C>) EMPTY; }

    @SuppressWarnings({ "unchecked" })
    public static <A,C> Ordinal<A,C> UNITY() { return (Ordinal<A,C>) UNITY; }

}
