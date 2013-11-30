/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A view of the floats in a float container.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 3:29:56 AM
 */
public interface FloatIterable extends Iterable<Float> {
    public interface FloatIterator extends Iterator<Float> {
        boolean hasNext();

        Float next();

        float nextValue();

        void remove();
    }

    public FloatIterable.FloatIterator iterator();
}