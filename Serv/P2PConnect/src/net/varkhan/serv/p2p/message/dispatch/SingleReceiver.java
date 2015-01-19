package net.varkhan.serv.p2p.message.dispatch;

import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.message.MesgPayload;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/30/11
 * @time 7:54 AM
 */
public class SingleReceiver implements MesgReceiver {
    private volatile long timeout;
    private final    long starttime;

    private volatile boolean     cancelled=false;
    private volatile boolean     received =false;
    private          PeerAddress src      =null;
    private          PeerAddress dst      =null;
    private          String      method   =null;
    private          MesgPayload message  =null;

    /**
     * Creates a reply handler for CALL message that will receive and store a single REPL message
     *
     * @param timeout the maximum time (in milliseconds) the hanler will wait for a reply before being released
     * @throws IllegalArgumentException if the value of timeout is negative
     */
    public SingleReceiver(long timeout) throws IllegalArgumentException {
        if(timeout<=0) throw new IllegalArgumentException("Timeout must be positive or zero");
        this.timeout = timeout;
        this.starttime = System.currentTimeMillis();
    }

    public void receive(PeerAddress src, PeerAddress dst, String method, MesgPayload message) {
        this.src = src;
        this.dst= dst;
        this.method = method;
        this.message = message;
        this.received = true;
        this.notifyAll();
    }

    /**
     * Indicate whether a single message has been received.
     * <p/>
     * This method will block until either a message was received, or the operation failed, timed out, or was cancelled
     *
     * @param timeout the maximum time to wait in milliseconds (or {@code 0} to wait forever)
     * @return {@code true} if a message was successfully received
     * @throws IllegalArgumentException if the value of timeout is negative
     * @see #isReceived()
     * @see #isCancelled()
     */
    public boolean isReceived(long timeout) throws IllegalArgumentException {
        if(timeout<=0) throw new IllegalArgumentException("Timeout must be positive or zero");
        if(timeout==0) {
            while(!finished() && !cancelled) try {
                // We need synchronization to allow us to wait()
                synchronized(this) { this.wait(); }
            } catch (InterruptedException e) {
                // Ignore spurious interrupts
            }
            return received;
        }
        else {
            long time = System.currentTimeMillis();
            while(!finished() && timeout>0) try {
                // We need synchronization to allow us to wait()
                synchronized(this) { this.wait(timeout); }
            } catch (InterruptedException e) {
                // Ignore spurious interrupts, but update the timer
                long t = System.currentTimeMillis();
                timeout = timeout+time-t;
                time = t;
            }
            return received;
        }
    }

    /**
     * Indicate whether a single message has been received.
     * <p/>
     * This method return immediately.
     *
     * @return {@code true} if a message was successfully received
     * @see #isReceived(long)
     */
    public boolean isReceived() {
        return received;
    }

    public void cancel() {
        this.cancelled = true;
        this.notifyAll();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public PeerAddress getSrc() {
        if(!received) return null;
        return src;
    }

    public PeerAddress getDst() {
        if(!received) return null;
        return dst;
    }

    public String getMethod() {
        if(!received) return null;
        return method;
    }

    public MesgPayload getMessage() {
        if(!received) return null;
        return message;
    }

    public void release() {
        this.cancelled = true;
        this.notifyAll();
    }

    public boolean finished() { return received || cancelled || timeout<(System.currentTimeMillis()-starttime); }

    /**
     * Set the maximum amount of time the handler will wait for a reply
     *
     * @param timeout the maximum reply time, in milliseconds
     */
    public void setMaxDelay(long timeout) {
        this.timeout = timeout;
    }

    public long getMaxDelay() { return timeout; }

    public long getMaxCount() { return 1; }
}
