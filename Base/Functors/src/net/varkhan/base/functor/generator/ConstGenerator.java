package net.varkhan.base.functor.generator;

import net.varkhan.base.functor.Generator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 5:48 PM
 */
public class ConstGenerator<R,C> implements Generator<R,C> {

    protected final R val;

    public ConstGenerator(R val) { this.val=val; }

    public R invoke(C ctx) { return val; }

    protected static final ConstGenerator<Object,Object> NULL=new ConstGenerator<Object,Object>(null);

    @SuppressWarnings({ "unchecked" })
    public static <R,C> Generator<R,C> NULL() { return (Generator) NULL;}

    public static <R,C> Generator<R,C> as(R val) { return new ConstGenerator<R,C>(val);}

}
