/**
 *
 */
package net.varkhan.core.management.logging;

import java.util.*;


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
     * @param propdefs a list of { context name, filter key, logging mask } triplets
     *        (each context name and filter key must be a String, and each logging mask must be either a string or an integer
     */
    @SuppressWarnings({ "unchecked" })
    public LogConfigProps(String[] levels, Object... propdefs) {
        this.levels=levels.clone();
        // No prop defs? we are done
        if(propdefs==null || propdefs.length==0) return;
        if((propdefs.length%3)!=0)
            throw new IllegalArgumentException("Contextual properties definition array must have a multiple of 3 number of elements");
        for(int i=0;i+2<propdefs.length;i+=3) {
            if(!(propdefs[i] instanceof CharSequence))
                throw new IllegalArgumentException("Context names must be strings");
            if(!(propdefs[i+1] instanceof CharSequence))
                throw new IllegalArgumentException("Filter keys must be strings");
            if(!(propdefs[i+2] instanceof Number) && !(propdefs[i+2] instanceof CharSequence))
                throw new IllegalArgumentException("Logging masks must be numbers or strings");
            String ctx=propdefs[i]==null?"":propdefs[i].toString();
            String key=propdefs[i+1].toString();
            String ldef=propdefs[i+2].toString();
            if(ctx==null || ctx.length()==0) {
                defprops.put(key,ldef);
            }
            else {
                Map<String,String> k2l = ctxprops.get(ctx);
                if(k2l==null) {
                    k2l = new HashMap<String,String>();
                    ctxprops.put(ctx, k2l);
                }
                k2l.put(key,ldef);
            }
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

    public Iterable<String> contexts() {
        return new Iterable<String>() {
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private boolean first = true;
                    private final Iterator<String> it = ctxprops.keySet().iterator();

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
                    private final Iterator<Map.Entry<String,String>> it = defprops.entrySet().iterator();
                    public boolean hasNext() { return it.hasNext(); }
                    public Level next() {
                        final Map.Entry<String,String> lev = it.next();
                        return new Level() {
                            public String ctx() { return ""; }
                            public String key() { return lev.getKey(); }
                            public long mask() {
                                String ldef = lev.getValue();
                                if(ldef==null) return 0L;
                                Long lev=levcache.get(ldef);
                                if(lev==null) lev=getLevelMask(levels, ldef);
                                return lev;
                            }
                        };
                    }
                    public void remove() { it.remove(); }
                };
            }
        };
        Map<String,String> k2l = ctxprops.get(ctx);
        if(k2l==null) k2l = Collections.emptyMap();
        final Iterator<Map.Entry<String,String>> it = k2l.entrySet().iterator();
        return new Iterable<Level>() {
            public Iterator<Level> iterator() {
                return new Iterator<Level>() {
                    public boolean hasNext() { return it.hasNext(); }
                    public Level next() {
                        final Map.Entry<String,String> lev = it.next();
                        return new Level() {
                            public String ctx() { return ""; }
                            public String key() { return lev.getKey(); }
                            public long mask() {
                                String ldef = lev.getValue();
                                if(ldef==null) return 0L;
                                Long lev=levcache.get(ldef);
                                if(lev==null) lev=getLevelMask(levels, ldef);
                                return lev;
                            }
                        };
                    }
                    public void remove() { it.remove(); }
                };
            }
        };
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
    public static long getLevelMask(String[] lev, String val) {
        if(val==null || val.length()==0 || "0".equals(val)) return 0;
        try {
            switch(val.charAt(0)) {
                case 'X': return Long.parseLong(val.substring(1), 16);
                case 'O': return Long.parseLong(val.substring(1), 8);
                case '0': return Long.parseLong(val.substring(1), 8);
                case 'B': return Long.parseLong(val.substring(1), 2);
                case '1':
                case '2':
                case '3':
                case '4':
                case '5':
                case '6':
                case '7':
                case '8':
                case '9':
                    return Long.parseLong(val);
            }
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

    public void setLevelMask(String ctx, String key, String lvm) {
        Map<String,String> k2l;
        if(ctx==null || ctx.length()==0) k2l=defprops;
        else {
            k2l=ctxprops.get(ctx);
            if(k2l==null) {
                k2l=new HashMap<String,String>();
                ctxprops.put(ctx, k2l);
            }
        }
        k2l.put(key, lvm);
    }

    public void setLevelMask(Level lev) {
        setLevelMask(lev.ctx(),lev.key(),Long.toString(lev.mask()));
    }

    public void loadConfig(LogConfig cfg) {
        for(String ctx: cfg.contexts()) {
            for(Level lev: cfg.levels(ctx)) {
                setLevelMask(lev);
            }
        }
    }

    public void loadConfig(String ctx, Map<String,Object> props) {
        for(Map.Entry<String,Object> prop: props.entrySet()) {
            if(prop.getValue()==null) setLevelMask(ctx,prop.getKey(),"0");
            else setLevelMask(ctx,prop.getKey(),prop.getValue().toString());
        }
    }
}
