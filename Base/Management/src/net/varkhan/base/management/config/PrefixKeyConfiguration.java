/**
 *
 */
package net.varkhan.base.management.config;

import net.varkhan.base.management.util.Prefix;

import java.util.Iterator;
import java.util.Map;


/**
 * <b>Dot-delimited prefix lookup for configurations.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:57:33 AM
 */
public class PrefixKeyConfiguration implements Configuration {

    private static  char          sep = '.';
    /**
     * A source of configuration parameters
     */
    protected final Configuration config;

    public PrefixKeyConfiguration(Configuration source) { this.config=source; }

    public Object get(String ctx, String key) {
        // Attempt to find exact key
        for(String pfx: Prefix.enumeratePrefixesAsc(sep, key)) {
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


    private static class PrefixContext implements Context {
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
            for(String pfx: Prefix.enumeratePrefixesAsc(sep, key)) {
                if(sub.has(pfx)) return true;
            }
            return false;
        }

        @Override
        public Object get(String key) {
            for(String pfx: Prefix.enumeratePrefixesAsc(sep, key)) {
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
