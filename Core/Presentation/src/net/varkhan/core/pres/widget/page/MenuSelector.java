package net.varkhan.core.pres.widget.page;

import net.varkhan.core.pres.format.Formatter;
import net.varkhan.core.pres.render.Renderable;
import net.varkhan.core.pres.widget.menu.Menu;
import net.varkhan.core.pres.widget.menu.MenuSelectorRenderer;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:16 PM
 */
public class MenuSelector<F extends Formatter,I,P> implements Renderable<F,String[],P> {

    private final MenuSelectorRenderer<F,I,P> renderer;
    private final Menu<I> menu;

    public MenuSelector(MenuSelectorRenderer<F,I,P> renderer, Menu<I> menu) {
        this.renderer=renderer;
        this.menu=menu;
    }

    @Override
    public void render(F fmt, String[] loc, P par) throws IOException {
        renderer.render(fmt, loc, menu, par);
    }

}
