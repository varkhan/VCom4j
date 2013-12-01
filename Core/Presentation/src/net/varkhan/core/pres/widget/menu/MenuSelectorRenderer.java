package net.varkhan.core.pres.widget.menu;

import net.varkhan.core.pres.format.Formatter;
import net.varkhan.core.pres.render.Renderer;
import net.varkhan.core.pres.widget.Orientation;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:19 PM
 */
public interface MenuSelectorRenderer<F extends Formatter, W, P> extends Renderer<F,String[],Menu<W>,P> {

    public void render(F fmt, String[] loc, Menu<W> obj, P par) throws IOException;

    public Orientation orientation();

    public static interface Layout {
        abstract String[] itemAttr(String id, boolean selected);
        abstract String[] subsAttr(String id, boolean selected);
    }
}
