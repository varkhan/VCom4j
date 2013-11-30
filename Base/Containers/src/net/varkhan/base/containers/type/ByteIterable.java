/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A view of the bytes in a byte container.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 3:29:56 AM
 */
public interface ByteIterable extends Iterable<Byte> {
    public interface ByteIterator extends Iterator<Byte> {
        boolean hasNext();

        Byte next();

        byte nextValue();

        void remove();
    }

    public ByteIterable.ByteIterator iterator();
}