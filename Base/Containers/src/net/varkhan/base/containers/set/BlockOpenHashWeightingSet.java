/**
 *
 */
package net.varkhan.base.containers.set;

import net.varkhan.base.containers.Hashes;
import net.varkhan.base.containers.HashingStrategy;
import net.varkhan.base.containers.Iterator;

import java.io.Serializable;
import java.util.NoSuchElementException;


/**
 * @author varkhan
 * @date May 28, 2009
 * @time 9:43:13 PM
 */
public class BlockOpenHashWeightingSet<Key> implements WeightingSet<Key>, Serializable, Cloneable {

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
     * The array of weights
     */
    protected float[][] vals;

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
     * Total weight of entries in the map
     */
    protected double weight;

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
     * @param blockshift the base-2 logarithm of the block size (such that the block size is {@code 1&lt;&lt;blockshift})
     * @param loadfact   the load factor (between 0 exclusive and 1 inclusive)
     * @param growfact   the growth factor (strictly greater than 1)
     * @param strategy   the hashing strategy
     */
    public BlockOpenHashWeightingSet(long size, int blockshift, float loadfact, float growfact, final HashingStrategy<Key> strategy) {
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
        this.weight=0;
        int bnum=(int) (this.capa>>>this.blockshift)+1;
        this.keys=new Object[bnum][];
        this.vals=new float[bnum][];
    }

    /**
     * Creates a new hash set.
     *
     * @param size the expected number of elements in the set
     */
    @SuppressWarnings("unchecked")
    public BlockOpenHashWeightingSet(long size) {
        this(size, 10, .75f, 1.5f, (HashingStrategy<Key>) Hashes.DefaultHashingStrategy);
    }

    /**
     * Creates a new hash set.
     */
    @SuppressWarnings("unchecked")
    public BlockOpenHashWeightingSet() {
        this(11, 10, .75f, 1.5f, (HashingStrategy<Key>) Hashes.DefaultHashingStrategy);
    }


    /**********************************************************************************
     **  Global information accessors
     **/

    public long size() { return size; }

    public boolean isEmpty() { return size==0; }

    public double weight() { return weight; }

    public void clear() {
        if(free==capa) return;
        free=capa;
        size=0;
        weight=0;
        for(int i=0;i<keys.length;i++) keys[i]=null;
        for(int i=0;i<vals.length;i++) vals[i]=null;
    }


    /**********************************************************************************
     **  Set elements accessors
     **/

    /**
     * This reference is used to fill the keys of empty entries, and allows the
     * search algorithm to distinguish absent keys from actual {@literal null}
     * keys.
     */
    protected static final Object NULL=new Serializable() {
        private static final long serialVersionUID=1L;

        public String toString() { return "@NULL"; }
    };

    /**
     * This reference is used to fill the keys of removed entries, and allows the
     * search algorithm to distinguish removed keys from actual {@literal null}
     * keys.
     */
    protected static final Object DEL=new Object() {
        public String toString() { return "@DEL"; }
    };

    private Object _getKey(long index) {
        int kblockpos=(int) (index>>>blockshift);
        int kblockoff=(int) (index&blockmask);
        Object[] kblock=keys[kblockpos];
        // No block == NULL key
        return (kblock==null) ? NULL : kblock[kblockoff];
    }

    private void _setEntry(long index, Object k, double v) {
        int kblockpos=(int) (index>>>blockshift);
        int kblockoff=(int) (index&blockmask);
        Object[] kblock=keys[kblockpos];
        if(kblock==null) {
            keys[kblockpos]=kblock=new Object[blocksize];
            for(int j=0;j<blocksize;j++) kblock[j]=NULL;
        }
        float[] vblock=vals[kblockpos];
        if(vblock==null) {
            vals[kblockpos]=vblock=new float[blocksize];
        }
        kblock[kblockoff]=k;
        vblock[kblockoff]=(float)v;
    }

    private float _getVal(long index) {
        int kblockpos=(int) (index>>>blockshift);
        int kblockoff=(int) (index&blockmask);
        float[] vblock=vals[kblockpos];
        // No block == NULL key
        return (vblock==null) ? 0 : vblock[kblockoff];
    }

//    private double _decVal(long index) {
//        int kblockpos=(int) (index>>>blockshift);
//        int kblockoff=(int) (index&blockmask);
//        double[] vblock=vals[kblockpos];
//        if(vblock==null) {
//            vals[kblockpos]=new double[blocksize];
//            return 0;
//        }
//        return --vblock[kblockoff];
//    }

    private double _incVal(long index, double weight) {
        int kblockpos=(int) (index>>>blockshift);
        int kblockoff=(int) (index&blockmask);
        float[] vblock=vals[kblockpos];
        if(vblock==null) {
            vals[kblockpos]=vblock=new float[blocksize];
        }
        return vblock[kblockoff]+=weight;
    }

    @SuppressWarnings("unchecked")
    public boolean has(final Key key) {
        final long capa=this.capa;
        final long mixr=this.mixr;
        final long hash=this.strategy.hash(key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        long slot=ph%capa;
        // The key table value
        Object kval=_getKey(slot);
        if(kval==DEL||(kval!=NULL&&!(this.strategy.hash((Key) kval)==hash&&this.strategy.equal(key, (Key) kval)))) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                kval=_getKey(slot);
                // There's always an EMPTY slot
            }
            while(kval==DEL||(kval!=NULL&&!(this.strategy.hash((Key) kval)==hash&&this.strategy.equal(key, (Key) kval))));
        }
        return kval!=DEL&&kval!=NULL; // If OCCUPIED, necessarily, key_match(key, hash, kval).
    }

    public boolean add(final Key key) {
        return add(key,1.0);
    }

    @SuppressWarnings("unchecked")
    public boolean add(final Key key, double wgh) {
        if(wgh==0) return false;
        final long capa=this.capa;
        final long mixr=this.mixr;
        final long hash=this.strategy.hash(key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        long slot=ph%capa;
        // The key table value
        Object kval=_getKey(slot);
        if(kval!=DEL&&kval!=NULL&&!(hash==this.strategy.hash((Key) kval)&&this.strategy.equal(key, (Key) kval))) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                kval=_getKey(slot);
                // There's always an EMPTY slot
            }
            while(kval!=DEL&&kval!=NULL&&!(hash==this.strategy.hash((Key) kval)&&this.strategy.equal(key, (Key) kval)));
        }
        final long pos=slot; // Remember first available slot for later.
        // Key found
        if(kval!=DEL&&kval!=NULL) {
            weight+=wgh;
            if(_incVal(pos,wgh)==0) {
                _setEntry(slot,DEL,0L);
                size--;
                return true;
            }
            return false; // If OCCUPIED, necessarily, key_match(key, hash, kval).
        }
        // If this slot is REMOVED, keep looking for the key until we find it or get to an empty slot
        else if(kval==DEL) {
            // We are on a REMOVED spot, so we have to continue scanning
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                kval=_getKey(slot);
                // There's always an EMPTY slot
            }
            while(kval==DEL||(kval!=NULL&&!(hash==this.strategy.hash((Key) kval)&&this.strategy.equal(key, (Key) kval))));
            if(kval!=DEL&&kval!=NULL) {
                weight+=wgh;
                if(_incVal(pos,wgh)==0) {
                    _setEntry(slot,DEL,0L);
                    size--;
                    return true;
                }
                return false; // If OCCUPIED, necessarily, key_match(key, hash, kval).
            }
        }
        // If this slot is EMPTY, reserve a slot
        else {
            free--;
        }
        // Mark slot as OCCUPIED
        _setEntry(pos, key, wgh);
        weight+=wgh;
        if(++size>=fill)
            rehash(getCapa((long) (size*gfact)));    // Too many elements for the capacity, rehash at an increased capa
        if(free==0)
            rehash(capa);                                    // Too many deletions, rehash at the same capa for cleanup
        return true;
    }

    @SuppressWarnings("unchecked")
    public double weight(final Key key) {
        final long capa=this.capa;
        final long mixr=this.mixr;
        final long hash=this.strategy.hash((Key) key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        long slot=ph%capa;
        // The key table value
        Object kval=_getKey(slot);
        if(kval==DEL||(kval!=NULL&&!(this.strategy.hash((Key) kval)==hash&&this.strategy.equal(key, (Key) kval)))) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                kval=_getKey(slot);
                // There's always an EMPTY slot
            }
            while(kval==DEL||(kval!=NULL&&!(this.strategy.hash((Key) kval)==hash&&this.strategy.equal(key, (Key) kval))));
        }
        if(kval==DEL||kval==NULL) return 0; // If OCCUPIED, necessarily, key_match(key, hash, kval).
        // At this point we necessarily have an OCCUPIED slot
        return _getVal(slot);
    }

    @SuppressWarnings("unchecked")
    public boolean del(final Key key) {
        final long capa=this.capa;
        final long mixr=this.mixr;
        final long hash=this.strategy.hash((Key) key);
        // Get a positive integer for the hash
        final long ph=hash&0x7FFFFFFFFFFFFFFFL;
        // The slot position, starting at the primary hash
        long slot=ph%capa;
        // The key table value
        Object kval=_getKey(slot);
        if(kval==DEL||(kval!=NULL&&!(this.strategy.hash((Key) kval)==hash&&this.strategy.equal(key, (Key) kval)))) {
            // The scan increment
            final long scan=(ph%mixr)+1;
            do {
                slot+=scan;
                if(slot>=capa||slot<0) slot-=capa;
                kval=_getKey(slot);
                // There's always an EMPTY slot
            }
            while(kval==DEL||(kval!=NULL&&!(this.strategy.hash((Key) kval)==hash&&this.strategy.equal(key, (Key) kval))));
        }
        if(kval==DEL||kval==NULL) return false; // If OCCUPIED, necessarily, key_match(key, hash, kval).
        // At this point we necessarily have an OCCUPIED slot
        weight-=_getVal(slot);
        _setEntry(slot,DEL,0L);
        size--;
        return true;
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
        try { rehash(capa); }
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
        try { rehash(getCapa(size)); }
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
        long mixr=getMixr(capa, size);
        long fill=(long) (capa*this.lfact);
        long pos=0, s=size;
        final Object[][] keys=new Object[(int) (capa>>>blockshift)+1][];
        final float[][] vals=new float[(int) (capa>>>blockshift)+1][];
        long count=0;
        while(s-->0) {
            Object key;
            while((key=_getKey(pos))==NULL||key==DEL) pos++;
            final long ph=this.strategy.hash((Key) key)&0x7FFFFFFFFFFFFFFFL;
            long slot=ph%capa;
            int kblockpos=(int) (slot>>>blockshift);
            int kblockoff=(int) (slot&blockmask);
            Object[] kblock=keys[kblockpos];
            if(kblock!=null&&kblock[kblockoff]!=NULL) {
                long scan=(ph%mixr)+1;
                do {
                    slot+=scan;
                    if(slot>=capa||slot<0) slot-=capa;
                    kblockpos=(int) (slot>>>blockshift);
                    kblockoff=(int) (slot&blockmask);
                    kblock=keys[kblockpos];
                } while(kblock!=null&&kblock[kblockoff]!=NULL);
            }
            if(kblock==null) {
                keys[kblockpos]=kblock=new Object[blocksize];
                for(int t=0;t<blocksize;t++) kblock[t]=NULL;
            }
            float[] vblock=vals[kblockpos];
            if(vblock==null) {
                vals[kblockpos]=vblock=new float[blocksize];
            }
            kblock[kblockoff]=key;
            count+=(vblock[kblockoff]=_getVal(pos));
            pos++;
        }
        this.capa=capa;
        this.mixr=mixr;
        this.free=capa-size;
        this.fill=fill;
        this.keys=keys;
        this.vals=vals;
        this.weight=count;
    }


    protected long getCapa(long size) {
        if(size<this.size) size=this.size;
        long capa=(long) (size/lfact)+1;
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

    protected static final long PRIMES[]={ 3, 5, 7, 13, 19, 31, 43, 61, 73, 103, 109, 139, 151, 181, 193, 199,
                                           229, 241, 271, 283, 313, 349, 421, 433, 463, 523, 571, 601, 619, 661, 823, 859, 883,
                                           1021, 1063, 1093, 1153, 1231, 1321, 1429, 1489, 1621, 1699, 1789, 1873, 1951,
                                           2029, 2131, 2143, 2311, 2383, 2383, 2593, 2731, 2803, 3001, 3121, 3259, 3391, 3583, 3673, 3919,
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
     **  Map elements iterators
     **/


     public Iterator<Key> iterator() {
        return new Iterator<Key>() {
            /** The position of the next entry to be returned. */
            long pos=0;
            /** The position of the last entry that has been returned. */
            long last=-1;
            /** A downward counter measuring how many entries have been returned. */
            long c=size;

            {
                Object k;
                if(c!=0) while(pos<BlockOpenHashWeightingSet.this.capa&&((k=_getKey(pos))==NULL||k==DEL)) pos++;
            }

            public boolean hasNext() {
                return c!=0&&pos<BlockOpenHashWeightingSet.this.capa;
            }

            @SuppressWarnings("unchecked")
            public Key next() {
                if(!hasNext()) throw new NoSuchElementException();
                Key key=(Key) _getKey(last=pos);
                double val=_getVal(pos);
                Object k;
                if(--c!=0) do pos++; while(pos<BlockOpenHashWeightingSet.this.capa&&((k=_getKey(pos))==NULL||k==DEL));
                return key;
            }

            public void remove() {
                Object k;
                if(last==-1||(k=_getKey(pos))==NULL||k==DEL) throw new IllegalStateException();
                weight-=_getVal(last);
                _setEntry(last, DEL, 0);
                size--;
            }
        };
    }

    public <Par> long visit(Visitor<Key,Par> vis, Par par) {
        long c=0;
        int pos=0;
        while(pos<capa) {
            Object k=_getKey(pos);
            if(k==NULL||k==DEL) {
                pos++;
                continue;
            }
            @SuppressWarnings("unchecked")
            long r=vis.invoke((Key) k, par);
            if(r<0) return c;
            c+=r;
            pos++;
        }
        return c;
    }

    /**
     * Returns a clone of this set.
     *
     * @return an identical, yet independent copy of this set
     */
    @SuppressWarnings("unchecked")
    public Object clone() {
        BlockOpenHashWeightingSet<Key> c;
        try {
            c=(BlockOpenHashWeightingSet<Key>) super.clone();
        }
        catch(CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys=keys.clone();
        c.vals=vals.clone();
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
            Object k;
            while((k=_getKey(i))==NULL||k==DEL) i++;
            if(this!=k) h+=strategy.hash((Key) k);
            i++;
        }
        return (int) h;
    }

    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if(this==o) return true;
        if(o instanceof WeightingSet){
            WeightingSet that=(WeightingSet) o;
            if(this.size!=that.size()) return false;
            if(this.weight!=that.weight()) return false;
            int pos=0;
            while(pos<capa) {
                Object k=_getKey(pos);
                if(k==NULL||k==DEL) {
                    pos++;
                    continue;
                }
                double v=_getVal(pos);
                double w=that.weight(k);
                if(v!=w) return false;
                pos++;
            }
            return true;
        }
        return false;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append('{');
        boolean first = true;
        int pos=0;
        while(pos<capa) {
            Object k=_getKey(pos);
            if(k==NULL||k==DEL) {
                pos++;
                continue;
            }
            double v=_getVal(pos);
            if(first) first=false;
            else buf.append(',');
            buf.append(' ').append(k).append(':').append(v);
            pos++;
        }
        buf.append(' ').append('}');
        return buf.toString();
    }

//    public String toDebugString() {
//        StringBuilder buf = new StringBuilder();
//        buf.append(this.getClass().getSimpleName()).append(" [").append(size).append('/').append(free).append('/').append(capa).append("] {\n");
//        for(int i=0; i<capa;i++) {
//            Object k = getKey(i);
//            buf.append('\t');
//            if(k==DEL) {
//                buf.append('X');
//            }
//            else if(k==NULL) {
//                buf.append('-');
//            }
//            else {
//                buf.append('=').append(' ').append('\t').append(k);
//            }
//            buf.append('\n');
//        }
//        return buf.toString();
//    }


}
