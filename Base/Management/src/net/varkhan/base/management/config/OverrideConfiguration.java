package net.varkhan.base.management.config;

import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 3:06 PM
 */
public class OverrideConfiguration extends DelegateConfiguration implements SettableConfiguration {

    protected final SettableConfiguration override;

    /**
     * Creates a new delegating configuration.
     *
     * @param override the highest priority configuration, that allows for overriding of other fallback configurations
     * @param configs  a variadic array of configurations, in order of increasing priorities
     */
    public OverrideConfiguration(SettableConfiguration override, Configuration... configs) {
        super(merge(override, configs));
        this.override = override;
    }

    private static Configuration[] merge(SettableConfiguration override, Configuration[] configs) {
        if(configs==null ||configs.length==0) return new Configuration[]{override};
        Configuration[] confs = new Configuration[1+configs.length];
        confs[0] = override;
        System.arraycopy(configs,0,confs,1,configs.length);
        return confs;
    }

    @Override
    public SettableConfiguration.Context context(String ctx) {
        Configuration.Context[] contexts = new Configuration.Context[configs.length];
        for(int i=0;i<configs.length;i++) {
            Configuration conf=configs[i];
            if(conf!=null) contexts[i] = conf.context(ctx);
        }
        return new Context(ctx, override.context(ctx), contexts);
    }

    public void add(String key, Object val) { override.add(key, val); }

    public void add(String ctx, String key, Object val) { override.add(ctx, key, val); }

    public void add(Configuration.Entry ent) { override.add(ent); }

    public void loadConfig(Configuration cfg) { override.loadConfig(cfg); }

    public void loadConfig(String ctx, Map<String,?> props) { override.loadConfig(ctx, props); }

    protected static class Context extends DelegateConfiguration.Context implements SettableConfiguration.Context {
        protected final SettableConfiguration.Context override;

        public Context(String ctx, SettableConfiguration.Context override, Configuration.Context... contexts) {
            super(ctx, contexts);
            this.override=override;
        }

        @Override
        public boolean add(String key, Object val) {
            return override.add(key, val);
        }

        @Override
        public boolean add(Entry cfg) {
            return override.add(cfg);
        }

        @Override
        public boolean add(Map<String,?> cfgs) {
            return override.add(cfgs);
        }

        @Override
        public boolean del(String key) {
            return override.del(key);
        }
    }

}
