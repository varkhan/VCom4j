/**
 *
 */
package net.varkhan.base.management.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;


/**
 * <b>Dot-delimited prefix lookup for configurations.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:57:33 AM
 */
public class PrefixConfiguration implements Configuration {

    private static  char          sep = '.';
    /**
     * A source of configuration parameters
     */
    protected final Configuration config;

    public PrefixConfiguration(Configuration source) { this.config=source; }

    public Object get(String ctx, String key) {
        // Attempt to find exact key
        for(String pfx: enumeratePrefixesAsc(sep,key)) {
            Object val=config.get(ctx, pfx);
            if(val!=null) return val;
        }
        return null;
    }

    public Iterable<String> contexts() {
        return config.contexts();
    }

    public Context context(final String ctx) {
        return new PrefixContext(ctx, config.context(ctx));
    }

    public static Iterable<String> enumeratePrefixesAsc(final char sep, final String key) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int dot=Integer.MAX_VALUE;
                    @Override
                    public boolean hasNext() {
                        return key!=null && dot>0;
                    }

                    @Override
                    public String next() {
                        if(key==null) throw new NoSuchElementException();
                        if(dot>key.length()) {
                            dot=key.length();
                            return key;
                        }
                        dot=key.lastIndexOf(sep, dot-1);
                        if(dot<0) return "";
                        return key.substring(0, dot);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static Iterable<String> enumeratePrefixesDes(final char sep, final String key) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int dot=-1;
                    @Override
                    public boolean hasNext() {
                        return key!=null && dot<key.length();
                    }

                    @Override
                    public String next() {
                        if(key==null) throw new NoSuchElementException();
                        if(dot<0) {
                            dot = 0;
                            return "";
                        }
                        dot=key.indexOf(sep, dot+1);
                        if(dot<0) {
                            dot=key.length();
                            return key;
                        }
                        return key.substring(0, dot);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }


    private class PrefixContext implements Context {
        private final String ctx;
        private final Context sub;

        public PrefixContext(String ctx, Context sub) {
            this.ctx=ctx;
            this.sub=sub;
        }

        @Override
        public String name() {
            return ctx;
        }

        @Override
        public boolean has(String key) {
            for(String pfx: enumeratePrefixesAsc(sep,key)) {
                if(sub.has(pfx)) return true;
            }
            return false;
        }

        @Override
        public Object get(String key) {
            for(String pfx: enumeratePrefixesAsc(sep,key)) {
                if(sub.has(pfx)) return sub.get(pfx);
            }
            return null;
        }

        @Override
        public Map<String,?> get() {
            // We can't generate the full set of possible suffixes of existing keys
            // hence the returned map keyset is NOT equivalent to the keys that will be recognized
            return sub.get();
        }

        public Iterator<Entry> iterator() {
            // We can't generate the full set of possible suffixes of existing keys
            // hence the returned list of entries is NOT equivalent to the keys that will be recognized
            return sub.iterator();
        }

    }

}
