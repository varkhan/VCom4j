package net.varkhan.serv.p2p.network;

import net.varkhan.serv.p2p.connect.PeerGroup;
import net.varkhan.serv.p2p.connect.PeerHost;
import net.varkhan.serv.p2p.connect.PeerNode;
import net.varkhan.serv.p2p.connect.PeerProperties;

import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/17/15
 * @time 4:48 PM
 */
public abstract class P2PHost extends P2PNode implements PeerNode, PeerProperties, PeerHost {
    protected ConcurrentMap<String,P2PGroup> groups   =new ConcurrentHashMap<String,P2PGroup>();
    protected long                           heartBeat=DEFAULT_HEARTBEAT;

    public P2PHost(String name, long heartBeat) {
        super(name);
        this.heartBeat=heartBeat;
        setProperty(Realm.LOCAL, PROPERTY_HEART_BEAT, Long.toString(heartBeat));
    }

    public P2PHost(String name, PeerProperties props, long heartBeat) {
        super(name, props);
        this.heartBeat=heartBeat;
    }

    public Collection<P2PGroup> groups() {
        return Collections.unmodifiableCollection(groups.values());
    }

    @Override
    public abstract void start();

    @Override
    public abstract void stop();

    public abstract State state();

    public long getHeartBeat() { return heartBeat; }

    public void addGroup(PeerGroup group) {
        groups.putIfAbsent(group.name(), (P2PGroup) group);
    }

    public void delGroup(PeerGroup g) {
        groups.remove(g.name(),g);
    }
}
