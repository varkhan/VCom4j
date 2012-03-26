package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.HtmlDocFormatter;

import java.io.IOException;


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
        super("_page",page);
    }

    public void render(F fmt, String[] loc, P par) throws IOException {
        fmt.setTitle(widget.title(loc,par));
        fmt.open();
        fmt.append("\n");
        ((Page<F,P>)widget).render(fmt, loc, par);
        fmt.append("\n");
        fmt.close();
    }

    public String link(String[] loc, P par) {
        return widget.link(loc,par);
    }

}
