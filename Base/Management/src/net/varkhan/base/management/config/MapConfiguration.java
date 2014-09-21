/**
 *
 */
package net.varkhan.base.management.config;

import java.util.Collections;
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
            if(ctx==null || ctx.length()==0) add(key, val);
            else add(ctx, key, val);
        }
    }

    @Override
    public Object get(String ctx, String key) {
        // Get context to key map
        Map<String,Object> k2e;
        if(ctx==null||ctx.length()==0) k2e=defentries;
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
    public SettableConfiguration.Context context(final String ctx) {
        if(ctx==null||ctx.length()==0) return new Context("",defentries);
        ConcurrentMap<String,Object> k2e=ctxentries.get(ctx);
        if(k2e==null) {
            k2e=new ConcurrentHashMap<String,Object>();
            ConcurrentMap<String,Object> k2ex=ctxentries.putIfAbsent(ctx, k2e);
            if(k2ex!=null) k2e=k2ex;
        }
        return new Context(ctx, k2e);
    }

    @Override
    public void add(String key, Object val) {
        defentries.put(key, val);
    }

    @Override
    public void add(String ctx, String key, Object val) {
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
    public void add(Configuration.Entry ent) {
        add(ent.ctx(), ent.key(), ent.value());
    }

    @Override
    public void loadConfig(Configuration cfg) {
        for(String ctx: cfg.contexts()) {
            for(Configuration.Entry ent: cfg.context(ctx)) {
                add(ent);
            }
        }
    }

    @Override
    public void loadConfig(String ctx, Map<String,?> props) {
        for(Map.Entry<String,?> prop: props.entrySet()) {
            if(prop.getValue()!=null) add(ctx, prop.getKey(), prop.getValue().toString());
        }
    }

    protected static class Context implements SettableConfiguration.Context {
        private final String ctx;
        private final Map<String,Object> map;

        public Context(String ctx, ConcurrentMap<String,Object> map) {
            this.ctx=ctx;
            this.map=map;
        }

        @Override
        public String name() {
            return ctx;
        }

        @Override
        public boolean has(String key) {
            return map.containsKey(key);
        }

        @Override
        public Object get(String key) {
            return map.get(key);
        }

        @Override
        public Map<String,?> get() {
            return Collections.unmodifiableMap(map);
        }

        public Iterator<Configuration.Entry> iterator() {
            return new Iterator<Configuration.Entry>() {
                private final Iterator<Map.Entry<String,Object>> it=map.entrySet().iterator();

                public boolean hasNext() { return it.hasNext(); }

                public Configuration.Entry next() {
                    final Map.Entry<String,Object> ent=it.next();
                    return new Entry(ctx,ent);
                }

                public void remove() { it.remove(); }
            };
        }

        @Override
        public boolean add(String key, Object val) {
            Object ret=map.put(key, val);
            return ret==val || ret!=null&&ret.equals(val);
        }

        @Override
        public boolean add(Configuration.Entry cfg) {
            return add(cfg.key(),cfg.value());
        }

        @Override
        public boolean add(Map<String,?> cfgs) {
            boolean mod=false;
            for(Map.Entry<String,?> cfg: cfgs.entrySet()) {
                Object ret=map.put(cfg.getKey(),cfg.getValue());
                mod |= ret==cfg.getValue() || ret!=null&&ret.equals(cfg.getValue());
            }
            return mod;
        }

        @Override
        public boolean del(String key) {
            return null!=map.remove(key);
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("[").append(ctx==null?"":ctx).append("]\n");
            for(Map.Entry<String,Object> e: map.entrySet()) {
                buf.append("\t").append(e.getKey()).append("=").append(e.getValue()).append("\n");
            }
            return buf.toString();
        }
    }

    protected static class Entry implements Configuration.Entry {
        private final String                   ctx;
        private final Map.Entry<String,Object> ent;

        public Entry(String ctx, Map.Entry<String,Object> ent) {
            this.ctx=ctx;
            this.ent=ent;
        }

        public String ctx() { return ctx; }

        public String key() { return ent.getKey(); }

        public Object value() { return ent.getValue(); }

        @Override
        public boolean equals(Object obj) {
            if(!(obj instanceof Configuration.Entry)) return false;
            Configuration.Entry that = (Configuration.Entry) obj;
            if(ctx==null || that.ctx()==null) return false;
            if(!ctx.equals(that.ctx())) return false;
            String key=ent.getKey();
            if(key==null) return that.key()==null;
            return key.equals(that.key());
        }

        @Override
        public int hashCode() {
            return 31 * (ctx==null?0:ctx.hashCode()) + (ent.getKey()==null?0:ent.getKey().hashCode());
        }

        @Override
        public String toString() {
            return (ctx==null?"":ctx)+":"+(ent.getKey()==null?"":ent.getKey())+"="+(ent.getValue()==null?"":ent.getValue());
        }
    }
}
