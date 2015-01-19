package net.varkhan.serv.p2p.connect;

import java.util.Set;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 7:29 AM
 */
public interface PeerProperties {

    public String getProperty(String name);

    public String getProperty(Realm realm, String name);

    public int getProperty(String name, int def);

    public int getProperty(Realm realm, String name, int def);

    public long getProperty(String name, long def);

    public long getProperty(Realm realm, String name, long def);


    public PeerProperties getProperties(Realm realm);

    public Set<String> getPropertyNames();

    public Set<String> getPropertyNames(Realm realm);

    public enum Realm {
        LOCAL,
        GLOBAL,
        USER,
    }

}
