/**
 *
 */
package net.varkhan.base.management.logging;

import net.varkhan.base.management.config.*;

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

    protected final SettableConfiguration config;
    protected final String[]              levels;
    protected final Map<String,Long> levcache=new HashMap<String,Long>();

    /**
     * Create a property based log config.
     *
     * @param levels   the symbolic label definitions
     * @param defcfg   a default configuration set
     * @param propdefs a list of { context name, filter key, logging mask } triplets
     */
    @SuppressWarnings({ "unchecked" })
    public LogConfigProps(String[] levels, Configuration defcfg, Object... propdefs) throws ConfigurationError {
        this(levels, defcfg);
        // No prop defs? we are done
        if(propdefs==null || propdefs.length==0) return;
        if((propdefs.length%3)!=0)
            throw new IllegalArgumentException("Contextual properties definition array must have a multiple of 3 number of elements");
        for(int i=0;i+2<propdefs.length;i+=3) {
            String ctx=propdefs[i]==null?"":propdefs[i].toString();
            String key=propdefs[i+1].toString();
            Object val=propdefs[i+2];
            if(!(propdefs[i] instanceof CharSequence))
                throw new IllegalArgumentException("Context names must be strings");
            if(!(propdefs[i+1] instanceof CharSequence))
                throw new IllegalArgumentException("Filter keys must be strings");
            if(!(propdefs[i+2] instanceof Number) && !(propdefs[i+2] instanceof CharSequence))
                throw new ConfigurationError("Logging masks must be numbers or strings",ctx,key,val);
            if(ctx==null || ctx.length()==0) config.setEntry(key, parseLevelMask(val));
            else config.setEntry(ctx, key, parseLevelMask(val));
        }
    }

    /**
     * Create a property based log config.
     *
     * @param levels the symbolic label definitions
     * @param defcfg a default configuration set
     */
    public LogConfigProps(String[] levels, Configuration defcfg) {
        this(levels, defcfg,new MapConfiguration());
    }

    /**
     * Create a property based log config.
     *
     * @param levels the symbolic label definitions
     * @param defcfg a default configuration set
     * @param setcfg an overriding, modifiable configuration set
     */
    public LogConfigProps(String[] levels, Configuration defcfg, SettableConfiguration setcfg) {
        this.levels = levels.clone();
        this.config = defcfg==null?setcfg:new OverrideConfiguration(setcfg,defcfg);
    }

    public long getLevelMask(String ctx, String key) {
        Object val = config.getConfig(ctx, key);
        if(val==null) return 0L;
        return parseLevelMask(val.toString());
    }

    protected long parseLevelMask(Object val) {
        if(val==null) return 0L;
        if(val instanceof Number) return ((Number)val).longValue()|SET_MARKER;
        String str = val.toString();
        Long lev=levcache.get(str);
        if(lev==null) {
            lev=parseLevelMask(levels, str);
            levcache.put(str, lev);
        }
        return lev|SET_MARKER;
    }

    public boolean isLevelEnabled(String ctx, String key, int lev) {
        return lev<SET_BITPOS && (getLevelMask(ctx, key)&(1<<lev))!=0;
    }

    public Iterable<String> contexts() {
        return config.contexts();
    }

    public Iterable<Level> levels(final String ctx) {
        return new Iterable<Level>() {
            protected final Iterable<Configuration.Entry> itb = config.entries(ctx);
            public Iterator<Level> iterator() {
                return new Iterator<Level>() {
                    protected final Iterator<Configuration.Entry> itr = itb.iterator();
                    public boolean hasNext() { return itr.hasNext(); }
                    public Level next() {
                        final Configuration.Entry ent = itr.next();
                        return new Level() {
                            public String ctx() { return ent.ctx(); }
                            public String key() { return ent.key(); }
                            public long mask() {
                                Object val = ent.value();
                                return val==null?0L:parseLevelMask(val);
                            }
                        };
                    }
                    public void remove() { itr.remove(); }
                };
            }
        };
    }


    public void setLevelMask(String ctx, String key, long lev) {
        config.setEntry(ctx,key,lev);
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

    public void loadConfig(String ctx, Map<String,Object> props) {
        for(Map.Entry<String,Object> prop: props.entrySet()) {
            if(prop.getValue()==null) setLevelMask(ctx,prop.getKey(),0);
            else setLevelMask(ctx,prop.getKey(),parseLevelMask(prop.getValue()));
        }
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
    public static long parseLevelMask(String[] lev, String val) {
        if(val==null||val.length()==0||"0".equals(val)) return 0;
        try {
            switch(val.charAt(0)) {
                case 'X':
                    return Long.parseLong(val.substring(1), 16);
                case 'O':
                    return Long.parseLong(val.substring(1), 8);
                case '0':
                    return Long.parseLong(val.substring(1), 8);
                case 'B':
                    return Long.parseLong(val.substring(1), 2);
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

}
