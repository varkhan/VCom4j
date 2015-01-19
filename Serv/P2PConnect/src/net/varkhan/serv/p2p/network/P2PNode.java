package net.varkhan.serv.p2p.network;

import net.varkhan.serv.p2p.Listenable;
import net.varkhan.serv.p2p.connect.PeerNode;
import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.connect.PeerProperties;
import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.message.dispatch.MesgReceiver;
import net.varkhan.serv.p2p.connect.config.MapProperties;

import java.io.IOException;



/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 7:19 AM
 */
public abstract class P2PNode extends MapProperties implements PeerAddress, PeerProperties, PeerNode {

    /**
     * Default heartbeat: every 10s
     */
    public static final long DEFAULT_HEARTBEAT=10000;
    public static final String PROPERTY_HEART_BEAT="HeartBeat";

    protected final String name;

    public P2PNode(String name, PeerProperties props) {
        super(props);
        this.name=name;
    }


    public P2PNode(String name) {
        super();
        this.name=name;
    }

    public String name() { return name; }

    public abstract boolean call(String method, MesgPayload message, MesgReceiver handler) throws IOException;

    public abstract boolean repl(String method, MesgPayload message, long sequence) throws IOException;

    public abstract <T extends Listenable> void addListener(UpdateListener<T> list);

}
