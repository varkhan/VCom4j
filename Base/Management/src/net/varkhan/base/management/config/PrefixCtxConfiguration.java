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
public class PrefixCtxConfiguration implements Configuration {

    private static  char          sep = '.';
    /**
     * A source of configuration parameters
     */
    protected final Configuration config;

    public PrefixCtxConfiguration(Configuration source) { this.config=source; }

    public Object get(String ctx, String key) {
        // Attempt to find exact context
        for(String pfx: Prefix.enumeratePrefixesAsc(sep, ctx)) {
            if(config.context(pfx).has(key)) return config.context(pfx).get(key);
        }
        return null;
    }

    public Iterable<String> contexts() {
        return config.contexts();
    }

    public Context context(final String ctx) {
        return new PrefixContext(ctx);
    }


    private class PrefixContext implements Context {
        private final String ctx;

        public PrefixContext(String ctx) {
            this.ctx=ctx;
        }

        @Override
        public String name() {
            return ctx;
        }

        @Override
        public boolean has(String key) {
            for(String pfx: Prefix.enumeratePrefixesAsc(sep, ctx)) {
                if(config.context(pfx).has(key)) return true;
            }
            return false;
        }

        @Override
        public Object get(String key) {
            for(String pfx: Prefix.enumeratePrefixesAsc(sep, ctx)) {
                if(config.context(pfx).has(key)) return config.context(pfx).get(pfx);
            }
            return null;
        }

        @Override
        public Map<String,?> get() {
            // We can't generate the full set of possible suffixes of existing contexts
            // hence the returned map keyset is NOT equivalent to the keys that will be recognized
            return config.context(ctx).get();
        }

        public Iterator<Entry> iterator() {
            // We can't generate the full set of possible suffixes of existing contexts
            // hence the returned list of entries is NOT equivalent to the keys that will be recognized
            return config.context(ctx).iterator();
        }

    }

}
