/**
 *
 */
package net.varkhan.serv.p2p.message.dispatch;

import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.connect.PeerAddress;


/**
 * <b>.</b>
 * <p/>
 * @author varkhan
 * @date Nov 12, 2009
 * @time 12:26:18 AM
 */
public interface MesgAction {

    public void invoke(PeerAddress src, PeerAddress dst, String name, MesgPayload call, MesgPayload repl);

}
