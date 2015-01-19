package net.varkhan.serv.p2p;

import net.varkhan.serv.p2p.connect.PeerAddress;
import net.varkhan.serv.p2p.connect.PeerProperties;
import net.varkhan.serv.p2p.connect.PeerWorld;
import net.varkhan.serv.p2p.connect.PeerHost;
import net.varkhan.serv.p2p.message.PeerResolver;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/23/11
 * @time 10:58 PM
 */
public interface PeerHub extends PeerResolver, PeerAddress {

    public PeerHost local();

    public PeerWorld world();

    public void ping(PeerAddress dst);

    public void join(String group, PeerProperties props);

    public void leave(String group);

    public String set(String var, String val);

    public String get(String var);

}
