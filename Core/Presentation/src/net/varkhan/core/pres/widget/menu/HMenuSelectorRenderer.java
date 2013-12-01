package net.varkhan.core.pres.widget.menu;

import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.core.pres.format.HtmlFormatter;
import net.varkhan.core.pres.format.XmlFormatter;
import net.varkhan.core.pres.widget.Located;
import net.varkhan.core.pres.widget.Orientation;
import net.varkhan.core.pres.widget.Widget;

import java.io.IOException;

import static net.varkhan.core.pres.format.HtmlFormatter.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/13/11
 * @time 9:23 PM
 */
public class HMenuSelectorRenderer<W extends Widget<String[], P> & Located<String[], P>,P> implements MenuSelectorRenderer<HtmlFormatter,W,P> {

    public HMenuSelectorRenderer(Layout layout, MenuSelectorRenderer<HtmlFormatter,W,P> subrdr) {
        this.layout=layout;
        this.subrdr=subrdr;
    }

    public HMenuSelectorRenderer(Layout layout) {
        this.layout=layout;
        this.subrdr=this;
    }

    public Orientation orientation() { return Orientation.H; }

    public static enum Layout implements MenuSelectorRenderer.Layout {
        EXPANDED {
            public String[] itemAttr(String id, boolean selected) {
                return new String[] {};
            }

            public String[] subsAttr(String id, boolean selected) {
                return new String[] {};
            }
        },
        CASCADED {
            public String[] itemAttr(String id, boolean selected) {
                return new String[] {};
            }

            public String[] subsAttr(String id, boolean selected) {
                return new String[] {
                        ATR_STYLE,"display: "+(selected?"block":"none"),
                };
            }
        },
        HOVER {
            public String[] itemAttr(String id, boolean selected) {
                return new String[] {
                        "onMouseOver","javascript: document.getElementById('MenuSubs_"+id+"').style.display = 'block';",
                        "onMouseOut", "javascript: document.getElementById('MenuSubs_"+id+"').style.display = 'none';",
                };
            }

            public String[] subsAttr(String id, boolean selected) {
                return new String[] {
                        ATR_STYLE,"position: absolute; z-index: 1; display: none",
                        "onMouseOver","javascript: document.getElementById('MenuSubs_"+id+"').style.display = 'block';",
                        "onMouseOut", "javascript: document.getElementById('MenuSubs_"+id+"').style.display = 'none';",
                };
            }
        },
        CLICK {
            public String[] itemAttr(String id, boolean selected) {
                return new String[] {
                        ATR_STYLE,"display: inline",
                        "onClick","javascript: " +
                                  "var elt = document.getElementById('MenuSubs_"+id+"') ;" +
                                  "if(elt.style.display=='none') {elt.style.display='block';} else {elt.style.display='none';}",

                };
            }

            public String[] subsAttr(String id, boolean selected) {
                return new String[] {
                        ATR_STYLE,"display: none",
                };
            }
        },
        ;

    }

    private final Layout layout;
    private final MenuSelectorRenderer<HtmlFormatter,W,P> subrdr;

    public void render(HtmlFormatter fmt, String[] loc, Menu<W> obj, P par) throws IOException {
        fmt.tb_(ATR_CLASS,"menu block");
        fmt.tr_(ATR_CLASS,"menu block");
        Iterator<Menu.Item<W>> it = (Iterator<Menu.Item<W>>) obj.items().iterator();
        while(it.hasNext()) {
            Menu.Item<W> item=it.next();
            boolean selected = (loc==null || loc.length==0)? item.id().length()==0:item.id().equals(loc[0]);
            fmt.td_(XmlFormatter.ATR_ID,"MenuBlock_"+item.id(),ATR_CLASS,"menu block"+(selected?" selected":""),"cellspacing","0","cellpadding","0");
            fmt.div_(new String[][]{ new String[]{XmlFormatter.ATR_ID,"MenuItem_"+item.id(),ATR_CLASS,"menu item"+(selected?" selected":"")},layout.itemAttr(item.id(),selected)});
            renderWidget(fmt, loc, item, par);
            fmt._div();
            fmt.div_(new String[][]{ new String[]{XmlFormatter.ATR_ID,"MenuSubs_"+item.id(),ATR_CLASS,"menu subs"+(selected?" selected":"")},layout.subsAttr(item.id(),selected)});
            renderSubMenu(fmt, loc, item, par);
            fmt._div();
            fmt._td();
        }
        fmt._tr();
        fmt._tb();
    }

    protected void renderSubMenu(HtmlFormatter fmt, String[] loc, Menu.Item<W> item, P par) throws IOException {
        fmt.append("\n");
        HtmlFormatter sfmt = new HtmlFormatter(fmt);
        sfmt.setBaseUrl(fmt.getBaseUrl()+'/'+item.id);
        sfmt.open();
        String[] sloc = Menu.NO_LOC;
        if(loc!=null && loc.length>0) sloc = Arrays.subarray(loc, 1, loc.length);
        subrdr.render(sfmt, sloc, item, par);
        sfmt.close();
        fmt.append("\n");
    }

    protected void renderWidget(HtmlFormatter fmt, String[] loc, Menu.Item<W> item, P par) throws IOException {
        fmt.append("\n");
        W obj = item.it();
        HtmlFormatter sfmt = new HtmlFormatter(fmt);
        sfmt.setBaseUrl(fmt.getBaseUrl()+'/'+item.id);
        sfmt.open();
        String[] sloc = Menu.NO_LOC;
        if(loc!=null && loc.length>0) sloc = Arrays.subarray(loc, 1, loc.length);
        sfmt.a(obj.name(), obj.id(), obj.getLoc(sloc, par), "title", obj.title(loc,par));
        sfmt.close();
        fmt.append("\n");
    }

}
