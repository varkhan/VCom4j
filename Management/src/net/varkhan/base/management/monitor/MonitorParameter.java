/**
 *
 */
package net.varkhan.base.management.monitor;


/**
 * <b>A monitor whose value is governed by several parameters.</b>
 * <p/>
 * A monitor that links a main monitored value point to several named
 * components, either source or product of the main value.
 * <p/>
 *
 * @param <P> an Enum identifying the parameters
 * @param <V> the type of the main monitored value
 * @param <T> the type of the parameters
 *
 * @author varkhan
 * @date Jun 16, 2009
 * @time 10:35:00 PM
 */
public interface MonitorParameter<P extends Enum<P>,V,T> extends Monitor<V> {

    /**
     * Clears internal memory, and resets the value to initialization state.
     */
    @Name("reset")
    @Description("Clears internal memory, and resets the value to initialization state")
    public void reset();

    /**
     * Returns the current monitor value.
     *
     * @return the current internal value of the monitored state
     */
    @Name("value")
    @Description("The current monitor value")
    public V value();

    /**
     * Updates internal memory, updating value(s) to reflect the monitored process.
     */
    @Name("update")
    @Description("Updates internal memory, updating value(s) to reflect the monitored process")
    public void update();

    /**
     * Return an array of parameter identifiers
     *
     * @return all the parameter identifiers (name and ordinal) in this monitor
     */
    public P[] parameters();

    /**
     * Gets the value of a parameter
     *
     * @param p the identifier designing the parameter
     *
     * @return the value of the parameter
     */
    public T parameter(P p);

    /**
     * Sets the value of a parameter
     *
     * @param p the identifier designing the parameter
     * @param t the value of the parameter
     */
    public void parameter(P p, T t);

    /**
     * Gets the value of a parameter
     *
     * @param p the name of the identifier designing the parameter
     *
     * @return the value of the parameter
     */
    public T parameter(String n);

    /**
     * Sets the value of a parameter
     *
     * @param n the name of the identifier designing the parameter
     * @param t the value of the parameter
     */
    public void parameter(String n, T t);
}
