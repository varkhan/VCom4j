/**
 *
 */
package net.varkhan.base.management.config;

import java.util.Iterator;


/**
 * <b>Dot-delimited prefix lookup for configurations.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:57:33 AM
 */
public class PrefixConfiguration implements Configuration {

    /**
     * A source of configuration parameters
     */
    protected final Configuration config;

    public PrefixConfiguration(Configuration source) { this.config=source; }

    public Object getConfig(String ctx, String key) {
        // Attempt to find exact key
        Object val=config.getConfig(ctx, key);
        if(val!=null) return val;
        // Search for dot-delimited prefixes of the key
        int dot=key.lastIndexOf('.');
        while(dot>0) {
            dot--;
            String sk=key.substring(0, dot);
            val=config.getConfig(ctx, sk);
            if(val!=null) return val;
            dot=key.lastIndexOf('.', dot);
        }
        // No more dots in key, look for empty key
        return config.getConfig(ctx, "");
    }

    public Iterable<String> contexts() {
        return config.contexts();
    }

    public Iterable<Entry> entries(final String ctx) {
        return new Iterable<Entry>() {
            public Iterator<Entry> iterator() {
                return new Iterator<Entry>() {
                    private final Iterator<Entry> it=config.entries(ctx).iterator();
                    private Entry last=null;
                    private int pos=-1;

                    public boolean hasNext() {
                        return (last!=null&&pos<last.key().length())||it.hasNext();
                    }

                    public Entry next() {
                        if(last!=null) {
                            if(pos<0) {
                                pos=0;
                                final Entry ent=last;
                                final String key="";
                                return new Entry() {
                                    public String ctx() { return ent.ctx(); }
                                    public String key() { return key; }
                                    public Object value() { return ent.value(); }
                                };
                            }
                            if(pos<last.key().length()) {
                                pos=last.key().indexOf('.', pos+1);
                                if(pos>0) {
                                    final Entry ent=last;
                                    final String key=last.key().substring(0, pos-1);
                                    return new Entry() {
                                        public String ctx() { return ent.ctx(); }
                                        public String key() { return key; }
                                        public Object value() { return ent.value(); }
                                    };
                                }
                            }
                            final Entry ent=last;
                            final String key=last.key();
                            last = null;
                            pos = -1;
                            return new Entry() {
                                public String ctx() { return ent.ctx(); }
                                public String key() { return key; }
                                public Object value() { return ent.value(); }
                            };
                        }
                        // Throw exception if we ran out
                        if(!it.hasNext()) throw new IndexOutOfBoundsException();
                        last = it.next();
                        pos = 0;
                        final Entry ent = last;
                        final String key = "";
                        return new Entry() {
                            public String ctx() { return ent.ctx(); }
                            public String key() { return key; }
                            public Object value() { return ent.value(); }
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
