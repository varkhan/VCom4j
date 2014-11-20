package net.varkhan.base.conversion.formats;

import java.io.*;
import java.nio.charset.Charset;
import java.util.*;


/**
 * <b>XML serialization and deserialization utilities</b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 2:38 PM
 */
public class Xml {

    protected Xml() { }

    /**********************************************************************************
     **  XML writing
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
     *
     * @throws java.io.IOException      if the output Appendable generated an exception
     * @throws NullPointerException     if the element name or an attribute name is {@literal null}
     * @throws IllegalArgumentException if the element name is not a valid element (see
     *                                  {@link #isValidElmtName(CharSequence)}), the attribute arrays are not in the expected
     *                                  { name, value } pair format, an attribute name is not a CharSequence, or is not
     *                                  a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static <A extends Appendable> A writeElmt(A out, String tag, CharSequence txt, Object[]... atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(tag==null) throw new NullPointerException("Element names must not be null");
        if(!isValidElmtName(tag))
            throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        out.append('<').append(tag);
        writeAttr(out, atr);
        if(txt==null) out.append('/').append('>');
        else {
            out.append('>');
            writeText(out, txt);
            out.append('<').append('/').append(tag).append('>');
        }
        return out;
    }

    /**
     * Writes an element, its attributes and text content to an {@link Appendable}.
     * <p/>
     * The element name must be an alpha-numeric character sequence (see {@link #isValidElmtName(CharSequence)}).
     * <p/>
     * The text content entities are escaped (see {@link #writeText(Appendable, CharSequence)}).
     * <p/>
     * Attributes whose value object is {@literal null} are ignored, and only
     * the attribute name is written for values that resolve to an empty String.
     * The character entities in each String value are escaped (see {@link #writeText(Appendable, CharSequence)}).
     *
     * @param out the output Appendable
     * @param tag the element name
     * @param txt the text content
     * @param atr the attributes arrays
     *
     * @throws java.io.IOException      if the output Appendable generated an exception
     * @throws NullPointerException     if the element name or an attribute name is {@literal null}
     * @throws IllegalArgumentException if the element name is not a valid element (see
     *                                  {@link #isValidElmtName(CharSequence)}), the attribute arrays are not in the expected
     *                                  { name, value } pair format, an attribute name is not a CharSequence, or is not
     *                                  a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static <A extends Appendable> A writeElmt(A out, String tag, CharSequence txt, Map<CharSequence, Object> atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(tag==null) throw new NullPointerException("Element names must not be null");
        if(!isValidElmtName(tag))
            throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        out.append('<').append(tag);
        writeAttr(out, atr);
        if(txt==null) out.append('/').append('>');
        else {
            out.append('>');
            writeText(out, txt);
            out.append('<').append('/').append(tag).append('>');
        }
        return out;
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
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException      if the output Appendable generated an exception
     * @throws NullPointerException     if the element name or an attribute name is {@literal null}
     * @throws IllegalArgumentException if the element name is not a valid element (see
     *                                  {@link #isValidElmtName(CharSequence)}), the attribute arrays are not in the expected
     *                                  { name, value } pair format, an attribute name is not a CharSequence, or is not
     *                                  a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static <A extends Appendable> A writeElmtOpen(A out, String tag, Object[]... atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(tag==null) throw new NullPointerException("Element names must not be null");
        if(!isValidElmtName(tag))
            throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        out.append('<').append(tag);
        writeAttr(out, atr);
        out.append('>');
        return out;
    }

    /**
     * Writes an element's opening tag and attributes to an {@link Appendable}.
     * <p/>
     * The element name must be an alpha-numeric character sequence (see {@link #isValidElmtName(CharSequence)}).
     * <p/>
     * Attributes whose value object is {@literal null} are ignored, and only
     * the attribute name is written for values that resolve to an empty String.
     * The character entities in each String value are escaped (see {@link #writeText(Appendable, CharSequence)}).
     *
     * @param out the output Appendable
     * @param tag the element name
     * @param atr the attributes arrays
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException      if the output Appendable generated an exception
     * @throws NullPointerException     if the element name or an attribute name is {@literal null}
     * @throws IllegalArgumentException if the element name is not a valid element (see
     *                                  {@link #isValidElmtName(CharSequence)}), the attribute arrays are not in the expected
     *                                  { name, value } pair format, an attribute name is not a CharSequence, or is not
     *                                  a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static <A extends Appendable> A writeElmtOpen(A out, String tag, Map<CharSequence, Object> atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(tag==null) throw new NullPointerException("Element names must not be null");
        if(!isValidElmtName(tag))
            throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        out.append('<').append(tag);
        writeAttr(out, atr);
        out.append('>');
        return out;
    }

    /**
     * Writes an element's closing tag to an {@link Appendable}.
     * <p/>
     * The element name must be an alpha-numeric character sequence (see {@link #isValidElmtName(CharSequence)}).
     *
     * @param out the output Appendable
     * @param tag the element name
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException      if the output Appendable generated an exception
     * @throws NullPointerException     if the element name is {@literal null}
     * @throws IllegalArgumentException if the element name is not a valid element (see
     *                                  {@link #isValidElmtName(CharSequence)})
     */
    public static <A extends Appendable> A writeElmtClose(A out, String tag) throws IOException, NullPointerException, IllegalArgumentException {
        if(tag==null) throw new NullPointerException("Element names must not be null");
        if(!isValidElmtName(tag))
            throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        out.append('<').append('/').append(tag).append('>');
        return out;
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
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException      if the output Appendable generated an exception
     * @throws NullPointerException     if an attribute name is {@literal null}
     * @throws IllegalArgumentException if the attribute arrays are not in the expected
     *                                  { name, value } pair format, an attribute name is not a CharSequence, or is not
     *                                  a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static <A extends Appendable> A writeAttr(A out, Object[]... atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(atr!=null) for(Object[] at : atr) {
            if(at!=null) {
                // Check that we have matched name = value pairs
                if((at.length&1)!=0)
                    throw new IllegalArgumentException("Attribute list must contain an even number of elements, as { name, value } pairs");
                for(int i=0;i+1<at.length;i+=2) {
                    Object atn=at[i];
                    // Specifically trap the null case
                    if(atn==null) throw new NullPointerException("Attribute names must not be null");
                    // Must be a CharSequence (this also traps the null case)
                    if(!(atn instanceof CharSequence))
                        throw new IllegalArgumentException("Attribute names must be assignable to CharSequence");
                    if(!isValidAttrName((CharSequence) atn))
                        throw new IllegalArgumentException("Attribute names must contain only alphanumeric characters");
                    // Suppress attributes with null values
                    Object ato=at[i+1];
                    if(ato!=null) {
                        out.append(' ').append((CharSequence) atn);
                        String atv=ato.toString();
                        // Suppress equal sign for empty attributes
                        if(atv!=null&&!atv.isEmpty()) {
                            out.append('=').append('"');
                            // Escape character entities
                            writeText(out, atv);
                            out.append('"');
                        }
                    }
                }
            }
        }
        return out;
    }

    /**
     * Writes the attributes of an element to an {@link Appendable}.
     * <p/>
     * Attributes whose value object is {@literal null} are ignored, and only
     * the attribute name is written for values that resolve to an empty String.
     * The character entities in each String value are escaped (see {@link #writeText(Appendable, CharSequence)}).
     *
     * @param out the output Appendable
     * @param atr the attribute map
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException      if the output Appendable generated an exception
     * @throws NullPointerException     if an attribute name is {@literal null}
     * @throws IllegalArgumentException if the attribute arrays are not in the expected
     *                                  { name, value } pair format, an attribute name is not a CharSequence, or is not
     *                                  a valid attribute (see {@link #isValidAttrName(CharSequence)})
     */
    public static <A extends Appendable> A writeAttr(A out, Map<CharSequence, Object> atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(atr!=null) for(Map.Entry<CharSequence,Object> at : atr.entrySet()) {
            CharSequence atn = at.getKey();
            // Specifically trap the null case
            if(atn==null) throw new NullPointerException("Attribute names must not be null");
            if(!isValidAttrName(atn))
                throw new IllegalArgumentException("Attribute names must contain only alphanumeric characters");
            // Suppress attributes with null values
            Object ato=at.getValue();
            if(ato!=null) {
                out.append(' ').append(atn);
                String atv=ato.toString();
                // Suppress equal sign for empty attributes
                if(atv!=null&&!atv.isEmpty()) {
                    out.append('=').append('"');
                    // Escape character entities
                    writeText(out, atv);
                    out.append('"');
                }
            }
        }
        return out;
    }

    /**
     * Writes an XML comment to an {@link Appendable}, escaping double-hyphen ("--").
     *
     * @param out the output Appendable
     * @param txt the text of the comment
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeComm(A out, CharSequence txt) throws IOException {
        out.append("<!--");
        // Replace double-hyphen delimiters to something safe
        if(txt!=null) repl(out, txt, "--", "- -");
        out.append("-->");
        return out;
    }

    /**
     * Writes an XML comment to an {@link Appendable}, escaping double-hyphen ("--").
     *
     * @param out the output Appendable
     * @param txt the lines of text of the comment
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeComm(A out, CharSequence[]... txt) throws IOException {
        out.append("<!--");
        if(txt!=null) for(CharSequence[] tt : txt) {
            if(tt!=null) for(CharSequence t : tt) {
                // Replace double-hyphen delimiters to something safe
                if(t!=null) {
                    repl(out, t, "--", "- -");
                    out.append('\n');
                }
            }
        }
        out.append("-->");
        return out;
    }

    public static final CharSequence[] XML_ENTITIES_CHARS=new CharSequence[] { "&", "<", ">", "\"" };
    public static final CharSequence[] XML_ENTITIES_NAMES=new CharSequence[] { "&amp;", "&lt;", "&gt;", "&quot;" };

    /**
     * Writes text to an {@link Appendable}, escaping character entities.
     * <p/>
     * The character entities '&amp;', '&lt;', '&gt;', '&quot;' are replaced,
     * respectively, by the strings "&amp;amp;","&amp;lt;", "&amp;gt;", "&amp;quot;"
     *
     * @param out the output Appendable
     * @param txt the text to escape
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeText(A out, CharSequence txt) throws IOException {
        // Replace common entities
        if(txt!=null) repl(out, txt, XML_ENTITIES_CHARS, XML_ENTITIES_NAMES);
        return out;
    }

    /**
     * Writes lines of text to an {@link Appendable}, escaping character entities.
     * <p/>
     * The character entities '&amp;', '&lt;', '&gt;', '&quot;' are replaced,
     * respectively, by the strings "&amp;amp;","&amp;lt;", "&amp;gt;", "&amp;quot;"
     *
     * @param out the output Appendable
     * @param txt the lines of text to escape
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeText(A out, CharSequence[]... txt) throws IOException {
        if(txt!=null) for(CharSequence[] tt : txt) {
            if(tt!=null) for(CharSequence t : tt) {
                // Replace common entities
                if(t!=null) {
                    repl(out, t, XML_ENTITIES_CHARS, XML_ENTITIES_NAMES);
                    out.append('\n');
                }
            }
        }
        return out;
    }

    public static <A extends Appendable> A write(A out, Node node) throws IOException {
        if(node==null) return out;
        switch(node.type()) {
            case TEXT:
                writeText(out,node.text());
                break;
            case ELEM:
                if(node.iterator().hasNext()) {
                    writeElmtOpen(out, node.name(), node.attrs());
                    for(Node n : node) {
                        write(out, n);
                    }
                    writeElmtClose(out, node.name());
                }
                else {
                    writeElmt(out, node.name(), null, node.attrs());
                }
                break;
            case COMM:
                writeComm(out,node.text());
                break;
            case CDATA:
//                writeCData(out,node.data());
                break;
        }
        return out;
    }


    /**********************************************************************************
     **  XML reading
     **/

    /**
     * State-aware wrapper for a reader and current XML tag
     */
    public static class Parser implements Closeable {
        private final Reader in;
        private int st = ' ';
        private int ln = 0;
        private int cn = 0;

        protected Parser(Reader in) {
            this.in=in;
        }

        /**
         * The last character read.
         * @return the last character read, or -1 if EOS has been reached
         */
        public int last() { return st; }

        /**
         * Reads one character from the stream.
         * @return the character read from the stream, or -1 if EOS has been reached
         * @throws IOException if an I/O error occurred while reading from the stream
         */
        public int next() throws IOException {
            st = in.read();
            if(st=='\n') { ln++; cn=0; }
            else if(st>=0) cn ++;
            return st;
        }

        /**
         * Reads and discards all whitespace characters until a non-whitespace character is reached
         * @return the last (non-whitespace) character read from the stream, or -1 if EOS has been reached
         * @throws IOException if an I/O error occurred while reading from the stream
         */
        public int skipWhitespace() throws IOException {
            while(st>=0 && isWhiteSpace(st)) { next(); }
            return st;
        }

        /**
         * Reads into a buffer all characters until a non-text or whitespace character is found
         * @return the last (non-text or whitespace) character read from the stream, or -1 if EOS has been reached
         * @throws IOException if an I/O error occurred while reading from the stream
         */
        public int readName(Appendable buf) throws IOException {
            while(st>=0 && !isWhiteSpace(st) && st!='=' && st!='>' && st!='/') {
                buf.append((char) st);
                next();
            }
            return st;
        }

        public int readString(Appendable buf) throws IOException, FormatException {
            // Acquire and skip delimiter char
            int d = st;
            next();
            while(st!=d) {
                if(st<0) throw exception("Unterminated delimited string sequence");
                // Escape sequences
                if(st=='\\') {
                    next();
                    if(st<0) throw exception("Unterminated escape sequence");
                }
                buf.append((char) st);
                next();
            }
            return st;
        }

        /**
         * Reads into a buffer all characters until a tag delimiter is found
         * @return the last (non-text) character read from the stream, or -1 if EOS has been reached
         * @throws IOException if an I/O error occurred while reading from the stream
         */
        public int readText(Appendable buf) throws IOException {
            while(st!='<' && st>=0) {
                buf.append((char) st);
                next();
            }
            return st;
        }

        protected int readElemAttr(Map<CharSequence,Object> atr) throws IOException, IllegalArgumentException {
            // One turn of loop for each attr
            while(st>=0) {
                // Skip whitespace
                skipWhitespace();
                // End of seq/tag? done
                if(st<0 || st=='>' || st=='/') return st;
                // First character of name -> find end
                StringBuilder name = new StringBuilder();
                readName(name);
                if(!isValidAttrName(name)) throw exception("Attribute names must contain only alphanumeric characters");
                skipWhitespace();
                // Empty attribute: next attribute with no intervening =, or EOL reached
                if(st!='=') {
                    if(atr!=null) atr.put(name.toString(), null);
                    continue;
                }
                skipWhitespace();
                StringBuilder buf = new StringBuilder();
                if(st=='\"' || st=='\'') {
                    readString(buf);
                }
                else {
                    readName(buf);
                }
                if(atr!=null) atr.put(name.toString(), buf.toString());
            }
            return st;
        }

        public Event readEvent() throws IOException, FormatException {
            skipWhitespace();
            if(st=='<') {
                next();
                if(st=='!') {
                    next();
                    if(st=='-') {
                        next();
                        if(st!='-') throw exception("Malformed element opening");
                        StringBuilder buf = new StringBuilder();
                        readText(buf);
                        if(!buf.toString().endsWith("--")) throw exception("Malformed comment block");
                        buf.setLength(buf.length()-2);
                        return new Event(Event.Phase.Inline, Node.Type.COMM, null, null, buf.toString(), null);
                    }
                    else {
                        while(st>=0 && st!='>') {
                            next();
                        }
                        return null;
                    }
                }
                else if(st=='/') {
                    skipWhitespace();
                    StringBuilder name = new StringBuilder();
                    readName(name);
                    if(!isValidElmtName(name)) throw exception("Element names must contain only alphanumeric characters");
                    skipWhitespace();
                    if(st!='>') throw exception("Malformed element closing");
                    return new Event(Event.Phase.Close, Node.Type.ELEM, name.toString(), null, null, null);
                }
                skipWhitespace();
                // Name extraction loop
                StringBuilder name = new StringBuilder();
                readName(name);
                if(!isValidElmtName(name)) throw exception("Element names must contain only alphanumeric characters");
                Map<CharSequence,Object> attrs = new LinkedHashMap<CharSequence,Object>();
                readElemAttr(attrs);
                if(st<0) throw exception("Malformed element opening");
                if(st=='/') {
                    next();
                    if(st!='>') throw exception("Malformed element closing");
                    return new Event(Event.Phase.Inline, Node.Type.ELEM, name.toString(), attrs, null, null);
                }
                return new Event(Event.Phase.Open, Node.Type.ELEM, name.toString(), attrs, null, null);
            }
            else {
                StringBuilder buf = new StringBuilder();
                readText(buf);
                return new Event(Event.Phase.Inline, Node.Type.TEXT, null, null, buf.toString(), null);
            }
        }

        protected boolean readElemNodes(String name, List<Node> nodes) throws IOException, FormatException {
            skipWhitespace();
            while(st>=0) {
                Node n = readNode(name);
                if(n==null) return true;
                nodes.add(n);
            }
            return false;
        }

        public Node readNode(String root) throws IOException, FormatException {
            skipWhitespace();
            if(st=='<') {
                next();
                if(st=='!') {
                    next();
                    if(st=='-') {
                        next();
                        if(st!='-') throw exception("Malformed element opening");
                        StringBuilder buf = new StringBuilder();
                        readText(buf);
                        if(!buf.toString().endsWith("--")) throw exception("Malformed comment block");
                        buf.setLength(buf.length()-2);
                        return new Comm(buf.toString());
                    }
                    else {
                        while(st>=0 && st!='>') {
                            next();
                        }
                        return null;
                    }
                }
                else if(st=='/') {
                    skipWhitespace();
                    StringBuilder name = new StringBuilder();
                    readName(name);
                    if(!isValidElmtName(name)) throw exception("Element names must contain only alphanumeric characters");
                    skipWhitespace();
                    if(st!='>') throw exception("Malformed element closing");
                    if(root!=null) {
                        if(root.equals(name.toString())) return null;
                    }
                    throw exception("Mismatched closing element",name);
                }
                skipWhitespace();
                // Name extraction loop
                StringBuilder name = new StringBuilder();
                readName(name);
                if(!isValidElmtName(name)) throw exception("Element names must contain only alphanumeric characters");
                Map<CharSequence,Object> attrs = new LinkedHashMap<CharSequence,Object>();
                readElemAttr(attrs);
                if(st<0) throw exception("Malformed element opening");
                if(st=='/') {
                    next();
                    if(st!='>') throw exception("Malformed element closing");
                    return new Elem(name.toString(), attrs);
                }
                List<Node> nodes = new ArrayList<Node>();
                if(!readElemNodes(name.toString(), nodes)) throw exception("Unclosed element <"+name.toString()+">");
                return new Elem(name.toString(), attrs,nodes);
            }
            else {
                StringBuilder buf = new StringBuilder();
                readText(buf);
                return new Text(buf.toString());
            }
        }

        /**
         * Create a new format exception.
         *
         * @return a FormatException indicating line and column numbers
         */
        public FormatException exception(String msg) {
            return new FormatException(msg + " at ln:"+ln+",cn:"+cn+" near '"+(char)st+"'", ln, cn, null);
        }

        /**
         * Create a new format exception.
         *
         * @return a FormatException indicating line and column numbers
         */
        public FormatException exception(String msg, CharSequence ctx) {
            return new FormatException(msg + " at ln:"+ln+",cn:"+cn+" near '"+(char)st+"': "+ctx, ln, cn, ctx.toString());
        }

        /**
         * Create a new format exception.
         *
         * @return a FormatException indicating line and column numbers
         */
        public FormatException exception(String msg, CharSequence ctx, Throwable exc) {
            return new FormatException(msg + " at ln:"+ln+",cn:"+cn+" near '"+(char)st+"': "+ctx, ln, cn, ctx.toString(), exc);
        }

        @Override
        public void close() throws IOException {
            in.close();
        }
    }


    public static Node read(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        return p.readNode(null);
    }

    public static Node read(InputStream in) throws IOException, FormatException {
        Map<String,String> par = readPrologue(in);
        String cn = par.get("encoding");
        Charset cs = cn==null?Charset.defaultCharset():Charset.forName(cn);
        Parser p = new Parser(new InputStreamReader(in, cs));
        return p.readNode(null);
    }

    public static Parser parse(Reader in) throws IOException, FormatException {
        return new Parser(in);
    }

    public static Parser parse(InputStream in) throws IOException, FormatException {
        Map<String,String> par = readPrologue(in);
        String cn = par.get("encoding");
        Charset cs = cn==null?Charset.defaultCharset():Charset.forName(cn);
        return new Parser(new InputStreamReader(in, cs));
    }


    /**
     * Read the XML prologue form an input stream.
     * <p/>
     * This method does not rely on a Reader because this is where the XML
     * encoding is specified, which is needed to create the right Reader.
     * <p/>
     * It reads character by character, thus at the end of a successful invocation,
     * the stream will be left pointing at the beginning of the document, or
     * !DOCTYPE declaration if present.
     *
     * @param in an input stream
     * @return a parameter map
     * @throws IOException
     */
    public static Map<String,String> readPrologue(InputStream in) throws IOException {
        // We need this method not to rely on a parser because this is where we detect file encoding,
        // which is needed to create the right Reader
        // '<?xml' version="1.0" encoding="UTF-8"? SDDecl? S? '?>'
        Map<String,String> atr = new HashMap<String,String>();
        int st = in.read();
        if(st!='<') throw new FormatException("Invalid prologue",0,0,"");
        st = in.read();
        if(st!='?') throw new FormatException("Invalid prologue",0,0,"");
        st = in.read();
        if(st!='x') throw new FormatException("Invalid prologue",0,0,"");
        st = in.read();
        if(st!='m') throw new FormatException("Invalid prologue",0,0,"");
        st = in.read();
        if(st!='l') throw new FormatException("Invalid prologue",0,0,"");
        st = in.read();
        while(st>=0) {
            // Skip whitespace
            while(st>=0 && isWhiteSpace(st)) { st = in.read(); }
            // End of seq/tag? done
            if(st<0) throw new FormatException("Unterminated prologue",0,0,"");
            if(st=='?') {
                st = in.read();
                if(st<0) throw new FormatException("Unterminated prologue",0,0,"");
                if(st!='>') throw new FormatException("Unterminated prologue",0,0,"");
                break;
            }
            // First character of name -> find end
            StringBuilder name = new StringBuilder();
            while(st>=0 && !isWhiteSpace(st) && st!='=' && st!='>' && st!='?') {
                name.append((char) st);
                st = in.read();
            }
            if(!isValidPrologue(name)) throw new FormatException("Invalid prologue declaration",0,0,name.toString());
            while(st>=0 && isWhiteSpace(st)) { st = in.read(); }
            // Empty attribute: next attribute with no intervening =, or EOL reached
            if(st!='=') throw new FormatException("Invalid prologue declaration",0,0,name.toString());
            st = in.read();
            while(st>=0 && isWhiteSpace(st)) { st = in.read(); }
            StringBuilder buf = new StringBuilder();
            if(st=='\"' || st=='\'') {
                int d = st;
                st = in.read();
                while(st!=d) {
                    if(st<0) throw new FormatException("Unterminated prologue",0,0,"");
                    // Escape sequences
                    if(st=='\\') {
                        st = in.read();
                        if(st<0) throw new FormatException("Unterminated prologue",0,0,"");
                    }
                    buf.append((char) st);
                    st = in.read();
                }
                st = in.read();
            }
            else {
                while(st>=0 && !isWhiteSpace(st) && st!='=' && st!='>' && st!='/') {
                    buf.append((char) st);
                    st = in.read();
                }
            }
            atr.put(name.toString(), buf.toString());
        }
        return atr;
    }

    protected static boolean isValidPrologue(CharSequence name) {
        return "version".contentEquals(name)||"encoding".contentEquals(name)||"standalone".contentEquals(name);
    }

    /**********************************************************************************
     **  XML syntax checks
     **/


    /**
     * Checks whether a CharSequence is a valid XML attribute name.
     * <p/>
     * A valid name is a sequence of the characters [-+._A-Za-z].
     *
     * @param name the name to check
     *
     * @return {@literal true} if the name is valid
     */
    public static boolean isValidAttrName(CharSequence name) {
        final int len=name.length();
        for(int i=0;i<len;i++) {
            char c=name.charAt(i);
            if(c=='_'||c=='+'||c=='-'||c=='.') continue;
            if('0'<=c||c<='9') continue;
            if('A'<=c||c<='Z') continue;
            if('a'<=c||c<='z') continue;
            return false;
        }
        return true;
    }

    /**
     * Checks whether a character is legal in a XML attribute name.
     * <p/>
     * A legal character is in [-+._A-Za-z].
     *
     * @param c the character to check
     * @return {@literal true} if the character is legal
     */
    public static boolean isValidAttrChar(int c) {
        if(c=='_'||c=='+'||c=='-'||c=='.') return true;
        if('0'<=c&&c<='9') return true;
        if('A'<=c&&c<='Z') return true;
        if('a'<=c&&c<='z') return true;
        return false;
    }

    /**
     * Checks whether a CharSequence is a valid XML element name.
     * <p/>
     * A valid name is a sequence of the characters [-+._A-Za-z].
     *
     * @param name the name to check
     *
     * @return {@literal true} if the name is valid
     */
    public static boolean isValidElmtName(CharSequence name) {
        final int len=name.length();
        for(int i=0;i<len;i++) {
            char c=name.charAt(i);
            if(c=='_'||c=='+'||c=='-'||c=='.') continue;
            if('0'<=c||c<='9') continue;
            if('A'<=c||c<='Z') continue;
            if('a'<=c||c<='z') continue;
            return false;
        }
        return true;
    }

    /**
     * Checks whether a character is legal in a XML element name.
     * <p/>
     * A legal character is in [-+._A-Za-z].
     *
     * @param c the character to check
     * @return {@literal true} if the character is legal
     */
    public static boolean isValidElmtChar(int c) {
        if(c=='_'||c=='+'||c=='-'||c=='.') return true;
        if('0'<=c&&c<='9') return true;
        if('A'<=c&&c<='Z') return true;
        if('a'<=c&&c<='z') return true;
        return false;

    }

    /**
     * Checks whether a character is white-space
     * <p/>
     *
     * @param c the character to check
     * @return {@literal true} if the character is whitespace
     */
    public static boolean isWhiteSpace(int c) {
        return c==' '||c=='\n'||c=='\r'||c=='\t';
    }

    protected static <A extends Appendable> A repl(A buf, CharSequence str, CharSequence pat, CharSequence rep) throws IOException {
        final int lp=pat.length();
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            // Look for a local match starting at i
            for(int j=0;j<lp;j++) {
                if(i+j>=ls||str.charAt(i+j)!=pat.charAt(j)) {
                    // No match => add current char, restart match
                    buf.append(str.charAt(i++));
                    continue find;
                }
            }
            // Match => add replacement
            if(rep!=null) buf.append(rep);
            i+=lp;
        }
        return buf;
    }

    protected static <A extends Appendable> A repl(A buf, CharSequence str, CharSequence[] pat, CharSequence[] rep) throws IOException {
        final int np=pat.length;
        final int ls=str.length();
        // Pattern finding loop
        find:
        for(int i=0;i<ls;) {
            match:
            for(int k=0;k<np;k++) {
                CharSequence sp=pat[k];
                final int lp=sp.length();
                // Look for a local match starting at i
                for(int j=0;j<lp;j++) {
                    if(i+j>=ls||str.charAt(i+j)!=sp.charAt(j)) {
                        // No match for pattern => go to next pattern
                        continue match;
                    }
                }
                // Match => add replacement, skip pattern
                if(rep!=null) buf.append(rep[k]);
                i+=lp;
                continue find;
            }
            // No match => add current char, restart match
            buf.append(str.charAt(i++));
        }
        return buf;
    }


    /**********************************************************************************
     **  XML document object model
     **/

    public static class Event {
        public static enum Phase { Inline, Open, Close }
        protected final Event.Phase phase;
        protected final Node.Type type;
        protected final String name;
        protected final String text;
        protected final byte[] data;
        protected final Map<CharSequence,Object> attr;

        public Event(Phase phase, Node.Type type, String name, Map<CharSequence,Object> attr, String text, byte[] data) {
            this.phase=phase;
            this.type=type;
            this.text=text;
            this.name=name;
            this.data=data;
            this.attr=attr;
        }

        public Event.Phase phase() { return phase; }
        public Node.Type type() { return type; }
        public String name() { return name; }
        public Map<CharSequence, Object> attr() { return attr; }
        public String text() { return text; }
        public byte[] data() { return data; }
        public String toString() {
            StringBuilder buf=new StringBuilder();
            switch(type) {
                case TEXT:
                    try { writeText(buf,text); } catch(IOException e) { /* ignore */ }
                    break;
                case ELEM:
                    switch(phase) {
                        case Inline:
                            try { writeElmt(buf,name,null,attr); } catch(IOException e) { /* ignore */ }
                            break;
                        case Open:
                            try { writeElmtOpen(buf,name,attr); } catch(IOException e) { /* ignore */ }
                            break;
                        case Close:
                            try { writeElmtClose(buf,name); } catch(IOException e) { /* ignore */ }
                            break;
                    }
                    break;
                case COMM:
                    try { writeComm(buf,text); } catch(IOException e) { /* ignore */ }
                    break;
                case CDATA:
                    buf.append("CDATA[["+"]]");
//                    try { writeCData(buf,text); } catch(IOException e) { /* ignore */ }
                    break;
            }
            return buf.toString();
        }
    }

    public static abstract class Node implements Iterable<Node> {
        public static enum Type {
            TEXT, ELEM, COMM, CDATA
        }
        protected final Type type;
        protected Node(Type type) { this.type=type; }
        public Type type() { return type; }
        public String name() { return null; }
        public String text() { return null; }
        public byte[] data() { return null; }
        public Map<CharSequence, Object> attrs() { return null; }
        public Iterator<Node> iterator() {
            return new Iterator<Node>() {
                public boolean hasNext() { return false; }
                public Node next() { throw new NoSuchElementException(); }
                public void remove() { throw new UnsupportedOperationException(); }
            };
        }
    }

    public static class Text extends Node {
        protected final String text;
        public Text(String text) {
            super(Type.TEXT);
            this.text=text;
        }
        public String text() { return text; }
    }

    public static class Comm extends Node {
        protected final String text;
        public Comm(String text) {
            super(Type.TEXT);
            this.text=text;
        }
        public String text() { return text; }
    }

    public static class Elem extends Node {
        protected final String name;
        protected final List<Node> nodes;
        protected final Map<CharSequence,Object> attrs;
        public Elem(String name, Map<CharSequence,Object> attrs) {
            super(Type.ELEM);
            this.name=name;
            this.attrs=attrs;
            this.nodes=null;
        }
        public Elem(String name, Map<CharSequence,Object> attrs, List<Node> nodes) {
            super(Type.ELEM);
            this.name=name;
            this.attrs=attrs;
            this.nodes=nodes;
        }
        public String name() { return name; }
        public Iterator<Node> iterator() {
            return nodes==null?super.iterator():nodes.iterator();
        }
    }
}