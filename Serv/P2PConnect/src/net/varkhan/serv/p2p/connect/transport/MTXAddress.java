package net.varkhan.serv.p2p.connect.transport;

import net.varkhan.serv.p2p.connect.PeerAddress;

import java.net.InetAddress;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 1:47 AM
 */
public interface MTXAddress extends PeerAddress {

    public static String PROPERTY_MTX_ADDR="GroupAddr";
    public static String PROPERTY_MTX_PORT="GroupPort";

    public InetAddress addrMTXgroup();

    public int portMTXgroup();

}
