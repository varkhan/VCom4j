package net.varkhan.base.functor;

/**
 * <b>A class parametrized by an arbitrary type sequence</b>.
 * <p/>
 * This is a marker interface that uses type parameter recursion to allow
 * a type to have an arbitrary sequences of types as parameter.
 * <p/>
 * A typical use case would be to signify that a class has a left and a right
 * type parameter, such as:
 * <pre>
 *     class P&lt;L,R&gt; extends _&lt;L,R&gt; { ... }
 * </pre>
 * This can also be used to specify a single type:
 * <pre>
 *     class S&lt;L,_&gt; extends _&lt;L,_&gt; { ... }
 * </pre>
 * A more complex example involves currying:
 * <pre>
 *     class T&lt;T1,T2,T3&gt; extends _&lt;T1,_&lt;T2,T3&gt&gt; { ... }
 * </pre>
 * If an arbitrary number of types needs to be specified, recursive currying
 * can be employed:
 * <pre>
 *     class U&lt;T1,_T2 extends _&gt; extends _&lt;T1,_T2&gt { ... }
 * </pre>
 *
 * @param <V> the left-side (first element) of the type sequence
 * @param <T> the tail-side (remaining elements) of the type sequence, as a sequence
 *
 * @author varkhan
 * @date 12/1/13
 * @time 12:35 PM
 */
public interface __<V,T extends __<?,?>> {

    /**
     * The first (left) value of the sequence.
     *
     * @return the value of the first element in the typed sequence
     */
    public V value();

    /**
     * The total number of elements in the sequence.
     *
     * @return the total size of elements in the typed sequence
     */
    public int size();

    /**
     * Whether this typed sequence has remaining (tail) elements.
     *
     * @return {@literal true} iff {@link #next()} would return a non-null value
     */
    public boolean hasNext();

    /**
     * The remaining (tail) section of the typed sequence.
     *
     * @return a typed sequence representing all but the first element in this sequence,
     * or {@literal null} if no more elements exist (i,e, this sequence has a single element)
     */
    public T next();

    /**
     * The value at the specified position in the typed sequence.
     *
     * @param i     the index at which to retrieve a value
     * @param <U>   the expected type of the value returned
     * @return the value at index {@code i}
     */
    public <U> U value(int i);

    /**
     * All the values in the sequence.
     *
     * @return an array containing all the values in this typed sequence.
     */
    public Object[] values();

}
