package net.varkhan.serv.p2p.message.dispatch;

import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.message.MesgEnvelope;
import net.varkhan.serv.p2p.message.MesgPayload;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/30/11
 * @time 7:54 AM
 */
public class QueuedReceiver implements MesgReceiver {
    private volatile long maxtime;
    private final    long starttime;
    private final    long maxcount;

    private volatile boolean                             cancelled=false;
    private final    AtomicLong                          received =new AtomicLong();
    private final    ConcurrentLinkedQueue<MesgEnvelope> queue    =new ConcurrentLinkedQueue<MesgEnvelope>();

    /**
     * Creates a reply handler for CALL message that will receive and store a single REPL message
     *
     * @param maxtime the maximum time (in milliseconds) the hanler will wait for a reply before being released
     * @throws IllegalArgumentException if the value of timeout is negative
     */
    public QueuedReceiver(long maxtime, long maxcount) throws IllegalArgumentException {
        if(maxtime<=0) throw new IllegalArgumentException("Max time must be positive or zero");
        this.maxtime = maxtime;
        if(maxcount<=0) throw new IllegalArgumentException("Max count must be positive or zero");
        this.maxcount = maxcount;
        this.starttime = System.currentTimeMillis();
    }

    public void receive(PeerAddress src, PeerAddress dst, String method, MesgPayload message) {
        this.queue.offer(new Envelope(src,dst,method,message));
        this.received.incrementAndGet();
        this.notifyAll();
    }

    /**
     * Receive a message.
     * <p/>
     * This method will block until either a message was received, or the operation failed, timed out, or was cancelled
     *
     * @param timeout the maximum time to wait in milliseconds (or {@code 0} to wait forever)
     * @return an envelope containing the information received, or {@code null} if nothing was received
     * @throws IllegalArgumentException if the value of timeout is negative
     * @see #receive()
     */
    public MesgEnvelope receive(long timeout) throws IllegalArgumentException {
        if(timeout<=0) throw new IllegalArgumentException("Timeout must be positive or zero");
        if(timeout==0) {
            MesgEnvelope env;
            // The order matters here: we want to retrieve before checking aborts
            while((env=queue.poll())==null && !finished()) try {
                // We need synchronization to allow us to wait()
                synchronized(this) { this.wait(); }
            } catch (InterruptedException e) {
                // Ignore spurious interrupts
            }
            return env;
        }
        else {
            long time = System.currentTimeMillis();
            MesgEnvelope env;
            // The order matters here: we want to retrieve before checking aborts
            while((env=queue.poll())==null && !finished() && timeout>0) try {
                // We need synchronization to allow us to wait()
                synchronized(this) { this.wait(timeout); }
            } catch (InterruptedException e) {
                // Ignore spurious interrupts, but update the timer
                long t = System.currentTimeMillis();
                timeout = timeout+time-t;
                time = t;
            }
            return env;
        }
    }

    /**
     * Indicate whether a single message has been received.
     * <p/>
     * This method return immediately.
     *
     * @return an envelope containing the information received, or {@code null} if nothing was received
     * @see #receive(long)
     */
    public MesgEnvelope receive() { return queue.poll(); }

    /**
     * The number of messages received.
     *
     * @return the total number of messages received so far
     * @see #receive(long)
     * @see #receive()
     */
    public long received() { return received.get(); }

    public void cancel() {
        this.cancelled = true;
        this.notifyAll();
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public void release() {
        this.cancelled = true;
        this.notifyAll();
    }

    public boolean finished() { return received.get()>=maxcount || cancelled || maxtime<(System.currentTimeMillis()-starttime); }

    /**
     * Set the maximum amount of time the handler will wait for a reply
     *
     * @param timeout the maximum reply time, in milliseconds
     */
    public void setMaxDelay(long timeout) {
        this.maxtime= timeout;
    }

    public long getMaxDelay() { return maxtime; }

    public long getMaxCount() { return maxcount; }

    /**
     * <b></b>.
     * <p/>
     *
     * @author varkhan
     * @date 5/30/11
     * @time 8:21 AM
     */
    public static class Envelope implements MesgEnvelope {

        private final PeerAddress src;
        private final PeerAddress dst;
        private final String      method;
        private final MesgPayload message;

        public Envelope(PeerAddress src, PeerAddress dst, String method, MesgPayload message) {
            this.src=src;
            this.dst=dst;
            this.method=method;
            this.message=message;
        }

        public PeerAddress src() {
            return src;
        }

        public PeerAddress dst() {
            return dst;
        }

        public String method() {
            return method;
        }

        public MesgPayload message() {
            return message;
        }
    }
}
