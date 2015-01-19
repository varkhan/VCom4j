package net.varkhan.serv.p2p.connect;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/23/11
 * @time 11:03 PM
 */
public interface PeerAddress {

    public String name();

    public <T> T as(Class<T> c);

}
