package net.varkhan.core.pres.widget.page;

import net.varkhan.core.pres.widget.Widget;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:02 PM
 */
public abstract class SimpleWidget<L,P> extends AbstractWidget<L, P> implements Widget<L,P> {
    protected String title;
    protected String desc;

    public SimpleWidget(String id) {
        super(id);
    }

    public String name() {
        return name;
    }

    public SimpleWidget<L,P>  setName(String name) {
        this.name = name;
        return this;
    }

    public String title(L loc, P par) {
        return title;
    }

    public SimpleWidget<L,P>  setTitle(String title) {
        this.title=title;
        return this;
    }

    public String desc(L loc, P par) {
        return desc;
    }

    public SimpleWidget<L,P>  setDesc(String desc) {
        this.desc=desc;
        return this;
    }

}
