package net.varkhan.base.management.config;

import java.util.Map;


/**
 * <b>A set of configuration keys and values, that can be modified after creation.</b>
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 2:59 PM
 */
public interface SettableConfiguration extends Configuration {

    /**
     * Return a configuration value.
     *
     * @param ctx a context name
     * @param key a config name
     *
     * @return the named configuration value in a given context
     */
    public Object getConfig(String ctx, String key);

    /**
     * Iterate through known configuration contexts.
     *
     * @return an iterator over all conf contexts
     */
    public Iterable<String> contexts();

    /**
     * Iterate through known configuration entries for a given context.
     *
     * @param ctx the context name
     * @return an iterator over all configuration entries defined for this context
     */
    public Iterable<Entry> entries(String ctx);

    /**
     * Set a default configuration entry.
     *
     * @param key a config name
     * @param val a config value
     */
    public void setEntry(String key, Object val);

    /**
     * Set a contextualized configuration entry.
     *
     * @param ctx a context name
     * @param key a config name
     * @param val a config value
     */
    public void setEntry(String ctx, String key, Object val);

    /**
     * Set a configuration entry.
     *
     * @param ent the configuration entry
     */
    public void setEntry(Entry ent);

    /**
     * Load a set of configuration entries.
     *
     * @param cfg the configuration
     */
    public void loadConfig(Configuration cfg);

    /**
     * Load a set of configuration entries for a given context.
     *
     * @param ctx   the context name
     * @param props a map of configuration names and values
     */
    public void loadConfig(String ctx, Map<String,Object> props);

}
