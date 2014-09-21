/**
 *
 */
package net.varkhan.base.management.config;

import java.util.*;


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
     * Creates a new delegating configuration.
     *
     * @param configs a variadic array of configurations, in order of decreasing priorities
     */
    public DelegateConfiguration(Configuration... configs) {
        this.configs=configs==null ? new Configuration[]{} : configs.clone();
    }

    public Object get(String ctx, String key) {
        for(Configuration conf: configs) {
            if(conf!=null) {
                Object val=conf.get(ctx, key);
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

    public Configuration.Context context(String ctx) {
        Configuration.Context[] contexts = new Configuration.Context[configs.length];
        for(int i=0;i<configs.length;i++) {
            Configuration conf=configs[i];
            if(conf!=null) contexts[i] = conf.context(ctx);
        }
        return new Context(ctx, contexts);
    }

    protected static class Context implements Configuration.Context {
        protected final Configuration.Context[] contexts;
        protected final String                  ctx;

        public Context(String ctx, Configuration.Context... contexts) {
            this.ctx=ctx;
            this.contexts=contexts;
        }

        @Override
        public String name() {
            return ctx;
        }

        @Override
        public boolean has(String key) {
            for(Configuration.Context ctx : contexts) {
                if(ctx!=null&&ctx.has(key)) return true;
            }
            return false;
        }

        @Override
        public Object get(String key) {
            // Iterating through configurations in descending priority order
            // ensures that if a higher-priority conf had this entry, it will
            // be returned first.
            for(int i=0;i<contexts.length;) {
                Configuration.Context ctx=contexts[i++];
                if(ctx!=null&&ctx.has(key)) return ctx.get(key);
            }
            return null;
        }

        @Override
        public Map<String,?> get() {
            Map<String,Object> map=new HashMap<String,Object>();
            // Iterating through configurations in ascending priority order
            // ensures that if a higher-priority conf had this entry, it will
            // override the lower-priority ones.
            for(int i=contexts.length;i>0;) {
                Configuration.Context ctx=contexts[--i];
                if(ctx!=null) map.putAll(ctx.get());
            }
            return map;
        }

        @Override
        public Iterator<Configuration.Entry> iterator() {
            return new Iterator<Configuration.Entry>() {
                private final HashSet<String> seen=new HashSet<String>();
                private volatile int prio=0;
                private volatile Iterator<Configuration.Entry> iter;
                private volatile Configuration.Entry last = null;

                @Override
                public boolean hasNext() {
                    if(last!=null) return true;
                    while(prio<contexts.length) {
                        if(iter==null) iter = contexts[prio].iterator();
                        while(iter.hasNext()) {
                            Configuration.Entry ent = iter.next();
                            if(!seen.contains(ent.key())) {
                                last = ent;
                                return true;
                            }
                        }
                        iter = null;
                        prio++;
                    }
                    return false;
                }

                @Override
                public Configuration.Entry next() {
                    if(!hasNext()) throw new NoSuchElementException();
                    Configuration.Entry ent = last;
                    seen.add(ent.key());
                    last = null;
                    return ent;
                }

                @Override
                public void remove() {
                    iter.remove();
                    last = null;
                }
            };
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append("[").append(ctx==null?"":ctx).append("]\n");
            for(Configuration.Entry e: this) {
                buf.append("\t").append(e.key()).append("=").append(e.value()).append("\n");
            }
            return buf.toString();
        }

    }

}
