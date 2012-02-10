package net.varkhan.pres.format;

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

    public void hr(String... atr) throws IOException { elmt(TAG_HR,null,atr); }
    public void br(String... atr) throws IOException { elmt(TAG_BR,null,atr); }
    public void li(String... atr) throws IOException { elmt(TAG_LI,null,atr); }
    public void ol(String... atr) throws IOException { elmt(TAG_OL,null,atr); }
    public void img(String... atr) throws IOException { elmt(TAG_HR,null,atr); }

    public void span(CharSequence txt, String... atr) throws IOException { elmt(TAG_SPAN,txt,atr); }
    public void span(CharSequence txt, String[][] atr) throws IOException { elmt(TAG_SPAN,txt,atr); }
    public void span_(String... atr) throws IOException { elmt_(TAG_SPAN, atr); }
    public void span_(String[][] atr) throws IOException { elmt_(TAG_SPAN, atr); }
    public void _span() throws IOException { _elmt(TAG_SPAN); }

    public void div(CharSequence txt, String... atr) throws IOException { elmt(TAG_DIV,txt,atr); }
    public void div(CharSequence txt, String[][] atr) throws IOException { elmt(TAG_DIV,txt,atr); }
    public void div_(String... atr) throws IOException { elmt_(TAG_DIV, atr); }
    public void div_(String[][] atr) throws IOException { elmt_(TAG_DIV, atr); }
    public void _div() throws IOException { _elmt(TAG_DIV); }

    public void ul_(String... atr) throws IOException { elmt_(TAG_UL, atr); }
    public void _ul() throws IOException { _elmt(TAG_UL); }

    public void tb_(String... atr) throws IOException { elmt_(TAG_TABLE, atr); }
    public void tb_(String[][] atr) throws IOException { elmt_(TAG_TABLE, atr); }
    public void _tb() throws IOException { _elmt(TAG_TABLE); }
    public void th_(String... atr) throws IOException { elmt_(TAG_TH, atr); }
    public void th_(String[][] atr) throws IOException { elmt_(TAG_TH, atr); }
    public void _th() throws IOException { _elmt(TAG_TH); }
    public void tr_(String... atr) throws IOException { elmt_(TAG_TR, atr); }
    public void tr_(String[][] atr) throws IOException { elmt_(TAG_TR, atr); }
    public void _tr() throws IOException { _elmt(TAG_TR); }
    public void td_(String... atr) throws IOException { elmt_(TAG_TD, atr); }
    public void td_(String[][] atr) throws IOException { elmt_(TAG_TD, atr); }
    public void _td() throws IOException { _elmt(TAG_TD); }

    public void i(CharSequence txt, String... atr) throws IOException { elmt(TAG_I,txt,atr); }
    public void i_(String... atr) throws IOException { elmt_(TAG_I, atr); }
    public void _i() throws IOException { _elmt(TAG_I); }
    public void b(CharSequence txt, String... atr) throws IOException { elmt(TAG_B,txt,atr); }
    public void b_(String... atr) throws IOException { elmt_(TAG_B, atr); }
    public void _b() throws IOException { _elmt(TAG_B); }
    public void u(CharSequence txt, String... atr) throws IOException { elmt(TAG_U,txt,atr); }
    public void u_(String... atr) throws IOException { elmt_(TAG_U, atr); }
    public void _u() throws IOException { _elmt(TAG_U); }
    public void s(CharSequence txt, String... atr) throws IOException { elmt(TAG_S,txt,atr); }
    public void s_(String... atr) throws IOException { elmt_(TAG_S, atr); }
    public void _s() throws IOException { _elmt(TAG_S); }

    public String url(String url) {
        if(baseUrl==null) return url;
        if(url.startsWith("./")) {
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
        return url;
    }

    public void a(CharSequence txt, String name, String href, String... atr) throws IOException {
        if(href!=null) href = url(href);
        elmt(TAG_A, txt, new Object[][] { new CharSequence[] { ATR_HREF, href, ATR_NAME, name, }, atr });
    }

    public void a_(String href, String name, String... atr) throws IOException {
        if(href!=null) href = url(href);
        elmt_(TAG_A, new Object[][] { new CharSequence[] { ATR_HREF, href, ATR_NAME, name, }, atr });
    }

    public void _a() throws IOException { _elmt(TAG_A); }

    public void img(String href, String... atr) throws IOException {
        if(href!=null) href = url(href);
        elmt(TAG_A, null, new Object[][] { new CharSequence[] { ATR_SRC, href }, atr });
    }

    public void style(String type, String... lines) throws IOException {
        if(type==null) type = "text/css";
        writeElmtOpen(this,"style",new String[] { "language", type });
        append("<!--\n");
        if(lines!=null) for(String l: lines) {
            if(l!=null) append(l).append('\n');
        }
        append("-->");
        writeElmtClose(this,"style");
    }

    public void script(String type, String... lines) throws IOException {
        if(type==null) type = "text/javascript";
        writeElmtOpen(this,"script",new String[] { "language", type });
        append("// <!--\n");
        if(lines!=null) for(String l: lines) {
            if(l!=null) append(l).append('\n');
        }
        append("// -->");
        writeElmtClose(this, "script");
    }

}
