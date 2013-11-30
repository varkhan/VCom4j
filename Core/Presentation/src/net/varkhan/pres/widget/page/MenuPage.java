package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.HtmlFormatter;
import net.varkhan.pres.widget.menu.Menu;
import net.varkhan.pres.widget.menu.MenuContentRenderer;
import net.varkhan.pres.widget.menu.MenuSelectorRenderer;

import java.io.IOException;
import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:16 PM
 */
public class MenuPage<F extends HtmlFormatter,P> extends AbstractWidget<String[],P> implements HtmlPage<F,String[],P> {

    protected String title = null;
    protected final MenuSelectorRenderer<F,Page<F,String[],P>,P> srdr;
    protected final MenuContentRenderer<F,Page<F,String[],P>,P> crdr;
    protected final Menu<Page<F,String[],P>> menu;
    protected final Map<String,List<String>> links = new HashMap<String,List<String>>();

    public MenuPage(String id, MenuSelectorRenderer<F,Page<F,String[],P>,P> srdr, MenuContentRenderer<F,Page<F,String[],P>,P> crdr, Menu<Page<F,String[],P>> menu) {
        super(id);
        this.srdr=srdr;
        this.crdr=crdr;
        this.menu=menu;
    }

    @Override
    public void render(F fmt, String[] loc, P par) throws IOException {
        switch(srdr.orientation()) {
            case H:
                fmt.div_("id",id(),"class","menu bar Hmenu");
                srdr.render(fmt, loc, menu, par);
                fmt._div();
                fmt.append("\n");
                fmt.div_("class","menu page Hmenu");
                crdr.render(fmt, loc, menu, par);
                fmt._div();
                break;
            case V:
                fmt.div_("id",id(),"class","menu bar Vmenu","style", "float:left");
                srdr.render(fmt, loc, menu, par);
                fmt._div();
                fmt.append("\n");
                fmt.div_("class","menu page Vmenu","style", "float:right");
                crdr.render(fmt, loc, menu, par);
                fmt._div();
                break;
        }
    }

    @Override
    public String title(String[] loc, P par) {
        Page<F,String[],P> item=menu.getItem(loc);
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

    public String getLoc(String[] loc, P par) {
        return menu.getItem(loc).getLoc(loc, par);
    }

    public boolean addItem(Page<F,String[],P> item, String id, String... loc) {
        return menu.addItem(item, id, loc);
    }

    public Collection<String> getLinks(String type, String[] loc, P par) {
        Collection<String> lk1=links.get(type);
        Page<F,String[],P> it = menu.getItem(loc);
        if(it==null) return lk1;
        Collection<String> lk2 = it.getLinks(type,loc,par);
        if(lk2==null) return lk1;
        if(lk1==null) return lk2;
        Collection<String> lk = new ArrayList<String>();
        lk.addAll(lk1);
        lk.addAll(lk2);
        return lk;
    }

    public void addLink(String type, String link) {
        List<String> lk = links.get(type);
        if(lk==null) links.put(type,lk=new ArrayList<String>());
        lk.add(link);
    }

}
