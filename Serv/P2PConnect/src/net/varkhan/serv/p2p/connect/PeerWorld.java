package net.varkhan.serv.p2p.connect;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/30/11
 * @time 11:54 AM
 */
public interface PeerWorld extends PeerGroup {

    public Collection<? extends PeerHost> hosts();

    public Collection<? extends PeerGroup> groups();

    public static interface AllListener extends PeerNode.UpdateListener<PeerGroup> {

        public void update(PeerWorld all);

        public void addGroup(PeerWorld all, PeerGroup group);

        public void delGroup(PeerWorld all, PeerGroup group);

        public void addHost(PeerWorld all, PeerHost host);

        public void delHost(PeerWorld all, PeerHost host);

    }

}
