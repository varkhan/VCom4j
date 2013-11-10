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
     * Creates a new delegating configuration
     *
     * @param configs a variadic array of configurations, in order of increasing priorities
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


    public void setEntry(String key, Object val) { override.setEntry(key, val); }

    public void setEntry(String ctx, String key, Object val) { override.setEntry(ctx, key, val); }

    public void setEntry(Configuration.Entry ent) { override.setEntry(ent); }

    public void loadConfig(Configuration cfg) { override.loadConfig(cfg); }

    public void loadConfig(String ctx, Map<String,Object> props) { override.loadConfig(ctx, props); }
}
