package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Mapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/7/12
 * @time 6:00 PM
 */
public class ListMapper<R,A,C> implements Mapper<List<R>,A,C> {

    protected final List<Mapper<R,A,C>> maps;

    public ListMapper(Mapper<R,A,C>... maps) {
        this.maps = Arrays.asList(maps);
    }

    public ListMapper(List<Mapper<R,A,C>> maps) {
        this.maps = new ArrayList<Mapper<R, A, C>>(maps);
    }

    @SuppressWarnings({ "unchecked" })
    public List<R> invoke(A arg, C ctx) {
        List<R> res = new ArrayList<R>(maps.size());
        for(Mapper<R,A,C> m: maps) res.add(m.invoke(arg,ctx));
        return res;
    }

}
