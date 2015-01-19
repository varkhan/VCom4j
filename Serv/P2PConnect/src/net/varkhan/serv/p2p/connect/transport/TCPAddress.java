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
public interface TCPAddress extends PeerAddress {

    public static String PROPERTY_TCP_ADDR="TcpAddr";
    public static String PROPERTY_TCP_PORT="TcpPort";

    public InetAddress addrTCP();

    public int portTCP();

}
