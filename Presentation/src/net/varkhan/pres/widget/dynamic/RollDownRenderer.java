package net.varkhan.pres.widget.dynamic;

import net.varkhan.pres.format.HtmlFormatter;
import net.varkhan.pres.format.XmlFormatter;
import net.varkhan.pres.render.Renderer;

import java.io.IOException;

import static net.varkhan.pres.format.HtmlFormatter.*;

/**
 * <b></b>.
 * <p/>
 * Note that this renderer is <em>state-full</em> and should not be used in a multi-thread context.
 * <p/>
 *
 * @author varkhan
 * @date 1/13/11
 * @time 6:31 PM
 */
public class RollDownRenderer<F extends HtmlFormatter,L,I,P> implements Renderer<F,L,I,P>  {

    private String titleClass = "title";
    private String blockClass = "";
    private String collapsedClass = "collapsed";
    private String expandedClass = "expanded";

    private final Renderer<F,L,I,P> title;
    private final Renderer<F,L,I,P> block;

    public RollDownRenderer(Renderer<F,L,I,P> title, Renderer<F,L,I,P> block) {
        this.title=title;
        this.block=block;
    }

    public String getTitleClass() { return titleClass; }

    public void setTitleClass(String titleClass) { this.titleClass=titleClass; }

    public String getBlockClass() { return blockClass; }

    public void setBlockClass(String blockClass) { this.blockClass=blockClass; }

    public String getCollapsedClass() { return collapsedClass; }

    public void setCollapsedClass(String collapsedClass) { this.collapsedClass=collapsedClass; }

    public String getExpandedClass() { return expandedClass; }

    public void setExpandedClass(String expandedClass) { this.expandedClass=expandedClass; }


    protected String getWidgetId(F fmt, L loc, I obj, P par) { return fmt.uniqueId(); }

    /** The last seen formatter
     * Note: this is not pretty, but should work, in a single thread context */
    private F formatter = null;
    /** The stored name of the JS event handler, for the last seen formatter */
    private String handler = null;

    /**
     * Returns the name of the event handler (and defines it if not called before for this formatter)
     *
     * @param fmt the HTML formatter
     * @return the name of the event handler
     * @throws IOException if the output Formatter generated an exception
     */
    public String getEventHandler(F fmt) throws IOException {
        if(formatter!=fmt) {
            formatter = fmt;
            handler = null;
        }
        if(handler!=null) return handler;
        String id = fmt.uniqueId();
        handler = "toggleRollDown_"+id;
        writeEventHandler(fmt, handler);
        return handler;
    }

    /**
     * Defines a rollup/rolldown event handler
     *
     * @param fmt     the HTML formatter
     * @param handler the name of the handler
     * @throws IOException if the output Formatter generated an exception
     */
    protected void writeEventHandler(F fmt, String handler) throws IOException {
        fmt.script("text/javascript",
                   "function "+handler+"(id) {",
                   "    var block = document.getElementById('Block_'+id);",
                   "    var title = document.getElementById('Title_'+id);",
                   "    if(block.style.display=='none') {",
                   "        title.className = '"+titleClass+" "+expandedClass+"';",
                   "        block.style.display = 'block';",
                   "    } else {",
                   "        title.className = '"+titleClass+" "+collapsedClass+"';",
                   "        block.style.display = 'none';",
                   "    }",
                   "}"
                   );
    }


    public void render(F fmt, L loc, I obj, P par) throws IOException {
        String handler = getEventHandler(fmt);
        String id = getWidgetId(fmt, loc, obj, par);
        fmt.div_(XmlFormatter.ATR_ID, "Widget_"+id, ATR_STYLE, "text-align: top");
        fmt.div_(XmlFormatter.ATR_ID, "Title_"+id, ATR_CLASS, titleClass, "onClick", "javascript: "+handler+"('"+id+"');");
        title.render(fmt, loc, obj, par);
        fmt._div();
        fmt.div_(XmlFormatter.ATR_ID, "Block_"+id, ATR_CLASS, blockClass, ATR_STYLE, "display:none;");
        block.render(fmt, loc, obj, par);
        fmt._div();
        fmt._div();
    }

}
