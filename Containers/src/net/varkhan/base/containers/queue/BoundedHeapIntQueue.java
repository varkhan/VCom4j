/**
 *
 */
package net.varkhan.base.containers.queue;

import net.varkhan.base.containers.Iterator;

import java.util.NoSuchElementException;


/**
 * <b>A heap-based bounded priority queue containing {@code int}s.</b>
 * <p/>
 * This class uses a fixed-size heap to provide the semantics of a priority queue
 * on {@code int}s, using either ascending or descending order.
 * <p/>
 * When the size of the queue equals the maximum size bound (the size of the heap),
 * adding an element triggers the discarding of the smallest element in the queue
 * (which could be the added element).
 * <p/>
 *
 * @author varkhan
 * @date Mar 13, 2009
 * @time 10:11:02 PM
 */
public class BoundedHeapIntQueue implements IntQueue {

    /**
     * The heap array.
     */
    private final int[] heap;
    private final int   bound;

    /**
     * The number of elements in this queue.
     */
    private int size=0;

    /**
     * The starting point of the queue in the array.
     */
    private int base=0;

    /**
     * A flag indicating whether to sort in descending order
     */
    private final boolean desc;


    /**********************************************************************************
     **  Constructors
     **/


    /**
     * Creates a new empty queue with a given capacity, accessible by its lowest values
     *
     * @param bound the capacity, and maximum size, of this queue.
     */
    public BoundedHeapIntQueue(int bound) {
        this.bound=bound;
        this.heap=new int[bound];
        this.desc=false;
    }

    /**
     * Creates a new empty queue with a given capacity, accessible either by its lowest or by its highest values
     *
     * @param bound the capacity, and maximum size, of this queue.
     * @param desc  if {@literal true}, change the semantics of {@link #poll()} and {@link #peek()}
     *              to return the highest value instead of the lowest
     */
    public BoundedHeapIntQueue(int bound, boolean desc) {
        this.bound=bound;
        this.heap=new int[bound];
        this.desc=desc;
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
     * Indicates whether this queue is sorted in descending order instead of ascending order
     *
     * @return {@literal true} if {@link #poll()} and {@link #peek()}
     *         return the highest value in the queue instead of the lowest
     */
    public boolean descending() {
        return desc;
    }

    /**
     * Removes all objects from the queue
     */
    public void clear() {
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
     *         the object could not be added (for instance, because a limit on the
     *         queue content has been reached)
     */
    public boolean add(Integer obj) {
        return add(obj.intValue());
    }

    /**
     * Adds an object on the queue.
     *
     * @param objVal the object to add
     *
     * @return {@literal true} if the object was added, {@literal false} if
     *         the object could not be added (for instance, because a limit on the
     *         queue content has been reached)
     */
    public boolean add(int objVal) {
        if(heap.length==0) return false;
        int min=base;
        int max=base+size-1;

        if(desc) {
            // Sort in ascending order
            while(min<=max) {
                int med=(min+max)>>>1;
                int medVal=heap[med];

                if(medVal>objVal) min=med+1;
                else if(medVal<objVal) max=med-1;
                else {
                    if(base>0) {
                        System.arraycopy(heap, base, heap, base-1, med-base);
                        base--;
                        heap[med-1]=objVal;
                        size++;
                        return true;
                    }
                    else if(size<heap.length) {
                        System.arraycopy(heap, med, heap, med+1, size-med);
                        heap[med]=objVal;
                        size++;
                        return true;
                    }
                    else if(med<heap.length) {
                        System.arraycopy(heap, med, heap, med+1, heap.length-1-med);
                        heap[med]=objVal;
                        return true;
                    }
                    return false;
                }
            }
        }
        else {
            // Sort in descending order
            while(min<=max) {
                int med=(min+max)>>>1;
                int medVal=heap[med];

                if(medVal<objVal) min=med+1;
                else if(medVal>objVal) max=med-1;
                else {
                    if(base>0) {
                        System.arraycopy(heap, base, heap, base-1, med-base);
                        base--;
                        heap[med-1]=objVal;
                        size++;
                        return true;
                    }
                    else if(size<heap.length) {
                        System.arraycopy(heap, med, heap, med+1, size-med);
                        heap[med]=objVal;
                        size++;
                        return true;
                    }
                    else if(med<heap.length) {
                        System.arraycopy(heap, med, heap, med+1, heap.length-1-med);
                        heap[med]=objVal;
                        return true;
                    }
                    return false;
                }
            }
        }
        if(base>0) {
            System.arraycopy(heap, base, heap, base-1, min-base);
            base--;
            heap[min-1]=objVal;
            size++;
            return true;
        }
        else if(size<heap.length) {
            System.arraycopy(heap, min, heap, min+1, size-min);
            heap[min]=objVal;
            size++;
            return true;
        }
        else if(min<heap.length) {
//                System.err.println("System.arraycopy(heap,"+min+",heap,"+(min+1)+","+(heap.length-1-min)+");");
            System.arraycopy(heap, min, heap, min+1, heap.length-1-min);
            heap[min]=objVal;
            return true;
        }
        return false;
    }

    /**
     * Removes and return the lowest value on the queue
     *
     * @return the lowest value on the queue
     */
    public Integer poll() {
        if(size==0) throw new NoSuchElementException();
        final int obj=heap[base];
        base++;
        size--;
        return obj;
    }

    /**
     * Removes and return the lowest value on the queue
     *
     * @return the lowest value on the queue
     */
    public int dequeueInt() {
        if(size==0) throw new NoSuchElementException();
        final int obj=heap[base];
        base++;
        size--;
        return obj;
    }

    /**
     * Returns, without removing it, the lowest value on the queue
     *
     * @return the lowest value on the queue
     */
    public Integer peek() {
        if(size==0) throw new NoSuchElementException();
        return heap[base];
    }

    /**
     * Returns, without removing it, the lowest value on the queue
     *
     * @return the lowest value on the queue
     */
    public int getInt() {
        if(size==0) throw new NoSuchElementException();
        return heap[base];
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
    public Integer get(int pos) {
        if(pos>=size) throw new ArrayIndexOutOfBoundsException(pos);
        return heap[base+pos];
    }

    /**
     * Returns an element at a given position
     *
     * @param pos the position in the queue
     *
     * @return the element at position {@code pos}
     */
    public int getInt(int pos) {
        if(pos>=size) throw new ArrayIndexOutOfBoundsException(pos);
        return heap[base+pos];
    }


    /**
     * Returns an iterator over the elements of the queue
     *
     * @return an Iterator
     */
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            private int pos=0;

            public boolean hasNext() {
                return pos<size;
            }

            public Integer next() {
                return heap[base+pos++];
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
    public <Par> long visit(Visitor<Integer,Par> vis, Par par) {
        int pos=0;
        long c=0;
        while(pos<size) {
            int obj;
            if(base+pos<heap.length) obj=heap[base+(pos++)];
            else obj=heap[base+(pos++)-heap.length];
            long r=vis.invoke(obj, par);
            if(r<0) return c;
            c+=r;
        }
        return c;
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
    public <Par> long visit(IntVisitor<Par> vis, Par par) {
        int pos=0;
        long c=0;
        while(pos<size) {
            int obj;
            if(base+pos<heap.length) obj=heap[base+(pos++)];
            else obj=heap[base+(pos++)-heap.length];
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
        return net.varkhan.base.containers.array.IntArrays.toString(heap);
    }

}
