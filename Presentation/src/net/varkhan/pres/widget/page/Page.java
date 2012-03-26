package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.Formatter;
import net.varkhan.pres.render.Renderable;
import net.varkhan.pres.widget.Widget;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/12/11
 * @time 5:39 AM
 */
public interface Page<F extends Formatter,P> extends Widget<String[],P>, Renderable<F,String[],P> {

    public void render(F fmt, String[] loc, P par) throws IOException;

}
