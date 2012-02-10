/**
 *
 */
package net.varkhan.base.containers.queue;

import net.varkhan.base.containers.Iterator;

import java.util.NoSuchElementException;


/**
 * <b>A generic array-backed bounded FIFO queue.</b>
 * <p/>
 * This class uses a fixed-size backing array to provide the semantics of a FIFO
 * queue. Elements are added at the tail of the queue, and are removed from the
 * head.
 * <p/>
 * When the number of elements reaches the size of the backing array, adding an
 * element triggers the discarding of the head (fist-added) element, as if it
 * had been normally removed.
 * <p/>
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 8:03:07 AM
 */
public class BoundedBuffer<Type> implements Queue<Type> {

    /**
     * The heap array.
     */
    private final Object[] heap;
    private final int      bound;

    /**
     * The number of elements in this queue.
     */
    private int size=0;

    /**
     * The starting point of the queue in the array.
     */
    private int base=0;


    /**********************************************************************************
     **  Constructors
     **/


    /**
     * Creates a new empty queue with a given capacity.
     *
     * @param bound the capacity, and maximum size, of this queue.
     */
    public BoundedBuffer(int bound) {
        this.bound=bound;
        this.heap=new Object[bound];
    }


    /**********************************************************************************
     **  Global information accessors
     **/

    /**
     * Return the queue size
     *
     * @return the number of objects in the queue
     */
    public long size() {
        return size;
    }

    /**
     * Indicates whether this container is empty.
     *
     * @return {@literal true} if this container contains no entry,
     *         {@literal false} otherwise
     */
    public boolean isEmpty() {
        return size<=0;
    }

    /**
     * Return the maximum queue size
     *
     * @return the maximum size of this queue
     */
    public int bound() {
        return bound;
    }

    /**
     * Removes all objects from the queue
     */
    public final void clear() {
        size=0;
        base=0;
    }


    /**********************************************************************************
     **  Queue mechanism
     **/

    /**
     * Adds an object on the queue.
     *
     * @param obj the object to add
     *
     * @return {@literal true}
     */
    public final boolean add(Type obj) {
        if(heap.length==0) return true;
        int pos=base+size;
        if(pos<heap.length) {
            heap[pos]=obj;
            size++;
        }
        else {
            pos-=heap.length;
            if(pos==base) base++;
            else if(pos<base) size++;
                // This one should never happen: it would mean size>heap.length
            else {
                size=heap.length;
                base=pos+1;
            }
        }
        return true;
    }

    /**
     * Removes and return the smallest object on the queue
     *
     * @return the smallest object on the queue
     */
    public final Type poll() {
        if(size==0) throw new NoSuchElementException();
        @SuppressWarnings("unchecked")
        final Type obj=(Type) heap[base];
        heap[base]=null;
        base++;
        if(base>=heap.length) base=0;
        size--;
        return obj;
    }

    /**
     * Returns, without removing it, the smallest object on the queue
     *
     * @return the smallest object on the queue
     */
    @SuppressWarnings("unchecked")
    public final Type peek() {
        if(size==0) throw new NoSuchElementException();
        return (Type) heap[base];
    }


    /**********************************************************************************
     **  Iterator operations
     **/


    /**
     * Returns an element at a given position
     *
     * @param pos the position in the queue
     *
     * @return the element at position {@code pos}
     */
    @SuppressWarnings("unchecked")
    public Type get(int pos) {
        if(pos>=size) throw new ArrayIndexOutOfBoundsException(pos);
        pos+=base;
        if(pos<heap.length) return (Type) heap[pos];
        else return (Type) heap[pos-heap.length];
    }


    /**
     * Returns an iterator over the elements of this queue
     *
     * @return an Iterator
     */
    public Iterator<Type> iterator() {
        return new Iterator<Type>() {
            private int pos=0;

            public boolean hasNext() {
                return pos<size;
            }

            @SuppressWarnings("unchecked")
            public Type next() {
                if(base+pos<heap.length) return (Type) heap[base+pos];
                else return (Type) heap[base+pos-heap.length];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Iterate over each element of the queue, and pass it as argument to a
     * visitor's {@link Visitor#visit(Type, Par)} method, until this method returns
     * a negative count.
     *
     *
     * @param vis the visitor
     *
     * @param par
     * @return the sum of all positive return values from the visitor
     */
    @SuppressWarnings("unchecked")
    public <Par> long visit(Visitor<Type,Par> vis, Par par) {
        int pos=0;
        long c=0;
        while(pos<size) {
            Type obj;
            if(base+pos<heap.length) obj=(Type) heap[base+(pos++)];
            else obj=(Type) heap[base+(pos++)-heap.length];
            long r=vis.invoke(obj, par);
            if(r<0) return c;
            c+=r;
        }
        return c;
    }

    /**
     * Returns a string representation of the queue
     *
     * @return the string representation of the array of values in the queue
     */
    public String toString() {
        return net.varkhan.base.containers.array.StringArrays.toString(heap);
    }


}
