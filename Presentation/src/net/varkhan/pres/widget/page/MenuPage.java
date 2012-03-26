package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.HtmlFormatter;
import net.varkhan.pres.render.Renderable;
import net.varkhan.pres.widget.menu.Menu;
import net.varkhan.pres.widget.menu.MenuContentRenderer;
import net.varkhan.pres.widget.menu.MenuSelectorRenderer;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:16 PM
 */
public class MenuPage<F extends HtmlFormatter,P> extends AbstractWidget<String[],P> implements Page<F,P>, Renderable<F,String[],P> {

    protected String title = null;
    protected final MenuSelectorRenderer<F,Page<F,P>,P> srdr;
    protected final MenuContentRenderer<F,Page<F,P>,P> crdr;
    protected final Menu<Page<F,P>> menu;

    public MenuPage(String id, MenuSelectorRenderer<F,Page<F,P>,P> srdr, MenuContentRenderer<F,Page<F,P>,P> crdr, Menu<Page<F,P>> menu) {
        super(id);
        this.srdr=srdr;
        this.crdr=crdr;
        this.menu=menu;
    }

    @Override
    public void render(F fmt, String[] loc, P par) throws IOException {
        switch(srdr.orientation()) {
            case H:
                fmt.div_();
                srdr.render(fmt, loc, menu, par);
                fmt._div();
                fmt.append("\n");
                fmt.div_();
                crdr.render(fmt, loc, menu, par);
                fmt._div();
                break;
            case V:
                fmt.div_("style", "float:left");
                srdr.render(fmt, loc, menu, par);
                fmt._div();
                fmt.append("\n");
                fmt.div_("style", "float:right");
                crdr.render(fmt, loc, menu, par);
                fmt._div();
                break;
        }
    }

    @Override
    public String title(String[] loc, P par) {
        Page<F,P> item=menu.getItem(loc);
        if(title==null) return item==null?null:item.title(loc, par);
        return item==null?title:title+": "+item.title(loc, par);
    }

    public void setTitle(String title) {
        this.title=title;
    }

    @Override
    public String desc(String[] loc, P par) {
        return menu.getItem(loc).desc(loc, par);
    }

    public String link(String[] loc, P par) {
        return menu.getItem(loc).link(loc,par);
    }

    public boolean addItem(Page<F,P> item, String id, String... loc) {return menu.addItem(item, id, loc);}

}
