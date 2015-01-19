package net.varkhan.serv.p2p.connect;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/30/11
 * @time 3:39 AM
 */
public interface PeerHost extends PeerNode {

    public long getHeartBeat();

    public Collection<? extends PeerGroup> groups();

    public static interface HostListener extends PeerNode.UpdateListener<PeerHost> {

        public void update(PeerHost host);

    }

}
