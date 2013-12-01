package net.varkhan.core.pres.format;

import net.varkhan.base.conversion.formats.Xml;

import java.io.IOException;


/**
 * <b>An XHTML language formatter</b>.
 * <p/>
 * This class extends the XmlFormatter definition to include HTML constructs.
 * <p/>
 *
 * @author varkhan
 * @date 1/9/11
 * @time 8:42 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class HtmlFormatter extends XmlFormatter {

    public static final String TAG_A     = "a";
    public static final String TAG_B     = "b";
    public static final String TAG_I     = "i";
    public static final String TAG_U     = "u";
    public static final String TAG_S     = "s";
    public static final String TAG_SPAN  = "span";
    public static final String TAG_DIV   = "div";
    public static final String TAG_UL    = "ul";
    public static final String TAG_LI    = "li";
    public static final String TAG_OL    = "ol";
    public static final String TAG_HR    = "hr";
    public static final String TAG_BR    = "br";
    public static final String TAG_IMG   = "img";
    public static final String TAG_TABLE = "table";
    public static final String TAG_TH    = "th";
    public static final String TAG_TR    = "tr";
    public static final String TAG_TD    = "td";

    public static final String ATR_CLASS = "class";
    public static final String ATR_STYLE = "style";
    public static final String ATR_NAME  = "name";
    public static final String ATR_HREF  = "href";
    public static final String ATR_SRC   = "src";
    public static final String ATR_VALUE = "value";


    protected String baseUrl = null;

    public HtmlFormatter(Appendable out) { super(out); }

    public HtmlFormatter(Appendable out, boolean forceCloseTags) { super(out, forceCloseTags); }

    public String getBaseUrl() { return baseUrl; }

    public void setBaseUrl(String baseUrl) { this.baseUrl=baseUrl; }

    public HtmlFormatter append(CharSequence csq) throws IOException { super.append(csq); return this; }
    public HtmlFormatter append(CharSequence... csq) throws IOException { super.append(csq); return this; }
    public HtmlFormatter append(CharSequence csq, int beg, int end) throws IOException { super.append(csq, beg, end); return this; }
    public HtmlFormatter append(char c) throws IOException { super.append(c); return this; }
    public HtmlFormatter ln() throws IOException { super.ln(); return this; }

    public HtmlFormatter hr(String... atr) throws IOException { elmt(TAG_HR,null,atr); return this; }
    public HtmlFormatter br(String... atr) throws IOException { elmt(TAG_BR,null,atr); return this; }
    public HtmlFormatter li(String... atr) throws IOException { elmt(TAG_LI,null,atr); return this; }
    public HtmlFormatter ol(String... atr) throws IOException { elmt(TAG_OL,null,atr); return this; }
    public HtmlFormatter img(String... atr) throws IOException { elmt(TAG_HR,null,atr); return this; }

    public HtmlFormatter span(CharSequence txt, String... atr) throws IOException { elmt(TAG_SPAN,txt,atr); return this; }
    public HtmlFormatter span(CharSequence txt, String[][] atr) throws IOException { elmt(TAG_SPAN,txt,atr); return this; }
    public HtmlFormatter span_(String... atr) throws IOException { elmt_(TAG_SPAN, atr); return this; }
    public HtmlFormatter span_(String[][] atr) throws IOException { elmt_(TAG_SPAN, atr); return this; }
    public HtmlFormatter _span() throws IOException { _elmt(TAG_SPAN); return this; }

    public HtmlFormatter div(CharSequence txt, String... atr) throws IOException { elmt(TAG_DIV,txt,atr); return this; }
    public HtmlFormatter div(CharSequence txt, String[][] atr) throws IOException { elmt(TAG_DIV,txt,atr); return this; }
    public HtmlFormatter div_(String... atr) throws IOException { elmt_(TAG_DIV, atr); return this; }
    public HtmlFormatter div_(String[][] atr) throws IOException { elmt_(TAG_DIV, atr); return this; }
    public HtmlFormatter _div() throws IOException { _elmt(TAG_DIV); return this; }

    public HtmlFormatter ul_(String... atr) throws IOException { elmt_(TAG_UL, atr); return this; }
    public HtmlFormatter _ul() throws IOException { _elmt(TAG_UL); return this; }

    public HtmlFormatter tb_(String... atr) throws IOException { elmt_(TAG_TABLE, atr); return this; }
    public HtmlFormatter tb_(String[][] atr) throws IOException { elmt_(TAG_TABLE, atr); return this; }
    public HtmlFormatter _tb() throws IOException { _elmt(TAG_TABLE); return this; }
    public HtmlFormatter th_(String... atr) throws IOException { elmt_(TAG_TH, atr); return this; }
    public HtmlFormatter th_(String[][] atr) throws IOException { elmt_(TAG_TH, atr); return this; }
    public HtmlFormatter _th() throws IOException { _elmt(TAG_TH); return this; }
    public HtmlFormatter tr_(String... atr) throws IOException { elmt_(TAG_TR, atr); return this; }
    public HtmlFormatter tr_(String[][] atr) throws IOException { elmt_(TAG_TR, atr); return this; }
    public HtmlFormatter _tr() throws IOException { _elmt(TAG_TR); return this; }
    public HtmlFormatter td_(String... atr) throws IOException { elmt_(TAG_TD, atr); return this; }
    public HtmlFormatter td_(String[][] atr) throws IOException { elmt_(TAG_TD, atr); return this; }
    public HtmlFormatter _td() throws IOException { _elmt(TAG_TD); return this; }

    public HtmlFormatter i(CharSequence txt, String... atr) throws IOException { elmt(TAG_I,txt,atr); return this; }
    public HtmlFormatter i_(String... atr) throws IOException { elmt_(TAG_I, atr); return this; }
    public HtmlFormatter _i() throws IOException { _elmt(TAG_I); return this; }
    public HtmlFormatter b(CharSequence txt, String... atr) throws IOException { elmt(TAG_B,txt,atr); return this; }
    public HtmlFormatter b_(String... atr) throws IOException { elmt_(TAG_B, atr); return this; }
    public HtmlFormatter _b() throws IOException { _elmt(TAG_B); return this; }
    public HtmlFormatter u(CharSequence txt, String... atr) throws IOException { elmt(TAG_U,txt,atr); return this; }
    public HtmlFormatter u_(String... atr) throws IOException { elmt_(TAG_U, atr); return this; }
    public HtmlFormatter _u() throws IOException { _elmt(TAG_U); return this; }
    public HtmlFormatter s(CharSequence txt, String... atr) throws IOException { elmt(TAG_S,txt,atr); return this; }
    public HtmlFormatter s_(String... atr) throws IOException { elmt_(TAG_S, atr); return this; }
    public HtmlFormatter _s() throws IOException { _elmt(TAG_S); return this; }

    public String url(String url) {
        url = url.trim();
        if(baseUrl==null) return url;
        if(url.length()==0) {
            return baseUrl;
        }
        else if(url.startsWith("./")) {
            StringBuilder b = new StringBuilder(baseUrl);
            if(!baseUrl.endsWith("/")) b.append('/');
            b.append(url.substring(2));
            return b.toString();
        }
        else if(url.startsWith("../")) {
            StringBuilder b = new StringBuilder();
            int ppos;
            if(baseUrl.endsWith("/")) ppos = baseUrl.lastIndexOf('/',baseUrl.length()-1);
            else ppos = baseUrl.lastIndexOf('/');
            if(ppos>=0) b.append(baseUrl.substring(0,ppos));
            else {
                b.append(baseUrl);
                if(!baseUrl.endsWith("/")) b.append('/');
            }
            b.append(url.substring(3));
            return b.toString();
        }
        else if(url.startsWith("#")||url.startsWith("?")) {
            StringBuilder b = new StringBuilder(baseUrl);
            if(!baseUrl.endsWith("/")) b.append('/');
            b.append(url);
            return b.toString();
        }
        return url;
    }

    public HtmlFormatter a(CharSequence txt, String name, String href, String... atr) throws IOException {
        if(href!=null) href = url(href);
        elmt(TAG_A, txt, new Object[][] { new CharSequence[] { ATR_HREF, href, ATR_NAME, name, }, atr });
        return this;
    }

    public HtmlFormatter a_(String href, String name, String... atr) throws IOException {
        if(href!=null) href = url(href);
        elmt_(TAG_A, new Object[][] { new CharSequence[] { ATR_HREF, href, ATR_NAME, name, }, atr });
        return this;
    }

    public HtmlFormatter _a() throws IOException { _elmt(TAG_A); return this; }

    public HtmlFormatter img(String href, String... atr) throws IOException {
        if(href!=null) href = url(href);
        elmt(TAG_A, null, new Object[][] { new CharSequence[] { ATR_SRC, href }, atr });
        return this;
    }

    public HtmlFormatter style(String type, String... lines) throws IOException {
        if(type==null) type = "text/css";
        Xml.writeElmtOpen(this, "style", new String[] { "language", type });
        append("<!--\n");
        if(lines!=null) for(String l: lines) {
            if(l!=null) append(l).append('\n');
        }
        append("-->");
        Xml.writeElmtClose(this, "style");
        return this;
    }

    public HtmlFormatter script(String type, String... lines) throws IOException {
        if(type==null) type = "text/javascript";
        Xml.writeElmtOpen(this, "script", new String[] { "language", type });
        append("// <!--\n");
        if(lines!=null) for(String l: lines) {
            if(l!=null) append(l).append('\n');
        }
        append("// -->");
        Xml.writeElmtClose(this, "script");
        return this;
    }

}
