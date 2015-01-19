package net.varkhan.serv.p2p.network;

import net.varkhan.serv.p2p.Listenable;
import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.transport.TCPTransport;
import net.varkhan.serv.p2p.message.transport.UDPTransport;
import net.varkhan.serv.p2p.connect.PeerHost;
import net.varkhan.serv.p2p.connect.PeerNode;
import net.varkhan.serv.p2p.connect.PeerProperties;
import net.varkhan.serv.p2p.connect.config.MapProperties;
import net.varkhan.serv.p2p.connect.transport.TCPAddress;
import net.varkhan.serv.p2p.connect.transport.UDPAddress;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
public class P2PRemoteHost extends P2PHost implements PeerNode, PeerProperties, PeerHost, UDPAddress, TCPAddress {

    private InetAddress udpAddr;
    private int         udpPort;
    private InetAddress tcpAddr;
    private int         tcpPort;
    private long        lastUpdate;
    private long        heartBeat;

    private final P2PLocalHost here;
    private final UDPTransport udpch;
    private final TCPTransport tcpch;
    private volatile State state=State.STOPPED;

    private final Set<HostListener> hostUL=new CopyOnWriteArraySet<HostListener>();

    public P2PRemoteHost(String name, PeerProperties props, P2PLocalHost here, UDPTransport udpch, TCPTransport tcpch) {
        super(name, props, props.getProperty(Realm.LOCAL, PROPERTY_HEART_BEAT, here.getHeartBeat()));
        this.here=here;
        this.udpch=udpch;
        this.tcpch=tcpch;
        lastUpdate=System.currentTimeMillis();
    }

    @Override
    public void setProperty(Realm realm, String name, String value) {
        if(realm==Realm.LOCAL) {
            setLocalProperty(name, value);
        }
        super.setProperty(realm, name, value);
        notifyListeners();
    }

    private void setLocalProperty(String name, String value) {
        if(UDPAddress.PROPERTY_UDP_ADDR.equals(name)) {
            try { udpAddr=InetAddress.getByName(value); }
            catch(UnknownHostException e) { udpAddr=null; }
        }
        else if(UDPAddress.PROPERTY_UDP_PORT.equals(name)) {
            udpPort=Integer.parseInt(value);
        }
        else if(TCPAddress.PROPERTY_TCP_ADDR.equals(name)) {
            try { tcpAddr=InetAddress.getByName(value); }
            catch(UnknownHostException e) { tcpAddr=null; }
        }
        else if(TCPAddress.PROPERTY_TCP_PORT.equals(name)) {
            tcpPort=Integer.parseInt(value);
        }
    }

    @Override
    public void setProperties(Realm realm, PeerProperties props) {
        if(realm==Realm.LOCAL) {
            for(String name : props.getPropertyNames()) {
                setLocalProperty(name, props.getProperty(Realm.LOCAL, name));
            }
        }
        super.setProperties(realm, props);
        notifyListeners();
    }

    @Override
    public void setProperties(PeerProperties props) {
        for(Realm realm: Realm.values()) {
            if(realm==Realm.LOCAL) for(String name: props.getPropertyNames()) {
                setLocalProperty(name, props.getProperty(realm, name));
            }
            super.setProperties(realm, props);
        }
        notifyListeners();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> c) {
        if(c.isAssignableFrom(UDPAddress.class)) return (T) this;
        if(c.isAssignableFrom(TCPAddress.class)) return (T) this;
        return null;
    }

    public InetAddress addrTCP() {
        return tcpAddr;
    }

    public int portTCP() {
        return tcpPort;
    }

    public InetAddress addrUDP() {
        return udpAddr;
    }

    public int portUDP() {
        return udpPort;
    }

    public boolean call(String method, MesgPayload message, MesgReceiver handler) throws IOException {
        if(udpch.call(here, this, method, message, handler)) return true;
        if(tcpch.call(here, this, method, message, handler)) return true;
        return false;
    }

    public boolean repl(String method, MesgPayload message, long sequence) throws IOException {
        if(udpch.repl(here, this, method, message, sequence)) return true;
        if(tcpch.repl(here, this, method, message, sequence)) return true;
        return false;
    }

    public void update(MapProperties props) {
        lastUpdate = System.currentTimeMillis();
        setProperties(props);
        notifyListeners();
    }

    public long getHeartBeat() { return heartBeat; }

    public boolean isStale() {
        long age=System.currentTimeMillis()-lastUpdate;
        return age>=3*heartBeat;
    }

    @Override
    public <T extends Listenable> void addListener(UpdateListener<T> list) {
        if(list instanceof HostListener) hostUL.add((HostListener) list);
    }

    private void notifyListeners() {
        for(HostListener l: hostUL) l.update(this);
    }

    @Override
    public synchronized void start() {
        if(state!=State.STOPPED) return;
        state = State.RUNNING;
        notifyListeners();
    }

    @Override
    public synchronized void stop() {
        if(state!=State.RUNNING) return;
        state = State.STOPPED;
        notifyListeners();
    }

    @Override
    public State state() { return state; }

}
