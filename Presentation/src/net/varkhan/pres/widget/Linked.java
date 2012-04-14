package net.varkhan.pres.widget;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/14/12
 * @time 11:24 AM
 */
public interface Linked<L,P> {

    public Collection<String> getLinks(String type, L loc, P par);

    public void addLink(String type, String link, Object... args);

}
