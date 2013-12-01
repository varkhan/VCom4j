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
public abstract class AbstractWidget<L,P> implements Widget<L,P> {
    protected final String id;
    protected String name;

    public AbstractWidget(String id) {
        this.id=id;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public AbstractWidget<L,P> setName(String name) {
        this.name=name;
        return this;
    }

}
