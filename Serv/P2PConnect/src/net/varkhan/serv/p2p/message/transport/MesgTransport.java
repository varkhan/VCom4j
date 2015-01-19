/**
 *
 */
package net.varkhan.serv.p2p.message.transport;

import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.connect.PeerAddress;

import java.io.IOException;


/**
 * <b/>A message channel.</b>
 * <p/>
 * This interface specifies a physical message communication device, and the
 * operations needed to receive and send messages.
 * <p/>
 * @author varkhan
 * @date Nov 12, 2009
 * @time 12:02:30 AM
 */
public interface MesgTransport {

    public PeerAddress endpoint();

    /**********************************************************************************
     **  Life-cycle management
     **/

    /**
     * Indicates whether the channel is started and able to handle messages
     *
     * @return {@code true} if the channel transport layer is ready to handle outgoing messages,
     * and the listening thread is handling incoming messages
     */
    public boolean isStarted();

    /**
     * Starts sending and receiving messages.
     *
     * @throws java.io.IOException if the channel could not bind to an address
     */
    public void start() throws IOException;

    /**
     * Stops sending and receiving messages.
     *
     * @throws java.io.IOException if the channel could not free resources
     */
    public void stop() throws IOException;


    /**********************************************************************************
     **  Message transmission handling
     **/

    /**
     * Send a method call message through this channel.
     *
     *
     * @param src      the source location
     * @param dst      the destination location
     * @param method   the identifier for the remote method
     * @param message  the message to send
     * @param handler  the call reply handler, or {@literal null} to ignore replies
     *
     * @throws java.io.IOException if a communication or format error occurred while sending or receiving
     */
    public boolean call(PeerAddress src, PeerAddress dst, String method, MesgPayload message, MesgReceiver handler) throws IOException;

    /**
     * Send a reply message through this channel.
     *
     *
     *
     * @param src      the source location
     * @param dst      the destination location
     * @param method   the identifier for the remote method
     * @param message  the message to send
     * @param sequence the call sequence id
 *   @throws java.io.IOException if a communication or format error occurred while sending or receiving
     */
    public boolean repl(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException;

}
