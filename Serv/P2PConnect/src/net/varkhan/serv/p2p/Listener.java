package net.varkhan.serv.p2p;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/30/11
 * @time 11:34 AM
 */
public interface Listener {
    /**
     * Release resources used by the handler, upon removal from the reply queue.
     */
    void release();

    /**
     * Indicate whether the handler has stopped accepting more replies.
     *
     * @return {@code true} if the handler can be removed from the reply queue
     */
    boolean finished();
}
