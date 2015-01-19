/**
 *
 */
package net.varkhan.serv.p2p.message.dispatch;


import net.varkhan.serv.p2p.Listener;
import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.connect.PeerAddress;


/**
 * <b>.</b>
 * <p/>
 * @author varkhan
 * @date Nov 12, 2009
 * @time 12:26:18 AM
 */
public interface MesgReceiver extends Listener {

    /**
     * Process a reply.
     *
     * @param src     the source location
     * @param dst     the destination location
     * @param method  the identifier for the remote method
     * @param message the received message
     */
    public void receive(PeerAddress src, PeerAddress dst, String method, MesgPayload message);

    /**
     * The maximum amount of time the handler will accept replies.
     *
     * @return the maximum reply time, in milliseconds
     */
    public long getMaxDelay();

    /**
     * The maximum number of replies the handler will accept.
     *
     * @return the maximum number of expected replies
     */
    public long getMaxCount();

}
