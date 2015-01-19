/**
 *
 */
package net.varkhan.serv.p2p.message.transport;

import net.varkhan.base.management.report.JMXAverageMonitorReport;
import net.varkhan.serv.p2p.message.*;
import net.varkhan.serv.p2p.message.dispatch.MesgDispatcher;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.dispatch.MesgSender;
import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.message.protocol.BinaryPayload;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


/**
 * <b>.</b>
 * <p/>
 * @author varkhan
 * @date Nov 19, 2009
 * @time 12:53:57 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class LocalTransport implements MesgTransport, MesgSender {
    public static final Logger log=LogManager.getLogger(LocalTransport.class);

    private final MesgDispatcher dispatcher;
    private final PeerAddress    local;
    private final String         name;

    private JMXAverageMonitorReport stats;

    private volatile boolean run=false;


    /**
     * Creates a local PeerChannel.
     *
     *
     * @param dispatcher the message dispatcher
     * @param localPoint the local point node
     * @param stats      activity statistics collector
     */
    public LocalTransport(MesgDispatcher dispatcher, PeerAddress localPoint,
                          JMXAverageMonitorReport stats) {

        this.local=localPoint;
        this.dispatcher=dispatcher;
        this.stats=stats;

        this.name="local"+"@"+localPoint.name();
    }


    /**********************************************************************************
     **  Lifecycle management
     **/

    @Override
    public PeerAddress endpoint() {
        return local;
    }

    /**
     * Indicates whether the channel is started and able to handle messages
     *
     * @return {@code true} if the channel transport layer is ready to handle outgoing messages,
     *         and the listening thread is handling incoming messages
     */
    public boolean isStarted() {
        return run;
    }

    /**
     * Start the channel.
     */
    public void start() {
        if(run) return;
        if (log.isDebugEnabled()) {
            log.debug(LocalTransport.class.getSimpleName()+": start "+name);
        }
        run = true;
    }

    /**
     * Stop the channel.
     */
    public void stop() {
        run = false;
        if (log.isDebugEnabled()) {
            log.debug(LocalTransport.class.getSimpleName()+": stop "+name);
        }
    }


    /**********************************************************************************
     **  Message transmission handling
     **/


    public boolean call(PeerAddress src, PeerAddress dst, String method, MesgPayload message, MesgReceiver handler) {
        if(!run) {
            stats.inc(LocalTransport.class.getSimpleName()+".Call.NotRunning");
            return false;
        }
        // We only know how to send messages locally
        if(!local.equals(src) || !local.equals(dst)) {
            stats.inc(LocalTransport.class.getSimpleName()+".Call.NotLocal");
            return false;
        }
        if(handler==null) {
            dispatcher.call(src, dst, method, message, null, -1, null);
        }
        else {
            long sequence = dispatcher.register(handler);
            MesgPayload reply = new BinaryPayload();
            dispatcher.call(src, dst, method, message, reply, sequence, this);
        }
        return true;
    }

    public boolean repl(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) {
        if(!run) {
            stats.inc(LocalTransport.class.getSimpleName()+".Repl.NotRunning");
            return false;
        }
        // We only know how to send messages locally
        if(!local.equals(src) || !local.equals(dst)) {
            stats.inc(LocalTransport.class.getSimpleName()+".Repl.NotLocal");
            return false;
        }
        dispatcher.repl(src, dst, method, message, sequence);
        return true;
    }

    public void send(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) {
        dispatcher.repl(dst, src, method, message, sequence);
    }

}
