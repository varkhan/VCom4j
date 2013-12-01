package net.varkhan.core.pres.widget.menu;

import net.varkhan.base.containers.array.Arrays;
import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.render.Renderable;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 5:16 PM
 */
public class PMenuContentRenderer<W extends Renderable<HtmlFormatter,String[],P>, P> implements MenuContentRenderer<HtmlFormatter,W,P> {

    protected Renderable<HtmlFormatter,String[],P> defcont;

    public PMenuContentRenderer(Renderable<HtmlFormatter,String[],P> defcont) {
        this.defcont=defcont;
    }

    @Override
    public void render(HtmlFormatter fmt, String[] loc, Menu<W> obj, P par) throws IOException {
        Renderable<HtmlFormatter,String[],P> item=obj.getItem(loc);
        if(item==null) item = defcont;
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
        item.render(sfmt, sloc, par);
        if(sfmt!=fmt) sfmt.close();
    }
}
