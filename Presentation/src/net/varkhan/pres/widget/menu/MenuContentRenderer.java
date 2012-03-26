package net.varkhan.pres.widget.menu;

import net.varkhan.pres.format.Formatter;
import net.varkhan.pres.format.HtmlFormatter;
import net.varkhan.pres.render.Renderable;
import net.varkhan.pres.render.Renderer;
import net.varkhan.pres.widget.Widget;

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
