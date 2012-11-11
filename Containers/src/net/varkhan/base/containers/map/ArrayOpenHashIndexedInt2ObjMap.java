/**
 *
 */
package net.varkhan.base.containers.map;

import net.varkhan.base.containers.*;
import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.type.IndexedIntContainer;
import net.varkhan.base.containers.type.IntHashingStrategy;
import net.varkhan.base.containers.type.IntIterable;

import java.io.Serializable;
import java.util.NoSuchElementException;


/**
 * @author varkhan
 * @date May 28, 2009
 * @time 9:43:13 PM
 */
public class ArrayOpenHashIndexedInt2ObjMap<Value> implements IndexedInt2ObjMap<Value>, Serializable, Cloneable {

    public static final long serialVersionUID=1L;

    /**
     * The array of keys
     */
    protected int[] keys;

    /**
     * The array of values
     */
    protected Object[] vals;

    /**
     * The array of occupancy flags
     */
    protected byte[] flgs;

    /**
     * The array of indexes
     */
    protected int[] idxs;

    /**
     * The acceptable load factor
     */
    protected final float lfact;

    /**
     * The growth factor of the table
     */
    protected final float gfact;

    /**
     * The total number of entries in the table
     */
    protected int capa;

    /**
     * The mixing (secondary hashing) factor
     */
    protected int mixr;

    /**
     * Number of entries in the set
     */
    protected int size;

    /**
     * The maximum used index in the set, plus 1
     */
    protected int head;

    /**
     * Number of free entries in the table (may be less than the {@link #capa} - {@link #size} because of deleted entries)
     */
    protected int free;

    /**
     * Threshold after which we rehash. It must be the table size times {@link #lfact}
     */
    protected int fill;

    /**
     * The default value to return on absent keys
     */
    protected int defKey=0;

    /**
     * The hash strategy of this set
     */
    protected IntHashingStrategy strategy;

    /**
     * Creates a new hash set.
     *
     * @param size     the expected number of elements in the set
     * @param loadfact the load factor (between 0 exclusive and 1 inclusive)
     * @param growfact the growth factor (strictly greater than 1)
     * @param strategy the hashing strategy
     */
    public ArrayOpenHashIndexedInt2ObjMap(long size, float loadfact, float growfact, final IntHashingStrategy strategy) {
//        if(loadfact<=0 || loadfact>1) throw new IllegalArgumentException("Load factor must be between 0 exclusive and 1 inclusive");
//        if(growfact<=1) throw new IllegalArgumentException("Growth factor must be strictly greater than 1");
//        if(size<0) throw new IllegalArgumentException("Hash table size must be nonnegative");
        if(loadfact>=1) loadfact=.75f;
        if(growfact<=1) growfact=1.5f;
        if(size<0) size=0;
        this.strategy=strategy;
        this.lfact=loadfact;
        this.gfact=growfact;
        this.capa=getCapa((int) size);
        this.mixr=getMixr(this.capa, (int) size);
        this.fill=(int) (capa*loadfact);
        this.free=this.capa;
        this.size=0;
        this.head=0;
        this.idxs=new int[capa];
        this.flgs=new byte[((capa+7)>>3)];
        this.keys=new int[capa];
        this.vals=new Object[capa];
    }

    /**
     * Creates a new hash set.
     *
     * @param size the expected number of elements in the set
     */
    public ArrayOpenHashIndexedInt2ObjMap(long size) {
        this(size, .75f, 1.5f, IntHashingStrategy.DefaultHashingStrategy);
    }

    /**
     * Creates a new hash set.
     */
    public ArrayOpenHashIndexedInt2ObjMap() {
        this(11, .75f, 1.5f, IntHashingStrategy.DefaultHashingStrategy);
    }

    /**********************************************************************************
     **  Global information accessors
     **/

    public long size() { return size; }

    public boolean isEmpty() { return size==0; }

    public long head() { return head; }

    public void clear() {
        if(free==capa) return;
        free=capa;
        size=0;
        head=0;
        for(int i=0;i<idxs.length;i++) idxs[i]=0;
        for(int i=0;i<flgs.length;i++) flgs[i]=0;
        for(int i=0;i<vals.length;i++) vals[i]=null;
    }

    public int getDefaultKey() { return defKey; }

    public void setDefaultKey(int def) { defKey=def; }


    /**********************************************************************************
     **  Set elements accessors
     **/

    public boolean has(long index) {
        if(index<0||index>=this.head) return false;
        return (this.flgs[(int) (index>>3)]&(1<<(index&7)))!=0;
    }

    public long index(final Integer key) {
        if(key==null) return -1;
        return index(key.intValue());
    }

    public long index(final int key) {
        final int capa=this.capa;
        final int mixr=this.mixr;
        final long hash=this.strategy.hash(key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        int slot=(int) (ph%capa);
        // The signed lookup index in the key table
        int sidx=this.idxs[slot];
        // The key table value
        int kval;
        if(sidx<0||(sidx>0&&!(this.strategy.hash((kval=this.keys[(sidx-1)]))==hash&&this.strategy.equal(key, kval)))) {
            // The scan increment
            final int scan=(int) (ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=this.idxs[slot];
                // Fill limit ensures we can always reach an EMPTY slot
            }
            while(sidx<0||(sidx>0&&!(this.strategy.hash((kval=this.keys[(sidx-1)]))==hash&&this.strategy.equal(key, kval))));
        }
        return sidx>0 ? sidx-1 : -1; // If OCCUPIED, necessarily, key_match(key, hash, kval).
    }

    public IndexedInt2ObjMap.Entry<Value> get(long index) {
        if(index<0||index>=this.head) return null;
        if((this.flgs[(int) (index>>3)]&(1<<(index&7)))==0) return null;
        return new Entry((int) index, this.keys[(int) index]);
    }

    public Integer getKey(long index) {
        if(index<0||index>=this.head) return null;
        if((this.flgs[(int) (index>>3)]&(1<<(index&7)))==0) return null;
        return this.keys[(int) index];
    }

    public int getIntKey(long index) {
        if(index<0||index>=this.head) return defKey;
        if((this.flgs[(int) (index>>3)]&(1<<(index&7)))==0) return defKey;
        return this.keys[(int) index];
    }

    @SuppressWarnings("unchecked")
    public Value getValue(long index) {
        if(index<0||index>=this.head) return null;
        if((this.flgs[(int) (index>>3)]&(1<<(index&7)))==0) return null;
        return (Value) this.vals[(int) index];
    }

    public void setValue(long index, Value value) {
        if(index<0||index>=this.head) return;
        if((this.flgs[(int) (index>>3)]&(1<<(index&7)))==0) return;
        this.vals[(int) index]=value;
    }

    public long add(IndexedMap.Entry<Integer,Value> item) {
        if(item==null) return -1;
        return add(item.getKey(), item.getValue());
    }

    public long add(IndexedInt2ObjMap.Entry<Value> item) {
        if(item==null) return -1;
        return add(item.getIntKey(), item.getValue());
    }

    public long add(final Integer key, final Value val) {
        if(key==null) return -1;
        return add(key.intValue(), val);
    }

    public long add(final int key, final Value val) {
        final int capa=this.capa;
        final int mixr=this.mixr;
        final long hash=this.strategy.hash(key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        int slot=(int) (ph%capa);
        // The signed lookup index in the key table
        int sidx=this.idxs[slot];
        // The key table value
        int kval;
        if(sidx>0&&!(hash==this.strategy.hash((kval=this.keys[(sidx-1)]))&&this.strategy.equal(key, kval))) {
            // The scan increment
            final int scan=(int) (ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=this.idxs[slot];
                // Fill limit ensures we can always reach an EMPTY slot
            } while(sidx>0&&!(hash==this.strategy.hash((kval=this.keys[(sidx-1)]))&&this.strategy.equal(key, kval)));
        }
        final int pos=slot; // Remember first available slot for later.
        // Key found
        if(sidx>0) {
            sidx--;
            vals[sidx]=val;
            return sidx; // If OCCUPIED, necessarily, key_match(key, hash, kval).
        }
        // If this slot is REMOVED, keep looking for the key until we find it or get to an empty slot
        else if(sidx<0) {
            int pidx=sidx;
            sidx=-sidx;
            // We are on a REMOVED spot, so we have to continue scanning
            // The scan increment
            final int scan=(int) (ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                pidx=this.idxs[slot];
                // Fill limit ensures we can always reach an EMPTY slot
            }
            while(pidx<0||(pidx>0&&!(hash==this.strategy.hash((kval=this.keys[(pidx-1)]))&&this.strategy.equal(key, kval))));
            if(pidx>0) return pidx-1; // If OCCUPIED, necessarily, key_match(key, hash, kval).
        }
        // If this slot is EMPTY, allocate an index and reserve a slot
        else {
            sidx=++this.head;
            this.free--;
        }
        // Mark slot as OCCUPIED
        this.idxs[pos]=sidx;
        int index=sidx-1;
        this.keys[index]=key;
        this.vals[index]=val;
        this.flgs[(index>>3)]|=0x1<<(index&7);
        if(++this.size>=this.fill)
            rehash(getCapa((int) (this.size*this.gfact)));    // Too many elements for the capacity, rehash at an increased capa
        if(this.free==0)
            rehash(capa);                                    // Too many deletions, rehash at the same capa for cleanup
        return index;
    }

    public void del(long index) {
        if(index<0||index>=this.head) return;
        // We need to look up the key, to provide a starting point for the slot list
        if((this.flgs[(int) (index>>3)]&(1<<(index&7)))==0) return;
        int key=this.keys[(int) index];
        final int capa=this.capa;
        final int mixr=this.mixr;
        final long hash=this.strategy.hash(key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        int slot=(int) (ph%capa);
        // The signed lookup index in the key table
        int sidx=this.idxs[slot];
        // The actual lookup index in the key table
        int pidx=(int) (+index+1);
        int nidx=(int) (-index-1);
        // The key table value
//        int kval;
        if(sidx!=0&&sidx!=pidx&&sidx!=nidx) {
//        if ( sidx<0 || (sidx>0 && ! (strategy.hash((Key)(kval=getKey(sidx-1))))==hash && strategy.equal((Key)key, (Key)kval) )) ) {
            // The scan increment
            final int scan=(int) (ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=this.idxs[slot];
            } while(sidx!=0&&sidx!=pidx&&sidx!=nidx);
//            } while( sidx<0 || (sidx>0 && ! (strategy.hash((Key)(kval=getKey(sidx-1)))==hash && strategy.equal((Key)key, (Key)kval) )) );
            // At this point, we have either an EMPTY slot, or an OCCUPIED or DELETED slot with the right index
        }
        // If EMPTY or DELETED, nothing to be done
        if(sidx<=0) return;
        // At this point we necessarily have an OCCUPIED slot, and sidx-1 == index
        this.idxs[slot]=-sidx;
        this.vals[(int) index]=null;
        this.flgs[(int) (index>>3)]&=~(0x1<<(index&7));
        this.size--;
    }

    /**
     * Cleans-up the data in this set to make it more efficient.
     * <p/>
     * This method is generally useful to improve the set insertion and lookup
     * speed after many element deletions, or when it will not be modified anymore.
     *
     * @return {@literal true} if the operation succeeded
     *
     * @see #trim(long)
     * @see #pack()
     */
    public boolean rehash() {
        try { rehash(this.capa); }
        catch(OutOfMemoryError cantDoIt) { return false; }
        return true;
    }

    /**
     * Reorganizes the data in this set to make it more compact.
     * <p/>
     * This method is generally useful to minimize the set's memory footprint
     * after many element deletions, or when it will not be modified anymore.
     *
     * @return {@literal true} if the operation succeeded
     *
     * @see #trim(long)
     * @see #rehash()
     */
    public boolean pack() {
        try { rehash(getCapa(this.size)); }
        catch(OutOfMemoryError cantDoIt) { return false; }
        return true;
    }

    /**
     * Trims the set table to a given expected number of elements.
     * <p/>
     * This method is generally useful when reusing a set after clearing it,
     * to avoid keeping in memory tables that are significantly smaller than
     * required by the number of elements expected in the set.
     *
     * @param size the expected number of elements in the set
     *
     * @return {@literal true} if the operation succeeded
     *
     * @see #rehash()
     * @see #pack()
     */
    public boolean trim(long size) {
        try { rehash(getCapa((int) size)); }
        catch(OutOfMemoryError cantDoIt) { return false; }
        return true;
    }

    /**
     * Changes the capacity and rehashes the set.
     *
     * @param capa the new table capacity
     */
    protected void rehash(final int capa) {
        int mixr=getMixr(capa, this.size);
        int fill=(int) (capa*this.lfact);
        int pos=0, s=this.size;
        final int[] idxs=new int[(int) capa];
        int head=0;
        while(s-->0) {
            int idx;
            while((idx=this.idxs[pos])<=0) pos++;
            int key=this.keys[(idx-1)];
            final long ph=this.strategy.hash(key)&0x7FFFFFFFFFFFFFFFL;
            // The slot position, starting at the primary hash
            int slot=(int) (ph%capa);
            if(idxs[slot]!=0) {
                int scan=(int) (ph%mixr)+1;
                do {
                    slot+=scan;
                    if(slot>=capa||slot<0) slot-=capa;
                } while(idxs[slot]!=0);
            }
            // idx is actually the index + 1
            idxs[slot]=idx;
            if(head<idx) head=idx;
            pos++;
        }
        // Increase the size of the keys if needed
        if(this.keys.length<capa) {
            final int[] keys=new int[capa];
            System.arraycopy(this.keys, 0, keys, 0, this.keys.length);
            this.keys=keys;
            final Object[] vals=new Object[capa];
            System.arraycopy(this.vals, 0, vals, 0, this.vals.length);
            this.vals=vals;
            final byte[] flgs=new byte[((capa+7)>>3)];
            System.arraycopy(this.flgs, 0, flgs, 0, this.flgs.length);
            for(int p=this.flgs.length;p<((capa+7)>>3);p++) flgs[p]=0;
            this.flgs=flgs;
        }
        // Decrease the size of the keys if possible
        else if(head<=this.keys.length&&capa<this.keys.length) {
            int knum=(head<capa) ? capa : head;
            final int[] keys=new int[knum];
            System.arraycopy(this.keys, 0, keys, 0, knum);
            this.keys=keys;
            final Object[] vals=new Object[knum];
            System.arraycopy(this.vals, 0, vals, 0, knum);
            this.vals=vals;
            final byte[] flgs=new byte[((knum+7)>>3)];
            System.arraycopy(this.flgs, 0, flgs, 0, ((knum+7)>>3));
            this.flgs=flgs;
        }
        this.capa=capa;
        this.mixr=mixr;
        this.free=this.capa-this.size;
        this.fill=fill;
        this.head=head;
        this.idxs=idxs;
    }


    protected int getCapa(int size) {
        if(size<this.size) size=this.size;
        int capa=(int) (size/this.lfact)+1;
        int min=0;
        int max=PRIMES.length-1;
        while(min<=max) {
            int mid=(min+max)>>1;
            int val=PRIMES[mid];
            if(val<capa) min=mid+1;
            else if(val>capa) max=mid-1;
            else return val;
        }
        if(min>=PRIMES.length) return PRIMES[PRIMES.length-1];
        return PRIMES[min];
    }

    protected int getMixr(int capa, int size) {
        return capa-2;
    }

    protected static final int PRIMES[]={ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 5, 5, 7, 7, 7,
                                          7, 7, 7, 7, 7, 7, 7, 7, 13, 13, 13, 13, 13, 13, 13, 13, 19, 19, 19, 19, 19,
                                          19, 19, 19, 19, 19, 19, 19, 31, 31, 31, 31, 31, 31, 31, 43, 43, 43, 43, 43,
                                          43, 43, 43, 61, 61, 61, 61, 61, 73, 73, 73, 73, 73, 73, 73, 103, 103, 109,
                                          109, 109, 109, 109, 139, 139, 151, 151, 151, 151, 181, 181, 193, 199, 199,
                                          199, 229, 241, 241, 241, 271, 283, 283, 313, 313, 313, 349, 349, 349, 349,
                                          421, 433, 463, 463, 463, 523, 523, 571, 601, 619, 661, 661, 661, 661, 661,
                                          823, 859, 883, 883, 883, 1021, 1063, 1093, 1153, 1153, 1231, 1321, 1321,
                                          1429, 1489, 1489, 1621, 1699, 1789, 1873, 1951, 2029, 2131, 2143, 2311,
                                          2383, 2383, 2593, 2731, 2803, 3001, 3121, 3259, 3391, 3583, 3673, 3919,
                                          4093, 4273, 4423, 4651, 4801, 5023, 5281, 5521, 5743, 5881, 6301, 6571,
                                          6871, 7129, 7489, 7759, 8089, 8539, 8863, 9283, 9721, 10141, 10531, 11071,
                                          11551, 12073, 12613, 13009, 13759, 14323, 14869, 15649, 16363, 17029,
                                          17839, 18541, 19471, 20233, 21193, 22159, 23059, 24181, 25171, 26263,
                                          27541, 28753, 30013, 31321, 32719, 34213, 35731, 37309, 38923, 40639,
                                          42463, 44281, 46309, 48313, 50461, 52711, 55051, 57529, 60091, 62299,
                                          65521, 68281, 71413, 74611, 77713, 81373, 84979, 88663, 92671, 96739,
                                          100801, 105529, 109849, 115021, 120079, 125509, 131011, 136861, 142873,
                                          149251, 155863, 162751, 169891, 177433, 185071, 193381, 202129, 211063,
                                          220021, 229981, 240349, 250969, 262111, 273643, 285841, 298411, 311713,
                                          325543, 339841, 355009, 370663, 386989, 404269, 422113, 440809, 460081,
                                          480463, 501829, 524221, 547399, 571603, 596929, 623353, 651019, 679909,
                                          709741, 741343, 774133, 808441, 844201, 881539, 920743, 961531, 1004119,
                                          1048573, 1094923, 1143283, 1193911, 1246963, 1302181, 1359733, 1420039,
                                          1482853, 1548541, 1616899, 1688413, 1763431, 1841293, 1922773, 2008081,
                                          2097133, 2189989, 2286883, 2388163, 2493853, 2604013, 2719669, 2840041,
                                          2965603, 3097123, 3234241, 3377191, 3526933, 3682363, 3845983, 4016041,
                                          4193803, 4379719, 4573873, 4776223, 4987891, 5208523, 5439223, 5680153,
                                          5931313, 6194191, 6468463, 6754879, 7053331, 7366069, 7692343, 8032639,
                                          8388451, 8759953, 9147661, 9552733, 9975193, 10417291, 10878619, 11360203,
                                          11863153, 12387841, 12936529, 13509343, 14107801, 14732413, 15384673,
                                          16065559, 16777141, 17519893, 18295633, 19105483, 19951231, 20834689,
                                          21757291, 22720591, 23726449, 24776953, 25873963, 27018853, 28215619,
                                          29464579, 30769093, 32131711, 33554011, 35039911, 36591211, 38211163,
                                          39903121, 41669479, 43514521, 45441199, 47452879, 49553941, 51747991,
                                          54039079, 56431513, 58930021, 61539091, 64263571, 67108669, 70079959,
                                          73182409, 76422793, 79806229, 83339383, 87029053, 90881083, 94906249,
                                          99108043, 103495879, 108077731, 112863013, 117860053, 123078019, 128526943,
                                          134217439, 140159911, 146365159, 152845393, 159612601, 166679173,
                                          174058849, 181765093, 189812341, 198216103, 206991601, 216156043,
                                          225726379, 235720159, 246156271, 257054491, 268435009, 280319203,
                                          292730833, 305691181, 319225021, 333358513, 348117151, 363529759,
                                          379624279, 396432481, 413983771, 432312511, 451452613, 471440161,
                                          492312523, 514109251, 536870839, 560640001, 585461743, 611382451,
                                          638450569, 666717199, 696235363, 727060069, 759249643, 792864871,
                                          827967631, 864625033, 902905501, 942880663, 984625531, 1028218189,
                                          1073741719, 1121280091, 1170923713, 1222764841, 1276901371, 1333434301,
                                          1392470281, 1454120779, 1518500173, 1585729993, 1655935399, 1729249999,
                                          1805811253, 1885761133, 1969251079, 2056437379, 2147482951 };


    /**********************************************************************************
     **  Set elements iterators
     **/

    /**
     * The base Entry object for this map
     */
    @SuppressWarnings("unchecked")
    private class Entry implements IndexedInt2ObjMap.Entry<Value> {
        private final int idx;
        private final int key;

        public Entry(int idx, int key) {
            this.idx=idx;
            this.key=key;
        }

        public long index() { return idx; }

        public Integer getKey() { return key; }

        public int getIntKey() { return key; }

        public Value getValue() { return (Value) vals[idx]; }

        public Value setValue(Value val) {
            Value old=(Value) vals[idx];
            vals[idx]=val;
            return old;
        }
    }

    /**
     * Returns an {@link Index} over the indexes in the set.
     *
     * @return an {@code Index} enumerating all indexes in the set
     */
    public Index indexes() {
        return new Index() {
            int curr=-1;
            /** The position of the next entry to be returned. */
            int next=-1;
            /** The position of the last entry that has been returned. */
            int last=-1;
            /** A downward counter measuring how many entries have been returned. */
            int c=ArrayOpenHashIndexedInt2ObjMap.this.size;

            public long current() {
                int idx;
                if(curr==-1||(idx=idxs[curr])<=0) throw new IllegalStateException();
                return idx-1;
            }

            public boolean hasNext() {
                if(c==0) return false;
                if(next==-1) {
                    next=curr+1;
                    while(next<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[next]<=0) next++;
                }
                return next<ArrayOpenHashIndexedInt2ObjMap.this.capa;
            }

            public long next() {
                last=curr;
                if(!hasNext()) throw new NoSuchElementException();
                curr=next;
                next=-1;
                c--;
                return idxs[curr]-1;
            }

            public boolean hasPrevious() {
                if(c==ArrayOpenHashIndexedInt2ObjMap.this.size) return false;
                if(last==-1) {
                    last=curr-1;
                    while(last>=0&&idxs[last]<=0) last--;
                }
                return last>=0;
            }

            public long previous() {
                next=curr;
                if(!hasPrevious()) throw new NoSuchElementException();
                curr=last;
                last=-1;
                c++;
                return idxs[curr]-1;
            }
        };
    }

    /**
     * Iterates over all indexes in the set.
     *
     * @return an iterable over all the indexes that designate elements in the set
     */
    public java.lang.Iterable<Long> iterateIndexes() {
        return new java.lang.Iterable<Long>() {
            public java.util.Iterator<Long> iterator() {
                return new java.util.Iterator<Long>() {
                    /** The position of the next entry to be returned. */
                    int pos=0;
                    /** The position of the last entry that has been returned. */
                    int last=-1;
                    /** A downward counter measuring how many entries have been returned. */
                    int c=size;

                    {
                        if(c!=0) while(pos<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[pos]<=0) pos++;
                    }

                    public boolean hasNext() {
                        return c!=0&&pos<ArrayOpenHashIndexedInt2ObjMap.this.capa;
                    }

                    public Long next() {
                        if(!hasNext()) throw new NoSuchElementException();
                        int idx=idxs[(last=pos)]-1;
                        if(--c!=0) do pos++; while(pos<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[pos]<=0);
                        return (long) idx;
                    }

                    public void remove() {
                        int idx;
                        if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                        idxs[last]=-idx;
                        vals[last]=null;
                        flgs[(last>>3)]&=~(0x1<<(last&7));
                        size--;
                    }
                };
            }
        };
    }

    public Iterator<IndexedInt2ObjMap.Entry<Value>> iterator() {
        return new Iterator<IndexedInt2ObjMap.Entry<Value>>() {
            /** The position of the next entry to be returned. */
            int pos=0;
            /** The position of the last entry that has been returned. */
            int last=-1;
            /** A downward counter measuring how many entries have been returned. */
            int c=size;

            {
                if(c!=0) while(pos<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[pos]<=0) pos++;
            }

            public boolean hasNext() {
                return c!=0&&pos<ArrayOpenHashIndexedInt2ObjMap.this.capa;
            }

            public IndexedInt2ObjMap.Entry<Value> next() {
                if(!hasNext()) throw new NoSuchElementException();
                final int idx=idxs[(last=pos)]-1;
                final int key=keys[idx];
                if(--c!=0) do pos++; while(pos<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[pos]<=0);
                return new Entry(idx, key);
            }

            public void remove() {
                int idx;
                if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                idxs[last]=-idx;
                vals[last]=null;
                flgs[(last>>3)]&=~(0x1<<(last&7));
                size--;
            }
        };
    }

    public <Par> long visit(Visitor<IndexedMap.Entry<Integer,Value>,Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            int idx=idxs[pos];
            if(idx<=0) {
                pos++;
                continue;
            }
            idx --;
            long r=vis.invoke(new Entry(idx, keys[idx]), par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public <Par> long visit(IndexedVisitor<IndexedMap.Entry<Integer,Value>,Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            int idx=idxs[pos];
            if(idx<=0) {
                pos++;
                continue;
            }
            idx --;
            long r=vis.invoke(idx, new Entry(idx, keys[idx]), par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public <Par> long visit(IndexedMapVisitor<Integer,Value,Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            int idx=idxs[pos];
            if(idx<=0) {
                pos++;
                continue;
            }
            idx --;
            @SuppressWarnings("unchecked")
            long r=vis.invoke(idx, keys[idx], (Value) vals[idx], par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public Iterable<IndexedInt2ObjMap.Entry<Value>> iterate(final long[] indexes) {
        return new Iterable<IndexedInt2ObjMap.Entry<Value>>() {
            public Iterator<IndexedInt2ObjMap.Entry<Value>> iterator() {
                return new Iterator<IndexedInt2ObjMap.Entry<Value>>() {
                    /** The position of the next index */
                    int pos=0;
                    /** The position of the last entry that has been returned. */
                    int last=-1;

                    public boolean hasNext() {
                        return pos<indexes.length;
                    }

                    public IndexedInt2ObjMap.Entry<Value> next() {
                        if(pos>=indexes.length) throw new NoSuchElementException();
                        int idx=idxs[(last=(int) indexes[pos++])]-1;
                        if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                        return new Entry(idx, keys[idx]);
                    }

                    public void remove() {
                        int idx;
                        if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                        idxs[last]=-idx;
                        vals[last]=null;
                        flgs[(last>>3)]&=~(0x1<<(last&7));
                        size--;
                    }
                };
            }
        };
    }


    public Iterable<IndexedInt2ObjMap.Entry<Value>> iterate(final java.lang.Iterable<Long> indexes) {
        return new Iterable<IndexedInt2ObjMap.Entry<Value>>() {
            public Iterator<IndexedInt2ObjMap.Entry<Value>> iterator() {
                return new Iterator<IndexedInt2ObjMap.Entry<Value>>() {
                    /** An iterator over indexes */
                    final java.util.Iterator<Long> iter=indexes.iterator();
                    /** The position of the last entry that has been returned. */
                    int last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    public IndexedInt2ObjMap.Entry<Value> next() {
                        int idx=idxs[(last=iter.next().intValue())]-1;
                        if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                        return new Entry(idx, keys[idx]);
                    }

                    public void remove() {
                        int idx;
                        if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                        idxs[last]=-idx;
                        vals[last]=null;
                        flgs[(last>>3)]&=~(0x1<<(last&7));
                        size--;
                    }
                };
            }
        };
    }


    public Iterable<IndexedInt2ObjMap.Entry<Value>> iterate(final Indexable indexes) {
        return new Iterable<IndexedInt2ObjMap.Entry<Value>>() {
            public Iterator<IndexedInt2ObjMap.Entry<Value>> iterator() {
                return new Iterator<IndexedInt2ObjMap.Entry<Value>>() {
                    /** An iterator over indexes */
                    final Index iter=indexes.indexes();
                    /** The position of the last entry that has been returned. */
                    int last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    public IndexedInt2ObjMap.Entry<Value> next() {
                        int idx=idxs[(last=(int) iter.next())]-1;
                        if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                        return new Entry(idx, keys[idx]);
                    }

                    public void remove() {
                        int idx;
                        if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                        idxs[last]=-idx;
                        vals[last]=null;
                        flgs[(last>>3)]&=~(0x1<<(last&7));
                        size--;
                    }
                };
            }
        };
    }

    /**
     * An indexed container of all the keys in this map
     *
     * @return a container, backed by the map, providing a view of the keys in the map
     */
    public IndexedIntContainer keys() {
        return new IndexedIntContainer() {
            public long size() { return ArrayOpenHashIndexedInt2ObjMap.this.size(); }
            public boolean isEmpty() { return ArrayOpenHashIndexedInt2ObjMap.this.isEmpty(); }
            public long head() { return ArrayOpenHashIndexedInt2ObjMap.this.head(); }
            public int getDefaultValue() {  return ArrayOpenHashIndexedInt2ObjMap.this.getDefaultKey(); }
            public boolean has(long index) { return ArrayOpenHashIndexedInt2ObjMap.this.has(index); }
            public Integer get(long index) { return ArrayOpenHashIndexedInt2ObjMap.this.getKey(index); }
            public int getInt(long index) { return ArrayOpenHashIndexedInt2ObjMap.this.getIntKey(index); }
            public Index indexes() { return ArrayOpenHashIndexedInt2ObjMap.this.indexes(); }
            public java.lang.Iterable<Long> iterateIndexes() { return ArrayOpenHashIndexedInt2ObjMap.this.iterateIndexes(); }
            public IntIterator iterator() {
                return new IntIterator() {
                    /** The position of the next entry to be returned. */
                    int pos=0;
                    /** The position of the last entry that has been returned. */
                    int last=-1;
                    /** A downward counter measuring how many entries have been returned. */
                    int c=size;

                    {
                        if(c!=0) while(pos<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[pos]<=0) pos++;
                    }

                    public boolean hasNext() {
                        return c!=0&&pos<ArrayOpenHashIndexedInt2ObjMap.this.capa;
                    }

                    public Integer next() { return nextValue(); }

                    public int nextValue() {
                        if(!hasNext()) throw new NoSuchElementException();
                        final int idx=idxs[(last=pos)]-1;
                        final int key=keys[idx];
                        if(--c!=0) do pos++; while(pos<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[pos]<=0);
                        return key;
                    }

                    public void remove() {
                        int idx;
                        if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                        idxs[last]=-idx;
//                        keys[last]=0;
                        vals[last]=null;
                        flgs[(last>>3)]&=~(0x1<<(last&7));
                        size--;
                    }
                };
            }

            public <Par> long visit(Visitor<Integer,Par> vis, Par par) {
                long c=0;
                int pos=0;
                while(pos<capa) {
                    int idx=idxs[pos];
                    if(idx<=0) {
                        pos++;
                        continue;
                    }
                    long r=vis.invoke(keys[idx-1], par);
                    if(r<0) return c;
                    c+=r;
                    pos++;
                }
                return c;
            }

            public <Par> long visit(IntVisitor<Par> vis, Par par) {
                long c=0;
                int pos=0;
                while(pos<capa) {
                    int idx=idxs[pos];
                    if(idx<=0) {
                        pos++;
                        continue;
                    }
                    long r=vis.invoke(keys[idx-1], par);
                    if(r<0) return c;
                    c+=r;
                    pos++;
                }
                return c;
            }

            public <Par> long visit(IndexedVisitor<Integer,Par> vis, Par par) {
                long c=0;
                int pos=0;
                while(pos<capa) {
                    int idx=idxs[pos];
                    if(idx<=0) {
                        pos++;
                        continue;
                    }
                    long r=vis.invoke(idx, keys[idx-1], par);
                    if(r<0) return c;
                    c+=r;
                    pos++;
                }
                return c;
            }

            public <Par> long visit(IndexedIntVisitor<Par> vis, Par par) {
                long c=0;
                int pos=0;
                while(pos<capa) {
                    int idx=idxs[pos];
                    if(idx<=0) {
                        pos++;
                        continue;
                    }
                    long r=vis.invoke(idx, keys[idx-1], par);
                    if(r<0) return c;
                    c+=r;
                    pos++;
                }
                return c;
            }

            public IntIterable iterate(final long[] indexes) {
                return new IntIterable() {
                    public IntIterator iterator() {
                        return new IntIterator() {
                            /** The position of the next index */
                            int pos=0;
                            /** The position of the last entry that has been returned. */
                            int last=-1;

                            public boolean hasNext() {
                                return pos<indexes.length;
                            }

                            public Integer next() { return nextValue(); }

                            public int nextValue() {
                                if(pos>=indexes.length) throw new NoSuchElementException();
                                int idx=idxs[(last=(int) indexes[pos++])]-1;
                                if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                                return keys[idx];
                            }

                            public void remove() {
                                int idx;
                                if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                                idxs[last]=-idx;
//                                keys[last]=0;
                                vals[last]=null;
                                flgs[(last>>3)]&=~(0x1<<(last&7));
                                size--;
                            }
                        };
                    }
                };
            }

            public IntIterable iterate(final java.lang.Iterable<Long> indexes) {
                return new IntIterable() {
                    public IntIterator iterator() {
                        return new IntIterator() {
                            /** An iterator over indexes */
                            final java.util.Iterator<Long> iter=indexes.iterator();
                            /** The position of the last entry that has been returned. */
                            int last=-1;

                            public boolean hasNext() { return iter.hasNext(); }

                            public Integer next() { return nextValue(); }

                            public int nextValue() {
                                int idx=idxs[(last=iter.next().intValue())]-1;
                                if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                                return keys[idx];
                            }

                            public void remove() {
                                int idx;
                                if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                                idxs[last]=-idx;
//                                keys[last]=0;
                                vals[last]=null;
                                flgs[(last>>3)]&=~(0x1<<(last&7));
                                size--;
                            }
                        };
                    }
                };
            }

            public IntIterable iterate(final Indexable indexes) {
                return new IntIterable() {
                    public IntIterator iterator() {
                        return new IntIterator() {
                            /** An iterator over indexes */
                            final Index iter=indexes.indexes();
                            /** The position of the last entry that has been returned. */
                            int last=-1;

                            public boolean hasNext() { return iter.hasNext(); }

                            public Integer next() { return nextValue(); }

                            public int nextValue() {
                                int idx=idxs[(last=(int) iter.next())]-1;
                                if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                                return keys[idx];
                            }

                            public void remove() {
                                int idx;
                                if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                                idxs[last]=-idx;
//                                keys[last]=0;
                                vals[last]=null;
                                flgs[(last>>3)]&=~(0x1<<(last&7));
                                size--;
                            }
                        };
                    }
                };
            }
        };
    }

    /**
     * An indexed container of all the values in this map
     *
     * @return a container, backed by the map, providing a view of the values in the map
     */
    public IndexedContainer<Value> values() {
        return new IndexedContainer<Value>() {
            public long size() { return ArrayOpenHashIndexedInt2ObjMap.this.size(); }
            public boolean isEmpty() { return ArrayOpenHashIndexedInt2ObjMap.this.isEmpty(); }
            public long head() { return ArrayOpenHashIndexedInt2ObjMap.this.head(); }
            public boolean has(long index) { return ArrayOpenHashIndexedInt2ObjMap.this.has(index); }
            public Value get(long index) { return ArrayOpenHashIndexedInt2ObjMap.this.getValue(index); }
            public Index indexes() { return ArrayOpenHashIndexedInt2ObjMap.this.indexes(); }
            public java.lang.Iterable<Long> iterateIndexes() { return ArrayOpenHashIndexedInt2ObjMap.this.iterateIndexes(); }
            public Iterator<Value> iterator() {
                return new Iterator<Value>() {
                    /** The position of the next entry to be returned. */
                    int pos=0;
                    /** The position of the last entry that has been returned. */
                    int last=-1;
                    /** A downward counter measuring how many entries have been returned. */
                    int c=size;

                    {
                        if(c!=0) while(pos<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[pos]<=0) pos++;
                    }

                    public boolean hasNext() {
                        return c!=0&&pos<ArrayOpenHashIndexedInt2ObjMap.this.capa;
                    }

                    @SuppressWarnings("unchecked")
                    public Value next() {
                        if(!hasNext()) throw new NoSuchElementException();
                        final int idx=idxs[(last=pos)]-1;
                        final Value val=(Value) vals[idx];
                        if(--c!=0) do pos++; while(pos<ArrayOpenHashIndexedInt2ObjMap.this.capa&&idxs[pos]<=0);
                        return val;
                    }

                    public void remove() {
                        int idx;
                        if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                        idxs[last]=-idx;
//                        keys[last]=0;
                        vals[last]=null;
                        flgs[(last>>3)]&=~(0x1<<(last&7));
                        size--;
                    }
                };
            }

            public <Par> long visit(Visitor<Value,Par> vis, Par par) {
                long c=0;
                int pos=0;
                while(pos<capa) {
                    int idx=idxs[pos];
                    if(idx<=0) {
                        pos++;
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    long r=vis.invoke((Value) vals[idx-1], par);
                    if(r<0) return c;
                    c+=r;
                    pos++;
                }
                return c;
            }

            public <Par> long visit(IndexedVisitor<Value,Par> vis, Par par) {
                long c=0;
                int pos=0;
                while(pos<capa) {
                    int idx=idxs[pos];
                    if(idx<=0) {
                        pos++;
                        continue;
                    }
                    @SuppressWarnings("unchecked")
                    long r=vis.invoke(idx, (Value) vals[idx-1], par);
                    if(r<0) return c;
                    c+=r;
                    pos++;
                }
                return c;
            }

            public Iterable<Value> iterate(final long[] indexes) {
                return new Iterable<Value>() {
                    public Iterator<Value> iterator() {
                        return new Iterator<Value>() {
                            /** The position of the next index */
                            int pos=0;
                            /** The position of the last entry that has been returned. */
                            int last=-1;

                            public boolean hasNext() {
                                return pos<indexes.length;
                            }

                            @SuppressWarnings("unchecked")
                            public Value next() {
                                if(pos>=indexes.length) throw new NoSuchElementException();
                                int idx=idxs[(last=(int) indexes[pos++])]-1;
                                if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                                return (Value) vals[idx];
                            }

                            public void remove() {
                                int idx;
                                if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                                idxs[last]=-idx;
//                                keys[last]=0;
                                vals[last]=null;
                                flgs[(last>>3)]&=~(0x1<<(last&7));
                                size--;
                            }
                        };
                    }
                };
            }

            public Iterable<Value> iterate(final java.lang.Iterable<Long> indexes) {
                return new Iterable<Value>() {
                    public Iterator<Value> iterator() {
                        return new Iterator<Value>() {
                            /** An iterator over indexes */
                            final java.util.Iterator<Long> iter=indexes.iterator();
                            /** The position of the last entry that has been returned. */
                            int last=-1;

                            public boolean hasNext() { return iter.hasNext(); }

                            @SuppressWarnings("unchecked")
                            public Value next() {
                                int idx=idxs[(last=iter.next().intValue())]-1;
                                if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                                return (Value) vals[idx];
                            }

                            public void remove() {
                                int idx;
                                if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                                idxs[last]=-idx;
//                                keys[last]=0;
                                vals[last]=null;
                                flgs[(last>>3)]&=~(0x1<<(last&7));
                                size--;
                            }
                        };
                    }
                };
            }

            public Iterable<Value> iterate(final Indexable indexes) {
                return new Iterable<Value>() {
                    public Iterator<Value> iterator() {
                        return new Iterator<Value>() {
                            /** An iterator over indexes */
                            final Index iter=indexes.indexes();
                            /** The position of the last entry that has been returned. */
                            int last=-1;

                            public boolean hasNext() { return iter.hasNext(); }

                            @SuppressWarnings("unchecked")
                            public Value next() {
                                int idx=idxs[(last=(int) iter.next())]-1;
                                if(!((flgs[(idx>>3)]&(1<<(idx&7)))!=0)) throw new NoSuchElementException();
                                return (Value) vals[idx];
                            }

                            public void remove() {
                                int idx;
                                if(last==-1||(idx=idxs[last])<=0) throw new IllegalStateException();
                                idxs[last]=-idx;
//                                keys[last]=0;
                                vals[last]=null;
                                flgs[(last>>3)]&=~(0x1<<(last&7));
                                size--;
                            }
                        };
                    }
                };
            }
        };
    }


    /**********************************************************************************
     **  Override methods
     **/

    /**
     * Returns a clone of this set.
     *
     * @return an identical, yet independent copy of this set
     */
    @SuppressWarnings("unchecked")
    public ArrayOpenHashIndexedInt2ObjMap<Value> clone() {
        ArrayOpenHashIndexedInt2ObjMap<Value> c;
        try {
            c=(ArrayOpenHashIndexedInt2ObjMap<Value>) super.clone();
        }
        catch(CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys=keys.clone();
        c.vals=vals.clone();
        c.idxs=idxs.clone();
        c.strategy=strategy;
        return c;
    }

    /**
     * Returns a hash code for this set.
     *
     * @return a hash code for this set
     */
    public int hashCode() {
        long h=0;
        int i=0, j=size;
        while(j--!=0) {
            int idx;
            while((idx=idxs[i])<=0) i++;
            int k=keys[(idx-1)];
            h+=strategy.hash(k);
            i++;
        }
        return (int) h;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if(o instanceof IndexedInt2ObjMap) {
            IndexedInt2ObjMap that=(IndexedInt2ObjMap) o;
            if(this.size!=that.size()) return false;
            int pos=0;
            while(pos<capa) {
                int idx=idxs[pos];
                if(idx<=0) {
                    pos++;
                    continue;
                }
                idx --;
                if(!that.has(idx)) return false;
                int k=keys[idx];
                Object v=vals[idx];
                int l = that.getIntKey(idx);
                Object w = that.getValue(idx);
                if(k!=l) return false;
                if(v!=w&&(v==null||!v.equals(w))) return false;
                pos++;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('{').append(' ');
        boolean first = true;
        int pos=0;
        while(pos<capa) {
            int idx=idxs[pos];
            if(idx<=0) {
                pos++;
                continue;
            }
            idx --;
            if(first) first=false;
            else buf.append(',');
            buf.append(idx).append('@').append(keys[idx]).append(':').append(vals[idx]).append(' ');
            pos++;
        }
        buf.append('}');
        return buf.toString();
    }

//    public String toDebugString() {
//        StringBuilder buf = new StringBuilder();
//        buf.append(this.getClass().getSimpleName()).append(" [").append(size).append('/').append(free).append('/').append(capa).append("] {\n");
//        for(int i=0; i<capa;i++) {
//            int idx = getIndex(i);
//            buf.append('\t');
//            if(idx<0) {
//                idx = -idx-1;
//                buf.append('X').append(' ').append(idx).append('\t').append(getKey(idx));
//            }
//            else if(idx>0) {
//                idx = idx -1;
//                buf.append('=').append(' ').append(idx).append('\t').append(getKey(idx));
//            }
//            else {
//                buf.append('-');
//            }
//            buf.append('\n');
//        }
//        return buf.toString();
//    }


}
