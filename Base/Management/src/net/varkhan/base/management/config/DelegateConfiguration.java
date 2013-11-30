/**
 *
 */
package net.varkhan.base.management.config;

import java.util.HashSet;


/**
 * <b>A delegating configuration</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 6:32:12 AM
 */
public class DelegateConfiguration implements Configuration {
    /**
     * An array of configurations, in order of decreasing priorities
     */
    protected final Configuration[] configs;

    /**
     * Creates a new delegating configuration
     *
     * @param configs a variadic array of configurations, in order of decreasing priorities
     */
    public DelegateConfiguration(Configuration... configs) {
        this.configs=configs==null ? new Configuration[]{} : configs.clone();
    }

    public Object getConfig(String ctx, String key) {
        for(Configuration conf: configs) {
            if(conf!=null) {
                Object val=conf.getConfig(ctx, key);
                if(val!=null) return val;
            }
        }
        return null;
    }

    public Iterable<String> contexts() {
        HashSet<String> ctxs = new HashSet<String>();
        for(Configuration conf: configs) {
            if(conf!=null) for(String c: conf.contexts()) ctxs.add(c);
        }
        return ctxs;
    }

    public Iterable<Configuration.Entry> entries(String ctx) {
        HashSet<Configuration.Entry> ents = new HashSet<Configuration.Entry>();
        for(Configuration conf: configs) {
            // Iterating through configurations in descending priority order
            // ensures that if a higher-priority conf had this entry, it will
            // be already in the set with the appropriate value, thus the lower
            // priority one will not be added.
            if(conf!=null) for(Configuration.Entry ent: conf.entries(ctx)) ents.add(new Entry(ent));
        }
        return ents;
    }

    protected static class Entry implements Configuration.Entry {
        protected final String ctx;
        protected final String key;
        protected final Object val;

        public Entry(String ctx, String key, Object val) {
            this.ctx=ctx;
            this.key=key;
            this.val=val;
        }

        public Entry(Configuration.Entry ent) {
            this(ent.ctx(),ent.key(),ent.value());
        }

        public String ctx() { return ctx; }
        public String key() { return key; }
        public Object value() { return val; }

        public boolean equals(Object o) {
            if(this==o) return true;
            if(o==null||!(o instanceof Configuration.Entry)) return false;
            Configuration.Entry that=(Configuration.Entry) o;
            return ctx.equals(that.ctx()) && key.equals(that.key());
        }

        public int hashCode() { return 31* ctx.hashCode() +key.hashCode(); }
    }
}
