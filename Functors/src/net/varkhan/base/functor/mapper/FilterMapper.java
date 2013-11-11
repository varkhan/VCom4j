package net.varkhan.base.functor.mapper;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Predicate;

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
public class FilterMapper<A,C> implements Mapper<Iterable<A>,Iterable<A>,C> {

    protected final Predicate<A,C> pred;

    public FilterMapper(Predicate<A,C> pred) {
        this.pred=pred;
    }

    @Override
    public Iterable<A> invoke(Iterable<A> arg, C ctx) {
        return new FilterIterable<A,C>(pred,arg,ctx);
    }

    public static class FilterIterable<A,C> implements Iterable<A> {
        protected final Predicate<A,C> pred;
        protected final Iterable<A>    vals;
        protected final C              ctx;

        public FilterIterable(Predicate<A,C> pred, Iterable<A> vals, C ctx) {
            this.pred=pred;
            this.vals=vals;
            this.ctx=ctx;
        }

        @Override
        public Iterator<A> iterator() {
            return new FilterIterator<A,C>(pred,vals.iterator(),ctx);
        }

    }

    public static class FilterIterator<A,C> implements Iterator<A> {
        private static final Object MARK=new Object();
        protected final Predicate<A,C> pred;
        protected final Iterator<A>    iter;
        protected final C              ctx;
        protected volatile Object next=MARK;

        public FilterIterator(Predicate<A,C> pred, Iterator<A> iter, C ctx) {
            this.pred=pred;
            this.iter=iter;
            this.ctx=ctx;
        }

        @Override
        public boolean hasNext() {
            if(next!=MARK) return true;
            while(iter.hasNext()) {
                A val = iter.next();
                if(pred.invoke(val,ctx)) {
                    next = val;
                    return true;
                }
            }
            return false;
        }

        @Override
        @SuppressWarnings("unchecked")
        public A next() {
            if(!hasNext()) throw new NoSuchElementException();
            A val = (A) next;
            next = MARK;
            return val;
        }

        @Override
        public void remove() {
            iter.remove();
        }
    }
}
