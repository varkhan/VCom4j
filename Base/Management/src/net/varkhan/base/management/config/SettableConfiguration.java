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
    public Object get(String ctx, String key);

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
    public Context context(String ctx);

    /**
     * Set a default configuration entry.
     *
     * @param key a config name
     * @param val a config value
     */
    public void add(String key, Object val);

    /**
     * Set a contextualized configuration entry.
     *
     * @param ctx a context name
     * @param key a config name
     * @param val a config value
     */
    public void add(String ctx, String key, Object val);

    /**
     * Set a configuration entry.
     *
     * @param ent the configuration entry
     */
    public void add(Entry ent);

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
    public void loadConfig(String ctx, Map<String,?> props);

    /**
     * <b>A modifiable configuration context definition</b>.
     */
    public static interface Context extends Configuration.Context {

        /**
         * Adds or sets a configuration entry to this context.
         *
         * @param key the key part of the configuration entry
         * @param val the value
         *
         * @return {@literal true} if the container has been modified as a result of
         *         this operation, {@literal false} if the container remains unchanged (for
         *         instance, because the key is already present, with the same value)
         */
        public boolean add(String key, Object val);

        /**
         * Adds or sets a configuration entry to this context.
         *
         * @param cfg the configuration entry to add
         *
         * @return {@literal true} if the context has been modified as a result of
         *         this operation, {@literal false} if the context remains unchanged (for
         *         instance, because the configuration is already present, with the same value)
         */
        public boolean add(Entry cfg);

        /**
         * Adds or sets a group of configuration entries
         *
         * @param cfgs the configurations
         *
         * @return {@literal true} if the context has been modified as a result of
         *         this operation, {@literal false} if the context remains unchanged (for
         *         instance, because the configurations were already present, with the same values)
         */
        public boolean add(Map<String,?> cfgs);

        /**
         * Removes a configuration from this context.
         *
         * @param key the key part of the configuration entry
         *
         * @return {@literal true} if the context has been modified as a result of
         *         this operation, {@literal false} if the context remains unchanged (for
         *         instance, because the configuration was not initially in the container)
         */
        public boolean del(String key);

    }

}
