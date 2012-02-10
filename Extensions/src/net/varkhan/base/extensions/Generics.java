/**
 *
 */
package net.varkhan.base.extensions;

/**
 * <b>Extensions to Generics behavior.</b>
 * <p/>
 * Adds several extensions to define generic types, and tools to manipulate them.
 * <p/>
 * @author varkhan
 * @date Jun 25, 2009
 * @time 9:17:15 PM
 */
public class Generics {

    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    private Generics() {
    }



    /*********************************************************************************
     **  Strong-type Generic interface
     **/

    /**
     * <b>A Generic type signature.</b>
     * <p/>
     * Adds strong type-checking to the weak typing mechanism of generics,
     * through a linked-list definition of the type arguments of the generic
     * definition.
     * <p/>
     * @author varkhan
     * @date Jun 25, 2009
     * @time 9:34:33 PM
     * @param <T>
     * @param <N>
     */
    public static interface Type<T,N extends Type<?,?>> extends Typed<Class<T>> {

        /**
         * Gets the first type parameter of the generic definition.
         *
         * @return the first type parameter
         */
        public Class<T> type();

        /**
         * Returns a trailing type definition, specifying all the types in the definition but the first.
         *
         * @return the trailing types
         */
        public N types();
    }

    public static interface Type1<T1> extends Type<T1,Type<?,?>> {
        public Class<T1> type();
        public Type<?,?> types();
    }

    public static interface Type2<T1,T2> extends Type<T1,Type1<T2>> {
        public Class<T1> type();
        public Type1<T2> types();
    }

    public static interface Type3<T1,T2,T3> extends Type<T1,Type2<T2,T3>> {
        public Class<T1> type();
        public Type2<T2,T3> types();
    }

    public static interface Type4<T1,T2,T3,T4> extends Type<T1,Type3<T2,T3,T4>> {
        public Class<T1> type();
        public Type3<T2,T3,T4> types();
    }

    public static interface Type5<T1,T2,T3,T4,T5> extends Type<T1,Type4<T2,T3,T4,T5>> {
        public Class<T1> type();
        public Type4<T2,T3,T4,T5> types();
    }


    /*********************************************************************************
     **  Strong-type signature checking methods
     **/

    /**
     * Checks a strongly-typed generic for type compatibility.
     *
     * @param obj the strongly-typed generic object to check
     * @param sig a type signature array to check against
     * @return {@literal true} if and only if:
     * <li/> the object generic definition has the same number of elements than the target signature
     * <li/> every class in the object generic definition is assignable to the matching class in the signature
     * @see {@link Class#isAssignableFrom(Class)}
     */
    public static boolean isinstanceof(Type<?,?> obj, Class<?>... sig) {
        for(Class<?> type: sig) {
            if(obj==null || !type.isAssignableFrom(obj.type())) return false;
            obj = obj.types();
        }
        // Check that we are at the end of the generic definition
        return obj==null;
    }

    /**
     * Checks a strongly-typed generic for type compatibility.
     *
     * @param obj the strongly-typed generic object to check
     * @param sig a generic type signature to check against
     * @return {@literal true} if and only if:
     * <li/> the object generic definition has the same number of elements than the target signature
     * <li/> every class in the object generic definition is assignable to the matching class in the signature
     * @see {@link Class#isAssignableFrom(Class)}
     */
    public static boolean isinstanceof(Type<?,?> obj, Type<?,?> sig) {
        while(sig!=null && obj!=null) {
            if(!sig.type().isAssignableFrom(obj.type())) return false;
            obj = obj.types();
            sig = sig.types();
        }
        // Check that we are at the end of the generic definition AND the signature
        return sig==null && obj==null;
    }

}
