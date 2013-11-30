/**
 *
 */
package net.varkhan.base.containers.type;

import net.varkhan.base.containers.Iterable;
import net.varkhan.base.containers.Iterator;


/**
 * <b>A view of the characters in a character container.</b>
 * <p/>
 *
 * @author varkhan
 * @date Jan 10, 2010
 * @time 3:29:56 AM
 */
public interface CharIterable extends Iterable<Character> {
    public interface CharIterator extends Iterator<Character> {
        boolean hasNext();

        Character next();

        char nextValue();

        void remove();
    }

    public CharIterable.CharIterator iterator();
}