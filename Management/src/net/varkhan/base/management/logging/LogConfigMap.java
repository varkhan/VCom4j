/**
 *
 */
package net.varkhan.base.management.logging;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * <b>Dot-delimited prefix lookup for logging configurations.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:57:33 AM
 */
public class LogConfigMap implements LogConfig {

    /**
     * An internal map { key -> lev } for known default debug levels
     */
    private final ConcurrentMap<String,Long>                       deflevels=new ConcurrentHashMap<String,Long>();
    /**
     * An internal map { ctx -> { key -> lev } } for known contextualized debug levels
     */
    private final ConcurrentMap<String,ConcurrentMap<String,Long>> ctxlevels=new ConcurrentHashMap<String,ConcurrentMap<String,Long>>();

    public long getLevelMask(String ctx, String key) {
        // Get context to key map
        Map<String,Long> k2l;
        if(ctx==null) k2l=deflevels;
        else {
            k2l=ctxlevels.get(ctx);
            if(k2l==null) k2l=deflevels;
        }
        // Attempt to find exact key
        Long lvm=k2l.get(key);
        if(lvm!=null) return lvm;
        // Nothing found... no levels enabled, and no SET marker
        return 0L;
    }

    public boolean isLevelEnabled(String ctx, String key, int lev) {
        return (getLevelMask(ctx, key)&(1<<lev))!=0;
    }

    public Iterable<String> contexts() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private boolean first = true;
                    private final Iterator<String> it = ctxlevels.keySet().iterator();

                    public boolean hasNext() {
                        return first || it.hasNext();
                    }

                    public String next() {
                        if(first) {
                            first = false;
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

    public Iterable<Level> levels(String ctx) {
        if(ctx==null || ctx.length()==0) return new Iterable<Level>() {
            public Iterator<Level> iterator() {
                return new Iterator<Level>() {
                    private final Iterator<Map.Entry<String,Long>> it = deflevels.entrySet().iterator();
                    public boolean hasNext() { return it.hasNext(); }
                    public Level next() {
                        final Map.Entry<String,Long> lev = it.next();
                        return new Level() {
                            public String ctx() { return ""; }
                            public String key() { return lev.getKey(); }
                            public long mask() {
                                Long l=lev.getValue();
                                return l==null?0:l;
                            }
                        };
                    }
                    public void remove() { it.remove(); }
                };
            }
        };
        ConcurrentMap<String,Long> k2l = ctxlevels.get(ctx);
        if(k2l==null) {
            k2l=new ConcurrentHashMap<String,Long>();
            ConcurrentMap<String,Long> klx=ctxlevels.putIfAbsent(ctx, k2l);
            if(klx!=null) k2l=klx;
        }
        final Iterator<Map.Entry<String,Long>> it = k2l.entrySet().iterator();
        return new Iterable<Level>() {
            public Iterator<Level> iterator() {
                return new Iterator<Level>() {
                    public boolean hasNext() { return it.hasNext(); }
                    public Level next() {
                        final Map.Entry<String,Long> lev = it.next();
                        return new Level() {
                            public String ctx() { return ""; }
                            public String key() { return lev.getKey(); }
                            public long mask() {
                                Long l=lev.getValue();
                                return l==null?0:l;
                            }
                        };
                    }
                    public void remove() { it.remove(); }
                };
            }
        };
    }

    public void setLevelMask(String key, long lvm) {
        // Make sure the SET marker is present
        lvm|=SET_MARKER;
        deflevels.put(key, lvm);
    }

    public void setLevelMask(String ctx, String key, long lvm) {
        // Make sure the sign bit is set
        lvm|=SET_MARKER;
        ConcurrentMap<String,Long> k2l;
        if(ctx==null||ctx.length()==0) k2l=deflevels;
        else {
            k2l=ctxlevels.get(ctx);
            if(k2l==null) {
                k2l=new ConcurrentHashMap<String,Long>();
                ConcurrentMap<String,Long> klx=ctxlevels.putIfAbsent(ctx, k2l);
                if(klx!=null) k2l=klx;
            }
        }
        k2l.put(key, lvm);
    }

    public void setLevelMask(Level lev) {
        setLevelMask(lev.ctx(),lev.key(),lev.mask());
    }

    public void loadConfig(LogConfig cfg) {
        for(String ctx: cfg.contexts()) {
            for(Level lev: cfg.levels(ctx)) {
                setLevelMask(lev);
            }
        }
    }
}
