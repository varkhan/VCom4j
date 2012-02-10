package net.varkhan.pres.render;

import net.varkhan.pres.format.Formatter;

import java.io.IOException;


/**
 * <b>An object that <i>renders</i> objects in a specific way, to an adequate Formatter.</b>.
 * <p/>
 *
 * @param <F> the formatter type
 * @param <L> the location type
 * @param <I> the object type
 * @param <P> the render parameter type
 *
 * @author varkhan
 * @date 1/9/11
 * @time 9:13 AM
 */
public interface Renderer<F extends Formatter, L, I, P> {

    /**
     * Renders an object to a Formatter.
     *
     * @param fmt the output formatter
     * @param loc the object's location
     * @param obj the object's value
     * @param par the render parameters
     * @throws IOException if the output Formatter generated an exception
     */
    public void render(F fmt, L loc, I obj, P par) throws IOException;

}
