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
 * @time 3:45 PM
 */
public class BloomCountingSet<Key> implements CountingSet<Key> {

    private final HashingStrategy<Key> hash;
    private long size = 0;
    private long count = 0;
    private final int num;
    private final int len;
    private final long msk;
    private final int[][] filter;

    public BloomCountingSet(HashingStrategy<Key> hash, int num, int deg) {
        this.hash=hash;
        this.num = num>512?512:num;
        this.len = (1<<deg);
        this.msk = (1<<deg) - 1;
        this.filter = new int[this.num][];
        for(int i=0; i<this.filter.length; i++) filter[i] = new int[this.len];
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
        for(int i=0; i<this.filter.length; i++) {
            for(int j=0; j<filter[i].length; j++) filter[i][j] = 0;
        }
    }

    public boolean add(Key key) {
        long h = hash.hash(key);
        boolean b = true;
        for(int i=0; i<this.filter.length; i++) {
            int[] block = this.filter[i];
            long p = ( h * Hashes.prime(i) ) & msk;
            b &= ( block[(int)p] >0 );
            block[(int)p] ++;
        }
        if(!b) size++;
        return !b;
    }

    public boolean del(Key key) {
        long h = hash.hash(key);
        boolean b = true;
        for(int i=0; i<this.filter.length; i++) {
            int[] block = this.filter[i];
            long p = ( h * Hashes.prime(i) ) & msk;
            int x = block[(int)p];
            if(x>0) {
                block[(int)p]--;
                b &= x>1;
            }
        }
        if(!b) size--;
        return !b;
    }

    public boolean has(Key key) {
        long h = hash.hash(key);
        for(int i=0; i<this.filter.length; i++) {
            int[] block = this.filter[i];
            long p = ( h * Hashes.prime(i) ) & msk;
            int x = block[(int)p];
            if(x==0) return false;
        }
        return true;
    }

    public long count(Key key) {
        long h = hash.hash(key);
        long c = Long.MAX_VALUE;
        for(int i=0; i<this.filter.length; i++) {
            int[] block = this.filter[i];
            long p = ( h * Hashes.prime(i) ) & msk;
            int x = block[(int)p];
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

}
