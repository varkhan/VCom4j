/**
 *
 */
package net.varkhan.base.extensions;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

/**
 * <b>A hierarchic version of the Enum class.</b>
 * <br/>
 *
 * @author varkhan
 * @date May 12, 2009
 * @time 9:06:44 PM
 */
public abstract class Enumerated<Base extends Enumerated<Base>> implements Serializable, Label<Base>, Comparable<Enumerated<Base>> {

    private static final long serialVersionUID = 1L;
    private static final Class<?> BASE = Enumerated.class;
    private static final String BASE_NAME = BASE.getSimpleName();

    /**********************************************************************************
     **  @Constant annotation
     **/

    /**
     * This Annotation is used to mark an Enumerated constant field
     */
    @Target(ElementType.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public static @interface Constant { }




    /**********************************************************************************
     **  Exceptions
     **/


    /**
     * An InitializationError is thrown when initialization of an Enumerated type
     * fails for some reason.
     */
    public static class InitializationError extends RuntimeException {
        /** The Serial Version UID */
        private static final long serialVersionUID = 1L;
		/** The source Enumerated type for this exception */
        private final Class<? extends Enumerated<?>> enumclass;

        /**
         * Constructs a new Enumerated InitializationError with {@literal null} as its
         * detail message.
         * @param enumclass the source Enumerated type
         */
        public InitializationError(Class<? extends Enumerated<?>> enumclass) {
            super();
            this.enumclass = enumclass;
        }

        /**
         * Constructs a new Enumerated InitializationError with the specified detail message.
         * The cause is not initialized, and may subsequently be initialized by a
         * call to {@link #initCause}.
         *
         * @param enumclass the source Enumerated type
         * @param message the detail message. The detail message is saved for
         *                later retrieval by the {@link #getMessage()} method.
         */
        public InitializationError(Class<? extends Enumerated<?>> enumclass, String message) {
            super(message);
            this.enumclass = enumclass;
        }

        /**
         * Constructs a new Enumerated InitializationError with the specified detail message and
         * cause.
         *
         * @param enumclass the source Enumerated type
         * @param message the detail message (which is saved for later retrieval
         *                by the {@link #getMessage()} method).
         * @param cause   the cause (which is saved for later retrieval by the
         *                {@link #getCause()} method).  (A {@literal null} value is
         *                permitted, and indicates that the cause is nonexistent or
         *                unknown.)
         */
        public InitializationError(Class<? extends Enumerated<?>> enumclass, String message, Throwable cause) {
            super(message, cause);
            this.enumclass = enumclass;
        }

        /**
         * Constructs a new runtime exception with the specified cause and a
         * detail message of {@code (cause==null ? null : cause.toString())}
         * (which typically contains the class and detail message of
         * {@code cause}).
         *
         * @param enumclass the source Enumerated type
         * @param cause the cause (which is saved for later retrieval by the
         *              {@link #getCause()} method).  (A {@literal null} value is
         *              permitted, and indicates that the cause is nonexistent or
         *              unknown.)
         */
        public InitializationError(Class<? extends Enumerated<?>> enumclass, Throwable cause) {
            super(cause);
            this.enumclass = enumclass;
        }

        /**
         * Returns the Enumerated type at the origin of this exception.
         *
         * @return the source Enumerated type for this exception
         */
        public Class<? extends Enumerated<?>> getEnumeratedClass() {
            return enumclass;
        }
    }



    /**********************************************************************************
     **  Enumeration Class instance methods
     **/


    @SuppressWarnings({"hiding", "rawtypes"})
    public static final class Type<Base extends Enumerated<Base>, Enum extends Base> {

        /**
         * The map of Enumeration constants array for each Enumeration type class
         */
        private static final Map<Class<? extends Enumerated<?>>, Type<?,? extends Enumerated<?>>> instanceMap =
                new HashMap<Class<? extends Enumerated<?>>, Type<?,? extends Enumerated<?>>>();


        /** The Base type for this type instance */
        transient volatile private Class<Base> base = null;

        /** The Enumeration type for this type instance */
        transient volatile private Class<Enum> type = null;

        /** The map of Enumeration constants array for this Enumeration type */
        transient volatile private Enumerated<Base>[] values = null;

        /**
         * Returns the array of all Enumeration constant for this Enumeration type
         *
         * @return the array of all Enumeration constants of this Enumeration type
         */
        public Enumerated<Base>[] values() {
            return values;
        }


        /************************
         **  Constants initializer
         **/

        /**
         * Initializes a constant for the specified Enumeration type class, and adds
         * it in the global lookup tables.
         *
         * @param cons the Enumeration constant
         */
        @SuppressWarnings("unchecked")
        private void addConstant(Enum cons) {
            if(cons == null) throw new NullPointerException(BASE_NAME+" constant is null");
            synchronized(this) {
                // First initialization for this class
                if(values==null) {
                    Class<Enumerated> parnt = (Class<Enumerated>) type.getSuperclass();
                    if(parnt != BASE) {
                    	Type<Base,?> inst = (Type<Base,?>) instanceMap.get(parnt);
                    	if(inst!=null) values = inst.values;
                    }
                    // Fall-through if we can use the parent's values
                }
                if(values==null) {
            		cons.ordinal = 0;
            		values = new Enumerated[1];
            		values[0] = cons;
            		return;
            	}
                // Add to an already initialized class
                for(Enumerated c: values) if (c.name.equals(cons.name))
                    throw new InitializationError(type,
                            BASE_NAME+" type "+type+" has already a constant named "+cons.name
                    );
                int valnum = values.length;
                cons.ordinal = valnum;
                Enumerated[] enums = new Enumerated[valnum+1];
                System.arraycopy(values,0,enums,0,valnum);
                enums[valnum] = cons;
                values = enums;
            }
        }



        /************************
         **  Type instance initializer
         **/

        /**
         * Checks whether an uninitialized Enumeration constant actually extends its Enumeration type
         *
         * @param cons the Enumeration constant
         * @param type the Enumeration type
         * @return {@code true} if this constant extends {@code type}
         */
        private static <E extends Enumerated<? super E>> boolean isSubClass(E cons, Class<E> type) {
            Class<?> klass = cons.getClass();
            while(klass!=BASE) {
                if(klass==type) return true;
                klass = klass.getSuperclass();
            }
            return false;
        }

        /**
         * Initializes an Enumeration type instance, extracting annotated fields into the Enumeration type
         * constant array.
         *
         * @param type the Enumeration type
         */
        @SuppressWarnings("unchecked")
        private Type(Class<Enum> type) {
            // If we are already initialized, return immediately
            if(values!=null) return;
            synchronized(instanceMap) {
                if(instanceMap.get(type)!=null)
                    throw new InitializationError(type,
                            BASE_NAME+" type "+type+" has already been initialized"
                    );
                this.type = type;
                instanceMap.put(type,this);
                Class<? extends Enumerated> parnt = (Class<? extends Enumerated>) type.getSuperclass();
                if(parnt == BASE) {
                	this.values = new Enumerated[0];
                    this.base = (Class<Base>) type;
                }
                else {
                	Type<Base,?> inst = (Type<Base,?>) instanceMap.get(parnt);
                    if(inst==null) throw new InitializationError(type,
                            type+" does not extend a properly initialized "+BASE_NAME+" type"
                    );
                    this.values = inst.values;
                	this.base = inst.base;
                }
                Field[] fields = type.getDeclaredFields();
                for(final Field f: fields) {
                    if(Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers())) {
                        final Constant ann = f.getAnnotation(Constant.class);
                        if(ann!=null) {
                            java.security.AccessController.doPrivileged(
                                    (PrivilegedAction) () -> {
                                        f.setAccessible(true);
                                        return null;
                                    }
                            );
                            try {
                                final Enum cons = (Enum)f.get(null);
                                if(cons!=null) {
                                    if(cons.type!=null)
                                        // Check if the constant extends this type at a given level
                                        if(!isSubClass(cons,type))
                                            throw new InitializationError(type,
                                                    BASE_NAME+" constant "+type+"."+f.getName()+" does not extend "+type
                                            );
                                    // Initialize constant and adds it
                                    cons.type = type;
                                    cons.name = f.getName();
                                    this.addConstant(cons);
                                }
                            }
                            catch(IllegalAccessException e) {
                                throw new InitializationError(type,
                                        BASE_NAME+" constant "+type+"."+f.getName()+" is declared private"
                                );
                            }
                        }
                    }
                }
            }
        }


        /************************
         **  Name-based lookup
         **/

        transient volatile private Map<String, Enumerated<? extends Base>> valuemap = null;

        /**
         * Returns the Enumeration constant of this Enumeration type having the specified name.
         *
         * The name must match exactly an identifier used to declare an Enum
         * constant in this type. (Extraneous whitespace characters are not permitted.)
         *
         * @param name the name of the constant to return
         * @return the Enumeration constant of this Enumeration type, having the
         *         specified name
         * @throws IllegalArgumentException if this Enumeration type has
         *         no constant with the specified name, or the specified
         *         class object does not represent an Enumeration type
         * @throws NullPointerException if {@code name} is {@code null}
         */
        public Enumerated<? extends Base> valueOf(String name) {
            if(name==null)
                throw new NullPointerException(BASE_NAME +" name is null");
            if(name.length()==0)
                throw new IllegalArgumentException(BASE_NAME+" name is empty");
            if(valuemap==null) synchronized(this) {
            	// Lazy initialization
                valuemap = new HashMap<String, Enumerated<? extends Base>>(values.length);
                for(Enumerated<Base> cons: values) {
                    valuemap.put(cons.name,cons);
                }
            }
            Enumerated<? extends Base> cons = valuemap.get(name);
            if(cons==null) throw new IllegalArgumentException(BASE_NAME+" const not found "+type +"."+name);
            return cons;
        }


    }



    /**********************************************************************************
     **  Enumeration constant
     **/

    /**
     * This default constructor let Enumeration constants be derived in Enumeration sub-types.
     */
    public Enumerated() {}


    /************************
     **  Constant fields and accessors
     **/

    /**
     * The name of this Enumeration constant, as declared in the Enumeration type.
     *
     * This field is effectively final, but has a default value to avoid compiler complaints.
     */
    protected String name = null;

    /**
     * The ordinal of this Enumeration constant (its position in the Enumeration type,
     * where the initial constant is assigned an ordinal of zero).
     *
     * This field is effectively final, but has a default value to avoid compiler complaints.
     */
    protected int ordinal = -1;


    /**
     * The Enumeration type declaring class of this constant
     *
     * This field is effectively final, but has a default value to avoid compiler complaints.
     */
    protected Class<? extends Enumerated<Base>> type = null;


    /**
     * Returns the name of this Enumeration constant, as declared in its source Enumeration type.
     *
     * @return the name of this Enumeration constant
     */
    public final String name() {
        return name;
    }

    /**
     * Returns the ordinal of this constant (its position in the Enumeration type,
     * where the initial constant is assigned an ordinal of zero).
     *
     * @return the ordinal of this Enumeration constant
     */
    public final int ordinal() {
        return ordinal;
    }


    /************************
     **  Type checking
     **/

    /**
     * Returns the Enumeration type class of this constant
     *
     * @return the Enumeration type class
     */
    public final Class<? extends Enumerated<Base>> getEnumType() {
        return type;
    }

    /**
     * Returns the base Enumeration (topmost concrete ancestor) type class of this constant
     *
     * @return the base Enumeration type class
     */
    @SuppressWarnings("unchecked")
    public final Class<? extends Enumerated<Base>> getBaseType() {
        Class<? extends Enumerated<Base>> thisclass = type;
        while(thisclass != null && thisclass.getSuperclass() != BASE) {
            thisclass = (Class<? extends Enumerated<Base>>) thisclass.getSuperclass();
        }
        return thisclass;
    }

    /**
     * Indicates whether this Enumeration type extends an other Enumeration type.
     *
     * @param thatclass the Enumeration type class this Enumeration constant type may extend
     * @return {@code true} iff {@code this.getEnumType()} is a concrete subclass
     * 	       of {@code thatclass}
     * @see #getEnumType()
     */
    @SuppressWarnings({"unchecked"})
    public final boolean isSubType(Class<? super Enumerated<Base>> thatclass) {
        Class<? extends Enumerated<Base>> thisclass = this.getEnumType();
        while(thisclass != null && thisclass != BASE) {
            if(thisclass == thatclass) return true;
            thisclass = (Class<? extends Enumerated<Base>>) thisclass.getSuperclass();
        }
        return false;
    }

    /**
     * Indicates whether this Enumeration type is extended by an other Enumeration type.
     *
     * @param thatclass the Enumeration type class that may extend this Enumeration constant type
     * @return {@code true} iff {@code this.getEnumType()} is a concrete superclass
     *         of {@code thatclass}
     * @see #getEnumType()
     */
    @SuppressWarnings({"unchecked"})
    public final boolean isSuperType(Class<? extends Enumerated<Base>> thatclass) {
        while(thatclass != null && thatclass != BASE) {
            if(thatclass == this.getEnumType()) return true;
            thatclass = (Class<? extends Enumerated<Base>>) thatclass.getSuperclass();
        }
        return false;
    }

    /**
     * Returns the most specific common super-type of the Enumeration type of this
     * constant, and an other.
     *
     * @param thatclass an Enumeration type class
     * @return the most specific (lowest in inheritance hierarchy) common
     * concrete superclass of the Enumeration type class for this constant, and the argument.
     */
    @SuppressWarnings({"unchecked"})
    public final Class<? extends Enumerated<Base>> getCommonSuperType(Class<? extends Enumerated<Base>> thatclass) {
        while(thatclass != BASE) {
            Class<? extends Enumerated<Base>> superclass = this.getEnumType();
            while(superclass != BASE) {
                if(superclass == thatclass) return thatclass;
                superclass = (Class<? extends Enumerated<Base>>) superclass.getSuperclass();
            }
            thatclass = (Class<? extends Enumerated<Base>>) thatclass.getSuperclass();
        }
        return null;
    }



    /************************
     **  Constant comparison
     **/


    /**
     * Returns {@code true} if the specified object is equal to this
     * Enumeration constant.
     *
     * @param that the object to be compared for equality with this object.
     * @return {@code true} if the specified object is equal to this
     *         Enumeration constant.
     */
    public final boolean equals(Object that) {
        return this==that;
    }

    /**
     * Returns a hash code for this Enumeration constant.
     *
     * @return a hash code for this Enumeration constant.
     */
    public final int hashCode() {
        return super.hashCode();
    }

    /**
     * Compares this Enumeration constant with the specified object for order.
     *
     * @param that the other object to compare with
     * @return a negative integer, zero, or a positive integer as this Enumeration
     *         constant is less than, equal to, or greater than the specified
     *         Enumeration constant, according to the order in which the constants
     *         have been declared.
     * @throws ClassCastException when trying to compare Enumeration constant of incompatible
     *         Enumeration types (that have no direct inheritance relation).
     */
    public final int compareTo(Enumerated<Base> that) throws ClassCastException {
        if (this.type==that.type || this.getBaseType()==that.getBaseType())
        	return this.ordinal-that.ordinal;
        // If no direct inheritance relation exists, these classes are incompatible
        throw new ClassCastException(
        		BASE_NAME+" types "+
        		this.type.getSimpleName()+" and "+that.type.getSimpleName()+
        		" are not comparable"
        );
    }



    /************************
     **  Constant specific overrides
     **/

    /**
     * Throws {@code CloneNotSupportedException}. This guarantees that Enumeration
     * constants are never cloned, which is necessary to preserve their
     * "singleton" status.
     *
     * @return (never returns)
     */
    protected final Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Enumeration constants cannot have finalize methods.
     */
    protected final void finalize() {
    }

    /**
     * Returns the name of this Enumeration constant, as contained in the declaration.
     * An Enumeration type should override this method when a more
     * "programmer-friendly" string form exists.
     *
     * @return the name of this Enumeration constant
     */
    public String toString() {
        return name;
    }



    /************************
     **  Serialization management
     **/


    /**
     * Ensures the Enumeration constant only writes its source and ordinal
     * when serializing.
     *
     * @param out the stream to write the object to
     * @throws java.io.IOException includes any I/O exceptions that may occur
     * @serialData the ordinal of the Enumeration constant
     */
    protected final void writeExternal(ObjectOutput out) throws IOException {
        out.writeObject(type);
        out.writeInt(ordinal);
    }

    /**
     * Only deserialize the source and ordinal of the Enumeration constant
     *
     * @param in the stream to read data from in order to restore the object
     * @throws java.io.IOException    if an I/O error occurs
     * @throws ClassNotFoundException if the class for an object being
     *                                restored cannot be found.
     */
    @SuppressWarnings("unchecked")
    protected final void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        type = (Class<Enumerated<Base>>)in.readObject();
        ordinal = in.readInt();
    }

    /**
     * Returns alternate deserialized Enumeration constant to ensure singleton
     * status of deserialized constants.
     *
     * @return the actual Enumeration constant for the deserialized data
     * @throws java.io.InvalidObjectException if the deserialized data is invalid for this Enumeration type
     */
    protected final Object readResolve() throws InvalidObjectException {
    	Type<?,?extends Enumerated<?>> inst = Type.instanceMap.get(type);
    	if(inst==null || inst.values==null || ordinal<0 || ordinal>= inst.values.length) {
    		throw new InvalidObjectException(
    				BASE_NAME+" class "+ type +" has no constant with ordinal "+ordinal
    		);
    	}
        return inst.values[ordinal];
    }





}
