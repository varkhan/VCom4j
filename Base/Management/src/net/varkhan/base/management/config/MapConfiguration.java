/**
 *
 */
package net.varkhan.base.management.config;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * <b>Map-based thread-safe configurations.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:57:33 AM
 */
public class MapConfiguration implements SettableConfiguration {

    /**
     * An internal map { key -> val } for known default entries
     */
    protected final ConcurrentMap<String,Object>                       defentries=new ConcurrentHashMap<String,Object>();
    /**
     * An internal map { ctx -> { key -> val } } for known contextualized entries
     */
    protected final ConcurrentMap<String,ConcurrentMap<String,Object>> ctxentries=new ConcurrentHashMap<String,ConcurrentMap<String,Object>>();

    /**
     * Create an empty map config.
     *
     */
    public MapConfiguration() { }

    /**
     * Create a map config.
     *
     * @param defs a list of { context name, config key, config value } triplets, where
     *        each context name and config key must be a String.
     */
    public MapConfiguration(Object... defs) {
        // No prop defs? we are done
        if(defs==null || defs.length==0) return;
        if((defs.length%3)!=0)
            throw new IllegalArgumentException("Contextual properties definition array must have a multiple of 3 number of elements");
        for(int i=0;i+2<defs.length;i+=3) {
            if(!(defs[i] instanceof CharSequence))
                throw new IllegalArgumentException("Context names must be strings");
            if(!(defs[i+1] instanceof CharSequence))
                throw new IllegalArgumentException("Config keys must be strings");
            String ctx=defs[i]==null?"":defs[i].toString();
            String key=defs[i+1].toString();
            Object val=defs[i+2];
            if(ctx==null || ctx.length()==0) setEntry(key,val);
            else setEntry(ctx,key,val);
        }
    }

    @Override
    public Object getConfig(String ctx, String key) {
        // Get context to key map
        Map<String,Object> k2e;
        if(ctx==null) k2e=defentries;
        else {
            k2e=ctxentries.get(ctx);
            if(k2e==null) k2e=defentries;
        }
        // Attempt to find exact key, and return value or null if not found
        return k2e.get(key);
    }

    @Override
    public Iterable<String> contexts() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private boolean first=true;
                    private final Iterator<String> it=ctxentries.keySet().iterator();

                    public boolean hasNext() {
                        return first||it.hasNext();
                    }

                    public String next() {
                        if(first) {
                            first=false;
                            return "";
                        }
                        return it.next();
                    }

                    public void remove() {
                        if(!first) it.remove();
                    }
                };
            }
        };
    }

    @Override
    public Iterable<Entry> entries(String ctx) {
        if(ctx==null||ctx.length()==0) return new Iterable<Entry>() {
            public Iterator<Entry> iterator() {
                return new Iterator<Entry>() {
                    private final Iterator<Map.Entry<String,Object>> it=defentries.entrySet().iterator();

                    public boolean hasNext() { return it.hasNext(); }

                    public Entry next() {
                        final Map.Entry<String,Object> ent=it.next();
                        return new Entry() {
                            public String ctx() { return ""; }

                            public String key() { return ent.getKey(); }

                            public Object value() { return ent.getValue(); }
                        };
                    }

                    public void remove() { it.remove(); }
                };
            }
        };
        ConcurrentMap<String,Object> k2e=ctxentries.get(ctx);
        if(k2e==null) {
            k2e=new ConcurrentHashMap<String,Object>();
            ConcurrentMap<String,Object> k2ex=ctxentries.putIfAbsent(ctx, k2e);
            if(k2ex!=null) k2e=k2ex;
        }
        final Iterator<Map.Entry<String,Object>> it=k2e.entrySet().iterator();
        return new Iterable<Entry>() {
            public Iterator<Entry> iterator() {
                return new Iterator<Entry>() {
                    public boolean hasNext() { return it.hasNext(); }

                    public Entry next() {
                        final Map.Entry<String,Object> ent=it.next();
                        return new Entry() {
                            public String ctx() { return ""; }

                            public String key() { return ent.getKey(); }

                            public Object value() { return ent.getValue(); }
                        };
                    }

                    public void remove() { it.remove(); }
                };
            }
        };
    }

    @Override
    public void setEntry(String key, Object val) {
        defentries.put(key, val);
    }

    @Override
    public void setEntry(String ctx, String key, Object val) {
        ConcurrentMap<String,Object> k2e;
        if(ctx==null||ctx.length()==0) k2e=defentries;
        else {
            k2e=ctxentries.get(ctx);
            if(k2e==null) {
                k2e=new ConcurrentHashMap<String,Object>();
                ConcurrentMap<String,Object> k2ex=ctxentries.putIfAbsent(ctx, k2e);
                if(k2ex!=null) k2e=k2ex;
            }
        }
        k2e.put(key, val);
    }

    @Override
    public void setEntry(Entry ent) {
        setEntry(ent.ctx(),ent.key(),ent.value());
    }

    @Override
    public void loadConfig(Configuration cfg) {
        for(String ctx: cfg.contexts()) {
            for(Entry ent: cfg.entries(ctx)) {
                setEntry(ent);
            }
        }
    }

    @Override
    public void loadConfig(String ctx, Map<String,Object> props) {
        for(Map.Entry<String,Object> prop: props.entrySet()) {
            if(prop.getValue()!=null) setEntry(ctx,prop.getKey(),prop.getValue().toString());
        }
    }

}
