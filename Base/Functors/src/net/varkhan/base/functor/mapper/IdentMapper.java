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
public class IdentMapper<A,C> implements Mapper<A,A,C> {

    public IdentMapper() { }

    public A invoke(A arg, C ctx) { return arg; }

    protected static final IdentMapper<Object,Object> ID = new IdentMapper<Object,Object>();

    @SuppressWarnings({ "unchecked" })
    public static <A,C> Mapper<A,A,C> as() { return (Mapper) ID;}

}
