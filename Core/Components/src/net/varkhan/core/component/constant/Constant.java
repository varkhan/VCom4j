package net.varkhan.core.component.constant;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * <b>An extensible, hierarchical alternative to Java {@link java.lang.Enum Enum}</b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/5/14
 * @time 4:53 PM
 */
public abstract class Constant<
        D extends Constant<D,?,D,?>,
        S extends Constant<D,?,S,?>,
        E extends Constant<D,S,E,C>,
        C extends Constant<D,E,?,?>
        > implements Serializable, Comparable<E> {

    private static final long serialVersionUID = 1L;

    // Constant identifiers
    protected /*final*/ S               sup;
    protected /*final*/ long            id;
    protected /*final*/ int             ord;
    protected /*final*/ String          name;

    // Subconstant registry
    private volatile long           sid=0;
    private volatile Constant[]     sub=null;
    private volatile Map<String,Constant> map=null;


    /**********************************************************************************
     **  Methods
     **/

    /** The constant's unique ID in its hierarchy. */
    public long id() { return id; }

    /** The constant's unique ordinal among its siblings. */
    public int ord() { return ord; }

    /** The constant's name. */
    public String name() { return name; }

    /** The constant's path within the hierarchy. */
    public String path() { return sup==this?name:sup.path()+"."+name; }

    /** The constant's domain (top-most superior) in the hierarchy. */
    @SuppressWarnings("unchecked")
    public D dom() { return sup==this?(D)this :sup.dom(); }

    /** The constant's superior (ancestor) in the hierarchy. */
    public S sup() { return sup; }

    /** The constant's sub-constants in the hierarchy. */
    @SuppressWarnings("unchecked")
    public C[] sub() { return (C[]) (sub==null? new Constant[0] : sub.clone()); }

    /** Look up a constant's sub by name. */
    @SuppressWarnings("unchecked")
    public C valueOf(String name) { return (C) map.get(name); }

    public int compareTo(E that) { return ord-that.ord; }

    public String toString() { return name; }

    public final boolean equals(Object that) { return this==that; }

    public final int hashCode() { return super.hashCode(); }


    /**********************************************************************************
     **  Constructors
     **/

    protected Constant(S sup, String name) {
        this.sup=sup;
        this.name=name;
        this.id=sup.dom()._id();
        this.ord=sup._add(this);
    }

    @SuppressWarnings("unchecked")
    protected Constant(String name) {
        this.sup=(S) this;
        this.name=name;
        this.id=0;
        this.ord=0;
    }


    /**********************************************************************************
     **  ID and Hierarchy management
     **/

    /** Increment and return the hierarchy's base ID */
    private synchronized long _id() { return ++sid; }
    /** Update the hierarchy's base ID to a deserialized id (which might not come in order) */
    private synchronized void _id(long id) { if(sid<id) sid=id; }

    /** Add a new constant to the internal sub registry, and return its new ordinal */
    private synchronized int _add(Constant<D, E, ?, ?> c) {
        // All changes of fields operate by copy and replacement, using
        // volatile semantic to avoid imposing synchronization on concurrent
        // reads, while preserving thread-safety. Thread safety on concurrent
        // writes is imposed by regular synchronization. This comes at the
        // expense of timeliness of updates,  but will not be an issue with
        // normal use, as constants are not designed to be created outside of
        // a static block executed during the application initialization stage.
        if(sub==null) {
            sub=new Constant[] { c };
            return 0;
        }
        int ord=sub.length;
        Constant[] sub=new Constant[ord+1];
        System.arraycopy(this.sub, 0, sub, 0, ord);
        sub[ord]=c;
        this.sub=sub;
        Map<String,Constant> map;
        if(this.map==null) map = new LinkedHashMap<String,Constant>();
        else map=new LinkedHashMap<String,Constant>(this.map);
        map.put(c.name(),c);
        this.map=map;
        return ord;
    }

    /** Add a deserialized constant to the internal sub registry, if not alrady registered,
     *  at its registration ordinal, and return it or an existing alternate */
    @SuppressWarnings("unchecked")
    private synchronized Constant<D, E, ?, ?> _add(Constant<D, E, ?, ?> c, int ord) {
        // All changes of fields operate by copy and replacement, using
        // volatile semantic to avoid imposing synchronization on concurrent
        // reads, while preserving thread-safety. Thread safety on concurrent
        // writes is imposed by regular synchronization. This comes at the
        // expense of timeliness of updates,  but will not be an issue with
        // normal use, as constants are not designed to be created outside of
        // a static block executed during the application initialization stage.
        int len=ord+1;
        if(sub!=null && len<sub.length) {
            if(sub[ord]!=null) return (Constant<D, E, ?, ?>) sub[ord];
            sub[ord]=c;
        }
        else {
            Constant[] sub=new Constant[len];
            if(this.sub!=null) System.arraycopy(this.sub, 0, sub, 0, ord);
            sub[ord]=c;
            this.sub=sub;
            dom()._id(id);
        }
        Map<String,Constant> map;
        if(this.map==null) map = new LinkedHashMap<String,Constant>();
        else map=new LinkedHashMap<String,Constant>(this.map);
        map.put(c.name(),c);
        this.map=map;
        return c;
    }


    /**********************************************************************************
     **  Hierarchy leaf marker
     **/

    /**
     * Dummy marker class for constants that have no subconstants.
     *
     * @param <D> the domain type
     * @param <S> the superior type
     * @param <X> a dummy type, this should always be a '?' wildcard
     */
    public static class None<D extends Constant<D,?,D,?>,S extends Constant<D,?,S,?>,X extends Constant<D,None<D,S,X>,X,?>> extends Constant<D,S,None<D,S,X>,X> {
        private None() {
            super(null,null);
            throw new UnsupportedOperationException();
        }
    }


    /**********************************************************************************
     **  Serialization
     **/

    /**
     * Ensures the constant only writes its fields when serializing.
     *
     * @param out the stream to write the object to
     * @throws java.io.IOException includes any I/O exceptions that may occur
     * @serialData the ordinal of the Enumeration constant
     */
    protected final void writeExternal(ObjectOutput out) throws IOException {
        if(sup==this) out.writeObject(null);
        else out.writeObject(sup);
        out.writeLong(id);
        out.writeInt(ord);
        out.writeObject(name);
    }

    /**
     * Only deserialize the fields of the constant, and defer to resolve for sub refs.
     *
     * @param in the stream to read data from in order to restore the object
     * @throws java.io.IOException    if an I/O error occurs
     * @throws ClassNotFoundException if the class for an object being restored cannot be found.
     */
    @SuppressWarnings("unchecked")
    protected final void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        Constant sup = (Constant)in.readObject();
        this.id  = in.readLong();
        this.ord = in.readInt();
        // If we have no parent, create hierarchy
        if(sup==null) {
            this.sup=(S) this;
        }
        // Otherwise, resolve singleton for parent
        else {
            this.sup=(S) sup.readResolve();
        }
        this.name = (String) in.readObject();
    }

    /**
     * Returns alternate deserialized constant to ensure singleton status.
     *
     * @return the actual Enumeration constant for the deserialized data
     * @throws java.io.InvalidObjectException if the deserialized data is invalid for this constant
     */
    protected final Constant<D,S,?,?> readResolve() throws InvalidObjectException {
        if(sup==this) return this;
        return sup._add(this,ord);
    }


}