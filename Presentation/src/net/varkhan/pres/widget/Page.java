package net.varkhan.pres.widget;

import com.sun.org.apache.bcel.internal.generic.L2D;
import net.varkhan.pres.format.Formatter;
import net.varkhan.pres.render.Renderer;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/12/11
 * @time 5:39 AM
 */
public interface Page<F extends Formatter,P> extends Widget<P>, Renderer<F,String[],Page<F,P>,P> {

    public void render(F fmt, String[] loc, Page<F,P> obj, P par) throws IOException;

}
