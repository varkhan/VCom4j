/**
 *
 */
package net.varkhan.serv.p2p.message.transport;

import net.varkhan.base.management.report.JMXAverageMonitorReport;
import net.varkhan.serv.p2p.message.dispatch.MesgDispatcher;
import net.varkhan.serv.p2p.connect.PeerNode;
import net.varkhan.serv.p2p.message.PeerResolver;
import net.varkhan.serv.p2p.connect.transport.UDPAddress;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.dispatch.MesgSender;
import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.message.*;
import net.varkhan.serv.p2p.message.protocol.BinaryEnvelope;
import net.varkhan.serv.p2p.message.protocol.BinaryPayload;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;


/**
 * <b>.</b>
 * <p/>
 * @author varkhan
 * @date Nov 12, 2009
 * @time 6:52:29 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class UDPTransport implements MesgTransport, MesgSender {
    public static final Logger log=LogManager.getLogger(UDPTransport.class);

    // Incoming packet queue size
    public static final int QUEUE_SIZE   =100;
    // Default packet recv / send size
    public static final int PACKET_SIZE  =1024*16;
    // Maximum data payload inside packets
    public static final int MAX_DATA_SIZE=PACKET_SIZE-50;
    // Maximum time spent waiting for message reception
    public static final int RECV_TIMEOUT =500;

    protected int timeout   =RECV_TIMEOUT;
    protected int packetSize=PACKET_SIZE;
    protected int queueSize =QUEUE_SIZE;
    protected InetAddress localAddr;
    protected DatagramSocket server=null;


    protected final PeerResolver   resolver;
    protected final MesgDispatcher dispatcher;
    protected final PeerAddress    local;

    private final JMXAverageMonitorReport stats;
    private final String                  name;

    private volatile boolean run   =false;
    private volatile Thread  thread=null;


    /**
     * Create a UDP PeerChannel, specifying incoming packet and queue size.
     *
     * @param resolver   the point name resolver
     * @param dispatcher the message dispatcher
     * @param localPoint the local point node
     * @param localAddr  the local address to bind to (or {@code null} if any)
     * @param localPort  the local port number to bind to (or {@code 0} if any)
     * @param queueSize  the size of the packet queue
     * @param packetSize the size of outgoing/incoming packets
     * @param stats      activity statistics collector
     * @throws java.net.SocketException if the UDP listening socket could not be created and bound
     */
    public UDPTransport(
            PeerResolver resolver, MesgDispatcher dispatcher,
            PeerAddress localPoint, InetAddress localAddr, int localPort,
            int queueSize, int packetSize,
            JMXAverageMonitorReport stats) throws SocketException {

        this.resolver=resolver;
        this.dispatcher=dispatcher;
        this.local=localPoint;
        this.stats=stats;

        this.localAddr=localAddr;

        if (this.localAddr == null) {
            if(log.isDebugEnabled()) log.debug("BIND UDP 0.0.0.0:"+localPort);
            server= new DatagramSocket(localPort);
        } else {
            if(log.isDebugEnabled()) log.debug("BIND UDP "+this.localAddr.getHostAddress()+":"+localPort);
            server= new DatagramSocket(localPort, this.localAddr);
        }
        server.setBroadcast(true);
        server.setReuseAddress(true);
        server.setSoTimeout(timeout);

        setPacketSize(packetSize);
        setQueueSize(queueSize);

        this.name = ((server.getLocalAddress()==null)?"0.0.0.0": server.getLocalAddress().getHostAddress())+
                    ":"+server.getLocalPort()+
                    "@"+localPoint.name();
    }

    /**
     * Create a UDP WeaveChannel.
     *
     * @param resolver   the point name resolver
     * @param dispatcher the message dispatcher
     * @param localPoint the local point node
     * @param localAddr  the local address to bind to (or {@code null} if any)
     * @param localPort  the local port number to bind to (or {@code 0} if any)
     * @param stats      activity statistics collector
     * @throws java.net.SocketException if the UDP listening socket could not be created and bound
     */
    public UDPTransport(
            PeerResolver resolver, MesgDispatcher dispatcher,
            PeerAddress localPoint, InetAddress localAddr, int localPort,
            JMXAverageMonitorReport stats) throws SocketException {
        this(resolver, dispatcher, localPoint,localAddr,localPort,QUEUE_SIZE,PACKET_SIZE,stats);
    }


    /**
     * Get the size of datagram packets emitted from or received by this server
     *
     * @return the size of datagram packets emitted from or received by this server
     */
    public int getPacketSize() {
        return packetSize;
    }

    /**
     * Set the size of datagram packets emitted from or received by this server
     *
     * @param size the size of datagram packets emitted from or received by this server
     * @throws java.net.SocketException if the underlying transport did not accept reconfiguration
     */
    public void setPacketSize(int size) throws SocketException {
        packetSize = size;
        if (server!= null) {
            server.setReceiveBufferSize(packetSize * queueSize);
            server.setSendBufferSize(packetSize * queueSize);
        }
    }

    /**
     * Get the size of the message waiting queue
     *
     * @return the number of messages that can be queued in the reception buffer
     */
    public int getQueueSize() {
        return queueSize;
    }

    /**
     * Set the size of the message waiting queue
     *
     * @param size the number of messages that can be queued in the reception buffer
     * @throws java.net.SocketException if the underlying transport did not accept reconfiguration
     */
    public void setQueueSize(int size) throws SocketException {
        queueSize = size;
        if (server!= null) {
            server.setReceiveBufferSize(packetSize * queueSize);
            server.setSendBufferSize(packetSize * queueSize);
        }
    }

    /**
     * Get the message reception timeout
     *
     * @return the maximum number of milliseconds the channel will wait for the reception of a single message
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set the message reception timeout
     *
     * @param delay the maximum number of milliseconds the channel will wait for the reception of a single message
     * @throws java.net.SocketException if the underlying transport did not accept reconfiguration
     */
    public void setTimeout(int delay) throws SocketException {
        timeout = delay;
        if(server!=null) server.setSoTimeout(timeout);
    }

    /**
     * Gets the local listening address for this channel
     *
     * @return the local address this channel listens on
     */
    public InetAddress getAddress() {
        return server.getLocalAddress();
    }

    /**
     * Gets the local listening port for this channel
     *
     * @return the local port this channel listens on
     */
    public int getPort() {
        return server.getLocalPort();
    }



    /**********************************************************************************
     **  Life-cycle management
     **/

    @Override
    public UDPAddress endpoint() {
        return new UDPAddress() {
            @Override
            public InetAddress addrUDP() {
                return server.getLocalAddress();
            }

            @Override
            public int portUDP() {
                return server.getLocalPort();
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> T as(Class<T> c) {
                if(c.isAssignableFrom(UDPAddress.class)) return (T) this;
                return null;
            }
        };
    }

    /**
     * Indicates whether the channel is started and able to handle messages
     *
     * @return {@code true} if the channel transport layer is ready to handle outgoing messages,
     *         and the listening thread is handling incoming messages
     */
    public boolean isStarted() {
        return run && thread!=null && thread.isAlive();
    }

    /**
     * Start the channel.
     *
     * @throws java.io.IOException if the channel could not bind to an address
     */
    public synchronized void start() throws IOException {
        if(run || (thread!=null && thread.isAlive())) return;
        if (log.isDebugEnabled()) {
            log.debug(UDPTransport.class.getSimpleName()+": start "+name +
                      "(" + server.getLocalAddress().getHostAddress() + ":" + server.getLocalPort() + ")");
        }
        thread = new Thread() {
            public void run() { serverLoop(); }
        };
        thread.setName(UDPTransport.class.getSimpleName()+"("+server.getLocalAddress().getHostAddress()+":"+server.getLocalPort()+")");
        thread.setDaemon(true);
        run = true;
        thread.start();
    }

    /**
     * Stop the channel.
     *
     * @throws java.io.IOException if the channel could not free resources
     */
    public synchronized void stop() throws IOException {
        run = false;
        Thread t=thread;
        if(t==null) return;
        thread = null;
        t.interrupt();
        try {
            t.join(2*timeout);
        }
        catch(InterruptedException e) {
            // ignore
        }
        log.debug(UDPTransport.class.getSimpleName()+": stop "+name +
                  "(" + server.getLocalAddress().getHostAddress() + ":" + server.getLocalPort() + ")");
    }

    private void serverLoop() {
        DatagramPacket pakt = new DatagramPacket(new byte[packetSize], packetSize);

        // Main message listening loop
        while(run) {
            // Waiting for reception of a message
            try {
                server.receive(pakt);
            }
            catch(SocketTimeoutException e) {
                stats.inc(UDPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                continue;
            }
            catch(InterruptedIOException e) {
                if(!run) return;
                stats.inc(UDPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                continue;
            }
            catch(SocketException e) {
                stats.inc(UDPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                continue;
            }
            catch(IOException e) {
                stats.inc(UDPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if (log.isDebugEnabled()) {
                    log.debug("Receive on socket failed", e);
                }
                continue;
            }
            catch (Throwable e) {
                stats.inc(UDPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                log.error("Unexpected exception", e);
                continue;
            }

            if (log.isDebugEnabled()) {
                log.debug("Receiving a message from "+pakt.getAddress().getHostAddress());
            }

            // Handle the message
            try {
                receive(pakt);
                stats.inc(UDPTransport.class.getSimpleName()+".Recv.Success");
            }
            catch (IOException e) {
                stats.inc(UDPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if (log.isDebugEnabled()) {
                    log.debug("Data read on socket failed", e);
                }
            }
            catch (Throwable e) {
                stats.inc(UDPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                log.error("Unexpected exception", e);
            }
        }
    }

    private void receive(DatagramPacket pakt) throws IOException {// Building received message
        BinaryEnvelope recv = new BinaryEnvelope(pakt.getData(), pakt.getOffset(), pakt.getLength());
        String method=recv.method();
        MesgPayload mesg = recv.message();
        long sequence=recv.sequence();

        // Passing message to the dispatcher
        switch(recv.type()) {
            case BinaryEnvelope.PING: {
                PeerAddress remote = resolver.update(recv.srcId(),mesg);
                if(sequence>=0){
                    // Reply immediately, with decremented ttl
                    ping(local, remote, method, resolver.info(), sequence-1);
                }
            }
            break;
            case BinaryEnvelope.CALL: {
                PeerAddress src = resolver.resolve(recv.srcId());
                PeerAddress dst = resolver.resolve(recv.dstId());
                if(sequence<0) {
                    dispatcher.call(src, dst, method, mesg, null, -1, null);
                }
                else {
                    BinaryPayload repl = new BinaryPayload();
                    dispatcher.call(src, dst, method, mesg, repl, sequence, this);
                }
            }
            break;
            case BinaryEnvelope.REPL: {
                PeerAddress src = resolver.resolve(recv.srcId());
                PeerAddress dst = resolver.resolve(recv.dstId());
                dispatcher.repl(src, dst, method, mesg, sequence);
            }
            break;
        }
    }


    /**********************************************************************************
     **  Message transmission handling
     **/


    public boolean call(PeerAddress src, PeerAddress dst, String method, MesgPayload message, MesgReceiver handler) throws IOException {
        if(!run) {
            stats.inc(UDPTransport.class.getSimpleName()+".Call.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(UDPTransport.class.getSimpleName()+".Call.NotLocal");
            return false;
        }
        // We do not handle messages bigger than the packet size
        if(message.getLength()>MAX_DATA_SIZE) {
            stats.inc(UDPTransport.class.getSimpleName()+".Call.Oversize");
            return false;
        }
        UDPAddress node = dst.as(UDPAddress.class);
        if(node==null) {
            stats.inc(UDPTransport.class.getSimpleName()+".Call.Protocol.Unsupported");
            return false;
        }
        InetAddress addr=node.addrUDP();
        int port=node.portUDP();


        long sequence = -1;
        if(handler!=null) sequence = dispatcher.register(handler);

        if(log.isDebugEnabled()) log.debug("CALL udp://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence);

        BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.REPL, src.name(),"",dst.name(),sequence,message);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            send.send(data);
            byte[] buf = data.toByteArray();
            server.send(new DatagramPacket(buf,0,buf.length, addr, port));
        }
        catch(IOException e) {
            if(log.isInfoEnabled()) log.info("CALL udp://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence+" failed", e);
            stats.inc(UDPTransport.class.getSimpleName()+".Call.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        stats.inc(UDPTransport.class.getSimpleName()+".Call.Success");
        return true;
    }

    public boolean repl(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        if(!run) {
            stats.inc(UDPTransport.class.getSimpleName()+".Repl.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(UDPTransport.class.getSimpleName()+".Repl.NotLocal");
            return false;
        }
        // We do not handle messages bigger than the packet size
        if(message.getLength()>MAX_DATA_SIZE) {
            stats.inc(UDPTransport.class.getSimpleName()+".Repl.Oversize");
            return false;
        }
        UDPAddress node = dst.as(UDPAddress.class);
        if(node==null) {
            stats.inc(UDPTransport.class.getSimpleName()+".Repl.Protocol.Unsupported");
            return false;
        }
        InetAddress addr=node.addrUDP();
        int port=node.portUDP();

        if(log.isDebugEnabled()) log.debug("REPL udp://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence);

        BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.REPL, src.name(),method,dst.name(),sequence,message);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            send.send(data);
            byte[] buf = data.toByteArray();
            server.send(new DatagramPacket(buf,0,buf.length, addr, port));
        }
        catch(IOException e) {
            if(log.isInfoEnabled()) log.info("REPL udp://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence+" failed", e);
            stats.inc(UDPTransport.class.getSimpleName()+".Repl.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        stats.inc(UDPTransport.class.getSimpleName()+".Repl.Success");
        return true;
    }

    public boolean ping(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        if(!run) {
            stats.inc(UDPTransport.class.getSimpleName()+".Ping.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(UDPTransport.class.getSimpleName()+".Ping.NotLocal");
            return false;
        }
        // We do not handle messages bigger than the packet size
        if(message.getLength()>MAX_DATA_SIZE) {
            stats.inc(UDPTransport.class.getSimpleName()+".Ping.Oversize");
            return false;
        }
        UDPAddress node = dst.as(UDPAddress.class);
        if(node==null) {
            stats.inc(UDPTransport.class.getSimpleName()+".Ping.Protocol.Unsupported");
            return false;
        }
        InetAddress addr=node.addrUDP();
        int port=node.portUDP();

        if(log.isDebugEnabled()) log.debug("PING udp://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence);

        BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.PING, src.name(), method,dst.name(),sequence,message);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            send.send(data);
            byte[] buf = data.toByteArray();
            server.send(new DatagramPacket(buf,0,buf.length, addr, port));
        }
        catch(IOException e) {
            if(log.isInfoEnabled()) log.info("PING udp://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence+" failed", e);
            stats.inc(UDPTransport.class.getSimpleName()+".Ping.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        stats.inc(UDPTransport.class.getSimpleName()+".Ping.Success");
        return true;
    }

    public void send(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        // Send reply
        PeerNode node = (PeerNode)dst;
        node.repl(method, message, sequence);
    }

}
