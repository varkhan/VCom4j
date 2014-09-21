package net.varkhan.base.management.config;

import java.util.Collections;
import java.util.Iterator;
import java.util.Map;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 9/20/14
* @time 8:56 PM
*/
public class MapContext implements SettableConfiguration.Context {
    protected final String             ctx;
    protected final Map<String,Object> map;

    public MapContext(String ctx, Map<String,Object> map) {
        this.ctx=ctx==null?"":ctx;
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
                return new MapEntry(ctx,ent);
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
        buf.append("[").append(ctx==null ? "" : ctx).append("]\n");
        for(Map.Entry<String,Object> e: map.entrySet()) {
            buf.append("\t").append(e.getKey()).append("=").append(e.getValue()).append("\n");
        }
        return buf.toString();
    }
}
