package net.varkhan.base.extensions.type;

import net.varkhan.base.extensions.Named;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

/**
 * <b>A representation of a unique logical type.</b>
 * <br/>
 * While each type is expected to be abe to be stored as a definite Java class,
 * more than one type may be stored by a single Java class.
 *
 * @param <T> the Java type this logical type can be stored as
 *
 * @author varkhan
 * @date 14/1/13
 * @time 11:14 AM
 */
public interface Kind<T> extends Named {

    /**
     * The canonical name of this kind, used for string representations, schema parsing and registry lookups.
     *
     * @return the unique name of this kind
     */
    public String name();

    /**
     * Whether instances of this kind are allowed to be {@literal null}
     *
     * @return {@literal true} iff instances of this kind are allowed to be {@literal null}
     */
    public boolean isNullable();

    /**
     * Whether instances of a given kind can be logically cast into this kind.
     *
     * Note that this is unrelated to the assignability of the underlying Java storage classes:
     * two types stored as the same Java class may or may not be assignable to each other.
     *
     * @param from the kind to cast from
     * @param <F> the underlying Java storage of the kind to cast from
     * @return {@literal true} iff instances of the {@param from} Kind can be cast to this Kind.
     */
    public <F> boolean isAssignableFrom(Kind<F> from);

    /**
     * Returns a function able to cast the underlying Java storage of a kind into this kind.
     *
     * @param from the kind to cast from
     * @param <F> the underlying Java storage of the kind to cast from
     *
     * @return a {@link Caster}&lt;F,T&gt;
     */
    public <F> Caster<F,T> assignFrom(Kind<F> from);

    /**
     * A function able to cast the underlying Java storage of one Kind into another.
     *
     * @param <F>  the underlying Java storage of the kind to cast from
     * @param <T>  the underlying Java storage of the kind to cast into
     */
    public interface Caster<F,T> extends Function<F, T> {

        /**
         * The kind to cast from
         *
         * @return the definition object of the kind to cast from
         */
        public Kind<F> from();

        /**
         * The kind to cast into
         *
         * @return the definition object of the kind to cast into
         */
        public Kind<T> into();

        @Override
        public T apply(F from);
    }

    /************************
     ** Base kind implementations
     **/


    /**
     * A non-nullable abstract implementation of a Kind.
     *
     * To obtain a non-nullable kind, it is recommended to subclass {@link BaseKind}&lt;T&gt;
     * and implement the {@link #isAssignableFrom(Kind)} method.
     * To obtain a nullable kind, it is recommended to wrap an instance of a subclass of {@link BaseKind <T>}&lt;T&gt;
     * with an instance of {@link Nullable}&lt;T&gt;, using the {@link #nullable(Kind)} method.
     *
     * @param <T> the Java type this logical kind can be stored as
     */
    public static abstract class BaseKind<T> extends Named.Base implements Kind<T> {
        protected BaseKind(String name) { super(name); }

        /**
         * Whether instances of this kind are allowed to be {@literal null}
         *
         * @return always {@literal false}
         */
        @Override
        public final boolean isNullable() { return false; }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Kind)) return false;
            return isAssignableFrom((Kind<?>) obj) && ((Kind<?>) obj).isAssignableFrom(this);
        }

        @Override
        public String toString() { return name(); }

    }

    /**
     * An abstract implementation of a Caster.
     *
     * @param <F> the type to cast from
     * @param <T> the type to cast into
     */
    public static abstract class BaseCaster<F,T> implements Caster<F,T> {

        private final Kind<F> ft;
        private final Kind<T> tt;

        protected BaseCaster(Kind<F> ft, Kind<T> tt) {
            this.ft = ft;
            this.tt = tt;
        }

        @Override
        public Kind<F> from() {
            return ft;
        }

        @Override
        public Kind<T> into() {
            return tt;
        }

    }

    /**
     * A nullable wrapper around a Kind.
     *
     * This class is not directly instantiable, use the {@link #nullable(Kind)} method instead.
     *
     * To obtain a nullable kind, it is recommended to wrap a concrete subclass of {@link BaseKind}&lt;T&gt;
     * with an instance of this class, using the {@link #nullable(Kind)} method.
     *
     * @param <T> the Java type this logical kind can be stored as
     */
    public static final class Nullable<T> extends Named.Base implements Kind<T> {
        private final Kind<T> kind;

        protected Nullable(String name, Kind<T> kind) {
            super(name);
            this.kind = kind;
        }

        private Nullable(Kind<T> kind) {
            super("nullable<"+ kind.name()+">");
            this.kind = kind;
        }

        /**
         * The underlying non-nullable kind.
         *
         * @return the underlying non-nullable kind.
         */
        public Kind<T> kind() { return kind; }

        /**
         * Whether instances of this kind are allowed to be {@literal null}
         *
         * @return always {@literal true}
         */
        @Override
        public final boolean isNullable() { return true; }

        /**
         * Whether instances of a given kind can be logically cast into this kind.
         *
         * @param from the kind to cast from
         * @param <F> the underlying Java storage of the kind to cast from
         *
         * @return {@literal true} iff
         *  - {@param from} is Nullable and its underlying non-nullable kind can be assigned to this nullable's underlying kind
         *  - or {@param from} can be assigned to this nullable's underlying kind
         */
        @Override
        public <F> boolean isAssignableFrom(Kind<F> from) {
            // Get the concrete kind underlying a Nullable argument,
            // to allow assignment from
            if(from instanceof Nullable) {
                from = ((Nullable<F>) from).kind;
            }
            return kind.isAssignableFrom(from);
        }

        @Override
        public <F> Caster<F,T> assignFrom(Kind<F> from) {
            Kind<F> ft = from;
            if(from instanceof Nullable) ft = ((Nullable<F>) from).kind;
            if(!kind.isAssignableFrom(ft))
                throw new ClassCastException("Cannot cast "+from.name()+" to "+name);
            final Caster<F,T> c = kind.assignFrom(ft);
            return new BaseCaster<F,T>(from,this) {
                @Override
                public T apply(F from) {
                    if(from==null) return null;
                    return c.apply(from);
                }
            };
        }

        @Override
        public int hashCode() {
            return kind.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Kind)) return false;
            if(!((Kind<?>) obj).isNullable()) return false;
            return kind.equals(obj) || obj instanceof Nullable && kind.equals(((Nullable<?>) obj).kind);
        }

        @Override
        public String toString() { return name(); }
    }

    /**
     * Wraps an instance of a {@link Kind <T>} as a {@link Nullable<T>} Kind.
     *
     * @param kind the kind to wrap
     * @param <T> the Java type this logical kind can be stored as
     *
     * @return the {@param kind} argument if it is Nullable, or a Nullable version of this logical kind
     */
    public static <T> Nullable<T> nullable(Kind<T> kind) {
        if(kind instanceof Nullable) return (Nullable<T>) kind;
        return new Nullable<T>(kind);
    }


    /************************
     ** Value kind implementations
     **/

    /**
     * A simple Kind representing an atomic value.
     *
     * This kind assumes that such value can be canonically and equivalently represented
     * by a given Java type, identified by its Class.
     *
     * @param <T> the canonical Java type this atomic value is stored and represented by
     */
    public static abstract class ValueKind<T> extends BaseKind<T> {

        protected final Class<T> klass;

        protected ValueKind(String name, Class<T> klass) {
            super(name);
            this.klass = klass;
        }

        /**
         * The Class of the representing Java object.
         *
         * @return the Class of the Java type this logical kind is canonically represented as
         */
        public Class<T> javaClass() { return klass; }

        @Override
        public <F> boolean isAssignableFrom(Kind<F> from) {
            if(from instanceof ValueKind) {
                return klass.isAssignableFrom(((ValueKind<?>) from).javaClass());
            }
            return false;
        }

        @Override
        public <F> Caster<F,T> assignFrom(Kind<F> from) {
            return new BaseCaster<F,T>(from, this) {
                @SuppressWarnings("unchecked")
                @Override
                public T apply(F from) {
                    if(from==null)
                        throw new ClassCastException("Cannot cast null to "+name);
                    if(!klass.isAssignableFrom(from.getClass()))
                        throw new ClassCastException("Cannot cast "+from.getClass().getCanonicalName()+" to "+ klass.getCanonicalName());
                    return ((T) from);
                }
            };
        }

        @Override
        public int hashCode() {
            return klass.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof ValueKind)) return false;
            return klass.equals(((ValueKind<?>) obj).javaClass());
        }

        @Override
        public String toString() { return name(); }
    }

    /**
     * A Kind representing Java primitives
     * 
     * @param <T> the Java type of the primitive
     */
    public static abstract class Primitive<T> extends ValueKind<T> {

        protected final Class<T> prim;

        protected Primitive(String name, Class<T> klass, Class<T> prim) {
            super(name, klass);
            this.prim = prim;
        }

        public Class<T> primClass() {
            return prim;
        }

        @Override
        public <F> boolean isAssignableFrom(Kind<F> from) {
            if(from instanceof Primitive) {
                return prim==((Primitive<F>) from).primClass();
            }
            if(from instanceof ValueKind) {
                return klass.isAssignableFrom(((ValueKind<?>) from).javaClass());
            }
            return false;
        }
    }

    /**
     * A Kind representing character sequences
     *
     * @param <T> the CharSequence subtype to use as storage
     */
    public static abstract class CharsKind<T extends CharSequence> extends ValueKind<T> {

        protected CharsKind(String name, Class<T> klass) {
            super(name, klass);
        }

        @Override
        public <F> boolean isAssignableFrom(Kind<F> from) {
            if(! (from instanceof ValueKind)) return false;
            Class<?> k = ((ValueKind<?>) from).javaClass();
            return CharSequence.class.isAssignableFrom(k);
        }

        @Override
        public <F> Caster<F, T> assignFrom(Kind<F> from) {
            try {
                Constructor<T> cons = klass.getConstructor(String.class);
                return new CharSequenceCaster<F, T>(from, this, cons);
            }
            // ignore and return a dumb String caster
            catch (NoSuchMethodException ignored) { }
            return new StringCaster<F, T>(from, this) { };
        }

    }

    /**
     * A CharSequence caster using an explicit constructor for the destination type.
     *
     * @param <F> the kind to cast from
     * @param <T> the type of CharSequence to cast into using the constructor
     */
    public static class CharSequenceCaster<F,T extends CharSequence> extends BaseCaster<F, T> {

        protected final Function<String, T> cons;

        public CharSequenceCaster(Kind<F> from, CharsKind<T> into, Constructor<T> cons) {
            super(from, into);
            this.cons = s -> {
                try { return cons.newInstance(s); } catch (ReflectiveOperationException ignored) {
                    throw new ClassCastException("Cannot cast "+s.getClass().getCanonicalName()+" to "+cons.getDeclaringClass().getCanonicalName());
                }
            };
        }

        public CharSequenceCaster(Kind<F> from, CharsKind<T> into, Function<String,T> cons) {
            super(from, into);
            this.cons = cons;
        }

        @SuppressWarnings("unchecked")
        @Override
        public T apply(F from) {
            CharsKind<T> into = (CharsKind<T>) into();
            Class<? extends CharSequence> klass = into.javaClass();
            if(from==null)
                throw new ClassCastException("Cannot cast null to "+into.name());
            // No copies if we can avoid it
            if(klass.isAssignableFrom(from.getClass())) return (T) from;
            // Assign the toString value directly
            if(klass.isAssignableFrom(String.class)) return (T) from.toString();
            // Or attempt to make a copy of the toString value
            if(cons!=null) return cons.apply(from.toString());
            // We have no valid converter for this
            throw new ClassCastException("Cannot cast null to "+into.name());
        }
    }

    /**
     * A simple String caster, using {@link Object#toString()} to cast.
     *
     * @param <F> the kind to cast from
     * @param <T> the type of SharSequence to cast into
     */
    public static class StringCaster<F,T extends CharSequence> extends BaseCaster<F, T> {

        public StringCaster(Kind<F> from, CharsKind<T> into) {
            super(from, into);
        }

        @SuppressWarnings("unchecked")
        @Override
        public T apply(F from) {
            CharsKind<T> into = (CharsKind<T>) into();
            Class<? extends CharSequence> klass = into.javaClass();
            if(from==null) throw new ClassCastException("Cannot cast null to "+into.name());
            // No copies if we can avoid it
            if(klass.isAssignableFrom(from.getClass())) return (T) from;
            // Convert to String value directly
            return (T) from.toString();
        }
    }


    /************************
     ** Container kinds implementations
     **/

    /**
     * The Kind representing Array types (ordered, immutable, fixed-length sequences).
     * <br/>
     * This class is abstract to ensure the proper materialization of generic type parameters when instantiated.
     *
     * @param <E> the Java type of the array element
     * @param <A> the Java type of the array
     */
    public static abstract class ArrayKind<E, A> extends BaseKind<A> {
        protected final Kind<E> et;

        protected ArrayKind(String name, Kind<E> et) {
            super(name);
            this.et = et;
        }

        protected ArrayKind(Kind<E> et) {
            this("array<" + et.name() + ">", et);
        }

        public Kind<E> getElementType() {
            return et;
        }

        @Override
        public <F> boolean isAssignableFrom(Kind<F> from) {
            if (from instanceof Kind.ArrayKind) {
                return et.isAssignableFrom(((ArrayKind<?,?>) from).getElementType());
            }
            return false;
        }

        @Override
        public int hashCode() {
            return 0x5 & 31*et.hashCode();
        }

        @Override
        public abstract <F> Caster<F, A> assignFrom(Kind<F> from);

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Kind.ArrayKind)) return false;
            return et.equals(((ArrayKind<?,?>) obj).et);
        }

        @Override
        public String toString() { return name(); }

    }

    /**
     * A Caster into Array types.
     * <br/>
     * Actual storage types to cast from may be any List or Array with castable element types,
     * regardless of the type defined by the {@link #from()} kind.
     * <br/>
     * This class is abstract to ensure the proper materialization of generic type parameters when instantiated.
     *
     * @param <F> the kind to cast from
     * @param <E> the element kind of the array to cast into
     * @param <A> the Java type of the array to cast into
     */
    public static abstract class ArrayCaster<F, E, A> extends BaseCaster<F, A> {
        protected final Caster<Object, E> caster;
        protected final Class<E> elclass;

        @SuppressWarnings("unchecked")
        protected ArrayCaster(Kind<F> from, ArrayKind<E,A> into, Caster<?, E> caster, Class<E> elclass) {
            super(from, into);
            this.caster = (Caster<Object, E>) caster;
            this.elclass = elclass;
        }

        @SuppressWarnings("unchecked")
        public A apply(Object from) {
            if(from.getClass().isArray()) {
                int l = java.lang.reflect.Array.getLength(from);
                A a = (A) java.lang.reflect.Array.newInstance(elclass, l);
                for(int i=0; i<l; i++) {
                    Object e = java.lang.reflect.Array.get(from, i);
                    java.lang.reflect.Array.set(a,i,caster.apply(e));
                }
                return a;
            }
            if(from instanceof List) {
                int l = ((List<?>) from).size();
                A a = (A) java.lang.reflect.Array.newInstance(elclass, l);
                Iterator<?> it = ((List<?>) from).iterator();
                for(int i=0; i<l && it.hasNext(); i++) {
                    Object e = it.next();
                    java.lang.reflect.Array.set(a,i,caster.apply(e));
                }
                return a;
            }
            throw new ClassCastException("Cannot cast "+from.getClass().getCanonicalName()+" to "+into().name());
        }

    }

    /**
     * The Kind representing Array types (ordered, immutable, fixed-length sequences).
     * <br/>
     * This class is abstract to ensure the proper materialization of generic type parameters when instantiated.
     *
     * @param <E> the Java type of the array element
     */
    public static abstract class ObjectArrayKind<E> extends ArrayKind<E,E[]> {
        public ObjectArrayKind(String name, Kind<E> et) {
            super(name, et);
        }

        public ObjectArrayKind(Kind<E> et) {
            this("array<"+et.name()+">",et);
        }

        @Override
        public <F> Caster<F, E[]> assignFrom(Kind<F> from) {
            if (!(from instanceof Kind.ArrayKind)) throw new ClassCastException("Cannot cast "+from.name()+" to "+name);
            Kind<E> f = ((ArrayKind<E,?>) from).getElementType();
            final Caster<?,E> c = et.assignFrom(f);
            if (c==null) throw new ClassCastException("Cannot cast elements "+f.name()+" to "+et.name());
            return new ObjectArrayCaster<F, E>(from, this, c) { };
        }

    }

    /**
     * A Caster into Array types.
     * <br/>
     * Actual storage types to cast from may be any List or Array with castable element types,
     * regardless of the type defined by the {@link #from()} kind.
     * <br/>
     * This class is abstract to ensure the proper materialization of generic type parameters when instantiated.
     *
     * @param <F> the kind to cast from
     * @param <E> the element kind of the array to cast into
     */
    public static class ObjectArrayCaster<F, E> extends ArrayCaster<F, E, E[]> {

        @SuppressWarnings("unchecked")
        public ObjectArrayCaster(Kind<F> from, ObjectArrayKind<E> into, Caster<?, E> caster) {
            super(from, into, caster,
                    (into.getElementType() instanceof ValueKind<?>)
                            ? ((ValueKind<E>) into.getElementType()).javaClass()
                            : (Class<E>) Object.class
            );
        }

    }


    /**
     * The Kind representing a primitive array type.
     */
    public static class PrimitiveArrayKind<E,A> extends ArrayKind<E,A> {
        public PrimitiveArrayKind(String name, Primitive<E> et) {
            super(name, et);
        }

        public PrimitiveArrayKind(Primitive<E> et) {
            this("array<"+et.name()+">", et);
        }

        @Override
        public <F> Caster<F, A> assignFrom(Kind<F> from) {
            if (!(from instanceof Kind.ArrayKind)) throw new ClassCastException("Cannot cast "+from.name()+" to "+name);
            Kind<Boolean> f = ((ArrayKind<Boolean,?>) from).getElementType();
            final Caster<?,E> c = et.assignFrom(f);
            if (c==null) throw new ClassCastException("Cannot cast elements "+f.name()+" to "+et.name());
            return new ArrayCaster<F,E,A>(from, this, c, ((Primitive<E>)et).primClass()) { };
        }

    }

    /**
     * The Kind representing {@link List} types (ordered, mutable, dynamic-length sequences).
     * <br/>
     * This class is abstract to ensure the proper materialization of generic type parameters when instantiated.
     *
     * @param <E> the kind of the List element
     */
    public static abstract class ListKind<E> extends BaseKind<List<E>> {
        protected final Kind<E> et;

        public ListKind(String name, Kind<E> et) {
            super(name);
            this.et = et;
        }

        public ListKind(Kind<E> et) {
            this("list<" + et.name() + ">",et);
        }

        public Kind<E> getElementType() {
            return et;
        }

        @Override
        @SuppressWarnings("RedundantIfStatement")
        public <F> boolean isAssignableFrom(Kind<F> from) {
            if (!(from instanceof ListKind)) return false;
            if (!et.isAssignableFrom(((ListKind<?>) from).getElementType())) return false;
            return true;
        }

        @Override
        public <F> Caster<F, List<E>> assignFrom(Kind<F> from) {
            if (!(from instanceof ListKind)) throw new ClassCastException("Cannot cast "+from.name()+" to "+name);
            Kind<E> f = ((ListKind<E>) from).getElementType();
            final Caster<?,E> c = et.assignFrom(f);
            if (c==null) throw new ClassCastException("Cannot cast elements "+f.name()+" to "+et.name());
            return new ListCaster<F, E>(from, this, c) { };
        }

        @Override
        public int hashCode() {
            return 0x9 & 31*et.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof ListKind)) return false;
            return et.equals(((ListKind<?>) obj).et);
        }

        @Override
        public String toString() { return name(); }

    }

    /**
     * A Caster into {@link List} types.
     * <br/>
     * Actual storage types to cast from may be any List or Array with castable element types,
     * regardless of the type defined by the {@link #from()} kind.
     * <br/>
     * This class is abstract to ensure the proper materialization of generic type parameters when instantiated.
     *
     * @param <F> the kind to cast from
     * @param <E> the element kind of the List to cast into
     */
    public static class ListCaster<F, E> extends BaseCaster<F, List<E>> {
        protected final Caster<Object, E> caster;

        @SuppressWarnings("unchecked")
        public ListCaster(Kind<F> from, ListKind<E> into, Caster<?, E> caster) {
            super(from, into);
            this.caster = (Caster<Object, E>) caster;
        }

        public List<E> apply(Object from) {
            if(from.getClass().isArray()) {
                int l = java.lang.reflect.Array.getLength(from);
                List<E> a = new ArrayList<>(l);
                for(int i=0; i<l; i++) {
                    Object e = java.lang.reflect.Array.get(from, i);
                    a.add(caster.apply(e));
                }
                return a;
            }
            if(from instanceof List) {
                List<E> a = new ArrayList<>();
                for (Object e: (List<?>)from) {
                    a.add(caster.apply(e));
                }
                return a;
            }
            throw new ClassCastException("Cannot cast "+from.getClass().getCanonicalName()+" to "+into().name());
        }

    }


    /************************
     ** Map kind implementation
     **/

    /**
     * The Kind representing {@link Map} types.
     * <br/>
     * This class is abstract to ensure the proper materialization of generic type parameters when instantiated.
     *
     * @param <K> the kind of the map keys (which must be a {@link ValueKind})
     * @param <E> the kind of the map elements (values)
     */
    public static abstract class MapKind<K, E> extends BaseKind<Map<K, E>> {
        protected final ValueKind<K> kt;
        protected final Kind<E> et;

        public MapKind(String name, ValueKind<K> kt, Kind<E> et) {
            super(name);
            this.kt = kt;
            this.et = et;
        }

        public MapKind(ValueKind<K> kt, Kind<E> et) {
            this("map<" + kt.name() + "," + et.name() + ">", kt, et);
        }

        public Kind<K> getKeyType() {
            return kt;
        }

        public Kind<E> getElementType() {
            return et;
        }

        @Override
        @SuppressWarnings("RedundantIfStatement")
        public <F> boolean isAssignableFrom(Kind<F> from) {
            if (!(from instanceof MapKind)) return false;
            if (!kt.isAssignableFrom(((MapKind<?, ?>) from).getKeyType())) return false;
            if (!et.isAssignableFrom(((MapKind<?, ?>) from).getElementType())) return false;
            return true;
        }

        @Override
        public <F> Caster<F, java.util.Map<K, E>> assignFrom(Kind<F> from) {
            if (!(from instanceof MapKind)) throw new ClassCastException("Cannot cast "+from.name()+" to "+name);
            Kind<?> kf = ((MapKind<?, ?>) from).getKeyType();
            Kind<?> ef = ((MapKind<?, ?>) from).getElementType();
            Caster<?, K> kc = kt.assignFrom(kf);
            Caster<?, E> ec = et.assignFrom(ef);
            if (kc==null) throw new ClassCastException("Cannot cast keys "+kf.name()+" to "+kt.name());
            if (ec==null) throw new ClassCastException("Cannot cast elements "+ef.name()+" to "+et.name());
            return new MapCaster<>(from, this, kc, ec);
        }

        @Override
        public int hashCode() {
            return 0x11 & 31*et.hashCode() & 61*kt.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof MapKind)) return false;
            return kt.equals(((MapKind<?, ?>) obj).kt) && et.equals(((MapKind<?, ?>) obj).et);
        }

        @Override
        public String toString() { return name(); }

    }

    /**
     * A caster into {@link Map} kinds.
     * <br/>
     * This class is abstract to ensure the proper materialization of generic type parameters when instantiated.
     *
     * @param <F> the kind to cast from
     * @param <K> the kind of the map keys (which must be a {@link ValueKind})
     * @param <E> the kind of the map elements (values)
     */
    public static class MapCaster<F, K, E> extends BaseCaster<F, java.util.Map<K, E>> {

        protected final Caster<Object, K> kcaster;
        protected final Caster<Object, E> ecaster;

        @SuppressWarnings("unchecked")
        public MapCaster(Kind<F> from, MapKind<K, E> into,
                         Caster<?, K> keyCaster, Caster<?, E> elementCaster) {
            super(from, into);
            this.kcaster = (Caster<Object, K>) keyCaster;
            this.ecaster = (Caster<Object, E>) elementCaster;
        }

        public java.util.Map<K, E> apply(Object from) {
            if(!(from instanceof java.util.Map))
                throw new ClassCastException("Cannot cast "+from.getClass().getCanonicalName()+" to "+into().name());
            java.util.Map<K, E> m = new LinkedHashMap<>();
            for (java.util.Map.Entry<?,?> e: ((java.util.Map<?,?>)from).entrySet()) {
                m.put(kcaster.apply(e.getKey()),ecaster.apply(e.getValue()));
            }
            return m;
        }
    }


    /************************
     ** Kind definition constants
     **/

    public static final Primitive<Boolean>              BOOL =          new Primitive<Boolean>("bool", Boolean.class, boolean.class) { };
    public static final Primitive<Byte>                 BYTE =          new Primitive<Byte>("byte", Byte.class, byte.class) { };
    public static final Primitive<Short>                SHORT =         new Primitive<Short>("short", Short.class, short.class) { };
    public static final Primitive<Character>            CHAR =          new Primitive<Character>("char", Character.class, char.class) { };
    public static final Primitive<Integer>              INT =           new Primitive<Integer>("int", Integer.class, int.class) { };
    public static final Primitive<Long>                 LONG =          new Primitive<Long>("long", Long.class, long.class) { };
    public static final Primitive<Float>                FLOAT =         new Primitive<Float>("float", Float.class, float.class) { };
    public static final Primitive<Double>               DOUBLE =        new Primitive<Double>("double", Double.class, double.class) { };
    public static final CharsKind<String>               STRING =        new CharsKind<String>("string", String.class) { };
    public static final ValueKind<Date>                 DATE =          new ValueKind<Date>("date", Date.class) { };
    public static final ValueKind<BigDecimal>           DECIMAL =       new ValueKind<BigDecimal>("decimal", BigDecimal.class) { };
    public static final ArrayKind<Boolean,boolean[]>    ARRAY_BOOL =    new PrimitiveArrayKind<Boolean, boolean[]>("array<bool>",Kind.BOOL);
    public static final ArrayKind<Byte,byte[]>          ARRAY_BYTE =    new PrimitiveArrayKind<Byte, byte[]>("array<byte>",Kind.BYTE);
    public static final ArrayKind<Short,short[]>        ARRAY_SHORT =   new PrimitiveArrayKind<Short, short[]>("array<short>",Kind.SHORT);
    public static final ArrayKind<Character,char[]>     ARRAY_CHAR =    new PrimitiveArrayKind<Character, char[]>("array<char>",Kind.CHAR);
    public static final ArrayKind<Integer,int[]>        ARRAY_INT =     new PrimitiveArrayKind<Integer, int[]>("array<int>",Kind.INT);
    public static final ArrayKind<Long,long[]>          ARRAY_LONG =    new PrimitiveArrayKind<Long, long[]>("array<long>",Kind.LONG);
    public static final ArrayKind<Float,int[]>          ARRAY_FLOAT =   new PrimitiveArrayKind<Float, int[]>("array<float>",Kind.FLOAT);
    public static final ArrayKind<Double,long[]>        ARRAY_DOUBLE =  new PrimitiveArrayKind<Double, long[]>("array<double>",Kind.DOUBLE);

    public static List<Kind<?>> all() {
        return Collections.unmodifiableList(Arrays.asList(
                BOOL, BYTE, SHORT, CHAR, INT, LONG, FLOAT, DOUBLE, STRING, DATE, DECIMAL,
                ARRAY_BOOL, ARRAY_BYTE, ARRAY_SHORT, ARRAY_CHAR, ARRAY_INT, ARRAY_LONG, ARRAY_FLOAT, ARRAY_DOUBLE
        ));
    }

    @SuppressWarnings("unchecked")
    public static <T> Kind<T> of(String name) {
        for(Kind<?> k: all()) if(k.name().equalsIgnoreCase(name)) return (Kind<T>) k;
        return null;
    }

}
