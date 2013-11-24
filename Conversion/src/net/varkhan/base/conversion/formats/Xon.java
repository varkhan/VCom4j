package net.varkhan.base.conversion.formats;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.*;


/**
 * <b>XON syntax parser and converter</b>.
 * <p/>
 * XON is an extension of JSON that:<ul>
 * <li>recognizes both <em>arrays</em> (delimited by square brackets, as in JSON),
 * and <em>collections</em> (unordered bags of values, delimited by parentheses),</li>
 * <li>allows map syntax to omit field values for {@literal null} values, allowing
 * their use as a set.</li>
 * </ul><p/>
 * This class provides several static utilities, and helper objects, to serialize and deserialize
 * basic Java objects (boolean, number, String, arrays, List and Map) to and from their
 * representation as XON text.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 2:38 PM
 */
public class Xon {

    protected Xon() { }

    /*********************************************************************************
     **  Literals and separators
     **/

    protected static final char SEP_ENTRY   =':';
    protected static final char SEP_ELEMENT =',';
    protected static final char SEP_STRING  ='"';
    protected static final char SEP_VECTOR_S='[';
    protected static final char SEP_VECTOR_E=']';
    protected static final char SEP_COLLEC_S='(';
    protected static final char SEP_COLLEC_E=')';
    protected static final char SEP_OBJECT_S='{';
    protected static final char SEP_OBJECT_E='}';

    protected static final String LITERAL_TRUE ="true";
    protected static final String LITERAL_FALSE="false";
    protected static final String LITERAL_NULL ="null";


    /*********************************************************************************
     **  XON writing
     **/

    /**
     * Writes an object as a String.
     *
     * @param val the object to write
     * @return a JSON string representation of thr object
     */
    public static String write(Object val) {
        StringBuilder buf=new StringBuilder();
        try { write(buf, val); }
        catch(IOException e) { /* ignore: StringBuilder doesn't throw this */ }
        return buf.toString();
    }

    /**
     * Writes an object to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param obj the object to write
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException if the output Appendable generated an exception
     */
    @SuppressWarnings("unchecked")
    public static <A extends Appendable> A write(A out, Object obj) throws IOException {
        if(obj==null) {
            writeNull(out);
        }
        else if(obj instanceof Boolean) {
            writeBoolean(out, (Boolean) obj);
        }
        else if(obj instanceof Number) {
            writeNumber(out, (Number) obj);
        }
        else if(obj instanceof CharSequence) {
            out.append(SEP_STRING);
            writeString(out, (CharSequence) obj);
            out.append(SEP_STRING);
        }
//        else if(obj instanceof Map) {
//            out.append('{');
//            writeMap(out, (Map<CharSequence,Object>) obj);
//            out.append('}');
//        }
//        else if(obj instanceof List) {
//            out.append('(');
//            writeList(out, (List<Object>) obj);
//            out.append(')');
//        }
        else if(obj.getClass().isArray()) {
            out.append(SEP_VECTOR_S);
            writeVector(out, (Object[]) obj);
            out.append(SEP_VECTOR_E);
        }
        else if(obj instanceof Map) {
            out.append('{');
            writeObject(out, (Map<CharSequence,Object>) obj);
            out.append(SEP_OBJECT_E);
        }
        else if(obj instanceof Collection) {
            out.append(SEP_COLLEC_S);
            writeCollec(out, (Collection<Object>) obj);
            out.append(SEP_COLLEC_E);
        }
        else throw new IllegalArgumentException("Cannot serialize object to XON: unknown class "+obj.getClass().getCanonicalName());
        return out;
    }

    /**
     * Writes a map to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param map the map to write
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeObject(A out, Map<? extends CharSequence,?> map) throws IOException {
        @SuppressWarnings("unchecked")
        Iterator<Map.Entry<CharSequence, Object>> it = ((Map<CharSequence,Object>) map).entrySet().iterator();
        while(it.hasNext()) {
            Map.Entry<CharSequence, ?> x = it.next();
            writeField(out, x.getKey().toString(), x.getValue());
            if(it.hasNext()) out.append(SEP_ELEMENT);
        }
        return out;
    }

//    /**
//     * Writes a map to an {@link Appendable}.
//     *
//     * @param out the output Appendable
//     * @param map the map to write
//     * @param <A> the Appendable type
//     *
//     * @return the output Appendable (to facilitate chaining)
//     *
//     * @throws java.io.IOException if the output Appendable generated an exception
//     */
//    public static <A extends Appendable> A writeMap(A out, Map<? extends CharSequence,?> map) throws IOException {
//        @SuppressWarnings("unchecked")
//        Iterator<? extends Map.Entry<CharSequence,Object>> it = ((Map<CharSequence,Object>) map).iterator();
//        while(it.hasNext()) {
//            Map.Entry<CharSequence, ?> x = it.next();
//            writeField(out, x.getKey().toString(), x.getValue());
//            if(it.hasNext()) out.append(',');
//        }
//        return out;
//    }

    /**
     * Writes a list to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param lst the list to write
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeCollec(A out, Collection<?> lst) throws IOException {
        boolean f = true;
        for(Object obj : lst) {
            if(f) f=false;
            else out.append(SEP_ELEMENT);
            write(out, obj);
        }
        return out;
    }

    /**
     * Writes an array to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param lst the variadic array to write
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws java.io.IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeVector(A out, Object... lst) throws IOException {
        boolean f = true;
        for(Object obj : lst) {
            if(f) f=false;
            else out.append(SEP_ELEMENT);
            write(out, obj);
        }
        return out;
    }

//    /**
//     * Writes a list to an {@link Appendable}.
//     *
//     * @param out the output Appendable
//     * @param lst the list to write
//     * @param <A> the Appendable type
//     *
//     * @return the output Appendable (to facilitate chaining)
//     *
//     * @throws java.io.IOException if the output Appendable generated an exception
//     */
//    public static <A extends Appendable> A writeList(A out, List<?> lst) throws IOException {
//        boolean f = true;
//        for(Object obj : lst) {
//            if(f) f=false;
//            else out.append(',');
//            writeObject(out, obj);
//        }
//        return out;
//    }

    public static <A extends Appendable> A writeField(A out, CharSequence key, Object val) throws IOException {
        out.append(SEP_STRING);
        writeName(out, key);
        out.append(SEP_STRING);
        if(val!=null) {
            out.append(SEP_ENTRY);
            write(out, val);
        }
        return out;
    }

    public static <A extends Appendable> A writeName(A out, CharSequence str) throws IOException {
        final int ls = str.length();
        for(int i=0;i<ls;i++) {
            char c = str.charAt(i);
            switch (c) {
                case '\\': out.append("\\\\"); break;
                case '\"': out.append("\\\""); break;
                default:
                    if (c >= ' ' && c < 127) out.append(c);
                    else throw new IOException("Invalid XON field name: \""+str+"\"");
                    break;
            }
        }
        return out;
    }

    public static <A extends Appendable> A writeString(A out, CharSequence str) throws IOException {
        final int ls = str.length();
        for(int i=0;i<ls;i++) {
            char c= str.charAt(i);
            switch (c) {
                case '\\': out.append("\\\\"); break;
                case '\"': out.append("\\\""); break;
                case '\b': out.append("\\b"); break;
                case '\f': out.append("\\f"); break;
                case '\n': out.append("\\n"); break;
                case '\r': out.append("\\r"); break;
                case '\t': out.append("\\t"); break;
                default:
                    if (c >= ' ' && c < 127) out.append(c);
                    else out.append(String.format("\\u%04x" ,(int)c));
                    break;
            }
        }
        return out;
    }

    public static <A extends Appendable> A writeNumber(A out, Number n) throws IOException {
        if(n.longValue()==n.doubleValue()) {
            out.append(Long.toString(n.longValue()));
        }
        else {
            out.append(Double.toString(n.doubleValue()));
        }
        return out;
    }

    public static <A extends Appendable> A writeBoolean(A out, boolean b) throws IOException {
        if(b) out.append(LITERAL_TRUE);
        else out.append(LITERAL_FALSE);
        return out;
    }

    public static <A extends Appendable> A writeNull(A out) throws IOException {
        out.append(LITERAL_NULL);
        return out;
    }


    /*********************************************************************************
     **  XON reading
     **/

    /**
     * State-aware wrapper for a reader and current char, needed to be able to read JSON types that do not have delimiters
     */
    protected static class Parser {
        private final Reader in;
        private int st = ' ';
        private int ln = 0;
        private int cn = 0;

        public Parser(Reader in) {
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
         * @throws java.io.IOException if an I/O error occurred while reading from the stream
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
         * @throws java.io.IOException if an I/O error occurred while reading from the stream
         */
        public int skipWhitespace() throws IOException {
            while(st>=0 && isWhiteSpace(st)) { next(); }
            return st;
        }
    }

    public static Object read(CharSequence str) throws FormatException {
        if(str==null) return null;
        try { return read(new StringReader(str.toString())); }
        catch(IOException e) { return null; }
    }

    public static Object read(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return read(p);
    }

    protected static Object read(Parser p) throws IOException, FormatException {
        int c=p.last();
        switch(c) {
            case SEP_OBJECT_S: {
                p.next();
                Map<CharSequence,Object> obj=readObject(p, SEP_STRING, SEP_ENTRY, SEP_ELEMENT, SEP_OBJECT_E);
                c=p.last();
                if(c!=SEP_OBJECT_E) throw new FormatException("Unterminated object at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
                p.next();
                return obj;
            }
            case SEP_COLLEC_S: {
                p.next();
                Collection<Object> obj=readCollec(p, SEP_ELEMENT, SEP_COLLEC_E);
                c=p.last();
                if(c!=SEP_COLLEC_E) throw new FormatException("Unterminated collection at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
                p.next();
                return obj;
            }
            case SEP_VECTOR_S: {
                p.next();
                Object[] obj=readVector(p, SEP_ELEMENT, SEP_VECTOR_E);
                c=p.last();
                if(c!=SEP_VECTOR_E) throw new FormatException("Unterminated vector at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
                p.next();
                return obj;
            }
            case SEP_STRING: {
                p.next();
                String obj=readString(p, c);
                c=p.last();
                if(c!=SEP_STRING) throw new FormatException("Unterminated string at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
                p.next();
                return obj;
            }
            default:
                return readLiteral(p);
        }
    }

    public static Number readNumber(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return readNumber(p);
    }

    protected static Number readNumber(Parser p) throws IOException, FormatException {
        StringBuilder buf = new StringBuilder();
        boolean isInteger = true;
        boolean isFloat = true;
        int c = p.last();
        while(c>=0 && !isWhiteSpace(c)) {
            isInteger &= isValidIntegerChar(c);
            isFloat &= isValidNumberChar(c);
            buf.append((char)c);
            c = p.next();
        }
        if(isInteger) try {
            return Long.parseLong(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new FormatException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf,e);
        }
        if(isFloat) try {
            return Double.parseDouble(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new FormatException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf,e);
        }
        throw new FormatException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf);
    }

    public static Boolean readBoolean(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return readBoolean(p);
    }

    protected static Boolean readBoolean(Parser p) throws IOException, FormatException {
        StringBuilder buf = new StringBuilder();
        int c = p.last();
        while(c>=0 && c>'a' && c<'z') {
            buf.append((char)c);
            c = p.next();
        }
        if(isEqualSequence(buf, LITERAL_FALSE)) return false;
        if(isEqualSequence(buf, LITERAL_TRUE)) return true;
        throw new FormatException("Invalid boolean format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf);
    }

    protected static Object readLiteral(Parser p) throws IOException, FormatException {
        StringBuilder buf = new StringBuilder();
        boolean isInteger = true;
        boolean isFloat = true;
        int c = p.last();
        while(c>=0 && !isWhiteSpace(c) && !isReservedChar(c)) {
            isInteger &= isValidIntegerChar(c);
            isFloat &= isValidNumberChar(c);
            buf.append((char)c);
            c = p.next();
        }
        if(isEqualSequence(buf, LITERAL_NULL)) return null;
        if(isEqualSequence(buf, LITERAL_FALSE)) return false;
        if(isEqualSequence(buf, LITERAL_TRUE)) return true;
        if(isInteger) try {
            return Long.parseLong(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new FormatException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf,e);
        }
        if(isFloat) try {
            return Double.parseDouble(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new FormatException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf,e);
        }
        throw new FormatException("Invalid literal at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf);
    }

    public static String readString(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        char t=SEP_STRING;
        if(c!=t) throw new FormatException("Invalid string format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip leading "
        p.next();
        String obj=readString(p, t);
        c=p.last();
        if(c!=t) throw new FormatException("Unterminated string at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        p.next();
        return obj;
    }

    protected static String readString(Parser p, int t) throws IOException, FormatException {
        StringBuilder buf = new StringBuilder();
        int c = p.last();
        // Read all characters until an unescaped terminator is found
        while(c>=0 && c!=t) {
            // Decode escape sequences
            if(c=='\\') {
                c = p.next();
                if(c<=0) throw new IOException("Unterminated character escape");
                switch(c) {
                    case '\\': buf.append('\\'); break;
                    case '\"': buf.append('\"'); break;
                    case '\'': buf.append('\''); break;
                    case 'b': buf.append('\b'); break;
                    case 'f': buf.append('\f'); break;
                    case 'n': buf.append('\n'); break;
                    case 'r': buf.append('\r'); break;
                    case 't': buf.append('\t'); break;
                    case 'u':
                        // Decode unicode escapes
                        int x = 0;
                        c = p.next();
                        if(c<0) throw new FormatException("Unterminated unicode escape");
                        int d = asHexDigit(c);
                        if(d<0) throw new FormatException("Invalid unicode escape character "+(char)c);
                        x |= d<<12;
                        c = p.next();
                        if(c<0) throw new FormatException("Unterminated unicode escape");
                        d = asHexDigit(c);
                        if(d<0) throw new FormatException("Invalid unicode escape character "+(char)c);
                        x |= d<<8;
                        c = p.next();
                        if(c<0) throw new FormatException("Unterminated unicode escape");
                        d = asHexDigit(c);
                        if(d<0) throw new FormatException("Invalid unicode escape character "+(char)c);
                        x |= d<<4;
                        c = p.next();
                        if(c<0) throw new FormatException("Unterminated unicode escape");
                        d = asHexDigit(c);
                        if(d<0) throw new FormatException("Invalid unicode escape character "+(char)c);
                        x |= d;
                        buf.append((char)x);
                        break;
                    default: buf.append((char)c); break;
                }
            }
            else buf.append((char)c);
            c = p.next();
        }
        return buf.toString();
    }

    public static List<Object> readCollec(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        if(c!=SEP_COLLEC_S) throw new FormatException("Invalid collection format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip leading (
        p.next();
        List<Object> obj=readCollec(p, SEP_ELEMENT, SEP_COLLEC_E);
        c=p.last();
        if(c!=SEP_COLLEC_E) throw new FormatException("Unterminated collection at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip trailing )
        p.next();
        return obj;
    }

    protected static List<Object> readCollec(Parser p, char r, char t) throws IOException, FormatException {
        List<Object> lst = new ArrayList<Object>();
        int c = p.last();
        // Read all objects until t is found or the end of the stream is reached
        while(c>=0) {
            // Skip leading whitespace
            c = p.skipWhitespace();
            if(c==t|| c<0) break;
            Object val = read(p);
            lst.add(val);
            // Skip trailing whitespace
            c = p.skipWhitespace();
            // Return on terminator
            if(c==t || c<0) break;
            // Validate and skip separator
            else if(c==r) c = p.next();
            else throw new FormatException("Unexpected character in collection at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        }
        return lst;
    }

    public static Object[] readVector(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        if(c!=SEP_VECTOR_S) throw new FormatException("Invalid vector format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip leading [
        p.next();
        Object[] obj=readVector(p, SEP_ELEMENT, SEP_VECTOR_E);
        c=p.last();
        if(c!=SEP_VECTOR_E) throw new FormatException("Unterminated vector at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip trailing ]
        p.next();
        return obj;
    }

    protected static Object[] readVector(Parser p, char r, char t) throws IOException, FormatException {
        Object[] ary = new Object[16];
        int len = 0;
        int c = p.last();
        // Read all objects until t is found or the end of the stream is reached
        while(c>=0) {
            // Skip leading whitespace
            c = p.skipWhitespace();
            if(c==t|| c<0) break;
            Object val = read(p);
            if(len>=ary.length) {
                // Multiply capacity by 1.25
                Object[] a = new Object[ary.length+(ary.length>>2)+1];
                System.arraycopy(ary,0,a,0,len);
                ary = a;
            }
            ary[len++] = (val);
            // Skip trailing whitespace
            c = p.skipWhitespace();
            // Return on terminator
            if(c==t || c<0) break;
            // Validate and skip separator
            else if(c==r) c = p.next();
            else throw new FormatException("Unexpected character in array at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        }
        if(len<ary.length) {
            Object[] a = new Object[len];
            System.arraycopy(ary,0,a,0,len);
            return a;
        }
        return ary;
    }

    public static Map<CharSequence,Object> readObject(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        if(c!=SEP_OBJECT_S) throw new FormatException("Invalid map format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip leading {
        p.next();
        Map<CharSequence,Object> obj=readObject(p, SEP_STRING, SEP_ENTRY, SEP_ELEMENT, SEP_OBJECT_E);
        c=p.last();
        if(c!=SEP_OBJECT_E) throw new FormatException("Unterminated map at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip trailing
        p.next();
        return obj;
    }

    protected static Map<CharSequence,Object> readObject(Parser p, char f, char k, char r, char t) throws IOException, FormatException {
        Map<CharSequence,Object> map = new LinkedHashMap<CharSequence,Object>();
        int c = p.last();
        // Read all objects until } is found or the end of the stream is reached
        while(c>=0) {
            // Skip leading whitespace
            c = p.skipWhitespace();
            // Return on terminator
            if(c==t||c<0) break;
            String key;
            if(c==f) {
                // Skip first quote
                p.next();
                key = readString(p, f);
                c=p.last();
                if(c!=f) throw new FormatException("Unterminated field at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
                // Skip last quote
                p.next();
            }
            else {
                // Parse in raw field: sequence of non-whitespace, non-reserved chars
                StringBuilder buf = new StringBuilder();
                while(c>=0 && !isWhiteSpace(c) && !isReservedChar(c)) { buf.append((char)c); c = p.next(); }
                key = buf.toString();
            }
            // Skip intermediary whitespace
            c = p.skipWhitespace();
            // Return on terminator
            if(c==t||c<0) {
                map.put(key, null);
                break;
            }
            // Validate and skip entry separator, if no value specified
            else if(c==r) {
                map.put(key, null);
                c = p.next();
                continue;
            }
            // Otherwise, verify we have a key separator
            else if(c!=k) throw new FormatException("Unexpected character in map at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
            // Skip key separator
            p.next();
            // Skip intermediary whitespace
            p.skipWhitespace();
            Object val = read(p);
            map.put(key, val);
            // Skip trailing whitespace
            c = p.skipWhitespace();
            // Return on terminator
            if(c==t||c<0) break;
            // Validate and skip entry separator
            else if(c==r) c = p.next();
            else throw new FormatException("Unexpected character in map at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        }
        return map;
    }

    /**
     * Checks whether a character is white-space
     *
     * @param c the character to check
     * @return {@literal true} if the character is whitespace
     */
    public static boolean isWhiteSpace(int c) {
        return c==' '||c=='\n'||c=='\r'||c=='\t';
    }

    /**
     * Converts a decimal digit to its corresponding numeric value.
     *
     * @param c the character to convert
     * @return the decimal value of the character, or {@literal -1} if it is not a valid decimal digit
     */
    protected static int asDecDigit(int c) {
        if(c>='0' && c<='9') return c-'0';
        return -1;
    }

    /**
     * Converts an hexadecimal digit to its corresponding numeric value.
     *
     * @param c the character to convert
     * @return the hexadecimal value of the character, or {@literal -1} if it is not a valid hexadecimal digit
     */
    protected static int asHexDigit(int c) {
        if(c>='0' && c<='9') return c-'0';
        if(c>='A' && c<='F') return c-'A'+10;
        if(c>='a' && c<='f') return c-'a'+10;
        return -1;
    }

    /**
     * Checks whether a character is legal in the character representation of an integer number.
     *
     * @param c the character to check
     * @return {@literal true} if the character is legal in an integer number
     */
    public static boolean isValidIntegerChar(int c) {
        return (c>='0' && c<='9') || c=='-' || c=='+';
    }

    /**
     * Checks whether a character is legal in the character representation of a floating point number.
     *
     * @param c the character to check
     * @return {@literal true} if the character is legal in a floating point number
     */
    public static boolean isValidNumberChar(int c) {
        return (c>='0' && c<='9') || c=='-' || c=='+' || c=='.' || c=='e' || c=='E';
    }

    /**
     * Checks whether a character is a reserved JSON character.
     *
     * @param c the character to check
     * @return {@literal true} if the character is one of ,:[]{}'"
     */
    public static boolean isReservedChar(int c) {
        return c=='\'' || c==SEP_STRING
               || c==SEP_ELEMENT  || c==SEP_ENTRY
               || c==SEP_COLLEC_S || c==SEP_COLLEC_E
               || c==SEP_VECTOR_S || c==SEP_VECTOR_E
               || c==SEP_OBJECT_S || c==SEP_OBJECT_E
                ;
    }

    /**
     * Checks whether two character sequences are identical.
     *
     * @param o1 the first sequence
     * @param o2 the second sequence
     * @return {@literal true} iff the two sequences are both {@literal null},
     * or have the same length and identical characters at each respective index
     */
    protected static boolean isEqualSequence(CharSequence o1, CharSequence o2) {
        if(o1==o2) return true;
        if(o1==null || o2==null) return false;
        int l1=o1.length();
        if(l1!=o2.length()) return false;
        for(int i=0; i<l1; i++) if(o1.charAt(i)!=o2.charAt(i)) return false;
        return true;
    }


}
