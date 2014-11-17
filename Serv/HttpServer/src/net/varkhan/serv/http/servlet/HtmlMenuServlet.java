package net.varkhan.serv.http.servlet;

import net.varkhan.base.containers.Container;
import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.widget.menu.Menu;
import net.varkhan.core.pres.widget.menu.MenuSelectorRenderer;
import net.varkhan.core.pres.widget.menu.PMenuContentRenderer;
import net.varkhan.core.pres.widget.page.*;

import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/17/12
 * @time 6:30 PM
 */
public class HtmlMenuServlet extends HtmlPageServlet {

    protected Page<HtmlFormatter,String[],Map<String,String[]>> home;
    protected Menu<Page<HtmlFormatter,String[],Map<String,String[]>>> menu;

    public HtmlMenuServlet(
            Page<HtmlFormatter,String[],Map<String,String[]>> home,
            Menu<Page<HtmlFormatter,String[],Map<String,String[]>>> menu,
            MenuSelectorRenderer<HtmlFormatter,Page<HtmlFormatter,String[],Map<String,String[]>>,Map<String,String[]>> style) {
        this.home = home;
        this.menu = menu;
        this.page = new MenuPage<HtmlFormatter,Map<String,String[]>>(
                "_page", style,
                new PMenuContentRenderer<Page<HtmlFormatter,String[],Map<String,String[]>>,Map<String,String[]>>(home),
                this.menu);
        this.menu.addItem(this.home, "");
    }

    public Page<HtmlFormatter,String[],Map<String,String[]>> getItem(String[] loc) {return menu.getItem(loc);}

    public boolean addItem(Page<HtmlFormatter,String[],Map<String,String[]>> item, String id, String... loc) {
        return menu.addItem(item, id, loc);
    }

    public Container<Menu.Item<Page<HtmlFormatter,String[],Map<String,String[]>>>> items() {return menu.items();}



}
