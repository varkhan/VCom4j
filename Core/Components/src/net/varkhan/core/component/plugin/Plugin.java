package net.varkhan.core.component.plugin;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/12
 * @time 8:04 PM
 */
public interface Plugin<T> {

    /**
     * The target interface of the dynamic implementation.
     *
     * @return the interface class of the implementations
     */
    public Class<T> target();

    /**
     * Retrieve the value of the dynamic implementation.
     *
     * @return the dynamic implementation
     */
    public T value();

    /**
     * The failure status of the last attempt to load a dynamic implementation.
     *
     * @return {@literal null} if the implementation was loaded successfully, or the object thrown in the process, otherwise
     */
    public Throwable failure();

    /**
     * Indicate whether the currently loaded dynamic implementation is up to date.
     *
     * @return {@literal true} iff the implementation that would be returned by {@link #value()} was loaded more recently than the last change to its source
     */
    public boolean isUpdated();

    /**
     * Indicate whether a dynamic implementation is currently loaded and available.
     *
     * @return {@literal true} iff {@link #failure()} would return {@literal null} and {@link #value()} would return a (non-null) instance of the target interface
     */
    public boolean isAvailable();

    /**
     * Update the dynamic implementation from source.
     */
    public void update();

    /**
     * Update the dynamic implementation, if available.
     */
    public void updateIfAvailable();



}
