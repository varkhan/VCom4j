package net.varkhan.base.conversion.formats;

import java.io.IOException;
import java.io.Reader;
import java.util.Map;


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



    /**********************************************************************************
     **  XML reading
     **/


    public static boolean readElmtOpen(Reader in, Appendable elt, Map<CharSequence,Object> atr) throws IOException, NullPointerException, IllegalArgumentException {
        int c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        if(c!='<') throw new IllegalArgumentException("Malformed element opening");
        c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        // Name extraction loop
        StringBuilder name = new StringBuilder();
        while(c>=0 && !isWhiteSpace(c) && c!='>' && c!='/') {
            name.append((char)c);
            c = in.read();
        }
        if(!isValidElmtName(name)) throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        if(elt!=null) elt.append(name.toString());
        c = readAttr(in,atr);
        if(c<0) throw new IllegalArgumentException("Malformed element opening");
        if(c=='/') {
            c = in.read();
            if(c!='>') throw new IllegalArgumentException("Malformed element opening");
            return true;
        }
        return false;
    }

    public static void readElmtClose(Reader in, Appendable elt) throws IOException, NullPointerException, IllegalArgumentException {
        int c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        if(c!='<') throw new IllegalArgumentException("Malformed element closing");
        c = in.read();
        if(c!='/') throw new IllegalArgumentException("Malformed element closing");
        c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        // Name extraction loop
        StringBuilder name = new StringBuilder();
        while(c>=0 && !isWhiteSpace(c) && c!='>' && c!='/') {
            name.append((char)c);
            c = in.read();
        }
        if(!isValidElmtName(name)) throw new IllegalArgumentException("Element names must contain only alphanumeric characters");
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        if(c!='>') throw new IllegalArgumentException("Malformed element closing");
        if(elt!=null) elt.append(name.toString());
    }


    public static int readAttr(Reader in, Map<CharSequence, Object> atr) throws IOException, NullPointerException, IllegalArgumentException {
        // One turn of loop for each attr
        int c = in.read();
        while(c>=0) {
            // Skip whitespace
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            // End of seq/tag? done
            if(c<0 || c=='>' || c=='/') return c;
            // First character of name -> find end
            StringBuilder name = new StringBuilder();
            while(c>=0 && !isWhiteSpace(c) && c!='=' && c!='>' && c!='/') {
                name.append((char)c);
                c = in.read();
            }
            if(!isValidAttrName(name)) throw new IllegalArgumentException("Attribute names must contain only alphanumeric characters");
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            // Empty attribute: next attribute with no intervening =, or EOL reached
            if(c!='=') {
                if(atr!=null) atr.put(name.toString(), null);
                continue;
            }
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            StringBuilder buf = new StringBuilder();
            if(c=='\"' || c=='\'') {
                char d = (char)c;
                c = in.read();
                while(c!=d) {
                    if(c<0) throw new IllegalArgumentException("Unterminated delimited string sequence");
                    // Escape sequences
                    if(c=='\\') {
                        c = in.read();
                        if(c<0) throw new IllegalArgumentException("Unterminated escape sequence");
                    }
                    buf.append(c);
                    c = in.read();
                }
            }
            else {
                while(!isWhiteSpace(c) && c!='>' && c!='/') {
                    if(c<0) throw new IllegalArgumentException("Unterminated delimited string sequence");
                    // Escape sequences
                    if(c=='\\') {
                        c = in.read();
                        if(c<0) throw new IllegalArgumentException("Unterminated escape sequence");
                    }
                    buf.append(c);
                    c = in.read();
                }
            }
            if(atr!=null) atr.put(name.toString(), buf.toString());
        }
        return c;
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
        if(c=='_' || c=='+' || c=='-' || c=='.') return true;
        if('0'<=c && c<='9') return true;
        if('A'<=c && c<='Z') return true;
        if('a'<=c && c<='z') return true;
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
        if(c=='_' || c=='+' || c=='-' || c=='.') return true;
        if('0'<=c && c<='9') return true;
        if('A'<=c && c<='Z') return true;
        if('a'<=c && c<='z') return true;
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

}