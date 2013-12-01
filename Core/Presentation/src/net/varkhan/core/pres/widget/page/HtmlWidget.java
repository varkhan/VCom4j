package net.varkhan.core.pres.widget.page;

import net.varkhan.core.pres.format.HtmlFormatter;

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
public class HtmlWidget<F extends HtmlFormatter,L,P> extends SimpleWidget<L,P> implements HtmlPage<F,L,P> {

    protected final Map<String,List<String>> links = new HashMap<String,List<String>>();

    public HtmlWidget(String id) {
        super(id);
    }

    public void render(F fmt, L loc, P par) throws IOException {
        fmt.div_();
        fmt.div_();
        fmt.a_(name, getLoc(loc, par));
        fmt.append(title(loc, par));
        fmt._a();
        fmt.div(desc);
        fmt._div();
        fmt._div();
    }

    public HtmlWidget<F,L,P> setName(String name) {
        super.setName(name);
        return this;
    }

    public HtmlWidget<F,L,P> setTitle(String title) {
        super.setTitle(title);
        return this;
    }

    public HtmlWidget<F,L,P> setDesc(String desc) {
        super.setDesc(desc);
        return this;
    }

    public String getLoc(L loc, P par) {
        return "#"+name;
    }

    public Collection<String> getLinks(String type, L loc, P par) {
        return links.get(type);
    }

    public void addLink(String type, String link) {
        List<String> lk = links.get(type);
        if(lk==null) links.put(type,lk=new ArrayList<String>());
        lk.add(link);
    }

}
