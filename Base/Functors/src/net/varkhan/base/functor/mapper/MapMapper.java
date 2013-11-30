package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Mapper;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 6:00 PM
 */
public class MapMapper<K,R,A,C> implements Mapper<Map<K,R>,A,C> {

    protected final Map<K,Mapper<R,A,C>> maps;

    public MapMapper(Map<K,Mapper<R,A,C>> maps) {
        this.maps = new HashMap<K,Mapper<R,A,C>>(maps);
    }

    @SuppressWarnings({ "unchecked" })
    public Map<K,R> invoke(A arg, C ctx) {
        Map<K,R> res = new HashMap<K,R>(maps.size());
        for(Map.Entry<K,Mapper<R,A,C>> m: maps.entrySet()) res.put(m.getKey(),m.getValue().invoke(arg, ctx));
        return res;
    }

}
