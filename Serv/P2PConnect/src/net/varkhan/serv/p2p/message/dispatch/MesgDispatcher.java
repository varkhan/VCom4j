package net.varkhan.serv.p2p.message.dispatch;

import net.varkhan.serv.p2p.message.MesgPayload;
import net.varkhan.serv.p2p.connect.PeerAddress;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 12:18 AM
 */
public interface MesgDispatcher {

    public long register(MesgReceiver handler);

    public void unregister(long sequence);

    public void call(PeerAddress src, PeerAddress dst, String method, MesgPayload call, MesgPayload repl, long sequence, MesgSender send);

    public void repl(PeerAddress src, PeerAddress dst, String method, MesgPayload repl, long sequence);

}
