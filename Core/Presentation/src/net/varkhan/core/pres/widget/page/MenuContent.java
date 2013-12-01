package net.varkhan.core.pres.widget.page;

import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.render.Renderable;
import net.varkhan.core.pres.widget.Linked;
import net.varkhan.core.pres.widget.menu.Menu;
import net.varkhan.core.pres.widget.menu.MenuContentRenderer;

import java.io.IOException;
import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:16 PM
 */
public class MenuContent<F extends HtmlFormatter,I,P> implements Renderable<F,String[],P>, Linked<String[],P> {

    private final MenuContentRenderer<F,I,P> renderer;
    private final Menu<I> menu;

    public MenuContent(MenuContentRenderer<F,I,P> renderer, Menu<I> menu) {
        this.renderer=renderer;
        this.menu=menu;
    }

    public void render(F fmt, String[] loc, P par) throws IOException {
        renderer.render(fmt, loc, menu, par);
    }

    public Collection<String> getLinks(String type, String[] loc, P par) {
        I it = menu.getItem(loc);
        if(it instanceof Linked) return ((Linked) it).getLinks(type,loc,par);
        return null;
    }

    public void addLink(String type, String link) {
    }
}
