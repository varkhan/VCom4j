/**
 *
 */
package net.varkhan.base.containers.queue;

import net.varkhan.base.containers.Iterator;

import java.util.Arrays;
import java.util.Comparator;
import java.util.NoSuchElementException;


/**
 * <b>A generic heap-based unbounded priority queue.</b>
 * <p/>
 * This class uses an extensible heap to provide the semantics of a priority
 * queue on objects, either naturally ordered as per the {@link Comparable}
 * interface, or ordered by a {@link Comparator}.
 * <p/>
 * When the number of elements equals the current size of the heap, the heap is
 * extended. It does not shrink when elements are removed, but can be made to
 * fit the number of elements in the queue by calling the {@link #pack()} method.
 * <p/>
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 8:02:11 AM
 */
public class OpenHeapQueue<Type> implements Queue<Type> {
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

    /**
     * The type-specific comparator used in this queue.
     */
    private final Comparator<? super Type> comp;


    /**********************************************************************************
     **  Constructors
     **/

    /**
     * Creates a new empty queue with a given capacity and comparator.
     *
     * @param capacity     the initial capacity of this queue.
     * @param growthfactor the multiplicative resizing factor for the queue storage array
     * @param comp         the comparator used in this queue, or {@literal null} for the natural order.
     */
    public OpenHeapQueue(int capacity, float growthfactor, Comparator<? super Type> comp) {
        if(growthfactor<1.0f) growthfactor=1.0f;
        this.growthfactor=growthfactor;
        this.heap=new Object[capacity];
        this.comp=comp;
    }

    /**
     * Creates a new empty queue with a given capacity and comparator.
     *
     * @param capacity the initial capacity of this queue.
     * @param comp     the comparator used in this queue, or {@literal null} for the natural order.
     */
    public OpenHeapQueue(int capacity, Comparator<? super Type> comp) {
        this.growthfactor=1.5f;
        this.heap=new Object[capacity];
        this.comp=comp;
    }

    /**
     * Creates a new empty queue with a given capacity and using the natural order.
     *
     * @param capacity the initial capacity of this queue.
     */
    public OpenHeapQueue(int capacity) {
        this.growthfactor=1.5f;
        this.heap=new Object[capacity];
        this.comp=null;
    }

    /**
     * Creates a new empty queue with a given comparator.
     *
     * @param comp the comparator used in this queue, or {@literal null} for the natural order.
     */
    public OpenHeapQueue(Comparator<? super Type> comp) {
        this.growthfactor=1.5f;
        this.heap=new Object[DEFAULT_CAPACITY];
        this.comp=comp;
    }

    /**
     * Creates a new empty queue with a given capacity and using the natural order.
     */
    public OpenHeapQueue() {
        this.growthfactor=1.5f;
        this.heap=new Object[DEFAULT_CAPACITY];
        this.comp=null;
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
     * Return the comparator used to sort this queue
     *
     * @return the queue comparator, or
     *         {@literal null} if the natural order is used
     */
    public Comparator<? super Type> comparator() {
        return comp;
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
            heap=Arrays.copyOfRange(heap, base, base+size);
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
        if(comp==null) {
            int min=base;
            int max=base+size-1;

            while(min<=max) {
                int med=(min+max)>>>1;
                @SuppressWarnings("unchecked")
                Comparable<Type> midVal=(Comparable<Type>) heap[med];
                int cmp=midVal.compareTo(obj);

                if(cmp<0) min=med+1;
                else if(cmp>0) max=med-1;
                else {
                    if(base>0) {
                        System.arraycopy(heap, base, heap, base-1, med-base);
                        base--;
                        heap[med-1]=obj;
                        size++;
                    }
                    else {
                        if(size>=heap.length) {
                            heap=Arrays.copyOf(heap, (int) ((size+1)*growthfactor));
                        }
                        System.arraycopy(heap, med, heap, med+1, size-med);
                        heap[med]=obj;
                        size++;
                    }
                    return true;
                }
            }
            if(base>0) {
                System.arraycopy(heap, base, heap, base-1, min-base);
                base--;
                heap[min-1]=obj;
                size++;
            }
            else {
                if(size>=heap.length) {
                    heap=Arrays.copyOf(heap, (int) ((size+1)*growthfactor));
                }
                System.arraycopy(heap, min, heap, min+1, size-min);
                heap[min]=obj;
                size++;
            }
            return true;
        }
        else {
            int min=base;
            int max=size-1;

            while(min<=max) {
                int med=(min+max)>>>1;
                @SuppressWarnings("unchecked")
                Type midVal=(Type) heap[med];
                int cmp=comp.compare(midVal, obj);

                if(cmp<0) min=med+1;
                else if(cmp>0) max=med-1;
                else {
                    if(base>0) {
                        System.arraycopy(heap, base, heap, base-1, med-base);
                        base--;
                        heap[med-1]=obj;
                        size++;
                    }
                    else {
                        if(size>=heap.length) {
                            heap=Arrays.copyOf(heap, (int) ((size+1)*growthfactor));
                        }
                        System.arraycopy(heap, med, heap, med+1, size-med);
                        heap[med]=obj;
                        size++;
                    }
                    return true;
                }
            }
            if(base>0) {
                System.arraycopy(heap, base, heap, base-1, min-base);
                base--;
                heap[min-1]=obj;
                size++;
            }
            else {
                if(size>=heap.length) {
                    heap=Arrays.copyOf(heap, (int) ((size+1)*growthfactor));
                }
                System.arraycopy(heap, min, heap, min+1, size-min);
                heap[min]=obj;
                size++;
            }
            return true;
        }
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
        return (Type) heap[base+pos];
    }


    /**
     * Returns an iterator over the elements of the queue
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
                return (Type) heap[base+pos++];
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
