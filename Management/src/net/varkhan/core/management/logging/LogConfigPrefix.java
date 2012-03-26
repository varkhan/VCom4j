/**
 *
 */
package net.varkhan.core.management.logging;

import java.util.Iterator;


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

    public Iterable<String> contexts() {
        return config.contexts();
    }

    public Iterable<Level> levels(final String ctx) {
        return new Iterable<Level>() {
            public Iterator<Level> iterator() {
                return new Iterator<Level>() {
                    private final Iterator<Level> it = config.levels(ctx).iterator();
                    private Level last = null;
                    private int pos = -1;

                    public boolean hasNext() {
                        return (last!=null && pos<last.key().length()) || it.hasNext();
                    }

                    public Level next() {
                        if(last!=null) {
                            if(pos<0) {
                                pos = 0;
                                final Level lev = last;
                                final String key = "";
                                return new Level() {
                                    public String ctx() { return lev.ctx(); }
                                    public String key() { return key; }
                                    public long mask() { return lev.mask(); }
                                };
                            }
                            if(pos<last.key().length()) {
                                pos = last.key().indexOf('.',pos+1);
                                if(pos>0) {
                                    final Level lev = last;
                                    final String key = last.key().substring(0,pos-1);
                                    return new Level() {
                                        public String ctx() { return lev.ctx(); }
                                        public String key() { return key; }
                                        public long mask() { return lev.mask(); }
                                    };
                                }
                            }
                            final Level lev = last;
                            final String key = last.key();
                            last = null;
                            pos = -1;
                            return new Level() {
                                public String ctx() { return lev.ctx(); }
                                public String key() { return key; }
                                public long mask() { return lev.mask(); }
                            };
                        }
                        // Throw exception if we ran out
                        if(!it.hasNext()) throw new IndexOutOfBoundsException();
                        last = it.next();
                        pos = 0;
                        final Level lev = last;
                        final String key = "";
                        return new Level() {
                            public String ctx() { return lev.ctx(); }
                            public String key() { return key; }
                            public long mask() { return lev.mask(); }
                        };
                    }

                    public void remove() {
                        // Probably unimplemented, but propagate none the less
                        it.remove();
                    }
                };
            }
        };
    }


}
