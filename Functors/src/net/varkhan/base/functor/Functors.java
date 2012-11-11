package net.varkhan.base.functor;

import java.util.Iterator;


/**
 * <b>Static utilities to convert functor operations into and from container operations</b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 7:21 PM
 */
public class Functors {

    protected Functors() { }

    public <R,A,C> Iterable<R> transform(final Iterable<A> it, final Mapper<R,A,C> map, final Predicate<A,C> fil, final C ctx) {
        return new Iterable<R>() {
            @Override
            public Iterator<R> iterator() {
                return transform(it.iterator(), map, fil, ctx);
            }
        };
    }

    public <R,A,C> Iterator<R> transform(final Iterator<A> it, final Mapper<R,A,C> map, final Predicate<A,C> fil, final C ctx) {
        // Use an anonymous object as a marker
        final Object mark = new Object();
        return new Iterator<R>() {
            private Object last = mark;

            @Override
            public boolean hasNext() {
                if(last!=mark) return true;
                while(it.hasNext()) {
                    A val = it.next();
                    if(fil==null || fil.invoke(val,ctx)) {
                        last = val;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public R next() {
                if(last!=mark) {
                    @SuppressWarnings({ "unchecked" })
                    A val = (A)last;
                    last = mark;
                    return map.invoke(val,ctx);
                }
                while(it.hasNext()) {
                    A val = it.next();
                    if(fil==null || fil.invoke(val,ctx)) {
                        return map.invoke(val,ctx);
                    }
                }
                throw new IndexOutOfBoundsException();
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }

    public <T,C> Iterable<T> filter(final Iterable<T> it, final Predicate<T,C> fil, final C ctx) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return filter(it.iterator(), fil, ctx);
            }
        };
    }

    public <T,C> Iterator<T> filter(final Iterator<T> it, final Predicate<T,C> fil, final C ctx) {
        // Use an anonymous object as a marker
        final Object mark = new Object();
        return new Iterator<T>() {
            private Object last = mark;

            @Override
            public boolean hasNext() {
                if(last!=mark) return true;
                while(it.hasNext()) {
                    T val = it.next();
                    if(fil==null || fil.invoke(val,ctx)) {
                        last = val;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public T next() {
                if(last!=mark) {
                    @SuppressWarnings({ "unchecked" })
                    T val = (T)last;
                    last = mark;
                    return val;
                }
                while(it.hasNext()) {
                    T val = it.next();
                    if(fil==null || fil.invoke(val,ctx)) {
                        return val;
                    }
                }
                throw new IndexOutOfBoundsException();
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }

    public <R,A,C> Iterable<R> transform(final Iterable<A> it, final Mapper<R,A,C> map, final C ctx) {
        return new Iterable<R>() {
            @Override
            public Iterator<R> iterator() {
                return transform(it.iterator(), map, ctx);
            }
        };
    }

    public <R,A,C> Iterator<R> transform(final Iterator<A> it, final Mapper<R,A,C> map, final C ctx) {
        return new Iterator<R>() {

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public R next() {
                return map.invoke(it.next(),ctx);
            }

            @Override
            public void remove() {
                it.remove();
            }
        };
    }

    public <T,C> Iterable<T> generator(final Generator<T,C> gen, final C ctx) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return generate(gen, ctx);
            }
        };
    }

    public <T,C> Iterator<T> generate(final Generator<T,C> gen, final C ctx) {
        return new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public T next() {
                return gen.invoke(ctx);
            }

            @Override
            public void remove() {
            }
        };
    }


}
