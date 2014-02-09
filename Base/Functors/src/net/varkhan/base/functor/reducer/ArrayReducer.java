package net.varkhan.base.functor.reducer;

import net.varkhan.base.functor.Reducer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;


/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 5:14 PM
 */
public class ArrayReducer<T,C> implements Reducer<T[],T,C> {
    protected final Class<T> cls;

    public ArrayReducer(Class<T> cls) {
        this.cls=cls;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T[] invoke(Iterable<T> src, C ctx) {
        List<T> lst=new ArrayList<T>();
        for(T val : src) lst.add(val);
        return lst.toArray((T[])Array.newInstance(cls, lst.size()));
    }

    @Override
    public String toString() {
        return "$";
    }

}
