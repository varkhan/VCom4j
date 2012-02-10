/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A view of the doubles in a double container.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 3:29:56 AM
 */
public interface DoubleIterable extends Iterable<Double> {
    public interface DoubleIterator extends Iterator<Double> {
        boolean hasNext();

        Double next();

        double nextValue();

        void remove();
    }

    public DoubleIterable.DoubleIterator iterator();
}