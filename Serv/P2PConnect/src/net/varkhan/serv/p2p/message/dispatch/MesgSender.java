package net.varkhan.serv.p2p.message.dispatch;

import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.connect.PeerAddress;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 3:31 AM
 */
public interface MesgSender {

    /**
     * Expedite a reply.
     *
     *
     * @param src      the source location
     * @param dst      the destination location
     * @param message  the reply message
     * @param sequence the call sequence id
     *
     * @throws java.io.IOException if a communication or format error occurred while sending or receiving
     */
    public void send(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException;

}
