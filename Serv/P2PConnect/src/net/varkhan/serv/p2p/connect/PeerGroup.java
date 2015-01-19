package net.varkhan.serv.p2p.connect;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/30/11
 * @time 3:37 AM
 */
public interface PeerGroup extends PeerNode {

    public Collection<? extends PeerHost> hosts();

    public static interface GroupListener extends PeerNode.UpdateListener<PeerGroup> {

        public void update(PeerGroup point);

        public void addHost(PeerGroup group, PeerHost host);

        public void delHost(PeerGroup group, PeerHost host);

    }
}
