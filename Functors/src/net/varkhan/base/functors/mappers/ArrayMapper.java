package net.varkhan.base.functors.mappers;

import net.varkhan.base.functors.Mapper;

import java.lang.reflect.Array;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 6:00 PM
 */
public class ArrayMapper<R,A,C> implements Mapper<R[],A,C> {

    protected final Mapper<R,A,C>[] maps;

    public ArrayMapper(Mapper<R,A,C>... maps) {
        this.maps = maps;
    }

    @SuppressWarnings({ "unchecked" })
    public R[] invoke(A arg, C ctx) {
        R[] res = (R[]) new Object[maps.length];
        for(int i=0; i<maps.length; i++) res[i] = maps[i].invoke(arg,ctx);
        return res;
    }

}
