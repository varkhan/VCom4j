/**
 *
 */
package net.varkhan.base.management.monitor;


/**
 * <b>An aggregate monitor, linked to several component monitors.</b>
 * <p/>
 * A monitor that links a main monitored value point to several named
 * components, either source or product of the main value.
 * <p/>
 *
 * @param <C> an Enum identifying the components
 * @param <V> the type of the main monitored value
 * @param <T> the type of the value of the component monitors
 *
 * @author varkhan
 * @date Jun 16, 2009
 * @time 10:35:00 PM
 */
public interface MonitorAggregate<C extends Enum<C>,V,T> extends Monitor<V> {

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
     * Return an array of component identifiers
     *
     * @return all the component identifiers (name and ordinal) in this aggregate
     */
    public C[] components();

    /**
     * Return an instance of a component monitor for a given identifier
     *
     * @param c the identifier designing the monitor
     *
     * @return an instance of the component monitor
     */
    public Monitor<T> component(C c);

    /**
     * Return the value of the component monitor for a given identifier
     *
     * @param c the identifier designing the monitor
     *
     * @return the value of the component monitor
     */
    public T value(C c);

    /**
     * Return an instance of a component monitor for a given identifier
     *
     * @param n the name of the identifier designing the monitor
     *
     * @return an instance of the component monitor
     */
    public Monitor<T> component(String n);

    /**
     * Return the value of the component monitor for a given identifier
     *
     * @param n the name of the identifier designing the monitor
     *
     * @return the value of the component monitor
     */
    public T value(String n);


}
