package net.varkhan.core.pres.widget.page;

import net.varkhan.core.pres.widget.Linked;
import net.varkhan.core.pres.widget.Widget;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 8:02 PM
 */
public abstract class ContainerWidget<L,P> extends SimpleWidget<L,P> implements Widget<L,P>, Linked<L,P> {
    protected final Map<String,List<String>> links = new HashMap<String,List<String>>();

    public ContainerWidget(String id) {
        super(id);
    }

    @SuppressWarnings({ "unchecked" })
    public Collection<String> getLinks(String type, L loc, P par) {
        Collection<String> lk = new ArrayList<String>();
        Collection<String> lk1=links.get(type);
        if(lk1!=null) lk.addAll(lk1);
        for(Widget<L,P> w: widgets()) {
            if(w instanceof Linked) {
                Collection<String> lk2 = ((Linked) w).getLinks(type, loc, par);
                if(lk2!=null) lk.addAll(lk2);
            }
        }
        return lk;
    }

    public abstract Collection<? extends Widget<L,P>> widgets();

    @SuppressWarnings({ "unchecked" })
    public void addLink(String type, String link) {
        List<String> lk = links.get(type);
        if(lk==null) links.put(type,lk=new ArrayList<String>());
        lk.add(link);
    }

}
