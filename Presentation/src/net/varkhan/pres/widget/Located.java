package net.varkhan.pres.widget;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/14/12
 * @time 11:49 AM
 */
public interface Located<L, P> {
    public String href(L loc, P par);
}
