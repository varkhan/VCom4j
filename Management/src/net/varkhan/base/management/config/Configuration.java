/**
 *
 */
package net.varkhan.base.management.config;


/**
 * <b>A set of configuration keys and values.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:51:15 AM
 */
public interface Configuration {

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
     * <b>A log level definition</b>.
     */
    public static interface Entry {
        /** @return the context name */
        public String ctx();
        /** @return the config name */
        public String key();
        /** @return the config value */
        public Object value();
    }

}
