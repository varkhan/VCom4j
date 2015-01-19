package net.varkhan.serv.p2p.network;

import net.varkhan.base.management.report.JMXAverageMonitorReport;
import net.varkhan.serv.p2p.Listenable;
import net.varkhan.serv.p2p.connect.transport.TCPAddress;
import net.varkhan.serv.p2p.connect.transport.UDPAddress;
import net.varkhan.serv.p2p.message.dispatch.MesgDispatcher;
import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.PeerResolver;
import net.varkhan.serv.p2p.message.transport.LocalTransport;
import net.varkhan.serv.p2p.message.transport.TCPTransport;
import net.varkhan.serv.p2p.message.transport.UDPTransport;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 7:18 AM
 */
public class P2PLocalHost extends P2PHost implements UDPAddress, TCPAddress {

    protected final LocalTransport locch;
    protected final UDPTransport   udpch;
    protected final TCPTransport   tcpch;
    private volatile State             state =State.STOPPED;
    private final    Set<HostListener> hostUL=new CopyOnWriteArraySet<HostListener>();

    public P2PLocalHost(
            String name,
            PeerResolver resolver, MesgDispatcher dispatcher,
            InetAddress localAddr, int localPort,
            JMXAverageMonitorReport stats) throws IOException {
        this(name, resolver, dispatcher, localAddr, localPort, stats, DEFAULT_HEARTBEAT);
    }

    public P2PLocalHost(
            String name,
            PeerResolver resolver, MesgDispatcher dispatcher,
            InetAddress localAddr, int localPort,
            JMXAverageMonitorReport stats, long heartBeat) throws IOException {
        super(name, heartBeat);
        this.locch=new LocalTransport(dispatcher, this, stats);
        this.udpch=new UDPTransport(resolver, dispatcher, this, localAddr, localPort, stats);
        this.tcpch=new TCPTransport(resolver, dispatcher, this, localAddr, localPort, stats);
        setProperty(Realm.LOCAL, UDPAddress.PROPERTY_UDP_ADDR, udpch.getAddress().getHostAddress());
        setProperty(Realm.LOCAL, UDPAddress.PROPERTY_UDP_PORT, Integer.toString(udpch.getPort()));
        setProperty(Realm.LOCAL, TCPAddress.PROPERTY_TCP_ADDR, tcpch.getAddress().getHostAddress());
        setProperty(Realm.LOCAL, TCPAddress.PROPERTY_TCP_PORT, Integer.toString(tcpch.getPort()));
        setProperty(Realm.LOCAL, PROPERTY_HEART_BEAT, Long.toString(heartBeat));
        setProperty(Realm.LOCAL, PROPERTY_HEART_BEAT, Long.toString(heartBeat));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> c) {
        if(c.isAssignableFrom(UDPAddress.class)) return (T) this;
        if(c.isAssignableFrom(TCPAddress.class)) return (T) this;
        return null;
    }

    public InetAddress addrTCP() {
        return tcpch.getAddress();
    }

    public int portTCP() {
        return tcpch.getPort();
    }

    public InetAddress addrUDP() {
        return udpch.getAddress();
    }

    public int portUDP() {
        return udpch.getPort();
    }

    public boolean call(String method, MesgPayload message, MesgReceiver handler) throws IOException {
        return locch.call(this, this, method, message, handler);
    }

    public boolean repl(String method, MesgPayload message, long sequence) throws IOException {
        return locch.repl(this, this, method, message, sequence);
    }

    @Override
    public <T extends Listenable> void addListener(UpdateListener<T> list) {
        if(list instanceof HostListener) hostUL.add((HostListener) list);
    }

    protected void updHost() {
        for(HostListener l: hostUL) l.update(this);
    }

    LocalTransport getLocChannel() {
        return locch;
    }

    UDPTransport getUDPChannel() {
        return udpch;
    }

    TCPTransport getTCPChannel() {
        return tcpch;
    }

    @Override
    public synchronized void start() {
        if(state!=State.STOPPED) return;
        state = State.STARTING;
        locch.start();
        updHost();
        try { udpch.start(); }
        catch(IOException e) { /* ignore: non-fatal */ }
        try { tcpch.start(); }
        catch(IOException e) { /* ignore: non-fatal */ }
        state = State.RUNNING;
        updHost();
    }

    @Override
    public synchronized void stop() {
        if(state!=State.RUNNING) return;
        state = State.STOPPING;
        updHost();
        try { tcpch.stop(); }
        catch(IOException e) { /* ignore*/ }
        try { udpch.stop(); }
        catch(IOException e) { /* ignore*/ }
        locch.stop();
        state = State.STOPPED;
        updHost();
    }

    @Override
    public State state() { return state; }

    public void setHeartBeat(long heartBeat) {
        this.heartBeat=heartBeat;
        setProperty(Realm.LOCAL, PROPERTY_HEART_BEAT, Long.toString(heartBeat));
        updHost();
    }
}
