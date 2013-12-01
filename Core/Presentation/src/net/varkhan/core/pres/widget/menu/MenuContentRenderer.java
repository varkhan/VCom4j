package net.varkhan.core.pres.widget.menu;

import net.varkhan.core.pres.format.Formatter;
import net.varkhan.core.pres.render.Renderer;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:19 PM
 */
public interface MenuContentRenderer<F extends Formatter, I, P> extends Renderer<F,String[],Menu<I>,P> {

    public void render(F fmt, String[] loc, Menu<I> obj, P par) throws IOException;

}
