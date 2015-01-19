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
public interface UDPAddress extends PeerAddress {

    public static String PROPERTY_UDP_ADDR="UdpAddr";
    public static String PROPERTY_UDP_PORT="UdpPort";

    public InetAddress addrUDP();

    public int portUDP();

}
