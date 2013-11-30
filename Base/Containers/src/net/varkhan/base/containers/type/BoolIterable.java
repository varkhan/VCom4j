/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A view of the booleans in a boolean container.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 3:29:56 AM
 */
public interface BoolIterable extends Iterable<Boolean> {
    public interface BoolIterator extends Iterator<Boolean> {
        boolean hasNext();

        Boolean next();

        boolean nextValue();

        void remove();
    }

    public BoolIterable.BoolIterator iterator();
}