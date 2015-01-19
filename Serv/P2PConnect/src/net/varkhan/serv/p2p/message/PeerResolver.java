package net.varkhan.serv.p2p.message;

import net.varkhan.serv.p2p.connect.PeerAddress;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 1:36 AM
 */
public interface PeerResolver {

    public PeerAddress resolve(String name);

    public PeerAddress update(String name, MesgPayload data);

    public MesgPayload info();
}
