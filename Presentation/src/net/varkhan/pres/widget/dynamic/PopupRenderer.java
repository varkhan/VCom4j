package net.varkhan.pres.widget.dynamic;

import net.varkhan.pres.format.HtmlFormatter;
import net.varkhan.pres.render.Renderer;

import java.io.IOException;

import static net.varkhan.pres.format.HtmlFormatter.*;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/13/11
 * @time 6:17 PM
 */
public class PopupRenderer<F extends HtmlFormatter,L,I,P> implements Renderer<F,L,I,P> {

    private int zindex = 1;
    private String popupStyle=null;
    private String popupClass=null;

    private final Renderer<F,L,I,P> inset;
    private final Renderer<F,L,I,P> popup;

    public PopupRenderer(Renderer<F,L,I,P> inset, Renderer<F,L,I,P> popup) {
        this.inset=inset;
        this.popup=popup;
    }

    public PopupRenderer(Renderer<F,L,I,P> inset, Renderer<F,L,I,P> popup, int zindex, String popupClass, String popupStyle) {
        this.inset=inset;
        this.popup=popup;
        this.zindex=zindex;
        this.popupClass=popupClass;
        this.popupStyle=popupStyle;
    }

    public int getZIndex() { return zindex; }

    public void setZIndex(int zindex) { this.zindex=zindex; }

    public String getPopupStyle() { return popupStyle; }

    public void setPopupStyle(String popupStyle) { this.popupStyle=popupStyle; }

    public String getPopupClass() { return popupClass; }

    public void setPopupClass(String popupClass) { this.popupClass=popupClass; }

    protected String getWidgetId(F fmt, L loc, I obj, P par) { return fmt.uniqueId(); }

    public void render(F fmt, L loc, I obj, P par) throws IOException {
        String id = getWidgetId(fmt, loc, obj, par);
        fmt.div_(ATR_ID,"Widget_"+id,
                "onMouseOver", "javascript: document.getElementById('Popup_"+id+"').style.display = 'block';",
                "onMouseOut", "javascript: document.getElementById('Popup_"+id+"').style.display = 'none';"
                );
        fmt.div_(ATR_ID,"Popup_"+id,
                 ATR_CLASS,popupClass,
                 ATR_STYLE,"position: absolute; z-index: "+zindex+"; display:none;"+((popupStyle!=null)?" "+popupStyle:""),
                 "onMouseOver", "javascript: document.getElementById('Popup_"+id+"').style.display = 'block';",
                 "onMouseOut", "javascript: document.getElementById('Popup_"+id+"').style.display = 'none';"
                );
        popup.render(fmt, loc, obj, par);
        fmt._div();
        inset.render(fmt, loc, obj, par);
        fmt._div();
    }

}
