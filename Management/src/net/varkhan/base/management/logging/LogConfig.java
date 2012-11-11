/**
 *
 */
package net.varkhan.base.management.logging;


/**
 * <b>A log handling configuration.</b>
 * <p/>
 *
 * @author varkhan
 * @date Sep 30, 2010
 * @time 5:51:15 AM
 */
public interface LogConfig {

    /**
     * The position of SET bit marker, and lowest invalid value of severity levels.
     */
    public final int SET_BITPOS = 63;

    /**
     * The definition marker for SET level masks.
     */
    public final long SET_MARKER=(1L<<SET_BITPOS);

    /**
     * Return the logging level mask.
     *
     * @param ctx a context name
     * @param key a filter key
     *
     * @return the enabled severity level mask for context {@code ctx} and key {@code key},
     *         with a non-zero sign bit (set bit of {@link LogConfig#SET_MARKER})
     *         or {@literal 0L} if the mask is not defined
     */
    public long getLevelMask(String ctx, String key);

    /**
     * Indicate whether a particular logging level is enabled.
     *
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     *
     * @return {@code true} iff logging for the severity level {@code lev} is enabled, for context {@code ctx} and key {@code key}
     */
    public boolean isLevelEnabled(String ctx, String key, int lev);

    /**
     * <b>A log level definition</b>.
     */
    public static interface Level {
        /** @return the context name */
        public String ctx();
        /** @return the filter key */
        public String key();
        /** @return the severity level mask */
        public long mask();
    }

    /**
     * Iterate through known log contexts.
     *
     * @return an iterator over all log contexts
     */
    public Iterable<String> contexts();

    /**
     * Iterate through log level definitions for a given context.
     *
     * @param ctx the context name
     * @return an iterator over all log levels defined for this context
     */
    public Iterable<Level> levels(String ctx);

}
