package net.varkhan.base.extensions.type;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
public final class Generics {
    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected Generics() { }


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
