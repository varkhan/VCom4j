package net.varkhan.serv.p2p.connect;

import net.varkhan.serv.p2p.Listenable;
import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/23/11
 * @time 11:04 PM
 */
public interface PeerNode extends PeerAddress, Listenable {


    /**
     * The possible life-cycle states of a location.
     */
    public static enum State {
        /** Not available: no message will be handled */
        STOPPED,
        /** Soon to be available: messages can be sent, but will not be handled immediately */
        STARTING,
        /** Currently available: messages can be sent, and will be handled normally */
        RUNNING,
        /** Soon to be unavailable: received messages will be handled, but any new message will be discarded */
        STOPPING,
    }

    /**
     * Indicate the life-cycle state of this location.
     *
     * @return a state value
     * @see State
     */
    public State state();

    public void start();

    public void stop();

    /**
     * Send a method call message through this channel.
     *
     * @param method  the identifier for the remote method
     * @param message the message to send
     * @param handler the call reply handler, or {@literal null} to ignore replies
     *
     * @return {@code true} if the message was sent successfully
     *
     * @throws java.io.IOException if a communication or format error occurred while sending or receiving
     */
    public boolean call(String method, MesgPayload message, MesgReceiver handler) throws IOException;

    /**
     * Send a reply message through this channel.
     *
     * @param method  the identifier for the remote method
     * @param message  the message to send
     * @param sequence the call sequence id
     *
     * @return {@code true} if the message was sent successfully
     *
     * @throws java.io.IOException if a communication or format error occurred while sending or receiving
     */
    public boolean repl(String method, MesgPayload message, long sequence) throws IOException;

}
