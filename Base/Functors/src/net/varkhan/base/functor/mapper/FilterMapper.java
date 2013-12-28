package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Expander;
import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Predicate;
import net.varkhan.base.functor.Reducer;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 4:07 PM
 */
public class FilterMapper<R,A,C> implements Mapper<Iterable<R>,Iterable<A>,C>, Expander<R,Iterable<A>,C>, Reducer<Iterable<R>,A,C> {

    protected final Predicate<A,C> select;
    protected final Mapper<R,A,C>  convert;

    public FilterMapper(Predicate<A,C> select, Mapper<R,A,C> convert) {
        this.select=select;
        this.convert=convert;
    }

    @Override
    public Iterable<R> invoke(Iterable<A> arg, C ctx) {
        return new FilterIterable<R,A,C>(select, convert, arg, ctx);
    }

    public static class FilterIterable<R,A,C> implements Iterable<R> {
        protected final Predicate<A,C> select;
        protected final Mapper<R,A,C>  convert;
        protected final Iterable<A>    vals;
        protected final C              ctx;

        public FilterIterable(Predicate<A,C> select, Mapper<R,A,C> convert, Iterable<A> vals, C ctx) {
            this.select=select;
            this.convert=convert;
            this.vals=vals;
            this.ctx=ctx;
        }

        @Override
        public Iterator<R> iterator() {
            return new FilterIterator<R,A,C>(select, convert, vals.iterator(), ctx);
        }

    }


    public static class FilterIterator<R,A,C> implements Iterator<R> {
        private static final Object NONE=new Object();
        protected final Predicate<A,C> select;
        protected final Mapper<R,A,C>  convert;
        protected final Iterator<A>    iter;
        protected final C              ctx;
        protected volatile Object next=NONE;

        public FilterIterator(Predicate<A,C> select, Mapper<R,A,C> convert, Iterator<A> iter, C ctx) {
            this.select=select;
            this.convert=convert;
            this.iter=iter;
            this.ctx=ctx;
        }

        @Override
        public boolean hasNext() {
            if(next!=NONE) return true;
            while(iter.hasNext()) {
                A val=iter.next();
                if(select==null||select.invoke(val, ctx)) {
                    next=val;
                    return true;
                }
            }
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public R next() {
            if(!hasNext()) throw new NoSuchElementException();
            A val=(A) next;
            next=NONE;
            return convert==null?((R)val):convert.invoke(val,ctx);
        }

        @Override
        public void remove() {
            iter.remove();
        }
    }
}
