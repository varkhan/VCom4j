package net.varkhan.core.pres.widget.menu;

import net.varkhan.base.containers.array.Arrays;
import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.render.Renderer;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 5:16 PM
 */
public class RMenuContentRenderer<I,P> implements MenuContentRenderer<HtmlFormatter,I,P> {


    protected final Renderer<HtmlFormatter,String[],I,P> renderer;

    public RMenuContentRenderer(Renderer<HtmlFormatter,String[],I,P> renderer) {
        this.renderer=renderer;
    }

    @Override
    public void render(HtmlFormatter fmt, String[] loc, Menu<I> obj, P par) throws IOException {
        HtmlFormatter sfmt = fmt;
        if(loc!=null && loc.length>0) {
            sfmt = new HtmlFormatter(fmt);
            sfmt.setBaseUrl(fmt.getBaseUrl()+'/'+loc[0]);
            sfmt.open();
        }
        String[] sloc = Menu.NO_LOC;
        if(loc!=null && loc.length>0) {
            sloc = Arrays.subarray(loc, 1, loc.length);
        }
        I item=obj.getItem(loc);
        renderer.render(sfmt, sloc, item, par);
        if(sfmt!=fmt) sfmt.close();
    }
}
