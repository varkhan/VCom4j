/**
 *
 */
package net.varkhan.base.management.monitor;

import javax.management.DescriptorKey;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * <b>A monitored value.</b>
 * <p/>
 * A value point that reflects the state of a monitored process or resource.
 * <p/>
 *
 * @param <V> the type of the monitored value
 *
 * @author varkhan
 * @date Jun 16, 2009
 * @time 10:12:47 PM
 */
public interface Monitor<V> {

    /**
     * ******************************************************************************
     * *  Annotations
     */

    @Target( { ElementType.METHOD, ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Description {
        @DescriptorKey("Description") String value();
    }


    @Target( { ElementType.METHOD, ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Name {
        @DescriptorKey("Name") String value();
    }


    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.CLASS)
    public @interface MinValue {
        @DescriptorKey("MinValue") double value();
    }


    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MaxValue {
        @DescriptorKey("MaxValue") double value();
    }


    /*********************************************************************************
     **  Operations
     **/

    /**
     * Return the monitor value type.
     *
     * @return the type of the monitor's value
     */
    @Name("type")
    @Description("The value type of the monitor")
    public Class<V> type();

    /**
     * Clear internal memory, and resets the value to initialization state.
     */
    @Name("reset")
    @Description("Clears internal memory, and resets the value(s) to initialization state")
    public void reset();

    /**
     * Return the current monitor value.
     *
     * @return the current internal value of the monitored state
     */
    @Name("value")
    @Description("The current monitor value")
    public V value();

    /**
     * Update internal memory, updating value(s) to reflect the monitored process.
     */
    @Name("update")
    @Description("Updates internal memory, updating the value(s) to reflect the monitored process")
    public void update();


}
