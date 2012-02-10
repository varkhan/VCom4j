/**
 *
 */
package net.varkhan.base.management.logging;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * <b>Immutable property-based lookup for logging configurations.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:57:33 AM
 */
public class LogConfigProps implements LogConfig {

    private final String[]                       levels;
    private final Map<String,String>             defprops=new HashMap<String,String>();
    private final Map<String,Map<String,String>> ctxprops=new HashMap<String,Map<String,String>>();
    private final Map<String,Long>               levcache=new HashMap<String,Long>();

    /**
     * Create a property based log config.
     *
     * @param levels   the symbolic label definitions
     * @param defprops default proprties (must be a { String -> String } map)
     * @param propdefs a list of { context name -> logging properties } pairs
     *        (each context name must be a String, and each logging property must be a { String -> String } map
     */
    @SuppressWarnings({ "unchecked" })
    public LogConfigProps(String[] levels, Map defprops, Object... propdefs) {
        this.levels=levels.clone();
        this.defprops.putAll(defprops);
        if((propdefs.length&1)!=0)
            throw new IllegalArgumentException("Contextual properties definition array must have an even number of elements");
        for(int i=0;i+1<propdefs.length;i+=2) {
            if(!(propdefs[i] instanceof String))
                throw new IllegalArgumentException("Contextual properties definition array must have String objects as keys");
            if(!(propdefs[i+1] instanceof Map))
                throw new IllegalArgumentException("Contextual properties definition array must have Map objects as values");
            String ctx=(String) propdefs[i];
            Map<String,String> props=(Map<String,String>) propdefs[i+1];
            if(ctx!=null&&props!=null) ctxprops.put(ctx, new HashMap<String,String>(props));
        }
    }

    public long getLevelMask(String ctx, String key) {
        Map<String,String> props=ctxprops.get(ctx);
        if(props==null) props=defprops;
        String ldef=props.get(key);
        if(ldef==null) return 0L;
        Long lev=levcache.get(ldef);
        if(lev==null) {
            lev=getLevelMask(levels, ldef);
            levcache.put(ldef, lev);
        }
        return lev|SET_MARKER;
    }

    public boolean isLevelEnabled(String ctx, String key, int lev) {
        if(lev>=SET_BITPOS) return false;
        return (getLevelMask(ctx, key)&(1<<lev))!=0;
    }

    /**
     * Parse a level set definition
     *
     * @param lev an array of symbolic level specifications
     * @param val either a bit mask, or a comma or pipe separated set of enabled levels
     *
     * @return either the specified bit mask (parsed in, respectively, base 16, 8 or 2 if it is prefixed by the character X, O or B, or by default in decimal)
     *         or the mask whose bits are set for each numerical level specification, and for each position in the identifier array of a symbolic specification
     */
    protected static long getLevelMask(String[] lev, String val) {
        try {
            if(val.startsWith("X")) return Long.parseLong(val.substring(1), 16);
            else if(val.startsWith("O")) return Long.parseLong(val.substring(1), 8);
            else if(val.startsWith("B")) return Long.parseLong(val.substring(1), 2);
            else return Long.parseLong(val);
        }
        catch(Exception e) {
            // ignore, and go on to parse set
        }
        Set<String> flags=new HashSet<String>(lev.length);
        long mask=0;
        int j=-1;
        for(int i=0;i<val.length();i++) {
            char c=val.charAt(i);
            if(c==','||c=='|'||c==';') {
                String f=val.substring(j+1, i);
                try {
                    mask|=1<<Long.parseLong(f);
                }
                catch(NumberFormatException e) {
                    flags.add(f);
                }
                j=i;
            }
        }
        if(j<val.length()) {
            String f=val.substring(j+1);
            try {
                mask|=1<<Long.parseLong(f);
            }
            catch(NumberFormatException e) {
                flags.add(f);
            }
        }
        if(!flags.isEmpty()&&lev!=null) for(int i=0;i<lev.length;i++) {
            if(flags.contains(lev[i])) mask|=i<<i;
        }
        return mask;
    }

}
