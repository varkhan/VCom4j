package net.varkhan.base.containers.set;

import net.varkhan.base.containers.Hashes;
import net.varkhan.base.containers.HashingStrategy;
import net.varkhan.base.containers.Iterator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/11/12
 * @time 4:13 PM
 */
public class BloomSet<Key> implements Set<Key> {
    private final HashingStrategy<Key> hash;
    private int size = 0;
    private final int num;
    private final int len;
    private final long msk;
    private final byte[][] filter;

    public BloomSet(HashingStrategy<Key> hash, int num, int deg) {
        this.hash=hash;
        this.num = num>512?512:num;
        this.len = (1<<(deg-3));
        this.msk = (1<<deg) - 1;
        this.filter = new byte[this.num][];
        for(int i=0; i<this.filter.length; i++) filter[i] = new byte[this.len];
    }

    public long size() {
        return size;
    }

    public boolean isEmpty() {
        return size==0;
    }

    public void clear() {
        size = 0;
        for(int i=0; i<this.filter.length; i++) {
            for(int j=0; j<filter[i].length; j++) filter[i][j] = 0;
        }
    }

    public boolean add(Key key) {
        long h = hash.hash(key);
        boolean b = true;
        for(int i=0; i<this.filter.length; i++) {
            byte[] block = this.filter[i];
            long p = ( h * Hashes.prime(i) ) & msk;
            int o = (int) (p >> 3);
            int m = 1 << (p & 7);
            b &= ( block[o] & m) !=0 ;
            block[o] |= m;
        }
        if(!b) size++;
        return !b;
    }

    public boolean has(Key key) {
        long h = hash.hash(key);
        boolean b = true;
        for(int i=0; i<this.filter.length; i++) {
            byte[] block = this.filter[i];
            long p = ( h * Hashes.prime(i) ) & msk;
            int o = (int) (p >> 3);
            int m = 1 << (p & 7);
            b &= ( block[o] & m) !=0 ;
        }
        return b;
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

}
