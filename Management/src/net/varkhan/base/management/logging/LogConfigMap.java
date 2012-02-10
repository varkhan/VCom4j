/**
 *
 */
package net.varkhan.base.management.logging;

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

    public void setLevelMask(String key, long lvm) {
        // Make sure the SET marker is present
        lvm|=SET_MARKER;
        deflevels.put(key, lvm);
    }

    public void setLevelMask(String ctx, String key, long lvm) {
        // Make sure the sign bit is set
        lvm|=SET_MARKER;
        ConcurrentMap<String,Long> k2l;
        if(ctx==null) k2l=deflevels;
        else {
            k2l=ctxlevels.get(ctx);
            if(k2l==null) {
                k2l=new ConcurrentHashMap<String,Long>();
                ConcurrentMap<String,Long> klx=ctxlevels.putIfAbsent(key, k2l);
                if(klx!=null) k2l=klx;
            }
        }
        k2l.put(key, lvm);
    }

}
