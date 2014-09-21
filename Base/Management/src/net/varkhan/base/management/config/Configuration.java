/**
 *
 */
package net.varkhan.base.management.config;


import java.util.Iterator;
import java.util.Map;


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
    public Object get(String ctx, String key);

    /**
     * Iterate through known context names.
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
     * <b>A configuration entry definition</b>.
     */
    public static interface Entry {
        /** @return the context name */
        public String ctx();
        /** @return the config name */
        public String key();
        /** @return the config value */
        public Object value();
    }

    /**
     * <b>A configuration context definition</b>.
     */
    public static interface Context extends Iterable<Entry> {

        /**
         * The context name.
         *
         * @return the name (identifier) of this context
         */
        public String name();

        /**
         * Indicate whether a given configuration is present in this context.
         *
         * @param key the key part of the configuration entry
         *
         * @return {@literal true} if this key is in the context,
         *         or {@literal false} if this key is absent
         */
        public boolean has(String key);

        /**
         * Retrieves a value for a given configuration in this context.
         *
         * @param key the key part of the configuration entry
         * @return the value matching the key in the context,
         *         or {@literal null} if the key is not present in the context
         */
        public Object get(String key);

        /**
         * Retrieves all configuration values in this context, as a map.
         *
         * @return a map of configurations and their values
         */
        public Map<String, ?> get();

        /**
         * Iterates over all configuration entries in the context.
         *
         * @return an iterator over all the configuration entries in the context
         */
        public Iterator<Entry> iterator();

    }

}
