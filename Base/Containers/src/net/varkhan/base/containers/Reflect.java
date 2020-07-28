/**
 *
 */
package net.varkhan.base.containers;

import java.lang.reflect.*;
import java.util.*;


/**
 * <b>Reflection tools.</b>
 * <p/>
 * Reflexive object structure examination, and heap monitoring tools.
 * <p/>
 *
 * @author varkhan
 * @date Mar 12, 2009
 * @time 8:06:12 PM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public final class Reflect {

    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected Reflect() { }


    /**********************************************************************************
     **  Memory footprint computation for primitive types
     **/

    /** The size of a Java void */
    private static final long SIZE_VOID   = 0L;
    /** The size of a Java boolean -- note that this is hardly well defined by the JLS */
    private static final long SIZE_BOOL   = 1L;
    /** The size of a Java byte */
    private static final long SIZE_BYTE   = Byte.SIZE>>3;
    /** The size of a Java char */
    private static final long SIZE_CHAR   = Character.SIZE>>3;
    /** The size of a Java short */
    private static final long SIZE_SHORT  = Short.SIZE>>3;
    /** The size of a Java int */
    private static final long SIZE_INT    = Integer.SIZE>>3;
    /** The size of a Java long */
    private static final long SIZE_LONG   = Long.SIZE>>3;
    /** The size of a Java float */
    private static final long SIZE_FLOAT  = Float.SIZE>>3;
    /** The size of a Java double */
    private static final long SIZE_DOUBLE = Double.SIZE>>3;


    /**
     * Returns the number of bytes used by a primitive type.
     *
     * @param cls the primitive type class
     * @return the number of bytes used in memory by the type represented by the argument class
     */
    public static long sizeOfClass(Class<?> cls) {
        if(cls==null) return 0L;
        if(cls==Class.class)    return 0L;
        if(cls==Void.TYPE)      return SIZE_VOID;
        if(cls==Boolean.TYPE)   return SIZE_BOOL;
        if(cls==Byte.TYPE)      return SIZE_BYTE;
        if(cls==Character.TYPE) return SIZE_CHAR;
        if(cls==Short.TYPE)     return SIZE_SHORT;
        if(cls==Integer.TYPE)   return SIZE_INT;
        if(cls==Long.TYPE)      return SIZE_LONG;
        if(cls==Float.TYPE)     return SIZE_FLOAT;
        if(cls==Double.TYPE)    return SIZE_DOUBLE;
        return 0L;
    }


    /**
     * Returns the number of bytes used by the primitive type argument.
     *
     * @param val the type
     * @return the number of bytes this type uses in memory
     */
    public static long sizeOf(boolean val) {
        return SIZE_BOOL;
    }

    /**
     * Returns the number of bytes used by the primitive type argument.
     *
     * @param val the type
     * @return the number of bytes this type uses in memory
     */
    public static long sizeOf(byte val) {
        return SIZE_BYTE;
    }

    /**
     * Returns the number of bytes used by the primitive type argument.
     *
     * @param val the type
     * @return the number of bytes this type uses in memory
     */
    public static long sizeOf(char val) {
        return SIZE_CHAR;
    }

    /**
     * Returns the number of bytes used by the primitive type argument.
     *
     * @param val the type
     * @return the number of bytes this type uses in memory
     */
    public static long sizeOf(short val) {
        return SIZE_SHORT;
    }

    /**
     * Returns the number of bytes used by the primitive type argument.
     *
     * @param val the type
     * @return the number of bytes this type uses in memory
     */
    public static long sizeOf(int val) {
        return SIZE_INT;
    }

    /**
     * Returns the number of bytes used by the primitive type argument.
     *
     * @param val the type
     * @return the number of bytes this type uses in memory
     */
    public static long sizeOf(long val) {
        return SIZE_LONG;
    }

    /**
     * Returns the number of bytes used by the primitive type argument.
     *
     * @param val the type
     * @return the number of bytes this type uses in memory
     */
    public static long sizeOf(float val) {
        return SIZE_FLOAT;
    }

    /**
     * Returns the number of bytes used by the primitive type argument.
     *
     * @param val the type
     * @return the number of bytes this type uses in memory
     */
    public static long sizeOf(double val) {
        return SIZE_DOUBLE;
    }


    /**********************************************************************************
     **  Reference class
     **/

    /**
     * <b>A reference to an object.</b>
     * <p>
     * This class overrides standard object equality tests to
     * provide comparisons on object addresses rather than
     * object contents.
     * <p/>
     * @param <Type> the type of the object being referenced
     */
    public static class Ref<Type> {
        public final Type ref;

        public Ref(Type obj) { this.ref = obj; }

        public int hashCode() {
            return System.identityHashCode(ref);
        }

        @SuppressWarnings("unchecked")
        public boolean equals(Object obj) {
            return obj.getClass()==Ref.class && ref==((Ref)obj).ref;
        }

        public String toString() {
            String name = ref.getClass().getName();
            StringBuilder buf = new StringBuilder(name.length()+8);
            buf.append("#").append(name);
            buf.append("@").append(Integer.toHexString(System.identityHashCode(ref)));
            return buf.toString();
        }
    }


    /**********************************************************************************
     **  Memory footprint computation for generic objects
     **/

    private static final long SIZE_OBJ = 8;
    private static final long SIZE_REF = 4;


    /**
     * Computes the size of a particular class of objects, discarding any
     * contribution from objects already in the reference context.
     *
     * @param obj the object
     * @return the size, in bytes, of the object
     */
    public static long sizeOf(Object obj) {
        return sizeOf(obj, new HashSet<Ref<?>>());
    }


    /**
     * Computes the size of a particular class of objects, discarding any
     * contribution from objects already in the reference context.
     *
     * @param obj the object
     * @param ctx the reference context (or {@literal null} if no context should be used)
     * @return the size, in bytes, of the object
     */
    public static long sizeOf(final Object obj, final Set<Ref<?>> ctx) {
        if(obj==null) return 0;
        Class<?> cls = obj.getClass();
        if(cls==Class.class) return 0;
        if(cls.isPrimitive()) return sizeOfClass(cls);
        if(ctx!=null && !ctx.add(new Reflect.Ref<Object>(obj))) return 0;

        if(cls.isArray()) {
            cls = obj.getClass().getComponentType();
            if(cls==null) return 0; // This should not happen...
            int length = Array.getLength(obj);
            if(cls.isPrimitive()) {
                return SIZE_OBJ + sizeOfClass(Integer.TYPE) + length * sizeOfClass(cls);
            } else {
                long size = SIZE_OBJ + sizeOfClass(Integer.TYPE) + SIZE_REF * length;
                for(int i = 0; i < length; i++) {
                    size += sizeOf(Array.get(obj, i), ctx);
                }
                return size;
            }
        }

        long size = SIZE_OBJ;
        while(cls!=null && cls!=Object.class) {
            for(final Field fld: cls.getDeclaredFields()) {
                if((fld.getModifiers() & Modifier.STATIC) == 0) {
                    final Class<?> fcls = fld.getType();
                    if(fcls.isPrimitive()) {
                        size += sizeOfClass(fcls);
                    } else {
                        size += SIZE_REF;
                        // Use the doPrivileged method of the access controller
                        // to bypass the private and protected modifiers
                        Object val = java.security.AccessController.doPrivileged(
                                new java.security.PrivilegedAction<Object>() {
                                    public Object run() {
                                        try {
                                            fld.setAccessible(true);
                                            return fld.get(obj);
                                        }
                                        catch(Exception e) {
                                            return null;
                                        }
                                        finally {
                                            fld.setAccessible(false);
                                        }
                                    }
                                }
                        );
                        if(val!=null) size += sizeOf(val, ctx);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
        return size;
    }


    /**********************************************************************************
     **  Field class
     **/

    /**
     * An object's field specifications.
     *
     * @param <Type> the field type
     * @see Reflect#visit
     */
    public static class Fld<Type> {
        public final Class<Type> cls;
        public final String fld;
        public final Type val;

        public Fld(Class<Type> cls, String fld, Type val) {
            this.cls=cls;
            this.fld=fld;
            this.val=val;
        }

        /**
         * The field's class
         *
         * @return the class of the field
         */
        public Class<Type> fieldClass() { return cls; }

        /**
         * The field's name
         *
         * @return the name of the field
         */
        public String fieldName() { return fld; }

        /**
         * The field's value
         *
         * @return the value of the field
         */
        public Type fieldValue() { return val; }
    }


    /**********************************************************************************
     **  Object structure traversal
     **/

    /**
     * Calls a visitor for every field of an object, until the visitor returns a negative value.
     *
     * @param obj the object
     * @param vis the visitor
     * @param par a control parameter
     * @param <Par> the type of the control parameter
     *
     * @return the sum of all non-negative values returned by the visitor, or {@literal -1L} if an error occurred
     * @see Fld
     */
    @SuppressWarnings( { "unchecked" })
    public static <Par> long visit(final Object obj, final Visitable.Visitor<Fld<Object>,Par> vis, Par par) {
        if(obj==null) return -1L;
        Class ocl = obj.getClass();
        if(ocl==Class.class) return 0L;
        if(ocl.isPrimitive()) return 0L;
        long value = 0L;

        // Climb up the inheritance hierarchy
        while(ocl!=null && ocl!=Object.class) {
            // If object is an array, iterate through length, then all elements
            if(ocl.isArray()) {
                final int length = Array.getLength(obj);
                long v = vis.invoke(new Fld(int.class, "length", length), par);
                if(v<0) return value;
                value += v;
                Class cls = obj.getClass().getComponentType();
                if(cls==null) return -1L;
                for(int i=0; i<length; i++) {
                    Object val = Array.get(obj, i);
                    v = vis.invoke(new Fld(cls, "["+i+"]", val), par);
                    if(v<0) return value;
                    value += v;
                }
            }
            // If object is a normal object, iterate through declared fields
            else for(final Field fld: ocl.getDeclaredFields()) {
                if((fld.getModifiers() & Modifier.STATIC) == 0) {
                    Class cls = fld.getType();
                    // Use the doPrivileged method of the access controller
                    // to bypass the private and protected modifiers
                    Object val = java.security.AccessController.doPrivileged(
                            new java.security.PrivilegedAction<Object>() {
                                public Object run() {
                                    try {
                                        fld.setAccessible(true);
                                        return fld.get(obj);
                                    }
                                    catch(Exception e) {
                                        return null;
                                    }
                                    finally {
                                        fld.setAccessible(false);
                                    }
                                }
                            }
                    );
                    long v = vis.invoke(new Fld(cls, fld.getName(), val), par);
                    if(v<0) return value;
                    value += v;
                }
            }
            // Jump to superclass, and visit again
            ocl = ocl.getSuperclass();
        }
        return value;
    }

    /**********************************************************************************
     ** Generic signature resolvers
     **/

    /**
     * Extract the (potentially parameterized or variables) type parameters
     * with which a class implements a generic superclass or interface.
     *
     * @param klass     the implementing class
     * @param target    the target generic parameterizable class
     * @param <T>       the target generic type
     * @return  an array of Types for each generic parameter (possibly empty if there are none),
     * or {@literal null} if the provided class is not a subtype of the {@param target}
     */
    public static <T> Type[] getGenericParameters(Class<? extends T> klass, Class<T> target) {
        return getGenericParameters(klass, target, null);
    }

    /**
     * Extract the (potentially parameterized or variables) type parameters
     * with which a class implements a generic superclass or interface.
     * resolving specified type variable assignments if any are provided.
     *
     * @param klass     the implementing class
     * @param target    the target generic parameterizable class
     * @param assigns   a mapping between type variable names and actual types
     * @param <T>       the target generic type
     * @return  an array of resolved Types for each generic parameter (possibly empty if there are none),
     * or {@literal null} if the provided class is not a subtype of the {@param target}
     */
    @SuppressWarnings("unchecked")
    private static <T> Type[] getGenericParameters(Class<? extends T> klass, Class<T> target, Map<String,Type> assigns) {
        if(!target.isAssignableFrom(klass)) return null;
        if(target.equals(klass)) return resolveTypeVariables(klass.getTypeParameters(),assigns);

        Type sup = klass.getGenericSuperclass();
        Type[] ifs = klass.getGenericInterfaces();

        Type[] parents = new Type[1 + ifs.length];
        parents[0] = sup;
        System.arraycopy(ifs,0,parents,1,ifs.length);

        for(Type parent: parents) {
            if(parent instanceof Class) {
                if(target.isAssignableFrom((Class<?>) parent))
                    return resolveTypeVariables(((Class<?>) parent).getTypeParameters(),assigns);
            }
            if(parent instanceof ParameterizedType) {
                Type base = ((ParameterizedType) parent).getRawType();
                if(base instanceof Class && target.isAssignableFrom((Class<?>) base)) {
                    return resolveTypeVariables(
                            getGenericParameters((Class<? extends T>) base, target, assignTypeVariables((ParameterizedType) parent)),
                            assigns);
                }
            }
            // no other Type implementation should be found here, and if it is they have nothing to provide
        }
        return null;
    }

    private static Map<String,Type> assignTypeVariables(ParameterizedType type) {
        // There is no known case where getRawType() doesn't return a Class<?>...
        if(!(type.getRawType() instanceof GenericDeclaration)) return null;
        GenericDeclaration base = (GenericDeclaration) type.getRawType();
        TypeVariable<?>[] pars = base.getTypeParameters();
        // Get the specified values for the type parameters
        Type[] vals = type.getActualTypeArguments();
        // This may be empty, in which case we map to the type variables themselves
        if(vals==null || vals.length==0) vals = pars;
        else if(vals.length != pars.length) throw new IllegalArgumentException("Invalid type parameter count");
        // Map each variable to its inferred value (which may be class, a generic type or a variable itself)
        Map<String,Type> assigns = new HashMap<>(pars.length);
        for(int i=0; i<pars.length; i++) assigns.put(pars[i].getName(),vals[i]);
        return assigns;
    }

    private static Type[] resolveTypeVariables(Type[] params, Map<String,Type> assigns) {
        if(params==null || assigns == null) return params;
        // Make a copy of the array as a Type[], as a Class[] may actually have been provided
        params = Arrays.copyOf(params,params.length,Type[].class);
        for(int i=0; i<params.length; i++) {
            Type param = params[i];
            if(param instanceof TypeVariable) {
                TypeVariable<?> var = (TypeVariable<?>) param;
                // Resolve each variable to its assigned value, if one is defined
                if(assigns.containsKey(var.getName())) params[i] = assigns.get(var.getName());
            }
        }
        return params;
    }

    /**
     * Return a type's base class (i.e. raw type), resolving through parametrized and array types.
     *
     * @param type the type to get a concrete class for
     * @param <P> the target Object type the type is expected to resolve to
     * @return the direct class the specified type is a generic parametrization of
     * or {@literal null} if the Type has no defined base type (e.g. is a wildcard or a type variable).
     */
    @SuppressWarnings("unchecked")
    public static <P> Class<P> getBaseClass(Type type) {
        if(type instanceof Class) return (Class<P>) type;
        if(type instanceof ParameterizedType)
            return getBaseClass(((ParameterizedType) type).getRawType());
        if(type instanceof GenericArrayType)
            return (Class<P>) Array.newInstance(getBaseClass(((GenericArrayType) type).getGenericComponentType()),0);
        // Any other implementation does not have a defined base type,
        // but only bounds that may not resolve to any existing class, or resolve to multiple classes
        // if(type instanceof WildcardType || type instanceof TypeVariable) return null;
        return null;
    }

}
