/**
 *
 */
package net.varkhan.serv.p2p.message.transport;

import net.varkhan.base.management.report.JMXAverageMonitorReport;
import net.varkhan.serv.p2p.message.dispatch.MesgDispatcher;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.dispatch.MesgSender;
import net.varkhan.serv.p2p.connect.PeerNode;
import net.varkhan.serv.p2p.message.PeerResolver;
import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.connect.transport.MTXAddress;
import net.varkhan.serv.p2p.message.*;
import net.varkhan.serv.p2p.message.protocol.BinaryEnvelope;
import net.varkhan.serv.p2p.message.protocol.BinaryPayload;
import net.varkhan.serv.p2p.connect.transport.UDPAddress;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.*;
import java.util.Enumeration;


/**
 * <b>.</b>
 * <p/>
 * @author varkhan
 * @date Nov 12, 2009
 * @time 9:40:07 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class MTXTransport implements MesgTransport, MesgSender {
    public static final Logger log=LogManager.getLogger(MTXTransport.class);

    // Incoming packet queue size
    public static final  int QUEUE_SIZE   =100;
    // Default packet recv / send size
    public static final  int PACKET_SIZE  =1024*16;
    // Maximum data payload inside packets
    public static final  int MAX_DATA_SIZE=PACKET_SIZE-50;
    // Maximum time spent waiting for message reception (5sec)
    private static final int RECV_TIMEOUT =5000;

    /** The MTX group address */
    private InetAddress groupAddr;
    /** The MTX group port */
    private int         groupPort;

    private int timeout   =RECV_TIMEOUT;
    private int packetSize=PACKET_SIZE;
    private int queueSize =QUEUE_SIZE;
    private InetAddress localAddr;
    protected MulticastSocket server=null;

    protected PeerResolver   resolver;
    protected MesgDispatcher dispatcher;
    protected PeerAddress    local;

    private JMXAverageMonitorReport stats;
    private String                  name;

    private volatile boolean run   =false;
    private volatile Thread  thread=null;


    /**
     * Creates a Multicast PeerChannel, specifying incoming packet and queue size
     *
     * @param resolver   the point name resolver
     * @param dispatcher the message dispatcher
     * @param localPoint the local point node
     * @param localAddr  the local address to bind to (or {@code null} if any)
     * @param groupAddr  the multicast group address
     * @param groupPort  the multicast group port number
     * @param queueSize  the size of the packet queue
     * @param packetSize the size of outgoing/incoming packets
     * @param stats      activity statistics collector
     * @throws java.net.SocketException if the UDP listening socket could not be created and bound
     */
    public MTXTransport(
            PeerResolver resolver, MesgDispatcher dispatcher,
            PeerAddress localPoint, InetAddress localAddr,
            InetAddress groupAddr, int groupPort,
            int queueSize, int packetSize, JMXAverageMonitorReport stats) throws IOException {

        this.resolver=resolver;
        this.dispatcher=dispatcher;
        this.local=localPoint;
        this.stats=stats;

        this.localAddr=localAddr;
        this.groupAddr = groupAddr;
        this.groupPort = groupPort;
        if(log.isDebugEnabled()) log.debug("BIND MTX "+(localAddr==null?"0.0.0.0":localAddr.getHostAddress())+":"+groupPort);
        server=new MulticastSocket(new InetSocketAddress(localAddr,groupPort));
        server.setReuseAddress(true);
        server.setSoTimeout(timeout);
//        socket.joinGroup(groupAddr);
        if(this.localAddr==null) {
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            if(ifaces.hasMoreElements()) server.setNetworkInterface(ifaces.nextElement());
        }
        else {
            // This fails because Java sucks at networking
//            server.setInterface(this.localAddr);
            NetworkInterface localIface = NetworkInterface.getByInetAddress(this.localAddr);
            if(localIface!=null) {
                server.setNetworkInterface(localIface);
            }
        }

        setQueueSize(queueSize);
        setPacketSize(packetSize);

        this.name = ((server.getLocalAddress()==null)?"0.0.0.0": server.getLocalAddress().getHostAddress())+
                    ":"+server.getLocalPort()+
                    "@"+localPoint.name();
    }

    /**
     * Creates a Multicast MesgChannel
     *
     * @param resolver   the point name resolver
     * @param dispatcher the message dispatcher
     * @param localPoint the local point node
     * @param localAddr the local address to bind to (or {@code null} if any)
     * @param groupAddr  the multicast group address
     * @param groupPort  the multicast group port number
     * @param stats     activity statistics collector
     * @throws java.net.SocketException if the UDP listening socket could not be created and bound
     */
    public MTXTransport(
            PeerResolver resolver, MesgDispatcher dispatcher,
            PeerAddress localPoint, InetAddress localAddr,
            InetAddress groupAddr, int groupPort,
            JMXAverageMonitorReport stats) throws IOException {
        this(resolver,dispatcher,localPoint,localAddr,groupAddr,groupPort,QUEUE_SIZE,PACKET_SIZE,stats);
    }

    /**
     * Creates a Multicast MesgChannel
     *
     * @param resolver   the point name resolver
     * @param dispatcher the message dispatcher
     * @param localPoint the local point node
     * @param groupAddr  the multicast group address
     * @param groupPort  the multicast group port number
     * @param stats     activity statistics collector
     * @throws java.net.SocketException if the UDP listening socket could not be created and bound
     */
    public MTXTransport(
            PeerResolver resolver, MesgDispatcher dispatcher,
            PeerAddress localPoint, InetAddress groupAddr, int groupPort,
            JMXAverageMonitorReport stats) throws IOException {
        this(resolver,dispatcher,localPoint,null,groupAddr,groupPort,QUEUE_SIZE, PACKET_SIZE,stats);
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
     * @return the maximum number of milliseconds the exchange will wait for the reception of a single message
     */
    public int getTimeout() {
        return timeout;
    }

    /**
     * Set the message reception timeout
     *
     * @param delay the maximum number of milliseconds the exchange will wait for the reception of a single message
     * @throws java.net.SocketException if the underlying transport did not accept reconfiguration
     */
    public void setTimeout(int delay) throws SocketException {
        timeout = delay;
        if(server!=null) server.setSoTimeout(timeout);
    }

    /**
     * Gets the local listening address for this exchange
     *
     * @return the local address this exchange listens on
     */
    public InetAddress getAddress() {
        return server.getLocalAddress();
    }

    /**
     * Gets the local listening port for this exchange
     *
     * @return the local port this exchange listens on
     */
    public int getPort() {
        return server.getLocalPort();
    }


    public InetAddress getGroupAddr() {
        return groupAddr;
    }

    public int getGroupPort() {
        return groupPort;
    }


    /**********************************************************************************
     **  Lifecycle management
     **/

    @Override
    public PeerAddress endpoint() {
        return new MTXAddress() {
            @Override
            public InetAddress addrMTXgroup() {
                return groupAddr;
            }

            @Override
            public int portMTXgroup() {
                return groupPort;
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
     * Indicates whether the exchange is started and able to handle messages
     *
     * @return {@code true} if the exchange transport layer is ready to handle outgoing messages,
     *         and the listening thread is handling incoming messages
     */
    public boolean isStarted() {
        return run && thread!=null && thread.isAlive();
    }

    /**
     * Start the exchange
     *
     * @throws java.io.IOException if the exchange could not bind to an address
     */
    public synchronized void start() throws IOException {
        if(run || (thread!=null && thread.isAlive())) return;
        if (log.isDebugEnabled()) {
            log.debug(MTXTransport.class.getSimpleName()+": start "+name +
                      "(" + getAddress().getHostAddress() + ":" + getPort() +
                      "@" + groupAddr.getHostAddress()+":"+ groupPort + ")");
        }
        server.joinGroup(groupAddr);
        thread = new Thread() {
            public void run() { receive(); }
        };
        thread.setName(MTXTransport.class.getSimpleName()+"("+groupAddr.getHostAddress()+":"+ groupPort+")");
        thread.setDaemon(true);
        run = true;
        thread.start();
    }

    /**
     * Stop the exchange
     *
     * @throws java.io.IOException if the exchange could not free resources
     */
    public synchronized void stop() throws IOException {
        run = false;
        Thread t=thread;
        if(t==null) return;
        server.leaveGroup(groupAddr);
        thread = null;
        t.interrupt();
        try {
            t.join(2*timeout);
        }
        catch(InterruptedException e) {
            // ignore
        }
        if (log.isDebugEnabled()) {
            log.debug(MTXTransport.class.getSimpleName()+": stop "+name +
                      "(" + getAddress().getHostAddress() + ":" + getPort() +
                      "@" + groupAddr.getHostAddress()+":"+ groupPort + ")");
        }
    }

    public void receive() {
        DatagramPacket pakt = new DatagramPacket(new byte[packetSize], packetSize);

        // Main message listening loop
        while(run) {
            // Waiting for reception of a message
            try {
                server.receive(pakt);
            }
            catch(SocketTimeoutException e) {
                // Ignore this, as receive naturally times out because we set SO_TIMEOUT
                continue;
            }
            catch(InterruptedIOException e) {
                if(!run) return;
                stats.inc(MTXTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if(log.isDebugEnabled()) log.debug("RECV mtx://"+groupAddr+":"+groupPort+" interrupted",e);
                continue;
            }
            catch(SocketException e) {
                stats.inc(MTXTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if(log.isDebugEnabled()) log.debug("RECV mtx://"+groupAddr+":"+groupPort+" protocol error",e);
                continue;
            }
            catch(IOException e) {
                stats.inc(MTXTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if(log.isDebugEnabled()) log.debug("RECV mtx://"+groupAddr+":"+groupPort+" failed", e);
                continue;
            }
            catch (Throwable e) {
                stats.inc(MTXTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                log.error("Unexpected exception", e);
                continue;
            }

            if (log.isDebugEnabled()) log.debug("RECV mtx://"+groupAddr+":"+groupPort+" receiving from "+pakt.getAddress().getHostAddress());

            // Handle the message
            try {
                receive(pakt);
                stats.inc(MTXTransport.class.getSimpleName()+".Recv.Success");
            }
            catch (IOException e) {
                stats.inc(MTXTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if (log.isDebugEnabled()) log.debug("RECV mtx://"+groupAddr+":"+groupPort+" failed to read data", e);
            }
            catch (Throwable e) {
                stats.inc(MTXTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
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
            stats.inc(MTXTransport.class.getSimpleName()+".Call.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(MTXTransport.class.getSimpleName()+".Call.NotLocal");
            return false;
        }
        // We do not handle messages bigger than the packet size
        if(message.getLength()>MAX_DATA_SIZE) {
            stats.inc(MTXTransport.class.getSimpleName()+".Call.Oversize");
            return false;
        }
        MTXAddress node = dst.as(MTXAddress.class);
        if(node==null) {
            stats.inc(MTXTransport.class.getSimpleName()+".Call.Protocol.Unsupported");
            return false;
        }
        InetAddress addr=node.addrMTXgroup();
        int port=node.portMTXgroup();
        if(!groupAddr.equals(addr) || groupPort!=port) {
            stats.inc(MTXTransport.class.getSimpleName()+".Call.Address.Unsupported");
            return false;
        }

        long sequence = -1;
        if(handler!=null) sequence = dispatcher.register(handler);

        if(log.isDebugEnabled()) log.debug("CALL mtx://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence);

        BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.REPL, src.name(),"",dst.name(),sequence,message);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            send.send(data);
            byte[] buf = data.toByteArray();
            server.send(new DatagramPacket(buf,0,buf.length, addr, port));
        }
        catch(IOException e) {
            if(log.isInfoEnabled()) log.info("CALL mtx://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence+" failed", e);
            stats.inc(MTXTransport.class.getSimpleName()+".Call.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        stats.inc(MTXTransport.class.getSimpleName()+".Call.Success");
        return true;
    }

    public boolean repl(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        if(!run) {
            stats.inc(MTXTransport.class.getSimpleName()+".Repl.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(MTXTransport.class.getSimpleName()+".Repl.NotLocal");
            return false;
        }
        // We do not handle messages bigger than the packet size
        if(message.getLength()>MAX_DATA_SIZE) {
            stats.inc(MTXTransport.class.getSimpleName()+".Repl.Oversize");
            return false;
        }
        MTXAddress node = dst.as(MTXAddress.class);
        if(node==null) {
            stats.inc(MTXTransport.class.getSimpleName()+".Repl.Protocol.Unsupported");
            return false;
        }
        InetAddress addr=node.addrMTXgroup();
        int port=node.portMTXgroup();
        if(!groupAddr.equals(addr) || groupPort!=port) {
            stats.inc(MTXTransport.class.getSimpleName()+".Repl.Address.Unsupported");
            return false;
        }

        if(log.isDebugEnabled()) log.debug("REPL mtx://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence);

        BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.REPL, src.name(),method,dst.name(),sequence,message);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            send.send(data);
            byte[] buf = data.toByteArray();
            server.send(new DatagramPacket(buf,0,buf.length, addr, port));
        }
        catch(IOException e) {
            if(log.isInfoEnabled()) log.info("REPL mtx://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence+" failed", e);
            stats.inc(MTXTransport.class.getSimpleName()+".Repl.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        stats.inc(MTXTransport.class.getSimpleName()+".Repl.Success");
        return true;
    }

    public boolean ping(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        if(!run) {
            stats.inc(MTXTransport.class.getSimpleName()+".Ping.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(MTXTransport.class.getSimpleName()+".Ping.NotLocal");
            if (log.isDebugEnabled()) log.debug("PING mtx://"+groupAddr+":"+groupPort+" not from local "+src.name());
            return false;
        }
        // We do not handle messages bigger than the packet size
        if(message.getLength()>MAX_DATA_SIZE) {
            stats.inc(MTXTransport.class.getSimpleName()+".Ping.Oversize");
            if (log.isDebugEnabled()) log.debug("PING mtx://"+groupAddr+":"+groupPort+" oversize message "+message.getLength());
            return false;
        }
        MTXAddress node = dst.as(MTXAddress.class);
        if(node==null) {
            stats.inc(MTXTransport.class.getSimpleName()+".Ping.Protocol.Unsupported");
            if (log.isDebugEnabled()) log.debug("PING mtx://"+groupAddr+":"+groupPort+" not to multicast "+dst.name());
            return false;
        }
        InetAddress addr=node.addrMTXgroup();
        int port=node.portMTXgroup();
        if(!groupAddr.equals(addr) || groupPort!=port) {
            stats.inc(MTXTransport.class.getSimpleName()+".Ping.Address.Unsupported");
            if (log.isDebugEnabled()) log.debug("PING mtx://"+groupAddr+":"+groupPort+" not to group "+dst.name());
            return false;
        }

        if(log.isDebugEnabled()) log.debug("PING mtx://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence);

        BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.PING, src.name(), method,dst.name(),sequence,message);
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        try {
            send.send(data);
            byte[] buf = data.toByteArray();
            server.send(new DatagramPacket(buf,0,buf.length, addr, port));
        }
        catch(IOException e) {
            if(log.isInfoEnabled()) log.info("PING mtx://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence+" failed", e);
            stats.inc(MTXTransport.class.getSimpleName()+".Ping.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        stats.inc(MTXTransport.class.getSimpleName()+".Ping.Success");
        return true;
    }

    public void send(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        // Send reply
        PeerNode node = (PeerNode)dst;
        node.repl(method, message, sequence);
    }

}
