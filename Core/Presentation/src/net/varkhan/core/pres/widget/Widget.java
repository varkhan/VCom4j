package net.varkhan.core.pres.widget;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/12/11
 * @time 5:30 AM
 */
public interface Widget<L,P> {
    public String id();
    public String name();
    public String title(L loc, P par);
    public String desc(L loc, P par);
}
