/**
 *
 */
package net.varkhan.base.containers.set;

import net.varkhan.base.containers.Index;
import net.varkhan.base.containers.Indexable;
import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.type.DoubleHashingStrategy;
import net.varkhan.base.containers.type.DoubleIterable;

import java.io.Serializable;
import java.util.NoSuchElementException;


/**
 * @author varkhan
 * @date May 28, 2009
 * @time 9:43:13 PM
 */
public class BlockOpenHashIndexedDoubleSet implements IndexedDoubleSet, Serializable, Cloneable {

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
    protected double[][] keys;

    /**
     * The array of occupancy flags
     */
    protected byte[][] flgs;

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
     * The default value to return on absent keys
     */
    protected double defKey=0;

    /**
     * The hash strategy of this set
     */
    protected DoubleHashingStrategy strategy;

    /**
     * Creates a new hash set.
     *
     * @param size       the expected number of elements in the set
     * @param blockshift the base-2 logarithm of the block size (such that the block size is {@code 1&lt;&lt;blockshift})
     * @param loadfact   the load factor (between 0 exclusive and 1 inclusive)
     * @param growfact   the growth factor (strictly greater than 1)
     * @param strategy   the hashing strategy
     */
    public BlockOpenHashIndexedDoubleSet(long size, int blockshift, float loadfact, float growfact, final DoubleHashingStrategy strategy) {
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
        this.flgs=new byte[bnum][];
        this.keys=new double[bnum][];
    }

    /**
     * Creates a new hash set.
     *
     * @param size the expected number of elements in the set
     */
    public BlockOpenHashIndexedDoubleSet(long size) {
        this(size, 10, .75f, 1.5f, DoubleHashingStrategy.DefaultHashingStrategy);
    }

    /**
     * Creates a new hash set.
     */
    public BlockOpenHashIndexedDoubleSet() {
        this(11, 10, .75f, 1.5f, DoubleHashingStrategy.DefaultHashingStrategy);
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
        for(int i=0;i<flgs.length;i++) flgs[i]=null;
        for(int i=0;i<keys.length;i++) keys[i]=null;
    }

    public double getDefaultValue() { return defKey; }

    public void setDefaultValue(double def) { defKey=def; }


    /**********************************************************************************
     **  Set elements accessors
     **/


    private long _getIndex(long pos) {
        int iblockpos=(int) (pos>>>this.blockshift);
        int iblockoff=(int) (pos&this.blockmask);
        long[] iblock=this.idxs[iblockpos];
        // No block == EMPTY slots
        return (iblock==null) ? 0 : iblock[iblockoff];
    }

    private void _setIndex(long pos, long index) {
        int iblockpos=(int) (pos>>>this.blockshift);
        int iblockoff=(int) (pos&this.blockmask);
        long[] iblock=this.idxs[iblockpos];
        if(iblock==null) this.idxs[iblockpos]=iblock=new long[this.blocksize];
        iblock[iblockoff]=index;
    }

    private boolean _hasKey(long index) {
        int fblockpos=(int) (index>>>this.blockshift);
        int fblockoff=(int) (index&this.blockmask);
        byte[] fblock=this.flgs[fblockpos];
        return (fblock[fblockoff>>3]&(1<<(fblockoff&7)))!=0;
    }

    private double _getKey(long index) {
        int kblockpos=(int) (index>>>this.blockshift);
        int kblockoff=(int) (index&this.blockmask);
        double[] kblock=this.keys[kblockpos];
        // No block ??
        return (kblock==null) ? defKey : kblock[kblockoff];
    }

    private void _setKey(long index, double k) {
        int kblockpos=(int) (index>>>this.blockshift);
        int kblockoff=(int) (index&this.blockmask);
        double[] kblock=this.keys[kblockpos];
        byte[] fblock=this.flgs[kblockpos];
        if(kblock==null) this.keys[kblockpos]=kblock=new double[this.blocksize];
        if(fblock==null) this.flgs[kblockpos]=fblock=new byte[this.blocksize>>3];
        kblock[kblockoff]=k;
        fblock[kblockoff>>3]|=0x1<<(kblockoff&7);
    }

    private void _delKey(long index) {
        int kblockpos=(int) (index>>>this.blockshift);
        int kblockoff=(int) (index&this.blockmask);
        double[] kblock=this.keys[kblockpos];
        byte[] fblock=this.flgs[kblockpos];
        if(kblock==null||fblock==null) return;
        fblock[kblockoff>>3]&=~(0x1<<(kblockoff&7));
    }

    public boolean has(long index) {
        if(index<0||index>=this.head) return false;
        return _hasKey(index);
    }

    public long index(final Double key) {
        if(key==null) return -1;
        return index(key.doubleValue());
    }

    public long index(final double key) {
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
        double kval;
        if(sidx<0||(sidx>0&&!(this.strategy.hash((kval=_getKey(sidx-1)))==hash&&this.strategy.equal(key, kval)))) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=_getIndex(slot);
                // There's always an EMPTY slot
            }
            while(sidx<0||(sidx>0&&!(this.strategy.hash((kval=_getKey(sidx-1)))==hash&&this.strategy.equal(key, kval))));
        }
        return sidx>0 ? sidx-1 : -1; // If OCCUPIED, necessarily, key_match(key, hash, kval).
    }

    public Double get(long index) {
        if(index<0||index>=this.head) return defKey;
        if(!_hasKey(index)) return null;
        return _getKey(index);
    }

    public double getDouble(long index) {
        if(index<0||index>=this.head) return defKey;
        if(!_hasKey(index)) return defKey;
        return _getKey(index);
    }

    public long add(final Double key) {
        if(key==null) return -1;
        return add(key.doubleValue());
    }

    public long add(final double key) {
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
        double kval;
        if(sidx>0&&!(hash==this.strategy.hash((kval=_getKey(sidx-1)))&&this.strategy.equal(key, kval))) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=_getIndex(slot);
                // There's always an EMPTY slot
            } while(sidx>0&&!(hash==this.strategy.hash((kval=_getKey(sidx-1)))&&this.strategy.equal(key, kval)));
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
            while(pidx<0||(pidx>0&&!(hash==this.strategy.hash((kval=_getKey(pidx-1)))&&this.strategy.equal(key, kval))));
            if(pidx>0) return pidx-1; // If OCCUPIED, necessarily, key_match(key, hash, kval).
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
            rehash(capa);                                                    // Too many deletions, rehash at the same capa for cleanup
        return sidx-1;
    }

    public void del(long index) {
        if(index<0||index>=this.head) return;
        if(!_hasKey(index)) return;
        // We need to look up the key, to provide a starting point for the slot list
        double key=_getKey(index);
        final long capa=this.capa;
        final long mixr=this.mixr;
        final long hash=this.strategy.hash(key);
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
//        double kval;
        if(sidx!=0&&sidx!=pidx&&sidx!=nidx) {
//        if ( sidx<0 || (sidx>0 && ! (hash == this.strategy.hash((Key)(kval=getKey(sidx-1))) && this.strategy.equal(key, (Key)kval))) ) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                sidx=_getIndex(slot);
            } while(sidx!=0&&sidx!=pidx&&sidx!=nidx);
//            } while( sidx<0 || (sidx>0 && ! (hash == this.strategy.hash((Key)(kval=getKey(sidx-1))) && this.strategy.equal(key, (Key)kval))) );
            // At this point, we have either an EMPTY slot, or an OCCUPIED or DELETED slot with the right index
        }
        // If EMPTY or DELETED, nothing to be done
        if(sidx<=0) return;
        // At this point we necessarily have an OCCUPIED slot, and sidx == index+1
        _setIndex(slot, -sidx);
        _delKey(sidx-1);
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
    protected void rehash(final long capa) {
        long mixr=getMixr(capa, this.size);
        long fill=(long) (capa*this.lfact);
        long pos=0, s=this.size;
        final long[][] idxs=new long[(int) (capa>>>this.blockshift)+1][];
        long head=0;
        while(s-->0) {
            long idx;
            while((idx=_getIndex(pos))<=0) pos++;
            double key=_getKey(idx-1);
            final long ph=this.strategy.hash(key)&0x7FFFFFFFFFFFFFFFL;
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
                final double[][] keys=new double[bnum][];
                System.arraycopy(this.keys, 0, keys, 0, this.keys.length);
                this.keys=keys;
                final byte[][] flgs=new byte[bnum][];
                System.arraycopy(this.flgs, 0, flgs, 0, this.flgs.length);
                this.flgs=flgs;
            }
        }
        // Decrease the size of the keys if possible
        else if(head<=capa&&capa<this.capa) {
            int bnum=(int) (((head<capa) ? capa : head)>>>this.blockshift)+1;
            if(bnum<this.keys.length) {
                final double[][] keys=new double[bnum][];
                System.arraycopy(this.keys, 0, keys, 0, bnum);
                this.keys=keys;
                final byte[][] flgs=new byte[bnum][];
                System.arraycopy(this.flgs, 0, flgs, 0, bnum);
                this.flgs=flgs;
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
            long c=BlockOpenHashIndexedDoubleSet.this.size;

            public long current() {
                long idx;
                if(curr==-1||(idx=_getIndex(curr))<=0) throw new IllegalStateException();
                return idx-1;
            }

            public boolean hasNext() {
                if(c==0) return false;
                if(next==-1) {
                    next=curr+1;
                    while(next<BlockOpenHashIndexedDoubleSet.this.capa&&_getIndex(next)<=0) next++;
                }
                return next<BlockOpenHashIndexedDoubleSet.this.capa;
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
                if(c==BlockOpenHashIndexedDoubleSet.this.size) return false;
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
                        if(c!=0) while(pos<BlockOpenHashIndexedDoubleSet.this.capa&&_getIndex(pos)<=0) pos++;
                    }

                    public boolean hasNext() {
                        return c!=0&&pos<BlockOpenHashIndexedDoubleSet.this.capa;
                    }

                    public Long next() {
                        if(!hasNext()) throw new NoSuchElementException();
                        long idx=_getIndex(last=pos)-1;
                        if(--c!=0) do pos++; while(pos<BlockOpenHashIndexedDoubleSet.this.capa&&_getIndex(pos)<=0);
                        return idx;
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _delKey(last);
                        size--;
                    }
                };
            }
        };
    }

    public DoubleIterator iterator() {
        return new DoubleIterator() {
            /** The position of the next entry to be returned. */
            long pos=0;
            /** The position of the last entry that has been returned. */
            long last=-1;
            /** A downward counter measuring how many entries have been returned. */
            long c=size;

            {
                if(c!=0) while(pos<BlockOpenHashIndexedDoubleSet.this.capa&&_getIndex(pos)<=0) pos++;
            }

            public boolean hasNext() {
                return c!=0&&pos<BlockOpenHashIndexedDoubleSet.this.capa;
            }

            public Double next() { return nextValue(); }

            public double nextValue() {
                if(!hasNext()) throw new NoSuchElementException();
                double key=_getKey(_getIndex(last=pos)-1);
                if(--c!=0) do pos++; while(pos<BlockOpenHashIndexedDoubleSet.this.capa&&_getIndex(pos)<=0);
                return key;
            }

            public void remove() {
                long idx;
                if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                _setIndex(last, -idx);
                _delKey(last);
                size--;
            }
        };
    }

    public <Par> long visit(Visitor<Double,Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            long idx=_getIndex(pos);
            if(idx<=0) {
                pos++;
                continue;
            }
            long r=vis.invoke(_getKey(idx-1), par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public <Par> long visit(IndexedVisitor<Double,Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            long idx=_getIndex(pos);
            if(idx<=0) {
                pos++;
                continue;
            }
            long r=vis.invoke(idx-1, _getKey(idx-1), par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public <Par> long visit(DoubleVisitor<Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            long idx=_getIndex(pos);
            if(idx<=0) {
                pos++;
                continue;
            }
            long r=vis.invoke(_getKey(idx-1), par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public <Par> long visit(IndexedDoubleVisitor<Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            long idx=_getIndex(pos);
            if(idx<=0) {
                pos++;
                continue;
            }
            long r=vis.invoke(idx-1, _getKey(idx-1), par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    public DoubleIterable iterate(final long[] indexes) {
        return new DoubleIterable() {
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    /** The position of the next index */
                    int pos=0;
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() {
                        return pos<indexes.length;
                    }

                    public Double next() { return nextValue(); }

                    public double nextValue() {
                        if(pos>=indexes.length) throw new NoSuchElementException();
                        long idx=_getIndex(last=indexes[pos++])-1;
                        if(!_hasKey(idx)) throw new NoSuchElementException();
                        return _getKey(idx);
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _delKey(last);
                        size--;
                    }
                };
            }
        };
    }


    public DoubleIterable iterate(final java.lang.Iterable<Long> indexes) {
        return new DoubleIterable() {
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    /** An iterator over indexes */
                    final java.util.Iterator<Long> iter=indexes.iterator();
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    public Double next() { return nextValue(); }

                    public double nextValue() {
                        long idx=_getIndex(last=iter.next())-1;
                        if(!_hasKey(idx)) throw new NoSuchElementException();
                        return _getKey(idx);
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _delKey(last);
                        size--;
                    }
                };
            }
        };
    }


    public DoubleIterable iterate(final Indexable indexes) {
        return new DoubleIterable() {
            public DoubleIterator iterator() {
                return new DoubleIterator() {
                    /** An iterator over indexes */
                    final Index iter=indexes.indexes();
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    public Double next() { return nextValue(); }

                    public double nextValue() {
                        long idx=_getIndex(last=iter.next())-1;
                        if(!_hasKey(idx)) throw new NoSuchElementException();
                        return _getKey(idx);
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _delKey(last);
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
    private static class DoubleEntry implements IndexedDoubleSet.DoubleEntry {
        private final long   idx;
        private final double key;

        public DoubleEntry(long idx, double key) {
            this.idx=idx;
            this.key=key;
        }

        public long index() { return idx; }

        public Double getKey() { return key; }

        public double getDoubleKey() { return key; }
    }

    public Iterable<IndexedDoubleSet.DoubleEntry> entries() {
        return new Iterable<IndexedDoubleSet.DoubleEntry>() {
            public Iterator<IndexedDoubleSet.DoubleEntry> iterator() {
                return new Iterator<IndexedDoubleSet.DoubleEntry>() {
                    /** The position of the next entry to be returned. */
                    long pos=0;
                    /** The position of the last entry that has been returned. */
                    long last=-1;
                    /** A downward counter measuring how many entries have been returned. */
                    long c=size;

                    {
                        if(c!=0) while(pos<BlockOpenHashIndexedDoubleSet.this.capa&&_getIndex(pos)<=0) pos++;
                    }

                    public boolean hasNext() {
                        return c!=0&&pos<BlockOpenHashIndexedDoubleSet.this.capa;
                    }

                    public IndexedDoubleSet.DoubleEntry next() {
                        if(!hasNext()) throw new NoSuchElementException();
                        long idx=_getIndex(last=pos)-1;
                        if(--c!=0) do pos++; while(pos<BlockOpenHashIndexedDoubleSet.this.capa&&_getIndex(pos)<=0);
                        return new DoubleEntry(idx, _getKey(idx));
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _delKey(last);
                        size--;
                    }
                };
            }
        };
    }

    public Iterable<IndexedDoubleSet.DoubleEntry> entries(final long[] indexes) {
        return new Iterable<IndexedDoubleSet.DoubleEntry>() {
            public Iterator<IndexedDoubleSet.DoubleEntry> iterator() {
                return new Iterator<IndexedDoubleSet.DoubleEntry>() {
                    /** The position of the next index */
                    int pos=0;
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() {
                        return pos<indexes.length;
                    }

                    public IndexedDoubleSet.DoubleEntry next() {
                        if(pos>=indexes.length) throw new NoSuchElementException();
                        long idx=_getIndex(last=indexes[pos++])-1;
                        if(!_hasKey(idx)) throw new NoSuchElementException();
                        return new DoubleEntry(idx, _getKey(idx));
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _delKey(last);
                        size--;
                    }
                };
            }
        };
    }

    public Iterable<IndexedDoubleSet.DoubleEntry> entries(final Iterable<Long> indexes) {
        return new Iterable<IndexedDoubleSet.DoubleEntry>() {
            public Iterator<IndexedDoubleSet.DoubleEntry> iterator() {
                return new Iterator<IndexedDoubleSet.DoubleEntry>() {
                    /** An iterator over indexes */
                    final Iterator<? extends Long> iter=indexes.iterator();
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    public IndexedDoubleSet.DoubleEntry next() {
                        long idx=_getIndex(last=iter.next())-1;
                        if(!_hasKey(idx)) throw new NoSuchElementException();
                        return new DoubleEntry(idx, _getKey(idx));
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _delKey(last);
                        size--;
                    }
                };
            }
        };
    }

    public Iterable<IndexedDoubleSet.DoubleEntry> entries(final Indexable indexes) {
        return new Iterable<IndexedDoubleSet.DoubleEntry>() {
            public Iterator<IndexedDoubleSet.DoubleEntry> iterator() {
                return new Iterator<IndexedDoubleSet.DoubleEntry>() {
                    /** An iterator over indexes */
                    final Index iter=indexes.indexes();
                    /** The position of the last entry that has been returned. */
                    long last=-1;

                    public boolean hasNext() { return iter.hasNext(); }

                    public IndexedDoubleSet.DoubleEntry next() {
                        long idx=_getIndex(last=iter.next())-1;
                        if(!_hasKey(idx)) throw new NoSuchElementException();
                        return new DoubleEntry(idx, _getKey(idx));
                    }

                    public void remove() {
                        long idx;
                        if(last==-1||(idx=_getIndex(last))<=0) throw new IllegalStateException();
                        _setIndex(last, -idx);
                        _delKey(last);
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
    public BlockOpenHashIndexedDoubleSet clone() {
        BlockOpenHashIndexedDoubleSet c;
        try {
            c=(BlockOpenHashIndexedDoubleSet) super.clone();
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
    public int hashCode() {
        long h=0;
        long i=0, j=size;
        while(j--!=0) {
            long idx;
            while((idx=_getIndex(i))<=0) i++;
            double k=_getKey(idx-1);
            h+=strategy.hash(k);
            i++;
        }
        return (int) h;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if(o instanceof IndexedDoubleSet) {
            IndexedDoubleSet that = (IndexedDoubleSet) o;
            if(this.size!=that.size()) return false;
            int pos=0;
            while(pos<capa) {
                long idx=_getIndex(pos);
                if(idx<=0) {
                    pos++;
                    continue;
                }
                idx --;
                if(!that.has(idx)) return false;
                double k=_getKey(idx);
                double l=that.getDouble(idx);
                if(Double.doubleToRawLongBits(k)!=Double.doubleToRawLongBits(l)) return false;
                pos++;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder buf=new StringBuilder();
        buf.append('{').append(' ');
        boolean first = true;
        int pos=0;
        while(pos<capa) {
            long idx=_getIndex(pos);
            if(idx<=0) {
                pos++;
                continue;
            }
            idx--;
            double k=_getKey(idx);
            if(first) first=false;
            else buf.append(',');
            buf.append(idx).append('@').append(k).append(' ');
            pos++;
        }
        buf.append('}');
        return buf.toString();
    }

//    public String toDebugString() {
//        StringBuilder buf = new StringBuilder();
//        buf.append(this.getClass().getSimpleName()).append(" [").append(size).append('/').append(free).append('/').append(capa).append("] {\n");
//        for(int i=0; i<capa;i++) {
//            long idx = getIndex(i);
//            buf.append('\t');
//            if(idx<0) {
//                idx = -idx-1;
//                buf.append('X').append(' ').append(idx).append('\t').append((hasKey(idx)?'+':'-')).append(' ').append(getKey(idx));
//            }
//            else if(idx>0) {
//                idx = idx -1;
//                buf.append('=').append(' ').append(idx).append('\t').append((hasKey(idx)?'+':'-')).append(' ').append(getKey(idx));
//            }
//            else {
//                buf.append('-');
//            }
//            buf.append('\n');
//        }
//        return buf.toString();
//    }


}
