package net.varkhan.core.pres.format;

import net.varkhan.base.conversion.formats.Xml;

import java.io.IOException;
import java.util.LinkedList;


/**
 * <b>An XML language formatter</b>.
 * <p/>
 * This class extends the Formatter definition to include XML syntax elements, text and entities.
 * <p/>
 *
 * @author varkhan
 * @date 1/9/11
 * @time 7:13 AM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class XmlFormatter extends CheckedFormatter {

    public static final String ATR_ID = "id";

    protected final LinkedList<String> opentags = new LinkedList<String>();
    protected boolean forceCloseTags;
    private volatile long nextId = 0;
    private final String rootId = Integer.toHexString(System.identityHashCode(this));

    public XmlFormatter(Appendable out) { this(out, false); }

    public XmlFormatter(Appendable out, boolean forceCloseTags) { super(out); this.forceCloseTags=forceCloseTags; }

    /**
     * Returns a unique ID across a formatter's output.
     * <p/>
     * The generated String is guaranteed to be unique across all calls for a given XmlFormatter,
     * and, on a best effort basis, across all calls to any instance of XmlFormatter.
     *
     * @return a String ID guaranteed to be unique
     */
    public String uniqueId() { long id = ++nextId; return rootId+Long.toHexString(id); }

    /**
     * If enabled, all open tags will be forcefully closed on a call to {@link #close()}.
     *
     * @return {@literal true} if {@link #close()} also calls {@link #_all()}
     */
    public boolean forceCloseTags() { return forceCloseTags; }

    public void open() throws IOException, IllegalStateException { if(!super.isOpen()) super.open(); }

    public XmlFormatter append(CharSequence csq) throws IOException { super.append(csq); return this; }

    public XmlFormatter append(CharSequence... csq) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        if(csq!=null) for(CharSequence s: csq) if(s!=null) out.append(s).append('\n');
        return this;
    }

    public XmlFormatter append(CharSequence[]... csq) throws IOException {
        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting input");
        if(csq!=null) for(CharSequence[] ss: csq) if(ss!=null) for(CharSequence s: ss) if(s!=null) out.append(s).append('\n');
        return this;
    }

    public XmlFormatter append(CharSequence csq, int beg, int end) throws IOException { super.append(csq, beg, end); return this; }

    public XmlFormatter append(char c) throws IOException { super.append(c); return this; }

    public XmlFormatter ln() throws IOException { super.ln(); return this; }

    /**
     * Writes a comment to this formatter.
     *
     * @param txt the text of the comment
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter cmnt(CharSequence txt) throws IOException { Xml.writeComm(this, txt); return this; }

    /**
     * Writes an element to this formatter.
     *
     * @param tag the element name
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter elmt(String tag) throws IOException { this.elmt(tag, null, (Object[][]) null); return this; }

    /**
     * Writes an element to this formatter.
     *
     * @param tag the element name
     * @param txt the text content
     * @param atr the attributes array
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter elmt(String tag, CharSequence txt, Object... atr) throws IOException { this.elmt(tag, txt, new Object[][] { atr }); return this; }

    /**
     * Writes an element to this formatter.
     *
     * @param tag  the element name
     * @param txt the text content
     * @param atrs the attributes arrays
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter elmt(String tag, CharSequence txt, Object[][] atrs) throws IOException {
//        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting content");
        Xml.writeElmt(this, tag, txt, atrs);
        return this;
    }

    /**
     * Writes an element's opening tag to this formatter.
     *
     * @param tag the element name
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter elmt_(String tag) throws IOException { this.elmt_(tag, (Object[][]) null); return this; }

    /**
     * Writes an element's opening tag to this formatter.
     *
     * @param tag the element name
     * @param atr the attributes array
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter elmt_(String tag, Object... atr) throws IOException { this.elmt_(tag, new Object[][] { atr }); return this; }

    /**
     * Writes an element's opening tag to this formatter.
     *
     * @param tag  the element name
     * @param atrs the attributes arrays
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter elmt_(String tag, Object[][] atrs) throws IOException {
//        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting content");
        opentags.addFirst(tag);
        Xml.writeElmtOpen(this, tag, atrs);
        return this;
    }

    /**
     * Writes an element's closing tag to this formatter.
     *
     * @param xtag the element name
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     * @throws IllegalStateException if no open element matches the provided name
     */
    public XmlFormatter _elmt(String xtag) throws IOException, IllegalStateException {
//        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting content");
        if(opentags.isEmpty()) throw new IllegalStateException("No open element");
        String tag = opentags.getFirst();
        if(xtag==null || !xtag.equals(tag)) throw new IllegalStateException("No such element "+tag);
        tag = opentags.removeFirst();
        Xml.writeElmtClose(this, tag);
        return this;
    }

    /**
     * Writes the last opened element's closing tag to this formatter.
     *
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     * @throws IllegalStateException if there is no open element
     */
    public XmlFormatter _elmt() throws IOException, IllegalStateException {
//        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting content");
        if(opentags.isEmpty()) throw new IllegalStateException("No open element");
        String tag = opentags.removeFirst();
        Xml.writeElmtClose(this, tag);
        return this;
    }

    /**
     * Writes to this formatter all the opened elements' closing tags within a certain element to this formatter.
     *
     * @param xtag the enclosing element name (will not be closed)
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter _within(String xtag) throws IOException {
//        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting content");
        while(!opentags.isEmpty()) {
            String tag;
            if(xtag!=null) {
                tag = opentags.getFirst();
                if(xtag.equals(tag)) return this;
            }
            tag = opentags.removeFirst();
            Xml.writeElmtClose(this, tag);
        }
        return this;
    }

    /**
     * Writes to this formatter all the opened elements' closing tags.
     *
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter _all() throws IOException {
//        if(!opened || closed) throw new IllegalStateException("Formatter is not accepting content");
        while(!opentags.isEmpty()) {
            String tag = opentags.removeFirst();
            Xml.writeElmtClose(this, tag);
        }
        return this;
    }

    /**
     * Writes a string as text, escaping character entities.
     *
     * @param txt the text
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter text(CharSequence txt) throws IOException {
        Xml.writeText(this, txt);
        return this;
    }

    /**
     * Closes the formatter.
     * <p/>
     * If {@link #forceCloseTags()} is enabled, this will force a call to {@link #_all()}.
     *
     * @throws IllegalStateException if the formatter was already closed, or some elements remain unclosed
     * @throws IOException if the output Appendable generated an exception
     */
    public void close() throws IOException, IllegalStateException {
        if(forceCloseTags) _all();
        else if(!opentags.isEmpty()) throw new IllegalStateException("Elements remains unclosed");
        super.close();
    }


}
