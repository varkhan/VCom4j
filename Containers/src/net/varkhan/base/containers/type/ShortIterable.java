/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A view of the shorts in a short container.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 3:29:56 AM
 */
public interface ShortIterable extends Iterable<Short> {
    public interface ShortIterator extends Iterator<Short> {
        boolean hasNext();

        Short next();

        short nextValue();

        void remove();
    }

    public ShortIterable.ShortIterator iterator();
}