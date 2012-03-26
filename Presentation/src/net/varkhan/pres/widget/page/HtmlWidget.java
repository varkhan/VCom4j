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
public class HtmlWidget<F extends HtmlFormatter,P> extends SimpleWidget<String[],P> implements HtmlPage<F,P> {

    public HtmlWidget(String id) {
        super(id);
    }

    public void render(F fmt, String[] loc, P par) throws IOException {
        fmt.div_();
        fmt.div_();
        fmt.a(title, name, link(loc, par));
        fmt.div(desc);
        fmt._div();
        fmt._div();
    }

    public String link(String[] loc, P par) {
        return "#"+name;
    }

}
