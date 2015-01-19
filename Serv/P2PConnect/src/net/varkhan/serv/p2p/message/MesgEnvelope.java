package net.varkhan.serv.p2p.message;

import net.varkhan.serv.p2p.connect.PeerAddress;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/30/11
 * @time 8:14 AM
 */
public interface MesgEnvelope {

    public PeerAddress src();

    public PeerAddress dst();

    public String method();

    public MesgPayload message();

}
