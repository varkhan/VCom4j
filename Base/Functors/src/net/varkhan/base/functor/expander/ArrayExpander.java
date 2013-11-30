package net.varkhan.base.functor.expander;

import net.varkhan.base.functor.Expander;

import java.util.Iterator;


/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 5:14 PM
 */
public class ArrayExpander<T,C> implements Expander<T,T[],C> {

    @Override
    public Iterable<T> invoke(T[] src, C ctx) {
        return new ArrayIterable<T>(src);
    }


    public static class ArrayIterable<T> implements Iterable<T> {
        protected final T[] src;

        public ArrayIterable(T[] src) {
            this.src = src;
        }

        @Override
        public Iterator<T> iterator() {
            return new ArrayIterator<T>(src);
        }
    }

    public static class ArrayIterator<T> implements Iterator<T> {
        protected final T[] itr;
        protected volatile int pos=0;

        public ArrayIterator(T[] itr) {
            this.itr = itr;
        }

        @Override
        public boolean hasNext() {
            return pos<itr.length;
        }

        @Override
        public T next() {
            return itr[pos++];
        }

        @Override
        public void remove() { }
    }
}
