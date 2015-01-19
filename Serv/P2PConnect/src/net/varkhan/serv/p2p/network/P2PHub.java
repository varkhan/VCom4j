package net.varkhan.serv.p2p.network;

import net.varkhan.base.management.report.JMXAverageMonitorReport;
import net.varkhan.serv.p2p.Listenable;
import net.varkhan.serv.p2p.PeerHub;
import net.varkhan.serv.p2p.connect.PeerNode;
import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.connect.PeerProperties;
import net.varkhan.serv.p2p.connect.PeerGroup;
import net.varkhan.serv.p2p.connect.PeerHost;
import net.varkhan.serv.p2p.connect.config.MapProperties;
import net.varkhan.serv.p2p.connect.transport.MTXAddress;
import net.varkhan.serv.p2p.connect.PeerWorld;
import net.varkhan.serv.p2p.message.dispatch.MesgDispatcher;
import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.PeerResolver;
import net.varkhan.serv.p2p.message.transport.MTXTransport;
import net.varkhan.serv.p2p.message.protocol.BinaryPayload;
import net.varkhan.serv.p2p.connect.protocol.BinaryPropertiesSerializer;
import net.varkhan.serv.p2p.connect.transport.UDPAddress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 11:49 PM
 */
public class P2PHub extends P2PNode implements PeerNode, PeerProperties, PeerWorld, MTXAddress, /*UDPAddress,*/ PeerHub, PeerResolver {

    private final P2PLocalHost            local;
    private final MTXTransport            glmtx;
//    private final UDPTransport            gludp;
    private final JMXAverageMonitorReport stats;
    private final ConcurrentMap<String,P2PRemoteHost> hosts =new ConcurrentHashMap<String,P2PRemoteHost>();
    private final ConcurrentMap<String,P2PGroup>      groups=new ConcurrentHashMap<String,P2PGroup>();

    private final Set<PeerWorld.AllListener>   worldUL=new CopyOnWriteArraySet<PeerWorld.AllListener>();
    private final Set<PeerGroup.GroupListener> groupUL=new CopyOnWriteArraySet<PeerGroup.GroupListener>();
    private final Set<PeerHost.HostListener>   hostUL =new CopyOnWriteArraySet<PeerHost.HostListener>();
    private final InetAddress    localAddr;
    private final InetAddress    bcastAddr = InetAddress.getByName("255.255.255.255");
    private final MesgDispatcher dispatcher;

    private long   lastUpdate;
    private Thread thread;
    private volatile State state=State.STOPPED;

    public P2PHub(String name, MesgDispatcher dispatcher, InetAddress localAddr, int localPort, InetAddress globalAddr, int globalPort, long heartBeat) throws IOException {
        super("*");
        stats=new JMXAverageMonitorReport(10, name);
        this.localAddr=localAddr;
        this.dispatcher=dispatcher;
        local=new P2PLocalHost(name, this, dispatcher, localAddr, localPort, stats, heartBeat);
        glmtx=new MTXTransport(this, dispatcher, local, localAddr, globalAddr, globalPort, stats);
//        gludp=new UDPTransport(this, dispatcher, local, localAddr, globalPort+1, stats);
        setProperty(Realm.LOCAL, PROPERTY_MTX_ADDR, globalAddr.getHostAddress());
        setProperty(Realm.LOCAL, PROPERTY_MTX_PORT, Long.toString(globalPort));
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> c) {
        if(MTXAddress.class==c) return (T) this;
        if(UDPAddress.class==c) return (T) this;
        return null;
    }

    @Override
    public void join(String group, PeerProperties props) {
        P2PGroup g=groups.get(group);
        if(g==null) try {
            g=new P2PGroup(group, props, this);
        }
        catch(Exception e) {
            throw new RuntimeException("Invalid group properties "+props, e);
        }
        if(groups.putIfAbsent(group, g)!=null) g=groups.get(group);
        else {
            g.update(local, props);
        }
        if(g!=null) local.addGroup(g);
        addHost(g, local);
        updGroup(g);
    }

    public MTXTransport getMtxChannel(String group, int mtxPort, InetAddress mtxAddr) throws IOException {
        return new MTXTransport(this, dispatcher, local, localAddr, mtxAddr, mtxPort, stats);
    }

    @Override
    public void leave(String group) {
        P2PGroup g=groups.get(group);
        if(g==null) return;
        if(g.hosts().isEmpty()) groups.remove(group);
        local.delGroup(g);
        delHost(g, local);
        updGroup(g);
    }

    @Override
    public String set(String var, String val) {
        return val;
    }

    @Override
    public String get(String var) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public PeerWorld world() {
        return this;
    }

    public InetAddress addrMTXgroup() {
        return glmtx.getGroupAddr();
    }

    public int portMTXgroup() {
        return glmtx.getGroupPort();
    }

//    @Override
//    public InetAddress addrUDP() {
//        return bcastAddr;
//    }
//
//    @Override
//    public int portUDP() {
//        return gludp.getPort();
//    }

    private static int threadNumber;

    private static synchronized int nextThreadNumber() {
        return threadNumber++;
    }

    public synchronized void start() {
        state=State.STARTING;
        if(thread!=null) return;
        local.start();
        try {
            glmtx.start();
//            gludp.start();
        }
        catch(IOException e) {
            // This we can't recover from: there is no way to have a hub working properly without this
            try { local.stop(); }
            catch(Exception x) { /* ignore*/ }
            return;
        }
        thread=new WpHeartBeat();
        thread.setDaemon(true);
        thread.setName(P2PHub.class.getSimpleName()+"-"+nextThreadNumber());
        thread.start();
        state=State.RUNNING;
    }

    public boolean isStarted() { return thread!=null; }

    public void stop() { stop(local.getHeartBeat()); }

    public synchronized void stop(long timeout) {
        state=State.STOPPING;
        Thread t=thread;
        if(t==null) return;
        thread=null;
        t.interrupt();
        try { t.join(timeout); }
        catch(InterruptedException e) { /* ignore */ }
        // Shut down all group channels
        for(P2PGroup g : groups.values()) {
            g.stop();
        }
        try {
//            gludp.stop();
            glmtx.stop();
        }
        catch(IOException e) { /* ignore*/ }
        local.stop();
        state=State.STOPPED;
    }

    public Collection<PeerHost> hosts() {
        return new HashSet<PeerHost>(hosts.values());
    }

    public Collection<? extends PeerGroup> groups() {
        return new HashSet<PeerGroup>(groups.values());
    }

    public boolean call(String method, MesgPayload message, MesgReceiver handler) throws IOException {
        if(glmtx.call(local, this, method, message, handler)) return true;
//        if(gludp.call(local, this, method, message, handler)) return true;
        for(Map.Entry<String,P2PRemoteHost> e : hosts.entrySet()) {
            P2PNode p=e.getValue();
            try { p.call(method, message, handler); }
            catch(IOException x) { /* Ignore errors on individual nodes */ }
        }
        // Group calls always succeed
        return true;
    }

    public boolean repl(String method, MesgPayload message, long sequence) throws IOException {
        // Replies are never sent to a group
        return false;
    }

    @Override
    public <T extends Listenable> void addListener(Listenable.UpdateListener<T> list) {
        if(list instanceof PeerHost.HostListener) hostUL.add((PeerHost.HostListener) list);
        if(list instanceof PeerGroup.GroupListener) groupUL.add((PeerGroup.GroupListener) list);
        if(list instanceof PeerHost.HostListener) hostUL.add((PeerHost.HostListener) list);
    }

    public State state() {
        return state;
    }

    private void updHost(PeerHost p) {
        for(Iterator<PeerHost.HostListener> iterator=hostUL.iterator();iterator.hasNext();) {
            PeerHost.HostListener ul=iterator.next();
            try { ul.update(p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
    }

    private void addHost(PeerHost p) {
        hosts.putIfAbsent(p.name(), (P2PRemoteHost) p);
        for(Iterator<PeerWorld.AllListener> iterator=worldUL.iterator();iterator.hasNext();) {
            PeerWorld.AllListener ul=iterator.next();
            try { ul.addHost(this, p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
        for(PeerGroup g : p.groups()) {
            addHost(g, p);
        }
    }

    private void addHost(PeerGroup g, PeerHost p) {
        for(Iterator<GroupListener> iterator=groupUL.iterator();iterator.hasNext();) {
            GroupListener ul=iterator.next();
            try { ul.addHost(g, p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
        if(g instanceof P2PGroup) ((P2PGroup) g).addHost(p);
    }

    private void delHost(PeerHost p) {
        hosts.remove(p.name());
        for(PeerGroup g : p.groups()) {
            delHost(g, p);
        }
        for(Iterator<PeerWorld.AllListener> iterator=worldUL.iterator();iterator.hasNext();) {
            PeerWorld.AllListener ul=iterator.next();
            try { ul.delHost(this, p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
    }

    private void delHost(PeerGroup g, PeerHost p) {
        for(Iterator<GroupListener> iterator=groupUL.iterator();iterator.hasNext();) {
            GroupListener ul=iterator.next();
            try { ul.delHost(g, p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
        if(g instanceof P2PGroup) ((P2PGroup) g).delHost(p);
    }

    private void updGroup(PeerGroup g) {
        for(Iterator<GroupListener> iterator=groupUL.iterator();iterator.hasNext();) {
            GroupListener ul=iterator.next();
            try { ul.update(g); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
        if(g instanceof P2PGroup) ((P2PGroup) g).updGroup();
    }

    private void addGroup(PeerGroup p) {
        groups.putIfAbsent(p.name(), (P2PGroup) p);
        for(Iterator<PeerWorld.AllListener> iterator=worldUL.iterator();iterator.hasNext();) {
            PeerWorld.AllListener ul=iterator.next();
            try { ul.addGroup(this, p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
    }

    private void delGroup(PeerGroup p) {
        groups.remove(p.name());
        for(Iterator<PeerWorld.AllListener> iterator=worldUL.iterator();iterator.hasNext();) {
            PeerWorld.AllListener ul=iterator.next();
            try { ul.delGroup(this, p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
    }

    public P2PLocalHost local() { return local; }

    @Override
    public PeerAddress resolve(String name) {
        if(name==null || name.length()==0 || local.name().equals(name)) return local;
        if(this.name().equals(name)) return this;
        PeerAddress p = hosts.get(name);
        if(p!=null) return p;
        PeerAddress g = groups.get(name);
        if(g!=null) return g;
        return null;
    }

    private final class WpHeartBeat extends Thread {
        public void run() {
            long time = System.currentTimeMillis();
            // Send first ping
            ping(P2PHub.this);

            // Start cleanup / update cycle
            while(thread!=null) {
                long period = local.getHeartBeat();
                // Sleep until next heartbeat, or awoken
                long elapsed = System.currentTimeMillis()-time;
                if(elapsed<period) try { Thread.sleep(period-elapsed); }
                catch(InterruptedException e) { if(thread==null) return; }

                // By now, we should have gotten answers to our last ping
                time = System.currentTimeMillis();
                // Discard stale nodes
                for(Iterator<P2PRemoteHost> iterator=hosts.values().iterator();iterator.hasNext();) {
                    P2PRemoteHost p=iterator.next();
                    try {
                        if(p.isStale()) {
                            iterator.remove();
                            delHost(p);
                        }
                    }
                    catch(Throwable t) {
                        // Failing nodes are forcibly removed
                        iterator.remove();
                        delHost(p);
                    }
                }
                // Discard stale groups
                for(Iterator<P2PGroup> iterator=groups.values().iterator();iterator.hasNext();) {
                    P2PGroup p=iterator.next();
                    try {
                        if(p.isStale()) delGroup(p);
                    }
                    catch(Throwable t) {
                        // Failing groups are forcibly removed
                        iterator.remove();
                        delGroup(p);
                    }
                }

                // Send another ping
                ping(P2PHub.this);
            }
        }
    }

    public void ping(PeerAddress dst) {
        try {
            glmtx.ping(local, dst, "", info(), 1);
//            gludp.ping(local, dst, "", info(), 1);
        }
        catch(IOException e) {
            stats.inc(P2PHub.class.getSimpleName()+".Ping.error["+e.getClass().getSimpleName()+"]");
        }
    }

    @Override
    public PeerAddress update(String pname, MesgPayload data) {
        if(local.name().equals(pname)) return local;
        lastUpdate = System.currentTimeMillis();
        P2PRemoteHost host = this.hosts.get(pname);
        MapProperties props = toProperties(data);
        if(host!=null) {
            host.update(props);
            updHost(host);
        }
        else {
            host = new P2PRemoteHost(pname,props, local, local.getUDPChannel(), local.getTCPChannel());
            if(this.hosts.putIfAbsent(pname,host)!=null) {
                host = this.hosts.get(pname);
                if(host!=null) {
                    host.update(props);
                    updHost(host);
                }
            }
            else {
                addHost(host);
            }
        }
        // Case where location was concurrently removed
        if(host==null) return null;
        Collection<P2PGroup> groups = host.groups();
        if(groups!=null && !groups.isEmpty()) for(P2PGroup group: groups) {
            String gname = group.name();
            P2PGroup g = this.groups.get(gname);
            if(g!=null) {
                g.update(host,props);
                updGroup(g);
            }
            else {
                g = new P2PGroup(gname, props, this);
                if(this.groups.putIfAbsent(gname,g)!=null) {
                    g = this.groups.get(gname);
                    // Do a group update to get channels set up
                    g.update(host,props);
                    updGroup(g);
                }
                else {
                    // Do a group update to get channels set up
                    g.update(host,props);
                    addGroup(g);
                }
            }
        }
        return host;
    }

    @Override
    public MesgPayload info() {
        return toMessage(local);
    }


    private static final BinaryPropertiesSerializer<Object> ptser=new BinaryPropertiesSerializer<Object>();

    public MesgPayload toMessage(MapProperties point) {
        BinaryPayload msg=new BinaryPayload();
        ByteArrayOutputStream os=new ByteArrayOutputStream();
        ptser.encode(point, os, null);
        msg.setData(os.toByteArray());
        return msg;
    }

    public MapProperties toProperties(MesgPayload mesg) {
        try {
            return ptser.decode(mesg.getDataAsStream(), null);
        }
        catch(IOException e) {
            return null;
        }
    }

}
