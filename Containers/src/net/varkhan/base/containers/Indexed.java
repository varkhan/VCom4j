/**
 *
 */
package net.varkhan.base.containers;

/**
 * <b>A container whose elements can be retrieved through a {@code long} index.</b>
 * <p/>
 * This container stores objects, that are associated to a unique index through
 * which they can be retrieved. An {@link Index} allows access to all the valid
 * indexes in the container.
 * <p/>
 *
 * @param <Type> the type of the objects stored in the container
 *
 * @author varkhan
 * @date Mar 11, 2009
 * @time 1:19:16 AM
 */
public interface Indexed<Type> extends Indexable {

    /**********************************************************************************
     **  Container statistics accessors
     **/

    /**
     * Returns the number of elements in this container.
     *
     * @return the number of entries (elements and related indexes) stored in this list
     */
    public long size();

    /**
     * Indicates whether this container is empty.
     *
     * @return {@literal true} if this container contains no element,
     *         {@literal false} otherwise
     */
    public boolean isEmpty();

    /**
     * Returns an upper bound of valid indexes in this container.
     * <p/>
     * Implementors should return a value that is not unnecessarily large,
     * such as one above the actual greatest used index.
     *
     * @return an integer strictly greater than any valid index
     *         associated to a stored object in the container
     */
    public long head();


    /**********************************************************************************
     **  Container elements accessors
     **/

    /**
     * Indicates whether an index is valid (has an associated element).
     *
     * @param index a unique identifier
     *
     * @return {@literal true} if an element is available at this index,
     *         or {@literal false} if no element is available at this index
     */
    public boolean has(long index);

    /**
     * Obtains the element designated by an index.
     *
     * @param index a unique identifier
     *
     * @return the requested object, or {@literal null} if no element is available at this index
     */
    public Type get(long index);


    /**********************************************************************************
     **  Container elements iterators
     **/

    /**
     * Returns an {@link Index} over the indexes in the container.
     *
     * @return an {@code Index} enumerating all indexes in the container
     */
    public Index indexes();

    /**
     * Iterates over all indexes in the container.
     *
     * @return an iterable over all the indexes that designate elements in the container
     */
    public java.lang.Iterable<Long> iterateIndexes();

}
