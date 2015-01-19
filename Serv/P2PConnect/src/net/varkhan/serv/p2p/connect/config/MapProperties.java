package net.varkhan.serv.p2p.connect.config;

import net.varkhan.serv.p2p.connect.PeerProperties;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 5/29/11
 * @time 7:19 AM
 */
public class MapProperties implements PeerProperties {

    private final RealmProperties[] proptable;

    public MapProperties() {
        Realm[] realms=Realm.values();
        this.proptable= new RealmProperties[realms.length];
        for(int i=0; i<proptable.length; i++) proptable[i] = new RealmProperties(realms[i]);
    }

    public MapProperties(PeerProperties props) {
        this();
        setProperties(props);
    }

    public String getProperty(String name) {
        for(RealmProperties r: proptable) {
            String p = r.getProperty(name);
            if(p!=null) return p;
        }
        return null;
    }

    public String getProperty(Realm realm, String name) {
        return proptable[realm.ordinal()].getProperty(name);
    }

    @Override
    public int getProperty(String name, int def) {
        for(RealmProperties r: proptable) {
            String p = r.getProperty(name);
            if(p!=null)
                try { return Integer.parseInt(p); }
                catch(NumberFormatException e) { return def; }
        }
        return def;
    }

    @Override
    public int getProperty(Realm realm, String name, int def) {
        String p = proptable[realm.ordinal()].getProperty(name);
        try { return Integer.parseInt(p); }
        catch(NumberFormatException e) { return def; }
    }

    @Override
    public long getProperty(String name, long def) {
        for(RealmProperties r: proptable) {
            String p = r.getProperty(name);
            if(p!=null)
                try { return Long.parseLong(p); }
                catch(NumberFormatException e) { return def; }
        }
        return def;
    }

    @Override
    public long getProperty(Realm realm, String name, long def) {
        String p = proptable[realm.ordinal()].getProperty(name);
        try { return Long.parseLong(p); }
        catch(NumberFormatException e) { return def; }
    }

    public PeerProperties getProperties(Realm realm) {
        return proptable[realm.ordinal()];
    }

    public Set<String> getPropertyNames() {
        Set<String> names = new HashSet<String>();
        for(RealmProperties r: proptable) names.addAll(r.getPropertyNames());
        return names;
    }

    public Set<String> getPropertyNames(Realm realm) {
        return proptable[realm.ordinal()].getPropertyNames();
    }

    public void setProperty(Realm realm, String name, String value) {
        proptable[realm.ordinal()].setProperty(name,value);
    }

    public void setProperties(Realm realm, PeerProperties props) {
        proptable[realm.ordinal()].setProperties(props);
    }

    public void setProperties(PeerProperties props) {
        for(RealmProperties r: proptable) r.setProperties(props);
    }


    private static class RealmProperties implements PeerProperties {
        public final Realm realm;
        private final Map<String,String> map= new HashMap<String,String>();

        private RealmProperties(Realm realm) { this.realm=realm; }
        public Realm realm() { return realm; }

        public String getProperty(String name) {
            return map.get(name);
        }

        public void setProperty(String name,String value) {
            map.put(name, value);
        }

        public void setProperties(PeerProperties props) {
            for(String n: props.getPropertyNames(realm)) this.map.put(n, props.getProperty(realm, n));
        }

        public String getProperty(Realm realm, String name) {
            if(realm!=this.realm) return null;
            return map.get(name);
        }

        @Override
        public int getProperty(String name, int def) {
            String p=map.get(name);
            if(p==null) return def;
            try { return Integer.parseInt(p); }
            catch(NumberFormatException e) { return def; }
        }

        @Override
        public int getProperty(Realm realm, String name, int def) {
            if(realm!=this.realm) return def;
            String p=map.get(name);
            if(p==null) return def;
            try { return Integer.parseInt(p); }
            catch(NumberFormatException e) { return def; }
        }

        @Override
        public long getProperty(String name, long def) {
            String p=map.get(name);
            if(p==null) return def;
            try { return Long.parseLong(p); }
            catch(NumberFormatException e) { return def; }
        }

        @Override
        public long getProperty(Realm realm, String name, long def) {
            if(realm!=this.realm) return def;
            String p=map.get(name);
            if(p==null) return def;
            try { return Long.parseLong(p); }
            catch(NumberFormatException e) { return def; }
        }

        public PeerProperties getProperties(Realm realm) {
            if(realm!=this.realm) return null;
            return this;
        }

        public Set<String> getPropertyNames() {
            return map.keySet();
        }

        @Override
        public Set<String> getPropertyNames(Realm realm) {
            if(realm!=this.realm) return Collections.emptySet();
            return map.keySet();
        }

        protected StringBuilder toBuffer(StringBuilder buf) {
            for(Map.Entry<String,String> p: map.entrySet()) {
                buf.append(realm.name()).append('.').append(p.getKey()).append(" = ").append(p.getValue()).append('\n');
            }
            return buf;
        }

        @Override
        public String toString() {
            return toBuffer(new StringBuilder()).toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder();
        for(RealmProperties p: proptable) {
            p.toBuffer(buf);
        }
        return buf.toString();
    }
}
