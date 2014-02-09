package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 5:48 PM
 */
public class ConstMapper<R,A,C> implements Mapper<R,A,C> {

    protected final R val;

    public ConstMapper(R val) { this.val=val; }

    public R invoke(A arg, C ctx) { return val; }

    protected static final ConstMapper<Object,Object,Object> NULL = new ConstMapper<Object,Object,Object>(null);

    @SuppressWarnings({ "unchecked" })
    public static <R,A,C> Mapper<R,A,C> NULL() { return (Mapper) NULL;}

    public static <R,A,C> Mapper<R,A,C> as(R val) { return new ConstMapper<R,A,C>(val);}

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        if(val==null) buf.append('\u2205');
        else buf.append('(').append(val.toString()).append(')');
        return buf.toString();
    }

}
