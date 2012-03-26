package net.varkhan.pres.widget.page;

import net.varkhan.pres.widget.Widget;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:02 PM
 */
public abstract class ContainerWidget<L,P> extends AbstractWidget<L,P> implements Widget<L,P> {
    protected final Widget<L,P> widget;

    public ContainerWidget(String id, Widget<L,P> widget) {
        super(id);
        this.widget=widget;
    }

    public String title(L loc, P par) {
        return widget.title(loc, par);
    }

    public String desc(L loc, P par) {
        return widget.desc(loc, par);
    }

}
