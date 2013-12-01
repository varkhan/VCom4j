package net.varkhan.core.pres.render;

import net.varkhan.core.pres.format.Formatter;

import java.io.IOException;


/**
 * <b>An object that <i>renders</i> objects in a specific way, to an adequate Formatter.</b>.
 * <p/>
 *
 * @param <F> the formatter type
 * @param <L> the location type
 * @param <P> the render parameter type
 *
 * @author varkhan
 * @date 1/9/11
 * @time 9:13 AM
 */
public interface Renderable<F extends Formatter, L, P> {

    /**
     * Renders an object to a Formatter.
     *
     * @param fmt the output formatter
     * @param loc the object's location
     * @param par the render parameters
     * @throws java.io.IOException if the output Formatter generated an exception
     */
    public void render(F fmt, L loc, P par) throws IOException;

}
