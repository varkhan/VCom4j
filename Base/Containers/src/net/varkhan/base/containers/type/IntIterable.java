/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A view of the integers in an integer container.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 3:29:56 AM
 */
public interface IntIterable extends Iterable<Integer> {
    public interface IntIterator extends Iterator<Integer> {
        boolean hasNext();

        Integer next();

        int nextValue();

        void remove();
    }

    public IntIterable.IntIterator iterator();
}