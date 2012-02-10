package net.varkhan.pres.format;

import net.varkhan.base.containers.array.Arrays;

import java.io.IOException;

import static net.varkhan.pres.format.XmlDocFormatter.*;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/13/11
 * @time 6:52 PM
 */
public class HtmlDocFormatter extends HtmlFormatter {

    public static final String TAG_HTML = "html";
    public static final String TAG_HEAD = "head";
    public static final String TAG_BODY = "body";

    private String title;
    private String[] cssurl = null;
    private String[] cssdef = null;

    public HtmlDocFormatter(Appendable out) { super(out); }

    public HtmlDocFormatter(Appendable out, boolean forceCloseTags) { super(out, forceCloseTags); }

    public void setTitle(String title) {
        if(opened) throw new IllegalStateException("Cannot set headers after document has been opened");
        this.title=title;
    }

    public void addCssUrl(String url) {
        if(opened) throw new IllegalStateException("Cannot set headers after document has been opened");
        if(this.cssurl==null) this.cssurl=new String[] {url};
        else this.cssurl=Arrays.append(this.cssurl,url);
    }

    public void addCssDef(String def) {
        if(opened) throw new IllegalStateException("Cannot set headers after document has been opened");
        if(this.cssdef==null) this.cssdef=new String[] {def};
        else this.cssdef=Arrays.append(this.cssdef,def);
    }

    public void open() throws IOException, IllegalStateException {
        super.open();
        writeXmlDoctype(this, TAG_HTML, "-//W3C//DTD XHTML 1.0 Transitional//EN", null, null);
        writeElmtOpen(this, TAG_HTML);
        writeElmtOpen(this, TAG_HEAD);
        header();
        if(forceCloseTags) _all();
        else if(!opentags.isEmpty()) throw new IllegalStateException("Elements remains unclosed");
        writeElmtClose(this, TAG_HEAD);
        writeElmtOpen(this, TAG_BODY);
    }

    protected void header() throws IOException {
        if(title!=null) elmt("title",title);
        if(cssurl!=null) for(String url: cssurl) elmt("link", "rel", "stylesheet", "type", "text/css", "href", url);
        if(cssdef!=null) for(String def: cssdef) style("text/css", def);
    }

    public void close() throws IOException, IllegalStateException {
        writeElmtClose(this, TAG_BODY);
        writeElmtClose(this, TAG_HTML);
        super.close();
    }
}
