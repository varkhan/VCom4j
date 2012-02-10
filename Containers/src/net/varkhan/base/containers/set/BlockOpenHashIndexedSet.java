/**
 *
 */
package net.varkhan.base.containers.set;

import net.varkhan.base.containers.*;
import net.varkhan.base.containers.Iterable;

import java.io.Serializable;
import java.util.NoSuchElementException;


/**
 * @author varkhan
 * @date May 28, 2009
 * @time 9:43:13 PM
 */
public class BlockOpenHashIndexedSet<Key> implements IndexedSet<Key>, Serializable {

    public static final long serialVersionUID=1L;

    /**
     * The list block size 2-logarithm
     */
    protected final int blockshift;

    /**
     * The list block size ({@code 1 << blockshift})
     */
    protected final int blocksize;

    /**
     * The list block mask ({@code blocksize - 1})
     */
    protected final int blockmask;

    /**
     * The array of keys
     */
    protected Object[][] keys;

    /**
     * The array of indexes
     */
    protected long[][] idxs;

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
    protected long capa;

    /**
     * The mixing (secondary hashing) factor
     */
    protected long mixr;

    /**
     * Number of entries in the set
     */
    protected long size;

    /**
     * The maximum used index in the set, plus 1
     */
    protected long head;

    /**
     * Number of free entries in the table (may be less than the {@link #capa} - {@link #size} because of deleted entries)
     */
    protected long free;

    /**
     * Threshold after which we rehash. It must be the table size times {@link #lfact}
     */
    protected long fill;

    /**
     * The hash strategy of this set
     */
    protected HashingStrategy<Key> strategy;

    /**
     * Creates a new hash set.
     *
     * @param size       the expected number of elements in the set
     * @param blockshift the base-2 logarithm of the block size (such that the block size is {@code 1<<blockshift})
     * @param loadfact   the load factor (between 0 exclusive and 1 inclusive)
     * @param growfact   the growth factor (strictly greater than 1)
     * @param strategy   the hashing strategy
     */
    public BlockOpenHashIndexedSet(long size, int blockshift, float loadfact, float growfact, final HashingStrategy<Key> strategy) {
//        if(loadfact<=0 || loadfact>1) throw new IllegalArgumentException("Load factor must be between 0 exclusive and 1 inclusive");
//        if(growfact<=1) throw new IllegalArgumentException("Growth factor must be strictly greater than 1");
//        if(size<0) throw new IllegalArgumentException("Hash table size must be nonnegative");
        if(blockshift<=3) blockshift=3;
        if(loadfact>=1) loadfact=.75f;
        if(growfact<=1) growfact=1.5f;
        if(size<0) size=0;
        this.blockshift=blockshift;
        this.blocksize=(1<<blockshift);
        this.blockmask=(1<<blockshift)-1;
        this.strategy=strategy;
        this.lfact=loadfact;
        this.gfact=growfact;
        this.capa=getCapa(size);
        this.mixr=getMixr(this.capa, size);
        this.fill=(long) (capa*loadfact);
        this.free=this.capa;
        this.size=0;
        this.head=0;
        int bnum=(int) (this.capa>>>this.blockshift)+1;
        this.idxs=new long[bnum][];
        this.keys=new Object[bnum][];
    }

    /**
     * Creates a new hash set.
     *
     * @param size the expected number of elements in the set
     */
    @SuppressWarnings("unchecked")
    public BlockOpenHashIndexedSet(long size) {
        this(size, 10, .75f, 1.5f, (HashingStrategy<Key>) HashingStrategy.DefaultHashingStrategy);
    }

    /**
     * Creates a new hash set.
     */
    @SuppressWarnings("unchecked")
    public BlockOpenHashIndexedSet() {
        this(11, 10, .75f, 1.5f, (HashingStrategy<Key>) HashingStrategy.DefaultHashingStrategy);
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
        for(int i=0;i<idxs.length;i++) idxs[i]=null;
        for(int i=0;i<keys.length;i++) keys[i]=null;
    }


    /**********************************************************************************
     **  Set elements accessors
     **/

    /**
     * This reference is used to fill the keys of removed entries, and allows the
     * search algorithm to distinguish removed keys from actual {@literal null}
     * keys.
     */
    protected static final Object NULL=new Serializable() {
        private static final long serialVersionUID=1L;

        public String toString() { return "@NULL"; }
    };

    private long _getIndex(long pos) {
        int iblockpos=(int) (pos>>>blockshift);
        int iblockoff=(int) (pos&blockmask);
        long[] iblock=idxs[iblockpos];
        // No block == EMPTY slots
        return (iblock==null) ? 0 : iblock[iblockoff];
    }

    private void _setIndex(long pos, long index) {
        int iblockpos=(int) (pos>>>blockshift);
        int iblockoff=(int) (pos&blockmask);
        long[] iblock=idxs[iblockpos];
        if(iblock==null) idxs[iblockpos]=iblock=new long[blocksize];
        iblock[iblockoff]=index;
    }

    private Object _getKey(long index) {
        int kblockpos=(int) (index>>>blockshift);
        int kblockoff=(int) (index&blockmask);
        Object[] kblock=keys[kblockpos];
        // No block == NULL key
        return (kblock==null) ? NULL : kblock[kblockoff];
    }

    private void _setKey(long index, Object k) {
        int kblockpos=(int) (index>>>blockshift);
        int kblockoff=(int) (index&blockmask);
        Object[] kblock=keys[kblockpos];
        if(kblock==null) {
            keys[kblockpos]=kblock=new Object[blocksize];
            for(int j=0;j<blocksize;j++) kblock[j]=NULL;
        }
        kblock[kblockoff]=k;
    }

    public boolean has(long index) {
        if(index<0||index>=head) return false;
        return _getKey(index)!=NULL;
    }

    @SuppressWarnings("unchecked")
    public long index(final Key key) {
        final long capa=this.capa;
        final long mixr=this.mixr;
        final long hash=this.strategy.hash(key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        long slot=ph%capa;
        // The signed lookup index in the key table
        long sidx=_getIndex(slot);
        // The key table value
        Object kval;
        if(sidx<0||(sidx>0&&!(this.strategy.hash((Key) (kval=_getKey(sidx-1)))==hash&&this.strategy.equal(key, (Key) kval)))) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=_getIndex(slot);
//                kidx = (sidx>0)?sidx-1:(sidx<0)?-sidx-1:-1;
                // There's always an EMPTY slot
            }
            while(sidx<0||(sidx>0&&!(this.strategy.hash((Key) (kval=_getKey(sidx-1)))==hash&&this.strategy.equal(key, (Key) kval))));
        }
        return sidx>0 ? sidx-1 : -1; // If OCCUPIED, necessarily, key_match(key, hash, kval).
    }

    @SuppressWarnings("unchecked")
    public Key get(long index) {
        if(index<0||index>=this.head) return null;
        Object key=_getKey(index);
        if(key==NULL) return null;
        return (Key) key;
    }

    @SuppressWarnings("unchecked")
    public long add(final Key key) {
        final long capa=this.capa;
        final long mixr=this.mixr;
        final long hash=this.strategy.hash(key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        long slot=ph%capa;
        // The signed lookup index in the key table
        long sidx=_getIndex(slot);
        // The key table value
        Object kval;
        if(sidx>0&&!(hash==this.strategy.hash((Key) (kval=_getKey(sidx-1)))&&this.strategy.equal(key, (Key) kval))) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=_getIndex(slot);
                // There's always an EMPTY slot
            }
            while(sidx>0&&!(hash==this.strategy.hash((Key) (kval=_getKey(sidx-1)))&&this.strategy.equal(key, (Key) kval)));
        }
        final long pos=slot; // Remember first available slot for later.
        // Key found
        if(sidx>0) return sidx-1; // If OCCUPIED, necessarily, key_match(key, hash, kval).
            // If this slot is REMOVED, keep looking for the key until we find it or get to an empty slot
        else if(sidx<0) {
            long pidx=sidx;
            // We are on a REMOVED spot, so we have to continue scanning
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                pidx=_getIndex(slot);
                // There's always an EMPTY slot
            }
            while(pidx<0||(pidx>0&&!(hash==this.strategy.hash((Key) (kval=_getKey(pidx-1)))&&this.strategy.equal(key, (Key) kval))));
            if(pidx>0) return pidx-1; // If OCCUPIED, necessarily, key_match(key, hash, kval).
            // Original slot was REMOVED, set new signed lookup index to OCCUPIED value
            sidx=-sidx;
        }
        // If this slot is EMPTY, allocate an index and reserve a slot
        else {
            sidx=++this.head;
            this.free--;
        }
        // Mark slot as OCCUPIED
        _setIndex(pos, sidx);
        _setKey(sidx-1, key);
        if(++this.size>=this.fill)
            rehash(getCapa((long) (this.size*this.gfact)));    // Too many elements for the capacity, rehash at an increased capa
        if(this.free==0)
            rehash(capa);                                    // Too many deletions, rehash at the same capa for cleanup
        return sidx-1;
    }

    @SuppressWarnings("unchecked")
    public void del(long index) {
        if(index<0||index>=this.head) return;
        // We need to look up the key, to provide a starting point for the slot list
        Object key=_getKey(index);
        if(key==NULL) return;
        final long capa=this.capa;
        final long mixr=this.mixr;
        final long hash=this.strategy.hash((Key) key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        long slot=ph%capa;
        // The signed lookup index in the key table
        long sidx=_getIndex(slot);
        // The actual lookup index in the key table
        long pidx=+index+1;
        long nidx=-index-1;
        // The key table value
//        Object kval;
        if(sidx!=0&&sidx!=pidx&&sidx!=nidx) {
//        if ( sidx<0 || (sidx>0 && ! (hash == strategy.hash((Key)kval) && strategy.equal((Key)key, (Key)kval))) ) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=_getIndex(slot);
            } while(sidx!=0&&sidx!=pidx&&sidx!=nidx);
//            } while( sidx<0 || (sidx>0 && ! (hash == strategy.hash((Key)kval) && strategy.equal((Key)key, (Key)kval))) );
            // At this point, we have either an EMPTY slot, or an OCCUPIED or DELETED slot with the right index
        }
        // If EMPTY or DELETED, nothing to be done
        if(sidx<=0) return;
        // At this point we necessarily have an OCCUPIED slot, and sidx == index+1
        _setIndex(slot, -sidx);
        _setKey(index, NULL);
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
        try { rehash(getCapa(size)); }
        catch(OutOfMemoryError cantDoIt) { return false; }
        return true;
    }

    /**
     * Rehashes the set.
     *
     * @param capa the new table capacity
     */
    @SuppressWarnings("unchecked")
    protected void rehash(final long capa) {
        long mixr=getMixr(capa, this.size);
        long fill=(long) (capa*this.lfact);
        long pos=0, s=this.size;
        final long[][] idxs=new long[(int) (capa>>>this.blockshift)+1][];
        long head=0;
        while(s-->0) {
            long idx;
            while((idx=_getIndex(pos))<=0) pos++;
            Object key=_getKey(idx-1);
            final long ph=this.strategy.hash((Key) key)&0x7FFFFFFFFFFFFFFFL;
            // The slot position, starting at the primary hash
            long slot=ph%capa;
            int iblockpos=(int) (slot>>>this.blockshift);
            int iblockoff=(int) (slot&this.blockmask);
            long[] iblock=idxs[iblockpos];
            if(iblock!=null&&iblock[iblockoff]!=0) {
                long scan=(ph%mixr)+1;
                do {
                    slot+=scan;
                    if(slot>=capa||slot<0) slot-=capa;
                    iblockpos=(int) (slot>>>this.blockshift);
                    iblockoff=(int) (slot&this.blockmask);
                    iblock=idxs[iblockpos];
                } while(iblock!=null&&iblock[iblockoff]!=0);
            }
            // idx is actually the index + 1
            if(iblock==null) {
                idxs[iblockpos]=iblock=new long[this.blocksize];
            }
            iblock[iblockoff]=idx;
            if(head<idx) head=idx;
            pos++;
        }
        // Increase the size of the keys if needed
        if(head<capa&&this.capa<capa) {
            int bnum=(int) (capa>>>this.blockshift)+1;
            if(bnum>this.keys.length) {
                final Object[][] newKeys=new Object[bnum][];
                System.arraycopy(this.keys, 0, newKeys, 0, this.keys.length);
                this.keys=newKeys;
            }
        }
        // Decrease the size of the keys if possible
        else if(head<=capa&&capa<this.capa) {
            int bnum=(int) (((head<capa) ? capa : head)>>>this.blockshift)+1;
            if(bnum<this.keys.length) {
                final Object[][] keys=new Object[bnum][];
                System.arraycopy(this.keys, 0, keys, 0, bnum);
                this.keys=keys;
            }
        }
        this.capa=capa;
        this.mixr=mixr;
        this.free=this.capa-this.size;
        this.fill=fill;
        this.head=head;
        this.idxs=idxs;
    }

    protected long getCapa(long size) {
        if(size<this.size) size=this.size;
        long capa=(long) (size/this.lfact)+1;
        int min=0;
        int max=PRIMES.length-1;
        while(min<=max) {
            int mid=(min+max)>>1;
            long val=PRIMES[mid];
            if(val<capa) min=mid+1;
            else if(val>capa) max=mid-1;
            else return val;
        }
        if(min>=PRIMES.length) return PRIMES[PRIMES.length-1];
        return PRIMES[min];
    }

    protected long getMixr(long capa, long size) {
        return capa-2;
    }

    protected static final long PRIMES[]={ 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 5, 5, 5, 5, 5, 5, 7, 7, 7,
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
     * Returns an {@link Index} over the indexes in the set.
     *
     * @return an {@code Index} enumerating all indexes in the set
     */
    public Index indexes() {
        return new Index() {
            long curr=-1;
            /** The position of the next entry to be returned. */
            long next=-1;
            /** The position of the last entry that has been returned. */
            long last=-1;
            /** A downward counter measuring how many entries have been returned. */
            long c=BlockOpenHashIndexedSet.this.size;

            public long current() {
                long idx;
                if(curr==-1||(idx=_getIndex(curr))<=0) throw new IllegalStateException();
                return idx-1;
            }

            public boolean hasNext() {
                if(c==0) return false;
                if(next==-1) {
                    next=curr+1;
                    while(next<BlockOpenHashIndexedSet.this.capa&&_getIndex(next)<=0) next++;
                }
                return next<BlockOpenHashIndexedSet.this.capa;
            }

            public long next() {
                last=curr;
                if(!hasNext()) throw new NoSuchElementException();
                curr=next;
                next=-1;
                c--;
                return _getIndex(curr)-1;
            }

            public boolean hasPrevious() {
                if(c==BlockOpenHashIndexedSet.this.size) return false;
                if(last==-1) {
                    last=curr-1;
                    while(last>=0&&_getIndex(last)<=0) last--;
                }
                return last>=0;
            }

            public long previous() {
                next=curr;
                if(!hasPrevious()) throw new NoSuchElementException();
                curr=last;
                last=-1;
                c++;
                return _getIndex(curr)-1;
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
                    long pos=0;
                    /** The position of the last entry that has been returned. */
                    long last=-1;
                    /** A downward counter measuring how many entries have been returned. */
                    long c=size;

                    {
                        if(c!=0) while(pos<BlockOpenHashIndexedSet.this.capa&&_getIndex(pos)<=0) pos++;
                    }

                    public boolean hasNext() {
                        return c!=0&&pos<BlockOpenHashIndexedSet.this.capa;
                    }

                    public Long next() {
                        if(!hasNext()) throw new NoSuchElementException();
                        long idx=_getIndex(last=pos)-1;
                        if(--c!=0) do pos++; while(pos<BlockOpenHashIndexedSet.this.capa&&_getIndex(pos)<=0);
                        return idx;
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _setKey(last, NULL);
                        size--;
                    }
                };
            }
        };
    }


    public Iterator<Key> iterator() {
        return new Iterator<Key>() {
            /** The position of the next entry to be returned. */
            long pos=0;
            /** The position of the last entry that has been returned. */
            long last=-1;
            /** A downward counter measuring how many entries have been returned. */
            long c=size;

            {
                if(c!=0) while(pos<BlockOpenHashIndexedSet.this.capa&&_getIndex(pos)<=0) pos++;
            }

            public boolean hasNext() {
                return c!=0&&pos<BlockOpenHashIndexedSet.this.capa;
            }

            @SuppressWarnings("unchecked")
            public Key next() {
                if(!hasNext()) throw new NoSuchElementException();
                Key key=(Key) _getKey(_getIndex(last=pos)-1);
                if(--c!=0) do pos++; while(pos<BlockOpenHashIndexedSet.this.capa&&_getIndex(pos)<=0);
                return key;
            }

            public void remove() {
                long idx;
                if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                _setIndex(last, -idx);
                _setKey(last, NULL);
                size--;
            }
        };
    }

    public <Par> long visit(Visitor<Key,Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            long idx=_getIndex(pos);
            if(idx<=0) {
                pos++;
                continue;
            }
            @SuppressWarnings("unchecked")
            long r=vis.invoke((Key) _getKey(idx-1), par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public <Par> long visit(IndexedVisitor<Key,Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            long idx=_getIndex(pos);
            if(idx<=0) {
                pos++;
                continue;
            }
            @SuppressWarnings("unchecked")
            long r=vis.invoke(idx-1, (Key) _getKey(idx-1), par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public Iterable<Key> iterate(final long[] indexes) {
        return new Iterable<Key>() {
            public Iterator<Key> iterator() {
                return new Iterator<Key>() {
                    /** The position of the next index */
                    int pos=0;
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() {
                        return pos<indexes.length;
                    }

                    @SuppressWarnings("unchecked")
                    public Key next() {
                        if(pos>=indexes.length) throw new NoSuchElementException();
                        Object key=_getKey(_getIndex(last=indexes[pos++])-1);
                        if(key==NULL) throw new NoSuchElementException();
                        return (Key) key;
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _setKey(last, NULL);
                        size--;
                    }
                };
            }
        };
    }


    public Iterable<Key> iterate(final java.lang.Iterable<Long> indexes) {
        return new Iterable<Key>() {
            public Iterator<Key> iterator() {
                return new Iterator<Key>() {
                    /** An iterator over indexes */
                    final java.util.Iterator<Long> iter=indexes.iterator();
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    @SuppressWarnings("unchecked")
                    public Key next() {
                        Object key=_getKey(_getIndex(last=iter.next())-1);
                        if(key==NULL) throw new NoSuchElementException();
                        return (Key) key;
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _setKey(last, NULL);
                        size--;
                    }
                };
            }
        };
    }


    public Iterable<Key> iterate(final Indexable indexes) {
        return new Iterable<Key>() {
            public Iterator<Key> iterator() {
                return new Iterator<Key>() {
                    /** An iterator over indexes */
                    final Index iter=indexes.indexes();
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    @SuppressWarnings("unchecked")
                    public Key next() {
                        Object key=_getKey(_getIndex(last=iter.next())-1);
                        if(key==NULL) throw new NoSuchElementException();
                        return (Key) key;
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _setKey(last, NULL);
                        size--;
                    }
                };
            }
        };
    }


    /**********************************************************************************
     **  Set entries iterators
     **/

    /**
     * The base Entry object for this set
     */
    private static class Entry<Key> implements IndexedSet.Entry<Key> {
        private final long idx;
        private final Key  key;

        public Entry(long idx, Key key) {
            this.idx=idx;
            this.key=key;
        }

        public long index() { return idx; }

        public Key getKey() { return key; }

    }

    public Iterable<IndexedSet.Entry<Key>> entries() {
        return new Iterable<IndexedSet.Entry<Key>>() {
            public Iterator<IndexedSet.Entry<Key>> iterator() {
                return new Iterator<IndexedSet.Entry<Key>>() {
                    /** The position of the next entry to be returned. */
                    long pos=0;
                    /** The position of the last entry that has been returned. */
                    long last=-1;
                    /** A downward counter measuring how many entries have been returned. */
                    long c=size;

                    {
                        if(c!=0) while(pos<BlockOpenHashIndexedSet.this.capa&&_getIndex(pos)<=0) pos++;
                    }

                    public boolean hasNext() {
                        return c!=0&&pos<BlockOpenHashIndexedSet.this.capa;
                    }

                    @SuppressWarnings("unchecked")
                    public IndexedSet.Entry<Key> next() {
                        if(!hasNext()) throw new NoSuchElementException();
                        long idx=_getIndex(last=pos)-1;
                        if(--c!=0) do pos++; while(pos<BlockOpenHashIndexedSet.this.capa&&_getIndex(pos)<=0);
                        return new Entry<Key>(idx, (Key) _getKey(idx));
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _setKey(last, NULL);
                        size--;
                    }
                };
            }
        };
    }

    public Iterable<IndexedSet.Entry<Key>> entries(final long[] indexes) {
        return new Iterable<IndexedSet.Entry<Key>>() {
            public Iterator<IndexedSet.Entry<Key>> iterator() {
                return new Iterator<IndexedSet.Entry<Key>>() {
                    /** The position of the next index */
                    int pos=0;
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() {
                        return pos<indexes.length;
                    }

                    @SuppressWarnings("unchecked")
                    public IndexedSet.Entry<Key> next() {
                        if(pos>=indexes.length) throw new NoSuchElementException();
                        long idx=_getIndex(last=indexes[pos++])-1;
                        Object key=_getKey(idx);
                        if(key==NULL) throw new NoSuchElementException();
                        return new Entry<Key>(idx, (Key) key);
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _setKey(last, NULL);
                        size--;
                    }
                };
            }
        };
    }

    public Iterable<IndexedSet.Entry<Key>> entries(final Iterable<Long> indexes) {
        return new Iterable<IndexedSet.Entry<Key>>() {
            public Iterator<IndexedSet.Entry<Key>> iterator() {
                return new Iterator<IndexedSet.Entry<Key>>() {
                    /** An iterator over indexes */
                    final Iterator<? extends Long> iter=indexes.iterator();
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    @SuppressWarnings("unchecked")
                    public IndexedSet.Entry<Key> next() {
                        long idx=_getIndex(last=iter.next())-1;
                        Object key=_getKey(idx);
                        if(key==NULL) throw new NoSuchElementException();
                        return new Entry<Key>(idx, (Key) key);
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _setKey(last, NULL);
                        size--;
                    }
                };
            }
        };
    }

    public Iterable<IndexedSet.Entry<Key>> entries(final Indexable indexes) {
        return new Iterable<IndexedSet.Entry<Key>>() {
            public Iterator<IndexedSet.Entry<Key>> iterator() {
                return new Iterator<IndexedSet.Entry<Key>>() {
                    /** An iterator over indexes */
                    final Index iter=indexes.indexes();
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    @SuppressWarnings("unchecked")
                    public IndexedSet.Entry<Key> next() {
                        long idx=_getIndex(last=iter.next())-1;
                        Object key=_getKey(idx);
                        if(key==NULL) throw new NoSuchElementException();
                        return new Entry<Key>(idx, (Key) key);
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _setKey(last, NULL);
                        size--;
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
    public Object clone() {
        BlockOpenHashIndexedSet<Key> c;
        try {
            c=(BlockOpenHashIndexedSet<Key>) super.clone();
        }
        catch(CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys=keys.clone();
        c.idxs=idxs.clone();
        c.strategy=strategy;
        return c;
    }

    /**
     * Returns a hash code for this set.
     *
     * @return a hash code for this set
     */
    @SuppressWarnings("unchecked")
    public int hashCode() {
        long h=0;
        long i=0, j=size;
        while(j--!=0) {
            long idx;
            ;
            while((idx=_getIndex(i))<=0) i++;
            Object k=_getKey(idx-1);
            if(this!=k) h+=strategy.hash((Key) k);
            i++;
        }
        return (int) h;
    }


//    public String toString() {
//        StringBuilder buf = new StringBuilder();
//        buf.append(this.getClass().getSimpleName()).append(" [").append(size).append('/').append(free).append('/').append(capa).append("] {\n");
//        for(int i=0; i<capa;i++) {
//            long idx = getIndex(i);
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
