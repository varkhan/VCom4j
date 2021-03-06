package net.varkhan.core.pres.widget.list;

import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.render.Renderer;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/9/11
 * @time 9:20 AM
 */
public class ListRenderer<F extends HtmlFormatter,I,P> implements Renderer<F,Integer,List<I>,P> {

    protected Renderer<F,Integer,I,P> itemRenderer;

    public ListRenderer(Renderer<F,Integer,I,P> itemRenderer) { this.itemRenderer=itemRenderer; }

    public void render(F fmt, Integer loc, List<I> obj, P par) throws IOException {
        ListIterator<I> it = obj.listIterator();
        while(it.hasNext()) {
            fmt.div_();
            itemRenderer.render(fmt,it.nextIndex(),it.next(),par);
            fmt._div();
        }
    }
}
