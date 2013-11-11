package net.varkhan.data.ling.tokenize;

import net.varkhan.base.functor.Expander;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 4:48 PM
 */
public class NgramTokenizer<T,S,C> implements Expander<T[],S,C> {
    protected final Class<T> cls;
    protected final Expander<T,S,C> atk;
    protected final int min;
    protected final int max;


    public NgramTokenizer(Class<T> cls, Expander<T, S, C> atk, int min, int max) {
        this.cls = cls;
        this.atk = atk;
        this.min = min;
        this.max = max;
    }


    @Override
    public Iterable<T[]> invoke(S src, C ctx) {
        return new NgramTokens<T>(cls, atk.invoke(src, ctx),min,max);
    }

    protected static class NgramTokens<T> implements Iterable<T[]> {
        protected final Class<T> cls;
        protected final Iterable<T> atn;
        protected final int min;
        protected final int max;

        public NgramTokens(Class<T> cls, Iterable<T> atn, int min, int max) {
            this.cls = cls;
            this.atn = atn;
            this.min = min;
            this.max = max;
        }

        @Override
        public Iterator<T[]> iterator() {
            return new NgramIterator<T>(cls, atn.iterator(),min,max);
        }
    }

    protected static class NgramIterator<T> implements Iterator<T[]> {
        protected final Class<T> cls;
        protected final Iterator<T> itr;
        protected final int min;
        protected final int max;
        protected final Object[] buf;
        protected volatile int lps=0;
        protected volatile int hps=0;

        public NgramIterator(Class<T> cls, Iterator<T> itr, int min, int max) {
            this.cls = cls;
            this.itr = itr;
            this.min = min;
            this.max = max;
            this.buf = new Object[max];
        }

        @Override
        public boolean hasNext() {
            while(lps<min) {
                if(!getToken()) return false;
                lps ++;
            }
            if(lps>hps) {
                if(!getToken()) return false;
                lps=min;
            }
            return true;
        }

        protected boolean getToken() {
            if(!itr.hasNext()) return false;
            T tk = itr.next();
            System.arraycopy(buf,0,buf,1,buf.length-1);
            buf[0] = tk;
            if(hps<max) hps++;
            return true;
        }

        @Override
        @SuppressWarnings("unchecked")
        public T[] next() {
            if(!hasNext()) throw new NoSuchElementException();
            T[] ta = (T[])Array.newInstance(cls,lps);
            for(int i=0; i<lps; i++) {
                ta[i] = (T) buf[lps-i-1];
            }
            lps ++;
            return ta;
        }

        @Override
        public void remove() { }
    }
}
