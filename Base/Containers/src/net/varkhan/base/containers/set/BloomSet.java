package net.varkhan.base.containers.set;

import net.varkhan.base.containers.Hashes;
import net.varkhan.base.containers.HashingStrategy;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.array.BitArrays;

import java.io.Serializable;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 4:13 PM
 */
public class BloomSet<Key> implements Set<Key>, Serializable, Cloneable {

    protected final HashingStrategy<Key> hash;
    protected int size = 0;
    protected final int num;
    protected final int len;
    protected final long msk;
    protected /*final*/ byte[] filter;

    public BloomSet() {
        this(10,7);
    }

    @SuppressWarnings("unchecked")
    public BloomSet(int num, int deg) {
        this((HashingStrategy<Key>) Hashes.DefaultHashingStrategy,num,deg);
    }

    public BloomSet(HashingStrategy<Key> hash, int num, int len) {
        this.hash=hash;
        this.num = num>512?512:num;
        this.len = (1<<(BitArrays.lsb(len)-3));
        this.msk = (1<<BitArrays.lsb(len))-1;
        this.filter = new byte[this.len];
    }

    protected long hash(long h, int i) {
        return Hashes.mix(h+i) & msk;
    }

    public long size() {
        return size;
    }

    public boolean isEmpty() {
        return size==0;
    }

    public void clear() {
        size = 0;
        for(int i=0; i<filter.length; i++) filter[i] = 0;
    }

    public boolean add(Key key) {
        long h = hash.hash(key);
        boolean a = false;
        for(int i=0; i<num; i++) {
            long p = hash(h, i);
            int o = (int) (p >>> 3);
            int m = 1 << (p & 7);
            // If bit is not set, mark as add
            if( ( filter[o] & m ) == 0 ) a = true;
            filter[o] |= m;
        }
        if(a) size++;
        return a;
    }

    public boolean has(Key key) {
        long h = hash.hash(key);
        for(int i=0; i<num; i++) {
            long p = hash(h, i);
            int o = (int) (p >>> 3);
            int m = 1 << (p & 7);
            if( ( filter[o] & m) == 0 ) return false;
        }
        return true;
    }

    public boolean del(Key key) {
        return false;
    }

    @SuppressWarnings({ "unchecked" })
    public Iterator<? extends Key> iterator() {
        return Iterator.EMPTY;
    }

    public <Par> long visit(Visitor<Key,Par> vis, Par par) {
        return 0;
    }

    @SuppressWarnings("unchecked")
    public BloomSet<Key> clone() {
        BloomSet<Key> c;
        try {
            c = (BloomSet<Key>) super.clone();
        }
        catch(CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.filter = this.filter.clone();
        return c;
    }

}
