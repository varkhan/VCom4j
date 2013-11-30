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
 * @time 3:45 PM
 */
public class BloomCountingSet<Key> implements CountingSet<Key>, Serializable, Cloneable {

    protected final HashingStrategy<Key> hash;
    protected long size = 0;
    protected long count = 0;
    protected final int num;
    protected final int len;
    protected final long msk;
    protected /*final*/ int[] filter;

    @SuppressWarnings("unchecked")
    public BloomCountingSet(int num, int len) {
        this((HashingStrategy<Key>) Hashes.DefaultHashingStrategy,num,len);
    }

    public BloomCountingSet(HashingStrategy<Key> hash, int num, int len) {
        this.hash=hash;
        this.num = num>512?512:num;
        this.len = (1<<BitArrays.lsb(len));
        this.msk = (1<<BitArrays.lsb(len))-1;
        this.filter = new int[this.len];
    }

    protected int hash(long h, int i) {
        return (int) (Hashes.mix(h+i) & msk);
    }

    public long size() {
        return size;
    }

    public boolean isEmpty() {
        return count==0;
    }

    public long count() {
        return count;
    }

    public void clear() {
        size = 0;
        count = 0;
        for(int i=0; i<filter.length; i++) filter[i] = 0;
    }

    public boolean add(Key key) {
        long h = hash.hash(key);
        boolean a = false;
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            // Mark as add if count is 0, then increment count
            if(filter[p]++ == 0) a = true;
        }
        if(a) size++;
        count ++;
        return a;
    }

    public boolean del(Key key) {
        long h = hash.hash(key);
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            int x = filter[p];
            if(x==0) return false;
        }
        boolean d = false;
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            // Decrement count, then mark as del if count is 0
            if(--filter[p] == 0) d = true;
        }
        if(d) size--;
        count --;
        return d;
    }

    public boolean has(Key key) {
        long h = hash.hash(key);
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            int x = filter[p];
            if(x==0) return false;
        }
        return true;
    }

    public long count(Key key) {
        long h = hash.hash(key);
        long c = Long.MAX_VALUE;
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            int x = filter[p];
            if(c>x) c=x;
        }
        return c;
    }

    @SuppressWarnings({ "unchecked" })
    public Iterator<? extends Key> iterator() {
        return Iterator.EMPTY;
    }

    public <Par> long visit(Visitor<Key,Par> vis, Par par) {
        return 0;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        for(int i=0; i<this.filter.length; i++) {
            if(i>0) buf.append('|');
            buf.append(filter[i]>0 ? String.format("%3d", filter[i]) : "   ");
        }
        return buf.toString();
    }

    @SuppressWarnings("unchecked")
    public BloomCountingSet<Key> clone() {
        BloomCountingSet<Key> c;
        try {
            c=(BloomCountingSet<Key>) super.clone();
        }
        catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
        c.filter = this.filter.clone();
        return c;
    }

}
