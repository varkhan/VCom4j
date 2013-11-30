/**
 *
 */
package net.varkhan.base.containers.queue;

import net.varkhan.base.containers.Iterator;

import java.util.Comparator;
import java.util.NoSuchElementException;


/**
 * <b>A generic heap-based bounded priority queue.</b>
 * <p/>
 * This class uses a fixed-size heap to provide the semantics of a priority
 * queue on objects, either naturally ordered as per the {@link Comparable}
 * interface, or ordered by a {@link Comparator}.
 * <p/>
 * When the number of elements equals the maximum size bound (the size of the
 * heap), adding an element triggers the discarding of the smallest element in
 * the queue (which could be the added element).
 * <p/>
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 8:03:07 AM
 */
public class BoundedHeapQueue<Type> implements Queue<Type> {

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
     * @param bound the capacity, and maximum size, of this queue.
     * @param comp  the comparator used in this queue, or {@literal null} for the natural order.
     */
    public BoundedHeapQueue(int bound, Comparator<? super Type> comp) {
        this.bound=bound;
        this.heap=new Object[bound];
        this.comp=comp;
    }

    /**
     * Creates a new empty queue with a given capacity and using the natural order.
     *
     * @param bound the capacity, and maximum size, of this queue.
     */
    public BoundedHeapQueue(int bound) {
        this.bound=bound;
        this.heap=new Object[bound];
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
     * Return the maximum queue size
     *
     * @return the maximum size of this queue
     */
    public int bound() {
        return bound;
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


    /**********************************************************************************
     **  Queue mechanism
     **/

    /**
     * Adds an object on the queue.
     *
     * @param obj the object to add
     *
     * @return {@literal true} if the object was added, {@literal false} if
     *         the object could not be added because the queue is over-capacity.
     */
    public final boolean add(Type obj) {
        if(heap.length==0) return false;
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
                        return true;
                    }
                    else if(size<heap.length) {
                        System.arraycopy(heap, med, heap, med+1, size-med);
                        heap[med]=obj;
                        size++;
                        return true;
                    }
                    else if(med<heap.length) {
                        System.arraycopy(heap, med, heap, med+1, heap.length-1-med);
                        heap[med]=obj;
                        return true;
                    }
                    return false;
                }
            }
            if(base>0) {
                System.arraycopy(heap, base, heap, base-1, min-base);
                base--;
                heap[min-1]=obj;
                size++;
                return true;
            }
            else if(size<heap.length) {
                System.arraycopy(heap, min, heap, min+1, size-min);
                heap[min]=obj;
                size++;
                return true;
            }
            else if(min<heap.length) {
//                System.err.println("System.arraycopy(heap,"+min+",heap,"+(min+1)+","+(heap.length-1-min)+");");
                System.arraycopy(heap, min, heap, min+1, heap.length-1-min);
                heap[min]=obj;
                return true;
            }
            return false;
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
                        return true;
                    }
                    else if(size<heap.length) {
                        System.arraycopy(heap, med, heap, med+1, size-med);
                        heap[med]=obj;
                        size++;
                        return true;
                    }
                    else if(med<heap.length) {
                        System.arraycopy(heap, med, heap, med+1, heap.length-1-med);
                        heap[med]=obj;
                        return true;
                    }
                    return false;
                }
            }
            if(base>0) {
                System.arraycopy(heap, base, heap, base-1, min-base);
                base--;
                heap[min-1]=obj;
                size++;
                return true;
            }
            else if(size<heap.length) {
                System.arraycopy(heap, min, heap, min+1, size-min);
                heap[min]=obj;
                size++;
                return true;
            }
            else if(min<heap.length) {
                System.arraycopy(heap, min, heap, min+1, heap.length-1-min);
                heap[min]=obj;
                return true;
            }
            return false;
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
