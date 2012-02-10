/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.HashingStrategy;


/**
 * <b>A coordinated pair of hash-code and equality computing algorithms.</b>
 * <p/>
 * Implementations of this interface provide a method testing for equality of two
 * integers, and a method to compute hash-codes for integers, that returns a unique
 * value for all integers that test equal.
 * <p/>
 *
 * @author varkhan
 * @date Mar 25, 2009
 * @time 11:49:49 PM
 */
public interface IntHashingStrategy extends HashingStrategy<Integer> {

    /**
     * Compute the hash code of an object.
     * <p/>
     * Whenever this method is invoked on the same object more than once during
     * an execution of a Java application, it must consistently return the same
     * value, provided no information used in equals comparisons on the object
     * is modified.
     * <p/>
     * If two objects are equal according to the {@link #equal(Object, Object)}
     * method, then calling this method on each of the two objects must produce
     * the same result.
     *
     * @param o the object
     *
     * @return the object's hash code
     */
    public long hash(int o);

    /**
     * Indicates whether two objects are equal to each other.
     * <p/>
     * This method implements an equivalence relation on object references, which
     * means it is:
     * <ul>
     * <li> reflexive: for any reference value {@code x},
     * {@code equals(x, x)} must return {@literal true}.
     * <li> symmetric: for any reference values {@code x} and {@code y},
     * {@code equals(x, y)} must return {@literal true} if and only if equals(y,x) returns {@literal true}.
     * <li> transitive: for any reference values {@code x}, {@code y}, and {@code z},
     * if {@code equals(x, y)} returns {@literal true} and {@code equals(y, z)} returns {@literal true}, then {@code equals(x, z)} must return {@literal true}.
     * <li> consistent: for any reference values {@code x} and {@code y},
     * multiple invocations of {@code equals(x, y)} consistently return {@literal true} or consistently return {@literal false}, provided no information in the objects is modified.
     * </ul>
     * {@code equals(null, null)} must return {@literal true}, and for any non-null reference value {@code x}, {@code equals(x, null)} must return {@literal false}.
     *
     * @param o1 an object to compare
     * @param o2 an other object to compare
     *
     * @return {@literal true} if the objects are equal
     */
    public boolean equal(int o1, int o2);

    /**
     * The default hashing strategy, that use the default {@link #hashCode()} and {@link #equals(Object)} methods.
     */
    public static final IntHashingStrategy DefaultHashingStrategy=new IntHashingStrategy() {
        private static final long serialVersionUID=1L;

        public long hash(Integer o) { return o.intValue(); }

        ;

        public long hash(int o) { return o; }

        ;

        public boolean equal(Integer o1, Integer o2) {
            return (o1==null&&o2==null)||(o1!=null&&o2!=null&&o1.intValue()==o2.intValue());
        }

        ;

        public boolean equal(int o1, int o2) { return o1==o2; }

        ;
    };

}
