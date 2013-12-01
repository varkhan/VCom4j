package net.varkhan.core.pres.widget.page;

import net.varkhan.core.pres.format.HtmlFormatter;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 4/28/12
 * @time 4:23 PM
 */
public class HtmlCanvas<F extends HtmlFormatter,L,P> extends ContainerWidget<L,P> implements HtmlPage<F,L,P> {
    private final Map<HtmlPage<F,L,P>,int[]> pages = new LinkedHashMap<HtmlPage<F,L,P>,int[]>();

    public HtmlCanvas(String id) {
        super(id);
    }

    public Collection<HtmlPage<F,L,P>> widgets() {
        return pages.keySet();
    }

    public void render(F fmt, L loc, P par) throws IOException {
        fmt.div_("id",id);
        for(Map.Entry<HtmlPage<F,L,P>,int[]> p: pages.entrySet()) {
            HtmlPage<F,L,P> page=p.getKey();
            int[] pos=p.getValue();
            fmt.div_("id", "outer_"+page.id(), "style", "position: absolute; width:0; width:0");
            if(pos.length>=4) {
                fmt.div_("id", "inner_"+page.id(), "style",
                         "position: relative;"
                         + " left:"+pos[0]+"px;"
                         + " top:"+pos[1]+"px;"
                         + " width:"+pos[2]+"px;"
                         + " height:"+pos[3]+"px;");
            }
            else if(pos.length>=2) {
                fmt.div_("id", "inner_"+page.id(), "style",
                         "position: relative;"
                         + " left:"+pos[0]+"px;"
                         + " top:"+pos[1]+"px;"
                        );
            }
            else {
                fmt.div_("id", "inner_"+page.id(), "style",
                         "position: relative;"
                        );
            }
            page.render(fmt, loc, par);
            fmt._div();
            fmt._div();
        }
        fmt._div();
    }

    public String getLoc(L loc, P par) {
        return "#"+name;
    }

    public void addPage(HtmlPage<F,L,P> page,int x, int y) {
        pages.put(page, new int[] { x, y });
    }

    public void addPage(HtmlPage<F,L,P> page,int x, int y, int w, int h) {
        pages.put(page, new int[] { x, y, w, h });
    }

}
