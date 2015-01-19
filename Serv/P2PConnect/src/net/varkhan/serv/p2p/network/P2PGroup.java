package net.varkhan.serv.p2p.network;

import net.varkhan.serv.p2p.Listenable;
import net.varkhan.serv.p2p.connect.*;
import net.varkhan.serv.p2p.connect.config.MapProperties;
import net.varkhan.serv.p2p.connect.transport.MTXAddress;
import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.message.transport.MTXTransport;

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
 * @time 7:22 AM
 */
public class P2PGroup extends P2PNode implements PeerNode, PeerProperties, PeerGroup, MTXAddress {

    private long lastUpdate;
    private long minHeartBeat;
    private long maxHeartBeat;

    private P2PHub       hub;
    private P2PLocalHost here;

    private final ConcurrentMap<MTXTransport,Set<P2PHost>> channels=new ConcurrentHashMap<MTXTransport,Set<P2PHost>>();
    private final Set<GroupListener>                       groupUL =new CopyOnWriteArraySet<GroupListener>();

    public P2PGroup(String name, PeerProperties props, P2PHub hub) {
        super(name, props);
        this.here=hub.local();
        this.hub=hub;
        lastUpdate=System.currentTimeMillis();
    }

    private MTXTransport getPreferredChannel() {
        MTXTransport channel=null;
        long num=0;
        for(Map.Entry<MTXTransport,Set<P2PHost>> e : channels.entrySet()) {
            long n=e.getValue().size();
            if(n>num) {
                num=n;
                channel=e.getKey();
            }
        }
        return channel;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T as(Class<T> c) {
        if(c.isAssignableFrom(MTXAddress.class)) return (T) this;
        return null;
    }

    public InetAddress addrMTXgroup() {
        MTXTransport c=getPreferredChannel();
        if(c==null) return null;
        return c.getGroupAddr();
    }

    public int portMTXgroup() {
        MTXTransport c=getPreferredChannel();
        if(c==null) return 0;
        return c.getGroupPort();
    }

    public State state() {
        if(!channels.isEmpty()) return State.RUNNING;
        return State.STOPPED;
    }

    public void start() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    public void stop() {
        for(Iterator<MTXTransport> iterator=channels.keySet().iterator();iterator.hasNext();) {
            MTXTransport c=iterator.next();
            try { c.stop(); }
            catch(IOException e) { /* ignore*/ }
            iterator.remove();
        }
    }

    public boolean call(String method, MesgPayload message, MesgReceiver handler) throws IOException {
        for(Map.Entry<MTXTransport,Set<P2PHost>> e: channels.entrySet()) {
            MTXTransport c = e.getKey();
            try {
                if(c.call(here, this, method, message, handler)) continue;
            }
            catch(IOException x) { /* Go on next alternative */ }
            // We couldn't send as multicast. This can happen for a number of reasons:
            // improper configuration, unreachable nodes, or message too large.
            // We will try to resend to each node individually.
            Set<P2PHost> m=e.getValue();
            for(P2PHost p: m) {
                try { p.call(method, message, handler); }
                catch(IOException x) { /* Ignore errors on individual nodes */ }
            }
        }
        // Group calls always succeed
        return true;
    }

    public boolean repl(String method, MesgPayload message, long sequence) throws IOException {
        // Replies are never sent to a group
        return false;
    }

    @Override
    public <T extends Listenable> void addListener(UpdateListener<T> list) {
        if(list instanceof GroupListener) groupUL.add((GroupListener) list);
    }

    protected void addHost(PeerHost p) {
        for(Iterator<GroupListener> iterator=groupUL.iterator();iterator.hasNext();) {
            GroupListener ul=iterator.next();
            try { ul.addHost(this, p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
    }

    protected void delHost(PeerHost p) {
        for(Iterator<GroupListener> iterator=groupUL.iterator();iterator.hasNext();) {
            GroupListener ul=iterator.next();
            try { ul.delHost(this, p); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
    }

    protected void updGroup() {
        for(Iterator<GroupListener> iterator=groupUL.iterator();iterator.hasNext();) {
            GroupListener ul=iterator.next();
            try { ul.update(this); }
            catch(Throwable t) { /* ignore */ }
            try { if(ul.finished()) iterator.remove(); }
            catch(Throwable t) { /* ignore */ }
        }
    }

    public Collection<PeerHost> hosts() {
        Set<PeerHost> m = new HashSet<PeerHost>();
        for(Set<P2PHost> s: channels.values()) m.addAll(s);
        return m;
    }

    public void update(P2PHost host, PeerProperties props) {
        lastUpdate = System.currentTimeMillis();
        setProperties(props);
        checkChannels(host, props);
    }

    private void checkChannels(P2PHost host, PeerProperties props) {
        try {
            String mtxName=props.getProperty(Realm.LOCAL, name+'.'+PROPERTY_MTX_ADDR);
            InetAddress mtxAddr=InetAddress.getByName(mtxName);
            int mtxPort=props.getProperty(Realm.LOCAL, name+'.'+PROPERTY_MTX_PORT, -1);
            if(mtxAddr==null || mtxPort<0) return;
            for(Map.Entry<MTXTransport,Set<P2PHost>> e: channels.entrySet()) {
                MTXTransport c=e.getKey();
                if(c.getGroupAddr().equals(mtxAddr) && c.getGroupPort()==mtxPort) return;
            }
            MTXTransport ch=hub.getMtxChannel(name, mtxPort, mtxAddr);
            channels.putIfAbsent(ch,new CopyOnWriteArraySet<P2PHost>(Arrays.asList(host)));
        }
        catch(IOException e) {
            // ignore this update
        }
    }

    public boolean isStale() {
        long age=System.currentTimeMillis()-lastUpdate;
        return age>=3*minHeartBeat || age>=2*maxHeartBeat;
    }

    public Collection<MTXTransport> getMTXChannels() {
        return channels.keySet();
    }

    public static MapProperties getProperties(MapProperties props, String name, InetAddress mtxAddr, int mtxPort) {
        props.setProperty(Realm.LOCAL, name+'.'+PROPERTY_MTX_ADDR, mtxAddr.getHostAddress());
        props.setProperty(Realm.LOCAL, name+'.'+PROPERTY_MTX_PORT, Long.toString(mtxPort));
        return props;
    }

}
