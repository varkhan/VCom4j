package net.varkhan.pres.widget.page;

import net.varkhan.pres.format.HtmlFormatter;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 1:52 PM
 */
public class HtmlWindow<F extends HtmlFormatter,L,P> extends WrapperWidget<L,P> implements HtmlPage<F,L,P> {
    protected final String cssclass;

    public HtmlWindow(String id, Page<? super F,L,P> page, String cssclass) {
        super(id, page);
        this.cssclass = cssclass==null?"":(" "+cssclass);
    }

    @SuppressWarnings({ "unchecked" })
    public void render(F fmt, L loc, P par) throws IOException {
        fmt.div_("id",id,"class","window frame"+cssclass).ln();
        fmt.div_("class","window title"+cssclass);
        fmt.append(widget.title(loc, par));
        fmt._div().ln(); // window title
        fmt.div_("class", "window pane"+cssclass);
        ((Page<? super F,L,P>)widget).render(fmt, loc, par);
        fmt._div().ln(); // window pane
        fmt._div().ln(); // window frame
    }

}
