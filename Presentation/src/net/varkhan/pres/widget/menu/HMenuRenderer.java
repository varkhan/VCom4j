package net.varkhan.pres.widget.menu;

import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.pres.format.HtmlFormatter;
import net.varkhan.pres.render.Renderer;
import net.varkhan.pres.widget.Widget;

import java.io.IOException;

import static net.varkhan.pres.format.HtmlFormatter.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/13/11
 * @time 9:23 PM
 */
public class HMenuRenderer<P> implements Renderer<HtmlFormatter,String[],Menu<Widget<P>>,P> {

    public static enum Layout {
        EXPANDED {
            protected String[] itemAttr(String id, boolean selected) {
                return new String[] {};
            }

            protected String[] subsAttr(String id, boolean selected) {
                return new String[] {};
            }
        },
        CASCADED {
            protected String[] itemAttr(String id, boolean selected) {
                return new String[] {};
            }

            protected String[] subsAttr(String id, boolean selected) {
                return new String[] {
                        ATR_STYLE,"display: "+(selected?"block":"none"),
                };
            }
        },
        HOVER {
            protected String[] itemAttr(String id, boolean selected) {
                return new String[] {
                        "onMouseOver","javascript: document.getElementById('MenuSubs_"+id+"').style.display = 'block';",
                        "onMouseOut", "javascript: document.getElementById('MenuSubs_"+id+"').style.display = 'none';",
                };
            }

            protected String[] subsAttr(String id, boolean selected) {
                return new String[] {
                        ATR_STYLE,"position: absolute; z-index: 1; display: none",
                        "onMouseOver","javascript: document.getElementById('MenuSubs_"+id+"').style.display = 'block';",
                        "onMouseOut", "javascript: document.getElementById('MenuSubs_"+id+"').style.display = 'none';",
                };
            }
        },
        CLICK {
            protected String[] itemAttr(String id, boolean selected) {
                return new String[] {
                        ATR_STYLE,"display: inline",
                        "onClick","javascript: " +
                                  "var elt = document.getElementById('MenuSubs_"+id+"') ;" +
                                  "if(elt.style.display=='none') {elt.style.display='inline';} else {elt.style.display='none';}",

                };
            }

            protected String[] subsAttr(String id, boolean selected) {
                return new String[] {
                        ATR_STYLE,"display: none",
                };
            }
        },
        ;

        protected abstract String[] itemAttr(String id, boolean selected);
        protected abstract String[] subsAttr(String id, boolean selected);

    }

    private Layout          layout;
    private HMenuRenderer<P> subrdr = this;

    public void render(HtmlFormatter fmt, String[] loc, Menu<Widget<P>> obj, P par) throws IOException {
        fmt.tb_();
        fmt.tr_();
        Iterator<Menu.Item<Widget<P>>> it = (Iterator<Menu.Item<Widget<P>>>) obj.items().iterator();
        while(it.hasNext()) {
            Menu.Item<Widget<P>> item=it.next();
            Widget<P> widget = item.getWidget();
            boolean selected = loc!=null && loc.length>0 && item.id().equals(loc[0]);
            fmt.td_(ATR_ID,"MenuBlock_"+item.id(),ATR_CLASS,"menu block");
            fmt.div_(new String[][]{ new String[]{ATR_ID, "MenuItem_"+item.id(), ATR_CLASS, "menu item"+(selected ? " selected" : "")},layout.itemAttr(item.id(),selected)});
            renderWidget(fmt, loc, widget, par);
            fmt._div();
            fmt.div_(new String[][]{ new String[]{ATR_ID,"MenuSubs_"+item.id(),ATR_CLASS,"menu subs"+(selected?" selected":"")},layout.subsAttr(item.id(),selected)});
            subrdr.render(fmt, (loc!=null&&loc.length>0) ? Arrays.subarray(loc, 1, loc.length) : new String[0], item, par);
            fmt._div();
            fmt._td();
        }
        fmt._tr();
        fmt._tb();
    }

    protected void renderWidget(HtmlFormatter fmt, String[] loc, Widget<P> obj, P par) throws IOException {
        fmt.a(obj.name(), obj.id(), obj.link(par), "title", obj.title());
    }

}
