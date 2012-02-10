/**
 *
 */
package net.varkhan.base.management.logging;

/**
 * <b>Dot-delimited prefix lookup for logging configurations.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:57:33 AM
 */
public class LogConfigPrefix implements LogConfig {

    /**
     * A source of configuration parameters
     */
    private final LogConfig config;

    public LogConfigPrefix(LogConfig source) { this.config=source; }

    public long getLevelMask(String ctx, String key) {
        // Attempt to find exact key
        long lvm=config.getLevelMask(ctx, key);
        if(lvm!=0L) return lvm;
        // Search for dot-delimited prefixes of the key
        int dot=key.lastIndexOf('.');
        while(dot>0) {
            dot--;
            String sk=key.substring(0, dot);
            lvm=config.getLevelMask(ctx, sk);
            if(lvm!=0L) return lvm;
            dot=key.lastIndexOf('.', dot);
        }
        // No more dots in key, look for empty key
        lvm=config.getLevelMask(ctx, "");
        if(lvm!=0L) return lvm;
        // Nothing found... no levels enabled
        return 0L;
    }

    public boolean isLevelEnabled(String ctx, String key, int lev) {
        if(lev>=SET_BITPOS) return false;
        return (getLevelMask(ctx, key)&(1<<lev))!=0;
    }


}
