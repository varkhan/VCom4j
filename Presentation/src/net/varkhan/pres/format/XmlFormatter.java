package net.varkhan.pres.format;

import net.varkhan.base.containers.array.CharArrays;

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

    public static final String ATR_ID    = "id";

    protected final LinkedList<String> opentags = new LinkedList<String>();
    protected boolean forceCloseTags;
    private volatile long nextId = 0;
    private final String rootId = Integer.toHexString(System.identityHashCode(this));

    public XmlFormatter(Appendable out) { this(out, false); }

    public XmlFormatter(Appendable out, boolean forceCloseTags) { super(out); this.forceCloseTags=forceCloseTags; }

    /**
     * Returns a unique ID accross a formatter's output.
     * <p/>
     * The generated String is guaranteed to be unique accross all calls for a given XmlFormatter,
     * and, on a best effort basis, accross all calls to any instance of XmlFormatter.
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

    public void open() throws IOException, IllegalStateException { super.open(); }

    public XmlFormatter append(CharSequence csq) throws IOException { super.append(csq); return this; }

    public XmlFormatter append(CharSequence csq, int start, int end) throws IOException { super.append(csq, start, end); return this; }

    public XmlFormatter append(char c) throws IOException { super.append(c); return this; }

    /**
     * Writes a comment to this formatter.
     *
     * @param txt the text of the comment
     * @return this formatter
     * @throws IOException if the output Appendable generated an exception
     */
    public XmlFormatter cmnt(CharSequence txt) throws IOException { writeCmnt(this, txt); return this; }

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
        writeElmt(this, tag, txt, atrs);
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
        writeElmtOpen(this, tag, atrs);
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
        writeElmtClose(this,tag);
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
        writeElmtClose(this,tag);
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
            writeElmtClose(this,tag);
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
            writeElmtClose(this,tag);
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
        writeText(this, txt);
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


    /**********************************************************************************
     **  Static XML formatting utilities
     **/

    /**
     * Writes an element, its attributes and text content to an {@link Appendable}.
     * <p/>
     * The element name must be an alpha-numeric character sequence (see {@link #isValidElmtName(CharSequence)}).
     * <p/>
     * The text content entities are escaped (see {@link #writeText(Appendable, CharSequence)}).
     * <p/>
     * The attribute arrays must each have an even length, and contain { name, value } pairs,
     * where each name (an even-index element) is a {@link CharSequence}, and each value is
     * obtained by calling the {@link Object#toString()} method on the value object (the
     * following odd-index element). Attributes whose value object is {@literal null} are
     * ignored, and only the attribute name is written for values that resolve to an empty String.
     * The character entities in each String value are escaped.
     *
     * @param out the output Appendable
     * @param tag the element name
     * @param txt the text content
     * @param atr the attributes arrays
     * @throws IOException if the output Appendable generated an exception
     * @throws NullPointerException if the element name or an attribute name is {@literal null}
     * @throws IllegalArgumentException if the element name is not a valid element (see
     * {@link #isValidElmtName(CharSequence)}), the attribute arrays are not in the expected
     * { name, value } pair format, an attribute name is not a CharSequence, or is not
     * a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static void writeElmt(Appendable out, String tag, CharSequence txt, Object[]... atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(tag==null) throw new NullPointerException("Element names must not be null");
        if(!isValidElmtName(tag)) throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        out.append('<').append(tag);
        writeAttr(out,atr);
        if(txt==null) out.append('/').append('>');
        else {
            out.append('>');
            writeText(out,txt);
            out.append('<').append(tag).append('/').append('>');
        }
    }

    /**
     * Writes an element's opening tag and attributes to an {@link Appendable}.
     * <p/>
     * The element name must be an alpha-numeric character sequence (see {@link #isValidElmtName(CharSequence)}).
     * <p/>
     * The attribute arrays must each have an even length, and contain { name, value } pairs,
     * where each name (an even-index element) is a {@link CharSequence}, and each value is
     * obtained by calling the {@link Object#toString()} method on the value object (the
     * following odd-index element). Attributes whose value object is {@literal null} are
     * ignored, and only the attribute name is written for values that resolve to an empty String.
     * The character entities in each String value are escaped (see {@link #writeText(Appendable, CharSequence)}).
     *
     * @param out the output Appendable
     * @param tag the element name
     * @param atr the attributes arrays
     * @throws IOException if the output Appendable generated an exception
     * @throws NullPointerException if the element name or an attribute name is {@literal null}
     * @throws IllegalArgumentException if the element name is not a valid element (see
     * {@link #isValidElmtName(CharSequence)}), the attribute arrays are not in the expected
     * { name, value } pair format, an attribute name is not a CharSequence, or is not
     * a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static void writeElmtOpen(Appendable out, String tag, Object[]... atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(tag==null) throw new NullPointerException("Element names must not be null");
        if(!isValidElmtName(tag)) throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        out.append('<').append(tag);
        writeAttr(out,atr);
        out.append('>');
    }

    /**
     * Writes an element's closing tag to an {@link Appendable}.
     * <p/>
     * The element name must be an alpha-numeric character sequence (see {@link #isValidElmtName(CharSequence)}).
     *
     * @param out the output Appendable
     * @param tag the element name
     * @throws IOException if the output Appendable generated an exception
     * @throws NullPointerException if the element name is {@literal null}
     * @throws IllegalArgumentException if the element name is not a valid element (see
     * {@link #isValidElmtName(CharSequence)})
     */
    public static void writeElmtClose(Appendable out, String tag) throws IOException, NullPointerException, IllegalArgumentException {
        if(tag==null) throw new NullPointerException("Element names must not be null");
        if(!isValidElmtName(tag)) throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        out.append('<').append(tag).append('/').append('>');
    }

    /**
     * Writes the attributes of an element to an {@link Appendable}.
     * <p/>
     * The attribute arrays must each have an even length, and contain { name, value } pairs,
     * where each name (an even-index element) is a {@link CharSequence}, and each value is
     * obtained by calling the {@link Object#toString()} method on the value object (the
     * following odd-index element). Attributes whose value object is {@literal null} are
     * ignored, and only the attribute name is written for values that resolve to an empty String.
     * The character entities in each String value are escaped (see {@link #writeText(Appendable, CharSequence)}).
     *
     * @param out the output Appendable
     * @param atr the attribute arrays
     * @throws IOException if the output Appendable generated an exception
     * @throws NullPointerException if an attribute name is {@literal null}
     * @throws IllegalArgumentException if the attribute arrays are not in the expected
     * { name, value } pair format, an attribute name is not a CharSequence, or is not
     * a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static void writeAttr(Appendable out, Object[]... atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(atr!=null) for(Object[] at: atr) {
            if(at!=null) {
                // Check that we have matched name = value pairs
                if((at.length&1)!=0) throw new IllegalArgumentException("Attribute list must contain an even number of elements, as { name, value } pairs");
                for(int i=0; i+1<at.length; i+=2) {
                    Object atn=at[i];
                    // Specifically trap the null case
                    if(atn==null) throw new NullPointerException("Attribute names must not be null");
                    // Must be a CharSequence (this also traps the null case)
                    if(!(atn instanceof CharSequence)) throw new IllegalArgumentException("Attribute names must be assignable to CharSequence");
                    if(!isValidAttrName((CharSequence) atn)) throw new IllegalArgumentException("Attribute names must contain only alphanumeric characters");
                    // Suppress attributes with null values
                    Object ato=at[i+1];
                    if(ato!=null) {
                        out.append(' ').append((CharSequence) atn);
                        String atv = ato.toString();
                        // Suppress equal sign for empty attributes
                        if(atv!=null && !atv.isEmpty()) {
                            out.append('=').append('"');
                            // Escape character entities
                            writeText(out, atv);
                            out.append('"');
                        }
                    }
                }
            }
        }
    }

    /**
     * Writes an XML comment to an {@link Appendable}, escaping double-hyphen ("--").
     *
     * @param out the output Appendable
     * @param txt the text of the comment
     * @throws IOException if the output Appendable generated an exception
     */
    public static void writeCmnt(Appendable out, CharSequence txt) throws IOException {
        out.append("<!--");
        // Replace double-hyphen delimiters to something safe
        if(txt!=null) CharArrays.repl(out,txt,"--","- -");
        out.append("-->");
    }

    /**
     * Writes an XML comment to an {@link Appendable}, escaping double-hyphen ("--").
     *
     * @param out the output Appendable
     * @param txt the lines of text of the comment
     * @throws IOException if the output Appendable generated an exception
     */
    public static void writeCmnt(Appendable out, CharSequence[]... txt) throws IOException {
        out.append("<!--");
        if(txt!=null) for(CharSequence[] tt: txt) {
            if(tt!=null) for(CharSequence t: tt) {
                // Replace double-hyphen delimiters to something safe
                if(t!=null) {
                    CharArrays.repl(out,t,"--","- -");
                    out.append('\n');
                }
            }
        }
        out.append("-->");
    }

    protected static final CharSequence[] XML_ENTITIES_CHARS = new CharSequence[] { "&",      "<",      ">",      "\""};
    protected static final CharSequence[] XML_ENTITIES_NAMES = new CharSequence[] { "&amp;",  "&lt;",   "&gt;",   "&quot;"};

    /**
     * Writes text to an {@link Appendable}, escaping character entities.
     * <p/>
     * The character entities '&amp;', '&lt;', '&gt;', '&quot;' are replaced,
     * respectively, by the strings "&amp;amp;","&amp;lt;", "&amp;gt;", "&amp;quot;"
     *
     * @param out the output Appendable
     * @param txt the text to escape
     * @throws IOException if the output Appendable generated an exception
     */
    public static void writeText(Appendable out, CharSequence txt) throws IOException {
        // Replace common entities
        if(txt!=null) CharArrays.repl(out,txt,XML_ENTITIES_CHARS,XML_ENTITIES_NAMES);
    }

    /**
     * Writes lines of text to an {@link Appendable}, escaping character entities.
     * <p/>
     * The character entities '&amp;', '&lt;', '&gt;', '&quot;' are replaced,
     * respectively, by the strings "&amp;amp;","&amp;lt;", "&amp;gt;", "&amp;quot;"
     *
     * @param out the output Appendable
     * @param txt the lines of text to escape
     * @throws IOException if the output Appendable generated an exception
     */
    public static void writeText(Appendable out, CharSequence[]... txt) throws IOException {
        if(txt!=null) for(CharSequence[] tt: txt) {
            if(tt!=null) for(CharSequence t: tt) {
                // Replace common entities
                if(t!=null) {
                    CharArrays.repl(out,t,XML_ENTITIES_CHARS,XML_ENTITIES_NAMES);
                    out.append('\n');
                }
            }
        }
    }

    /**
     * Checks whether a CharSequence is a valid XML attribute name.
     * <p/>
     * A valid name is a sequence of the characters [-+._A-Za-z].
     *
     * @param name the name to check
     * @return {@literal true} if the name is valid
     */
    public static boolean isValidAttrName(CharSequence name) {
        final int len = name.length();
        for(int i=0; i<len; i++) {
            char c = name.charAt(i);
            if(c=='_' || c=='+' || c=='-' || c=='.') continue;
            if('0'<=c || c<='9') continue;
            if('A'<=c || c<='Z') continue;
            if('a'<=c || c<='z') continue;
            return false;
        }
        return true;
    }

    /**
     * Checks whether a CharSequence is a valid XML element name.
     * <p/>
     * A valid name is a sequence of the characters [-+._A-Za-z].
     *
     * @param name the name to check
     * @return {@literal true} if the name is valid
     */
    public static boolean isValidElmtName(CharSequence name) {
        final int len = name.length();
        for(int i=0; i<len; i++) {
            char c = name.charAt(i);
            if(c=='_' || c=='+' || c=='-' || c=='.') continue;
            if('0'<=c || c<='9') continue;
            if('A'<=c || c<='Z') continue;
            if('a'<=c || c<='z') continue;
            return false;
        }
        return true;
    }


}
