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
public class BloomWeightingSet<Key> implements WeightingSet<Key>, Serializable, Cloneable {

    protected final HashingStrategy<Key> hash;
    protected long   size  =0;
    protected double weight=0;
    protected final int     num;
    protected final int     len;
    protected final long    msk;
    protected /*final*/ float[] filter;

    @SuppressWarnings("unchecked")
    public BloomWeightingSet(int num, int len) {
        this((HashingStrategy<Key>) Hashes.DefaultHashingStrategy, num, len);
    }

    public BloomWeightingSet(HashingStrategy<Key> hash, int num, int len) {
        this.hash=hash;
        this.num=num>512 ? 512 : num;
        this.len=(1<<BitArrays.lsb(len));
        this.msk=(1<<BitArrays.lsb(len))-1;
        this.filter = new float[this.len];
    }

    protected int hash(long h, int i) {
        return (int) (Hashes.mix(h+i) & msk);
    }

    public long size() {
        return size;
    }

    public boolean isEmpty() {
        return weight==0;
    }

    public double weight() {
        return weight;
    }

    public void clear() {
        size = 0;
        weight= 0;
        for(int i=0; i<filter.length; i++) filter[i] = 0;
    }

    public boolean add(Key key) {
        return add(key,1.0);
    }

    public boolean add(Key key, double wgh) {
        if(wgh==0) return false;
        long h = hash.hash(key);
        boolean a = false;
        boolean d = false;
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            if(filter[p]==0) a = true;
            if((filter[p] += wgh)==0) d = true;
        }
        if(a) size++;
        if(d) size--;
        weight += wgh;
        return a||d;
    }

    public boolean del(Key key) {
        long h = hash.hash(key);
        float wp = +Float.MAX_VALUE;
        float wm = -Float.MAX_VALUE;
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            float x = filter[p];
            if(wp>x && x>0) wp=x;
            if(wm<x && x<0) wm=x;
        }
        if(wp==0||wm==0) return false;
        float w = (wp>-wm)?wm:wp;
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            filter[p]-=w;
        }
        size--;
        weight -= w;
        return true;
    }

    public boolean has(Key key) {
        long h = hash.hash(key);
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            float x = filter[p];
            if(x==0) return false;
        }
        return true;
    }

    public double weight(Key key) {
        long h = hash.hash(key);
        float wp = +Float.MAX_VALUE;
        float wm = -Float.MAX_VALUE;
        for(int i=0; i<num; i++) {
            int p = hash(h, i);
            float x = filter[p];
            if(wp>x && x>0) wp=x;
            if(wm<x && x<0) wm=x;
        }
        if(wp>-wm) return wm;
        else return wp;
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
            buf.append(filter[i]>0 ? String.format("%3f", filter[i]) : "   ");
        }
        return buf.toString();
    }

    @SuppressWarnings("unchecked")
    public BloomWeightingSet<Key> clone() {
        BloomWeightingSet<Key> c;
        try {
            c=(BloomWeightingSet<Key>) super.clone();
        }
        catch(CloneNotSupportedException e) {
            throw new InternalError();
        }
        c.filter = this.filter.clone();
        return c;
    }

}
