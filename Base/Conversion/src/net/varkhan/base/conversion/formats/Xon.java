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
     **  XON equality testing
     **/


    /**
     * Tests for deep equality of two XON objects.
     *
     * @param val1 the first object
     * @param val2 the second object
     * @return {@literal true} <i>iff</i> the two objects have the same XON type, and
     * either have the same value, or contain equal objects in the same positions or fields.
     */
    @SuppressWarnings("unchecked")
    public static boolean equals(Object val1, Object val2) {
        if(val1==val2) return true;
        if(val1==null || val2==null) return false;
        if(val1 instanceof Boolean && val2 instanceof Boolean)
            return equalsBoolean((Boolean) val1, (Boolean) val2);
        if(val1 instanceof Number && val2 instanceof Number)
            return equalsNumber((Number) val1, (Number) val2);
        if(val1 instanceof CharSequence && val2 instanceof CharSequence)
            return equalsString((CharSequence) val1, (CharSequence) val2);
//        if(obj instanceof Map)
//            return equals((Map)val1, (Map)val2);
//        else if(obj instanceof List) {
//            return equals((List)val1, (List)val2);
//        }
        if(val1.getClass().isArray() && val2.getClass().isArray())
            return equalsVector((Object[]) val1, (Object[]) val2);
        if(val1 instanceof Map && val2 instanceof Map)
            return equalsObject((Map<CharSequence,Object>) val1, (Map<CharSequence,Object>) val2);
        if(val1 instanceof Collection && val2 instanceof Collection)
            return equalsCollec((Collection<Object>) val1, (Collection<Object>) val2);
        // Incompatible types, no equality
        return false;
    }

    public static boolean equalsBoolean(Boolean val1, Boolean val2) {
        if(val1==val2) return true;
        if(val1==null || val2==null) return false;
        return val1.booleanValue()==val2.booleanValue();
    }

    public static boolean equalsNumber(Number val1, Number val2) {
        if(val1==val2) return true;
        if(val1==null || val2==null) return false;
        return val1.longValue()==val2.longValue() && val1.doubleValue()==val2.doubleValue();
    }

    public static boolean equalsString(CharSequence val1, CharSequence val2) {
        if(val1==val2) return true;
        if(val1==null || val2==null) return false;
        int len=val1.length();
        if(len!=val2.length()) return false;
        for(int i=0; i<len; i++) if(val1.charAt(i)!=val2.charAt(i)) return false;
        return true;
    }

    public static <T> boolean equalsVector(T[] val1, T[] val2) {
        if(val1==val2) return true;
        if(val1==null || val2==null) return false;
        int len=val1.length;
        if(len!=val2.length) return false;
        for(int i=0; i<len; i++) if(!equals(val1[i],val2[i])) return false;
        return true;
    }

    public static <K,V> boolean equalsObject(Map<K,V> val1, Map<K,V> val2) {
        if(val1==val2) return true;
        if(val1==null || val2==null) return false;
        if(val1.size()!=val2.size()) return false;
        for(Map.Entry<K,V> e: val1.entrySet()) if(!equals(e.getValue(),val2.get(e.getKey()))) return false;
        for(Map.Entry<K,V> e: val2.entrySet()) if(!equals(val1.get(e.getKey()),e.getValue())) return false;
        return true;
    }

    public static <T> boolean equalsCollec(Collection<T> val1, Collection<T> val2) {
        if(val1==val2) return true;
        if(val1==null || val2==null) return false;
        Iterator<T> itr1 = val1.iterator();
        Iterator<T> itr2 = val2.iterator();
        while(itr1.hasNext() && itr2.hasNext()) if(!equals(itr1.next(),itr2.next())) return false;
        return !(itr1.hasNext()||itr2.hasNext());
    }


    /*********************************************************************************
     **  XON writing
     **/

    /**
     * <b>An interface that can be implemented by objects that can be written as Xon.</b>
     * <p/>
     * This interface defines a #writeXon(Appendable) method, that handles the
     * serialization of the object's data as Xon to a character stream.
     * <p/>
     * It is the responsibility of the implementor to ensure that <em>valid Xon</em>
     * is produced.
     */
    public static interface Writable {
        public void writeXon(Appendable out) throws IOException;
    }

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
        else if(obj instanceof Writable) {
            ((Writable) obj).writeXon(out);
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
     * Writes a collection to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param lst the collection to write
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
     * Writes a vector to an {@link Appendable}.
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
    public static class Parser {
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

        protected boolean readBoolean() throws IOException, FormatException {
            StringBuilder buf = new StringBuilder();
            while(st>=0 && st>='a' && st<='z') {
                buf.append((char)st);
                next();
            }
            if(isEqualSequence(buf, LITERAL_FALSE)) return false;
            if(isEqualSequence(buf, LITERAL_TRUE)) return true;
            throw exception("Invalid boolean format",buf);
        }

        protected Number readNumber() throws IOException, FormatException {
            StringBuilder buf = new StringBuilder();
            boolean isInteger = true;
            boolean isFloat = true;
            while(st>=0 && !isWhiteSpace(st)) {
                isInteger &= isValidIntegerChar(st);
                isFloat &= isValidNumberChar(st);
                buf.append((char)st);
                next();
            }
            if(isInteger) try {
                return Long.parseLong(buf.toString());
            }
            catch(NumberFormatException e) {
                throw exception("Invalid number format",buf,e);
            }
            if(isFloat) try {
                return Double.parseDouble(buf.toString());
            }
            catch(NumberFormatException e) {
                throw exception("Invalid number format",buf,e);
            }
            throw exception("Invalid number format",buf);
        }

        protected Object readLiteral() throws IOException, FormatException {
            StringBuilder buf = new StringBuilder();
            boolean isInteger = true;
            boolean isFloat = true;
            while(st>=0 && !isWhiteSpace(st) && !isReservedChar(st)) {
                isInteger &= isValidIntegerChar(st);
                isFloat &= isValidNumberChar(st);
                buf.append((char)st);
                next();
            }
            if(isEqualSequence(buf, LITERAL_NULL)) return null;
            if(isEqualSequence(buf, LITERAL_FALSE)) return false;
            if(isEqualSequence(buf, LITERAL_TRUE)) return true;
            if(isInteger) try {
                return Long.parseLong(buf.toString());
            }
            catch(NumberFormatException e) {
                throw exception("Invalid number format", buf, e);
            }
            if(isFloat) try {
                return Double.parseDouble(buf.toString());
            }
            catch(NumberFormatException e) {
                throw exception("Invalid number format", buf, e);
            }
            throw exception("Invalid literal", buf);
        }

        protected String readString(int t) throws IOException, FormatException {
            StringBuilder buf = new StringBuilder();
            // Read all characters until an unescaped terminator is found
            while(st>=0 && st!=t) {
                // Decode escape sequences
                if(st=='\\') {
                    next();
                    if(st<=0) throw new IOException("Unterminated character escape");
                    switch(st) {
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
                            next();
                            if(st<0) throw exception("Unterminated unicode escape");
                            int d = asHexDigit(st);
                            if(d<0) throw exception("Invalid unicode escape character "+(char) st);
                            x |= d<<12;
                            next();
                            if(st<0) throw exception("Unterminated unicode escape");
                            d = asHexDigit(st);
                            if(d<0) throw exception("Invalid unicode escape character "+(char) st);
                            x |= d<<8;
                            next();
                            if(st<0) throw exception("Unterminated unicode escape");
                            d = asHexDigit(st);
                            if(d<0) throw exception("Invalid unicode escape character "+(char) st);
                            x |= d<<4;
                            next();
                            if(st<0) throw exception("Unterminated unicode escape");
                            d = asHexDigit(st);
                            if(d<0) throw exception("Invalid unicode escape character "+(char) st);
                            x |= d;
                            buf.append((char)x);
                            break;
                        default: buf.append((char)st); break;
                    }
                }
                else buf.append((char)st);
                next();
            }
            return buf.toString();
        }

        protected List<Object> readCollec(char r, char t) throws IOException, FormatException {
            List<Object> lst = new ArrayList<Object>();
            // Read all objects until t is found or the end of the stream is reached
            while(st>=0) {
                // Skip leading whitespace
                skipWhitespace();
                if(st==t|| st<0) break;
                Object val = read();
                lst.add(val);
                // Skip trailing whitespace
                skipWhitespace();
                // Return on terminator
                if(st==t || st<0) break;
                // Validate and skip separator
                else if(st==r) next();
                else throw exception("Unexpected character in collection");
            }
            return lst;
        }

        protected Object[] readVector(char r, char t) throws IOException, FormatException {
            Object[] ary = new Object[16];
            int len = 0;
            // Read all objects until t is found or the end of the stream is reached
            while(st>=0) {
                // Skip leading whitespace
                skipWhitespace();
                if(st==t|| st<0) break;
                Object val = read();
                if(len>=ary.length) {
                    // Multiply capacity by 1.25
                    Object[] a = new Object[ary.length+(ary.length>>2)+1];
                    System.arraycopy(ary,0,a,0,len);
                    ary = a;
                }
                ary[len++] = (val);
                // Skip trailing whitespace
                skipWhitespace();
                // Return on terminator
                if(st==t || st<0) break;
                // Validate and skip separator
                else if(st==r) next();
                else throw exception("Unexpected character in array");
            }
            if(len<ary.length) {
                Object[] a = new Object[len];
                System.arraycopy(ary,0,a,0,len);
                return a;
            }
            return ary;
        }

        protected Map<CharSequence,Object> readObject(char f, char k, char r, char t) throws IOException, FormatException {
            Map<CharSequence,Object> map = new LinkedHashMap<CharSequence,Object>();
            // Read all objects until t is found or the end of the stream is reached
            while(st>=0) {
                // Skip leading whitespace
                skipWhitespace();
                // Return on terminator
                if(st==t||st<0) break;
                String key;
                if(st==f) {
                    // Skip first quote
                    next();
                    key = readString(f);
                    if(st!=f) throw exception("Unterminated field");
                    // Skip last quote
                    next();
                }
                else {
                    // Parse in raw field: sequence of non-whitespace, non-reserved chars
                    StringBuilder buf = new StringBuilder();
                    while(st>=0 && !isWhiteSpace(st) && !isReservedChar(st)) { buf.append((char)st); next(); }
                    key = buf.toString();
                }
                // Skip intermediary whitespace
                skipWhitespace();
                // Return on terminator
                if(st==t||st<0) {
                    map.put(key, null);
                    break;
                }
                // Validate and skip entry separator, if no value specified
                else if(st==r) {
                    map.put(key, null);
                    next();
                    continue;
                }
                // Otherwise, verify we have a key separator
                else if(st!=k) throw exception("Unexpected character in map");
                // Skip key separator
                next();
                // Skip intermediary whitespace
                skipWhitespace();
                Object val = read();
                map.put(key, val);
                // Skip trailing whitespace
                skipWhitespace();
                // Return on terminator
                if(st==t||st<0) break;
                // Validate and skip entry separator
                else if(st==r) next();
                else throw exception("Unexpected character in map");
            }
            return map;
        }

        protected Object read() throws IOException, FormatException {
            switch(st) {
                case SEP_OBJECT_S: {
                    next();
                    Map<CharSequence,Object> obj=readObject(SEP_STRING, SEP_ENTRY, SEP_ELEMENT, SEP_OBJECT_E);
                    if(st!=SEP_OBJECT_E) throw exception("Unterminated object");
                    next();
                    return obj;
                }
                case SEP_COLLEC_S: {
                    // Skip first parenthesis
                    next();
                    Collection<Object> obj=readCollec(SEP_ELEMENT, SEP_COLLEC_E);
                    if(st!=SEP_COLLEC_E) throw exception("Unterminated collection");
                    next();
                    return obj;
                }
                case SEP_VECTOR_S: {
                    // Skip first bracket
                    next();
                    Object[] obj=readVector(SEP_ELEMENT, SEP_VECTOR_E);
                    if(st!=SEP_VECTOR_E) throw exception("Unterminated vector");
                    next();
                    return obj;
                }
                case SEP_STRING: {
                    // Skip first quote
                    next();
                    String obj=readString(SEP_STRING);
                    if(st!=SEP_STRING) throw exception("Unterminated string");
                    next();
                    return obj;
                }
                default:
                    return readLiteral();
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

    }

    /**
     * Read a XON value from a string.
     *
     * @param str the string representation of the XON value
     * @return the XON value
     * @throws FormatException if the input is not valid XON
     */
    public static Object read(CharSequence str) throws FormatException {
        if(str==null) return null;
        try { return read(new StringReader(str.toString())); }
        catch(IOException e) { return null; }
    }

    /**
     * Read a XON value from a stream.
     *
     * @param in the input stream containing a representation of the XON value
     * @return the XON value
     * @throws FormatException if the input is not valid XON
     * @throws IOException     if reading from the input failed
     */
    public static Object read(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return p.read();
    }

    /**
     * Read a XON number from a stream.
     *
     * @param in the input stream containing a representation of the XON number
     * @return the XON number
     * @throws FormatException if the input is not valid XON
     * @throws IOException     if reading from the input failed
     */
    public static Number readNumber(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return p.readNumber();
    }

    /**
     * Read a XON boolean from a stream.
     *
     * @param in the input stream containing a representation of the XON boolean
     * @return the XON boolean
     * @throws FormatException if the input is not valid XON
     * @throws IOException     if reading from the input failed
     */
    public static boolean readBoolean(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return p.readBoolean();
    }

    /**
     * Read a XON string from a stream.
     *
     * @param in the input stream containing a representation of the XON string
     * @return the XON string
     * @throws FormatException if the input is not valid XON
     * @throws IOException     if reading from the input failed
     */
    public static String readString(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        char t=SEP_STRING;
        if(c!=t) throw p.exception("Invalid string format");
        // Skip first quote
        p.next();
        String obj=p.readString(t);
        c=p.last();
        if(c!=t) throw p.exception("Unterminated string");
        p.next();
        return obj;
    }

    /**
     * Read a XON collection from a stream.
     *
     * @param in the input stream containing a representation of the XON collection
     * @return the XON collection
     * @throws FormatException if the input is not valid XON
     * @throws IOException     if reading from the input failed
     */
    public static List<Object> readCollec(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        if(c!=SEP_COLLEC_S) throw p.exception("Invalid collection format");
        // Skip leading (
        p.next();
        List<Object> obj=p.readCollec(SEP_ELEMENT, SEP_COLLEC_E);
        c=p.last();
        if(c!=SEP_COLLEC_E) throw p.exception("Unterminated collection");
        // Skip trailing )
        p.next();
        return obj;
    }

    /**
     * Read a XON vector from a stream.
     *
     * @param in the input stream containing a representation of the XON vector
     * @return the XON vector
     * @throws FormatException if the input is not valid XON
     * @throws IOException     if reading from the input failed
     */
    public static Object[] readVector(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        if(c!=SEP_VECTOR_S) throw p.exception("Invalid vector format");
        // Skip leading [
        p.next();
        Object[] obj=p.readVector(SEP_ELEMENT, SEP_VECTOR_E);
        c=p.last();
        if(c!=SEP_VECTOR_E) throw p.exception("Unterminated vector");
        // Skip trailing ]
        p.next();
        return obj;
    }

    /**
     * Read a XON object (map) from a stream.
     *
     * @param in the input stream containing a representation of the XON object
     * @return the XON object
     * @throws FormatException if the input is not valid XON
     * @throws IOException     if reading from the input failed
     */
    public static Map<CharSequence,Object> readObject(Reader in) throws IOException, FormatException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        if(c!=SEP_OBJECT_S) throw p.exception("Invalid map format");
        // Skip leading {
        p.next();
        Map<CharSequence,Object> obj=p.readObject(SEP_STRING, SEP_ENTRY, SEP_ELEMENT, SEP_OBJECT_E);
        c=p.last();
        if(c!=SEP_OBJECT_E) throw p.exception("Unterminated map");
        // Skip trailing
        p.next();
        return obj;
    }


    /**
     * Checks whether a character is white-space.
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
     * Checks whether a character is a reserved XON character.
     *
     * @param c the character to check
     * @return {@literal true} if the character is one of ,:()[]{}'"
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
