/**
 *
 */
package net.varkhan.base.containers.queue;

import net.varkhan.base.containers.Iterator;

import java.util.NoSuchElementException;


/**
 * <b>A generic array-backed unbounded FIFO queue.</b>
 * <p/>
 * This class uses an extensible backing array to provide the semantics of a FIFO
 * queue. Elements are added at the tail of the queue, and are removed from the
 * head.
 * <p/>
 * When the number of elements reaches the size of the backing array, the array
 * is expanded. It does not shrink when elements are removed, but can be made to
 * fit the number of elements in the queue by calling the {@link #pack()} method.
 * <p/>
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 8:02:11 AM
 */
public class OpenBuffer<Type> implements Queue<Type> {
    private static final int DEFAULT_CAPACITY=8;

    /**
     * The heap array growth factor
     */
    private final float growthfactor;

    /**
     * The heap array.
     */
    private Object[] heap;

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
     * Creates a new empty queue with a given capacity
     *
     * @param capacity     the initial capacity of this queue.
     * @param growthfactor the multiplicative resizing factor for the queue storage array
     */
    public OpenBuffer(int capacity, float growthfactor) {
        if(growthfactor<1.0f) growthfactor=1.0f;
        this.growthfactor=growthfactor;
        this.heap=new Object[capacity];
    }

    /**
     * Creates a new empty queue with a given capacity and using the natural order.
     *
     * @param capacity the initial capacity of this queue.
     */
    public OpenBuffer(int capacity) {
        this.growthfactor=1.5f;
        this.heap=new Object[capacity];
    }

    /**
     * Creates a new empty queue with a given capacity and using the natural order.
     */
    public OpenBuffer() {
        this.growthfactor=1.5f;
        this.heap=new Object[DEFAULT_CAPACITY];
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
     * Removes all objects from the queue
     */
    public final void clear() {
        size=0;
        base=0;
    }

    /**
     * Resize the internal storage array(s) so that this object takes the minimal space possible
     */
    public void pack() {
        if(heap.length>size) {
            int end=base+size;
            if(end<=heap.length) {
                Object[] newheap=new Object[size];
                System.arraycopy(heap, base, newheap, 0, size);
                heap=newheap;
            }
            else {
                Object[] newheap=new Object[size];
                System.arraycopy(heap, base, newheap, 0, heap.length-base);
                System.arraycopy(heap, 0, newheap, heap.length-base, end-heap.length);
                heap=newheap;
            }
            base=0;
        }
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
        int pos=base+size;
        if(pos<heap.length) {
            heap[pos]=obj;
            size++;
        }
        else if(size<heap.length) {
            heap[pos-heap.length]=obj;
            size++;
        }
        else {
            Object[] newheap=new Object[(int) ((size+1)*growthfactor)];
            System.arraycopy(heap, base, newheap, 0, heap.length-base);
            System.arraycopy(heap, 0, newheap, heap.length-base, pos-heap.length);
            heap=newheap;
            base=0;
            heap[size++]=obj;
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
                if(base+pos<heap.length) return (Type) heap[base+(pos++)];
                else return (Type) heap[base+(pos++)-heap.length];
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /**
     * Iterate over each element of the queue, and pass it as argument to a
     * visitor's {@link Visitor#invoke(Object, Object)} method, until this method returns
     * a negative count.
     *
     *
     * @param vis the visitor
     * @param par the control parameter
     *
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
        return net.varkhan.base.containers.array.Arrays.toString(heap);
    }


}
