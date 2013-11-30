package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.Formatter;
import net.varkhan.pres.render.Renderable;
import net.varkhan.pres.widget.Linked;
import net.varkhan.pres.widget.Located;
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
public interface Page<F extends Formatter,L,P> extends Widget<L,P>, Linked<L,P>, Located<L,P>, Renderable<F,L,P> {

    public void render(F fmt, L loc, P par) throws IOException;

}
