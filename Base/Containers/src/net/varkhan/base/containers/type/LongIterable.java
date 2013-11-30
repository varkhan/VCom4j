/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A view of the longs in a long container.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 3:29:56 AM
 */
public interface LongIterable extends Iterable<Long> {
    public interface LongIterator extends Iterator<Long> {
        boolean hasNext();

        Long next();

        long nextValue();

        void remove();
    }

    public LongIterable.LongIterator iterator();
}