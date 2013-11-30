package net.varkhan.base.management.config;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 10/12/11
 * @time 7:51 PM
 */
public class ConfigurationError extends Exception {

    /** The configuration context */
    private final String ctx;
    /** The configuration name */
    private final String key;
    /** The configuration value */
    private final Object value;

    public ConfigurationError(String ctx, String key, Object value) {
        this.ctx=ctx;
        this.key=key;
        this.value=value;
    }

    public ConfigurationError(String message, String ctx, String key, Object value) {
        super(message);
        this.ctx=ctx;
        this.key=key;
        this.value=value;
    }

    public ConfigurationError(String message, Throwable cause, String ctx, String key, Object value) {
        super(message, cause);
        this.ctx=ctx;
        this.key=key;
        this.value=value;
    }

    public ConfigurationError(Throwable cause, String ctx, String key, Object value) {
        super(cause);
        this.ctx=ctx;
        this.key=key;
        this.value=value;
    }

    /**
     * The configuration context.
     *
     * @return the context for the erroneous configuration
     */
    public final String ctx() { return key; }

    /**
     * The configuration name.
     *
     * @return the name of the erroneous configuration entry
     */
    public final String key() { return key; }

    /**
     * The offending value.
     *
     * @return the configuration value that caused the error
     */
    public Object value() { return value; }

}
