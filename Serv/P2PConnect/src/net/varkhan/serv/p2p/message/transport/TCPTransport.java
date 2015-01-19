/**
 *
 */
package net.varkhan.serv.p2p.message.transport;

import net.varkhan.base.management.report.JMXAverageMonitorReport;
import net.varkhan.serv.p2p.message.PeerResolver;
import net.varkhan.serv.p2p.message.dispatch.MesgDispatcher;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.dispatch.MesgSender;
import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.connect.transport.TCPAddress;
import net.varkhan.serv.p2p.message.*;
import net.varkhan.serv.p2p.message.protocol.BinaryEnvelope;
import net.varkhan.serv.p2p.message.protocol.BinaryPayload;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.net.*;


/**
 * <b>.</b>
 * <p/>
 * @author varkhan
 * @date Nov 19, 2009
 * @time 12:53:57 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class TCPTransport implements MesgTransport {
    public static final Logger log=LogManager.getLogger(TCPTransport.class);


    private static final int RECV_TIMEOUT=500;
    private static final int BACK_LOG    =100;

    private int timeout=RECV_TIMEOUT;
    private InetAddress  localAddr;
    private ServerSocket server;

    protected PeerResolver   resolver;
    protected MesgDispatcher dispatcher;
    protected PeerAddress    local;

    private JMXAverageMonitorReport stats;
    private String                  name;

    private volatile boolean run   =false;
    private volatile Thread  thread=null;


    /**
     * Create a TCP PeerChannel.
     *
     * @param resolver   the point name resolver
     * @param dispatcher the message dispatcher
     * @param localPoint the local point node
     * @param localAddr  the local address to bind to (or {@code null} if any)
     * @param localPort  the local port number to bind to (or {@code 0} if any)
     * @param stats      activity statistics collector
     * @throws java.io.IOException if the TCP listening socket could not be created and bound
     */
    public TCPTransport(
            PeerResolver resolver, MesgDispatcher dispatcher,
            PeerAddress localPoint, InetAddress localAddr, int localPort,
            JMXAverageMonitorReport stats) throws IOException {

        this.resolver=resolver;
        this.dispatcher=dispatcher;
        this.local=localPoint;
        this.stats=stats;

        this.localAddr=localAddr;
        if(this.localAddr!=null) {
            server=new ServerSocket(localPort, BACK_LOG, this.localAddr);
        } else {
            server= new ServerSocket(localPort, BACK_LOG);
        }
        this.name = ((server.getInetAddress()==null)?"0.0.0.0": server.getInetAddress().getHostAddress())+
                    ":"+server.getLocalPort()+
                    "@"+localPoint.name();
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

    public InetAddress getAddress() {
        return server.getInetAddress();
    }

    public int getPort() {
        return server.getLocalPort();
    }



    /**********************************************************************************
     **  Lifecycle management
     **/

    @Override
    public TCPAddress endpoint() {
        return new TCPAddress() {
            @Override
            public InetAddress addrTCP() {
                return server.getInetAddress();
            }

            @Override
            public int portTCP() {
                return server.getLocalPort();
            }

            @Override
            public String name() {
                return name;
            }

            @Override
            @SuppressWarnings("unchecked")
            public <T> T as(Class<T> c) {
                if(c.isAssignableFrom(TCPAddress.class)) return (T) this;
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
     * Start the channel
     *
     * @throws java.io.IOException if the channel could not bind to an address
     */
    public synchronized void start() throws IOException {
        if(run || (thread!=null && thread.isAlive())) return;
        if (log.isDebugEnabled()) {
            log.debug(TCPTransport.class.getSimpleName()+": start "+name +
                      "(" + server.getInetAddress().getHostAddress() + ":" + getPort() + ")");
        }
        thread = new Thread() {
            public void run() { serverLoop(); }
        };
        thread.setName(TCPTransport.class.getSimpleName()+"("+server.getInetAddress().getHostAddress()+":"+getPort()+")");
        thread.setDaemon(true);
        run = true;
        thread.start();
    }

    /**
     * Stop the channel
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
        if (log.isDebugEnabled()) {
            log.debug(name+ " stop TCPMessageChannel (address=" + server.getInetAddress().getHostAddress() + ", port=" + server.getLocalPort() + ")");
        }
    }

    public void serverLoop() {
        // Main message listening loop
        while(run) {
            // Waiting for an incoming connection
            final Socket socket;
            try {
                socket = server.accept();
                socket.setSoTimeout(timeout);
            }
            catch(SocketTimeoutException e) {
                stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                continue;
            }
            catch(InterruptedIOException e) {
                if(!run) return;
                stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if (log.isDebugEnabled()) {
                    log.debug("Interrupted while receiving", e);
                }
                continue;
            }
            catch(SocketException e) {
                stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if (log.isDebugEnabled()) {
                    log.debug("Protocol error while receiving", e);
                }
                continue;
            }
            catch(IOException e) {
                stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if (log.isDebugEnabled()) {
                    log.debug("Accept on server socket failed", e);
                }
                continue;
            }
            catch (Throwable e) {
                stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                log.error("Unexpected exception", e);
                continue;
            }

            if (log.isDebugEnabled()) {
                log.debug("Receiving a message from "+socket.getInetAddress().getHostAddress());
            }

            // Handle the message
            try {
                receive(socket);
                stats.inc(TCPTransport.class.getSimpleName()+".Recv.Success");
            }
            catch (IOException e) {
                stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                if (log.isDebugEnabled()) {
                    log.debug("Data read on socket failed", e);
                }
                try { socket.close(); } catch (IOException x) { /* ignore */ }
            }
            catch (Throwable e) {
                stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
                log.error("Unexpected exception", e);
                try { socket.close(); } catch (IOException x) { /* ignore */ }
            }
        }
    }

    private void receive(Socket socket) throws IOException {// Here we could try to use an executor service.
        // But the overhead of starting a thread is in most cases higher that just doing the minimal
        // handling we need here when we are not getting a CALL (and in that case, the dispatcher will do it)
        // Building received message
        BinaryEnvelope recv = new BinaryEnvelope(socket.getInputStream());
        String method=recv.method();
        MesgPayload mesg = recv.message();
        long sequence=recv.sequence();

        // Passing message to the dispatcher
        switch(recv.type()) {
            case BinaryEnvelope.PING: {
                PeerAddress remote = resolver.update(recv.srcId(),mesg);
                if(sequence>=0){
                    // Reply immediately, with decremented ttl
                    ping(socket, local, remote, method, resolver.info(), sequence-1);
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
                    dispatcher.call(src, dst, method, mesg, repl, sequence, new WsTCPSender(socket));
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
            stats.inc(TCPTransport.class.getSimpleName()+".Call.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(TCPTransport.class.getSimpleName()+".Call.NotLocal");
            return false;
        }
        TCPAddress node = dst.as(TCPAddress.class);
        if(node==null) {
            stats.inc(TCPTransport.class.getSimpleName()+".Call.Protocol.Unsupported");
            return false;
        }
        InetAddress addr=node.addrTCP();
        int port=node.portTCP();

        long sequence = -1;
        if(handler!=null) sequence = dispatcher.register(handler);

        if(log.isDebugEnabled()) {
            log.debug("CALL tcp://"+addr.getHostAddress()+":"+port+"/"+method);
        }
        // We don't use the sequence ID system, since the answer will come trough the same socket anyway
        Socket socket = null;
        try {
            socket = setupSocket(node.addrTCP(), node.portTCP(), (int) handler.getMaxDelay());
            BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.CALL, src.name(), dst.name(), method, sequence, message);
            OutputStream outStm = socket.getOutputStream();
            send.send(outStm);
            outStm.flush();
            // We don't need output to this anymore
            socket.shutdownOutput();
            stats.inc(TCPTransport.class.getSimpleName()+".Call.Success");
        } catch (IOException e) {
            if(log.isInfoEnabled()) log.info("CALL tcp://"+node.addrTCP().getHostAddress()+":"+node.portTCP()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Call.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }

        // We hijack this thread to handle the reply immediately, since it should be available right now
        try {
            receive(socket);
            stats.inc(TCPTransport.class.getSimpleName()+".Recv.Success");
        }
        catch (SocketTimeoutException e) {
            if(log.isInfoEnabled()) log.info("CALL tcp://"+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        catch (IOException e) {
            if(log.isInfoEnabled()) log.info("CALL tcp://"+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        catch (Throwable e) {
            if(log.isInfoEnabled()) log.info("CALL tcp://"+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Recv.error["+e.getClass().getSimpleName()+"]");
            throw new IOException("Unexpected error", e);
        }
        finally {
            if (socket != null) {
                try {
                    if (!socket.isOutputShutdown() && socket.isConnected()) {
                        socket.shutdownOutput();
                    }
                    socket.close();
                } catch (IOException e) {
                    // ignore exception
                }
            }
        }
        return true;
    }

    public boolean repl(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        if(!run) {
            stats.inc(TCPTransport.class.getSimpleName()+".Repl.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(TCPTransport.class.getSimpleName()+".Repl.NotLocal");
            return false;
        }
        TCPAddress node = dst.as(TCPAddress.class);
        if(node==null) {
            stats.inc(TCPTransport.class.getSimpleName()+".Repl.Protocol.Unsupported");
            return false;
        }
        InetAddress addr=node.addrTCP();
        int port=node.portTCP();

        if(log.isDebugEnabled()) log.debug("REPL tcp://"+addr.getHostAddress()+":"+port+"/"+method+"#"+sequence);

        // We don't use the sequence ID system, since the answer will come trough the same socket anyway
        Socket socket = null;
        try {
            socket = setupSocket(node.addrTCP(), node.portTCP(), timeout);
            // We will not need input from this anymore
            socket.shutdownInput();
            BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.REPL, src.name(), dst.name(), method, sequence, message);
            OutputStream outStm = socket.getOutputStream();
            send.send(outStm);
            outStm.flush();
            // We don't need output to this anymore
            socket.shutdownOutput();
            stats.inc(TCPTransport.class.getSimpleName()+".Repl.Success");
        } catch (IOException e) {
            if(log.isInfoEnabled()) log.info("REPL tcp://"+node.addrTCP().getHostAddress()+":"+node.portTCP()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Repl.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        finally {
            if (socket!=null) {
                try {
                    if (!socket.isOutputShutdown() && socket.isConnected()) {
                        socket.shutdownOutput();
                    }
                    socket.close();
                }
                catch(IOException e) {
                    // ignore exception
                }
            }
        }
        return true;
    }

    public boolean ping(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        if(!run) {
            stats.inc(TCPTransport.class.getSimpleName()+".Ping.NotRunning");
            return false;
        }
        // We only know how to send messages from the local addr
        if(!local.equals(src)) {
            stats.inc(TCPTransport.class.getSimpleName()+".Ping.NotLocal");
            return false;
        }
        TCPAddress node = dst.as(TCPAddress.class);
        if(node==null) {
            stats.inc(TCPTransport.class.getSimpleName()+".Ping.Protocol.Unsupported");
            return false;
        }
        InetAddress addr=node.addrTCP();
        int port=node.portTCP();

        if(log.isDebugEnabled()) log.debug("PING tcp://"+addr.getHostAddress()+":"+port+"/"+method);

        // We don't use the sequence ID system, since the answer will come trough the same socket anyway
        Socket socket = null;
        try {
            socket = setupSocket(node.addrTCP(), node.portTCP(), timeout);
            ping(socket, src, dst, method, message, sequence);
        } catch (IOException e) {
            if(log.isInfoEnabled()) log.info("PING tcp://"+node.addrTCP().getHostAddress()+":"+node.portTCP()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Ping.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }

        // We hijack this thread to handle the reply immediately, since it should be available right now
        try {
            receive(socket);
            stats.inc(TCPTransport.class.getSimpleName()+".Ping.Success");
        }
        catch (SocketTimeoutException e) {
            if(log.isInfoEnabled()) log.info("PING tcp://"+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Ping.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        catch (IOException e) {
            if(log.isInfoEnabled()) log.info("PING tcp://"+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Ping.error["+e.getClass().getSimpleName()+"]");
            throw e;
        }
        catch (Throwable e) {
            if(log.isInfoEnabled()) log.info("PING tcp://"+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"/"+method+"#"+sequence+" failed", e);
            stats.inc(TCPTransport.class.getSimpleName()+".Ping.error["+e.getClass().getSimpleName()+"]");
            throw new IOException("Unexpected error", e);
        }
        finally {
            if (socket != null) {
                try {
                    if (!socket.isOutputShutdown() && socket.isConnected()) {
                        socket.shutdownOutput();
                    }
                    socket.close();
                } catch (IOException e) {
                    // ignore exception
                }
            }
        }
        return true;
    }

    private void ping(Socket socket, PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
        BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.PING, src.name(), dst.name(), method, sequence, message);
        OutputStream outStm = socket.getOutputStream();
        send.send(outStm);
        outStm.flush();
        stats.inc(TCPTransport.class.getSimpleName()+".Ping.Success");
        // Expect reply?
        if(sequence>=0) receive(socket);
    }

    private Socket setupSocket(InetAddress addr, int port, int timeout) throws IOException {
        Socket socket = new Socket();
        socket.setReuseAddress(true);
        socket.setSoTimeout(timeout);     // set read timeout
        if (localAddr != null) {
            socket.bind(new InetSocketAddress(localAddr, 0));
        }
        socket.connect(new InetSocketAddress(addr, port), timeout);
        return socket;
    }

    private class WsTCPSender implements MesgSender {

        protected Socket socket;

        private WsTCPSender(Socket socket) {
            this.socket=socket;
        }

        public void send(PeerAddress src, PeerAddress dst, String method, MesgPayload message, long sequence) throws IOException {
            try {
                socket.shutdownInput();
                BinaryEnvelope send = new BinaryEnvelope(BinaryEnvelope.REPL, src.name(), dst.name(), method, sequence, message);
                OutputStream outStm = socket.getOutputStream();
                send.send(outStm);
                outStm.flush();
                socket.shutdownOutput();
                stats.inc(TCPTransport.class.getSimpleName()+".Repl.Success");
            } catch (IOException e) {
                if(log.isInfoEnabled()) log.info("REPL tcp://"+socket.getInetAddress().getHostAddress()+":"+socket.getPort()+"/"+method+"#"+sequence+" failed", e);
                stats.inc(TCPTransport.class.getSimpleName()+".Repl.error["+e.getClass().getSimpleName()+"]");
            }
            finally {
                try { socket.close(); }
                catch(IOException e) { /* ignore */ }
            }
        }

        public void finalize() throws Throwable {
            socket.close();
            super.finalize();
        }
    }


}
