package net.varkhan.pres.widget.page;

import net.varkhan.pres.widget.Linked;
import net.varkhan.pres.widget.Located;
import net.varkhan.pres.widget.Widget;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:02 PM
 */
public abstract class WrapperWidget<L,P> extends AbstractWidget<L,P> implements Widget<L,P>, Linked<L,P>, Located<L,P> {
    protected final Widget<L,P> widget;

    public WrapperWidget(String id, Widget<L,P> widget) {
        super(id);
        this.widget=widget;
    }

    public String title(L loc, P par) {
        return widget.title(loc, par);
    }

    public String desc(L loc, P par) {
        return widget.desc(loc, par);
    }

    @SuppressWarnings({ "unchecked" })
    public Collection<String> getLinks(String type, L loc, P par) {
        if(widget instanceof Linked) return ((Linked<L,P>) widget).getLinks(type, loc, par);
        return null;
    }

    @SuppressWarnings({ "unchecked" })
    public void addLink(String type, String link) {
        if(widget instanceof Linked) ((Linked<L,P>) widget).addLink(type, link);
    }

    @SuppressWarnings({ "unchecked" })
    public String getLoc(L loc, P par) {
        if(widget instanceof Located) return ((Located) widget).getLoc(loc, par);
        return null;
    }
}
