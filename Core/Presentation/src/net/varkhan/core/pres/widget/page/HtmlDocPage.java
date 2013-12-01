package net.varkhan.core.pres.widget.page;

import net.varkhan.core.pres.format.HtmlDocFormatter;
import net.varkhan.core.pres.widget.Linked;

import java.io.IOException;
import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 1:52 PM
 */
public class HtmlDocPage<F extends HtmlDocFormatter,L,P> extends WrapperWidget<L,P> implements HtmlPage<F,L,P> {

    public HtmlDocPage(Page<? super F,L,P> page) {
        super("_page", page);
    }

    @SuppressWarnings({ "unchecked" })
    public void render(F fmt, L loc, P par) throws IOException {
        fmt.setTitle(widget.title(loc,par));
        if(widget instanceof Linked) {
            Collection<String> cssurls = ((Linked<L,P>) widget).getLinks("cssurl", loc, par);
            if(cssurls!=null) for(String url: cssurls) fmt.addCssUrl(url);
            Collection<String> cssdefs = ((Linked<L,P>) widget).getLinks("cssdef", loc, par);
            if(cssdefs!=null) for(String def: cssdefs) fmt.addCssDef(def);
        }
        fmt.open();
        fmt.append("\n");
        ((Page<? super F,L,P>)widget).render(fmt, loc, par);
        fmt.append("\n");
        fmt.close();
    }

}
