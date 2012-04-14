package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.HtmlDocFormatter;
import net.varkhan.pres.widget.Linked;

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
public class HtmlDocPage<F extends HtmlDocFormatter,P> extends ContainerWidget<String[],P> implements Page<F,P> {

    public HtmlDocPage(Page<? super F,P> page) {
        super("_page", page);
    }

    public void render(F fmt, String[] loc, P par) throws IOException {
        fmt.setTitle(widget.title(loc,par));
        if(widget instanceof Linked) {
            Collection<String> cssurls = ((Linked<String[],P>) widget).getLinks("cssurl", loc, par);
            if(cssurls!=null) for(String url: cssurls) fmt.addCssUrl(url);
            Collection<String> cssdefs = ((Linked<String[],P>) widget).getLinks("cssdef", loc, par);
            if(cssdefs!=null) for(String def: cssdefs) fmt.addCssDef(def);
        }
        fmt.open();
        fmt.append("\n");
        ((Page<F,P>)widget).render(fmt, loc, par);
        fmt.append("\n");
        fmt.close();
    }

}
