package net.varkhan.pres.widget;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/12/11
 * @time 5:30 AM
 */
public interface Widget<P> {
    public String id();
    public String name();
    public String title();
    public String desc();
    public String link(P par);
}
