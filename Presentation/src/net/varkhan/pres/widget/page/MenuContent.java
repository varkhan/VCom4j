package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.HtmlFormatter;
import net.varkhan.pres.render.Renderable;
import net.varkhan.pres.widget.menu.Menu;
import net.varkhan.pres.widget.menu.MenuContentRenderer;
import net.varkhan.pres.widget.page.Page;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:16 PM
 */
public class MenuContent<F extends HtmlFormatter,I,P> implements Renderable<F,String[],P> {

    private final MenuContentRenderer<F,I,P> renderer;
    private final Menu<I> menu;

    public MenuContent(MenuContentRenderer<F,I,P> renderer, Menu<I> menu) {
        this.renderer=renderer;
        this.menu=menu;
    }

    @Override
    public void render(F fmt, String[] loc, P par) throws IOException {
        renderer.render(fmt, loc, menu, par);
    }

}
