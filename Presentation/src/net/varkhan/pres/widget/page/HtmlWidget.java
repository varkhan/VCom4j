package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.HtmlFormatter;

import java.io.IOException;
import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 1:52 PM
 */
public class HtmlWidget<F extends HtmlFormatter,P> extends SimpleWidget<String[],P> implements HtmlPage<F,P> {

    protected final Map<String,List<String>> links = new HashMap<String,List<String>>();

    public HtmlWidget(String id) {
        super(id);
    }

    public void render(F fmt, String[] loc, P par) throws IOException {
        fmt.div_();
        fmt.div_();
        fmt.a(title, name, href(loc, par));
        fmt.div(desc);
        fmt._div();
        fmt._div();
    }

    public HtmlWidget<F,P> setName(String name) {
        super.setName(name);
        return this;
    }

    public HtmlWidget<F,P> setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    public HtmlWidget<F,P> setDesc(String desc) {
        super.setDesc(desc);
        return this;
    }

    public String href(String[] loc, P par) {
        return "#"+name;
    }

    public Collection<String> getLinks(String type, String[] loc, P par) {
        return links.get(type);
    }

    public void addLink(String type, String link, Object... args) {
        List<String> lk = links.get(type);
        if(lk==null) links.put(type,lk=new ArrayList<String>());
        lk.add(makeLink(type, link, args));
    }

    protected String makeLink(String type, String link, Object... args) { return link; }


}
