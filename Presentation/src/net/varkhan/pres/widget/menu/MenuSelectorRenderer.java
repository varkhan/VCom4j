package net.varkhan.pres.widget.menu;

import com.sun.xml.internal.ws.handler.HandlerTube;
import net.varkhan.pres.format.Formatter;
import net.varkhan.pres.format.HtmlFormatter;
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
public interface MenuSelectorRenderer<F extends Formatter, W, P> extends Renderer<F,String[],Menu<W>,P> {

    public void render(F fmt, String[] loc, Menu<W> obj, P par) throws IOException;

    public static enum Orientation {
        V, H
    }

    public Orientation orientation();

    public static interface Layout {
        abstract String[] itemAttr(String id, boolean selected);
        abstract String[] subsAttr(String id, boolean selected);
    }
}
