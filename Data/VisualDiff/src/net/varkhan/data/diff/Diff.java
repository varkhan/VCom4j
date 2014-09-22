package net.varkhan.data.diff;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.Iterable;


/**
 * <b>A difference analyzer</b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/21/14
 * @time 1:09 PM
 */
public interface Diff<T,S extends Container<T>,X> {

    public Iterable<Block<T>> invoke(S srcL, S srcR, X ctx);


    /**
     * <b>A difference block</b>.
     * <p/>
     * This block contains the set of differences between two sources (left and right).
     * <p/>
     * The methods' contract is that
     * <pre>0 <= begL() <= endL()</pre>
     * <pre>0 <= begR() <= endR()</pre>
     * <pre>endL()-begL() == blockL().size()</pre>
     * <pre>endR()-begR() == blockR().size()</pre>
     * In addition, both {@code blockL()} and  {@code blockR()} cannot be simultaneously
     * empty (or equivalently: {@code endL()-begL() == 0 == endR()-begR()} is never true).
     * <p/>
     */
    public static interface Block<T> {

        public int begL();
        public int begR();
        public int endL();
        public int endR();

        public Container<T> blockL();
        public Container<T> blockR();

    }
}
