package net.varkhan.base.management.config;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * <b>A Configuration composed with a fixed set of contexts</b>.
 * <p/>
 * This configuration is composed of a fixed, predefined set of Context blocks,
 * with a user-defined implementation, allowing to mix and match contexts from
 * various implementation in a cohesive Configuration structure.
 * <p/>
 *
 * @author varkhan
 * @date 9/20/14
 * @time 7:54 PM
 */
public class CompoundConfiguration<C extends Configuration.Context> implements Configuration {

    protected final Map<String,C> contexts=new LinkedHashMap<String,C>();

    public CompoundConfiguration(C... ctxs) {
        for(C ctx : ctxs) {
            contexts.put(ctx.name(), ctx);
        }
    }

    @Override
    public Object get(String ctx, String key) {
        C c=contexts.get(ctx==null?"":ctx);
        return c==null?null:c.get(key);
    }

    @Override
    public Iterable<String> contexts() {
        return contexts.keySet();
    }

    @Override
    public C context(String ctx) {
        return contexts.get(ctx==null?"":ctx);
    }

}
