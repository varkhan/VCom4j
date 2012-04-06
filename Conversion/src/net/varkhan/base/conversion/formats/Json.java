package net.varkhan.base.conversion.formats;

import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.array.CharArrays;

import net.varkhan.base.containers.list.ArrayList;
import net.varkhan.base.containers.list.List;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.base.containers.map.Map;

import java.io.IOException;
import java.io.Reader;



/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/18/12
 * @time 2:38 PM
 */
public class Json {

    protected Json() { }

    protected static final String LITERAL_TRUE  = "true";
    protected static final String LITERAL_FALSE = "false";
    protected static final String LITERAL_NULL  = "null";


    /*********************************************************************************
     **  JSON writing
     **/

    /**
     * Writes an object as a String.
     *
     * @param val the object to write
     * @return a JSON string representation of thr object
     */
    public static String toJson(Object val) {
        StringBuilder buf = new StringBuilder();
        try { writeObject(buf,val); }
        catch (IOException e) { /* ignore: StringBuilder doesn't throw this */ }
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
     * @throws IOException if the output Appendable generated an exception
     */
    @SuppressWarnings("unchecked")
    public static <A extends Appendable> A writeObject(A out, Object obj) throws IOException {
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
            out.append('"');
            writeString(out, (CharSequence) obj);
            out.append('"');
        }
        else if(obj instanceof Map) {
            out.append('{');
            writeMap(out, (Map<CharSequence,Object>) obj);
            out.append('}');
        }
        else if(obj instanceof List) {
            out.append('[');
            writeList(out, (List<Object>) obj);
            out.append(']');
        }
        else if(obj.getClass().isArray()) {
            out.append('[');
            writeArray(out, (Object[]) obj);
            out.append(']');
        }
        else if(obj instanceof java.util.Map) {
            out.append('{');
            writeMap(out, (java.util.Map<CharSequence,Object>) obj);
            out.append('}');
        }
        else if(obj instanceof java.util.List) {
            out.append('[');
            writeList(out, (java.util.List<Object>) obj);
            out.append(']');
        }
        else throw new IllegalArgumentException("Cannot serialize object to JSON: unknown class "+obj.getClass().getCanonicalName());
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
     * @throws IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeMap(A out, java.util.Map<? extends CharSequence,?> map) throws IOException {
        @SuppressWarnings("unchecked")
        java.util.Iterator<java.util.Map.Entry<CharSequence, Object>> it = ((java.util.Map<CharSequence,Object>) map).entrySet().iterator();
        while(it.hasNext()) {
            java.util.Map.Entry<CharSequence, ?> x = it.next();
            writeField(out, x.getKey().toString(), x.getValue());
            if(it.hasNext()) out.append(',');
        }
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
     * @throws IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeMap(A out, Map<? extends CharSequence,?> map) throws IOException {
        @SuppressWarnings("unchecked")
        Iterator<? extends Map.Entry<CharSequence,Object>> it = ((Map<CharSequence,Object>) map).iterator();
        while(it.hasNext()) {
            java.util.Map.Entry<CharSequence, ?> x = it.next();
            writeField(out, x.getKey().toString(), x.getValue());
            if(it.hasNext()) out.append(',');
        }
        return out;
    }

    /**
     * Writes a list to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param lst the list to write
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeList(A out, java.util.List<?> lst) throws IOException {
        java.util.Iterator<?> it = lst.iterator();
        while(it.hasNext()) {
            writeObject(out, it.next());
            if(it.hasNext()) out.append(',');
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
     * @throws IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeArray(A out, Object... lst) throws IOException {
        final int len = lst.length;
        for(int i=0;i<len;i++) {
            if(i>0) out.append(',');
            writeObject(out, lst[i]);
        }
        return out;
    }

    /**
     * Writes a list to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param cnt the list to write
     * @param <A> the Appendable type
     *
     * @return the output Appendable (to facilitate chaining)
     *
     * @throws IOException if the output Appendable generated an exception
     */
    public static <A extends Appendable> A writeList(A out, List<?> cnt) throws IOException {
        Iterator<?> it = cnt.iterator();
        while(it.hasNext()) {
            writeObject(out, it.next());
            if(it.hasNext()) out.append(',');
        }
        return out;
    }

    public static <A extends Appendable> A writeField(A out, CharSequence key, Object val) throws IOException {
        out.append('"');
        writeName(out, key);
        out.append('"').append(':');
        writeObject(out, val);
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
                    else throw new IOException("Invalid JSON field name: \""+str+"\"");
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
     **  JSON reading
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
    }

    public static Object readObject(Reader in) throws IOException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return readObject(p);
    }

    protected static Object readObject(Parser p) throws IOException {
        int c=p.last();
        switch(c) {
            case '{': {
                p.next();
                Map<CharSequence,Object> obj=readMap(p, '"', ':', ',', '}');
                c=p.last();
                if(c!='}') throw new IOException("Unterminated map at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
                p.next();
                return obj;
            }
            case '[': {
                p.next();
                List<Object> obj=readList(p, ',', ']');
                c=p.last();
                if(c!=']') throw new IOException("Unterminated list at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
                p.next();
                return obj;
            }
            case '"': {
                p.next();
                String obj=readString(p, c);
                c=p.last();
                if(c!='"') throw new IOException("Unterminated string at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
                p.next();
                return obj;
            }
            default:
                return readLiteral(p);
        }
    }

    public static Number readNumber(Reader in) throws IOException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return readNumber(p);
    }

    protected static Number readNumber(Parser p) throws IOException {
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
            throw new IOException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf,e);
        }
        if(isFloat) try {
            return Double.parseDouble(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new IOException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf,e);
        }
        throw new IOException("Invalid number at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf);
    }

    public static Boolean readBoolean(Reader in) throws IOException {
        Parser p = new Parser(in);
        p.skipWhitespace();
        return readBoolean(p);
    }

    protected static Boolean readBoolean(Parser p) throws IOException {
        StringBuilder buf = new StringBuilder();
        int c = p.last();
        while(c>=0 && c>'a' && c<'z') {
            buf.append((char)c);
            c = p.next();
        }
        if(CharArrays.equals(buf,LITERAL_FALSE)) return false;
        if(CharArrays.equals(buf,LITERAL_TRUE)) return true;
        throw new IOException("Invalid boolean at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf);
    }

    protected static Object readLiteral(Parser p) throws IOException {
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
        if(CharArrays.equals(buf,LITERAL_NULL)) return null;
        if(CharArrays.equals(buf,LITERAL_FALSE)) return false;
        if(CharArrays.equals(buf,LITERAL_TRUE)) return true;
        if(isInteger) try {
            return Long.parseLong(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new IOException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf,e);
        }
        if(isFloat) try {
            return Double.parseDouble(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new IOException("Invalid number format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf,e);
        }
        throw new IOException("Invalid literal at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c+": "+buf);
    }

    public static String readString(Reader in) throws IOException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        char t='"';
        if(c!=t) throw new IOException("Invalid character sequence format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip leading "
        p.next();
        String obj=readString(p, t);
        c=p.last();
        if(c!=t) throw new IOException("Unterminated string at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        p.next();
        return obj;
    }

    protected static String readString(Parser p, int t) throws IOException {
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
                        if(c<0) throw new IOException("Unterminated unicode escape");
                        int d = asHexDigit(c);
                        if(d<0) throw new IOException("Invalid unicode escape character "+(char)c);
                        x |= d<<12;
                        c = p.next();
                        if(c<0) throw new IOException("Unterminated unicode escape");
                        d = asHexDigit(c);
                        if(d<0) throw new IOException("Invalid unicode escape character "+(char)c);
                        x |= d<<8;
                        c = p.next();
                        if(c<0) throw new IOException("Unterminated unicode escape");
                        d = asHexDigit(c);
                        if(d<0) throw new IOException("Invalid unicode escape character "+(char)c);
                        x |= d<<4;
                        c = p.next();
                        if(c<0) throw new IOException("Unterminated unicode escape");
                        d = asHexDigit(c);
                        if(d<0) throw new IOException("Invalid unicode escape character "+(char)c);
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

    public static List<Object> readList(Reader in) throws IOException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        if(c!='[') throw new IOException("Invalid list format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip leading [
        p.next();
        List<Object> obj=readList(p, ',', ']');
        c=p.last();
        if(c!=']') throw new IOException("Unterminated list at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip trailing ]
        p.next();
        return obj;
    }

    protected static List<Object> readList(Parser p, char r, char t) throws IOException {
        List<Object> lst = new ArrayList<Object>();
        int c = p.last();
        // Read all objects until ] is found or the end of the stream is reached
        while(c>=0) {
            // Skip leading whitespace
            c = p.skipWhitespace();
            if(c==t|| c<0) break;
            Object val = readObject(p);
            lst.add(val);
            // Skip trailing whitespace
            c = p.skipWhitespace();
            // Return on terminator
            if(c==t || c<0) break;
            // Validate and skip separator
            else if(c==r) c = p.next();
            else throw new IOException("Unexpected bare object in list at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        }
        return lst;
    }

    public static Map<CharSequence,Object> readMap(Reader in) throws IOException {
        Parser p = new Parser(in);
        int c = p.skipWhitespace();
        if(c!='{') throw new IOException("Invalid map format at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip leading {
        p.next();
        Map<CharSequence,Object> obj=readMap(p, '"', ':', ',', '}');
        c=p.last();
        if(c!='}') throw new IOException("Unterminated list at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
        // Skip trailing
        p.next();
        return obj;
    }

    protected static Map<CharSequence,Object> readMap(Parser p, char f, char k, char r, char t) throws IOException {
        Map<CharSequence,Object> map = new ArrayOpenHashMap<CharSequence,Object>();
        int c = p.last();
        // Read all objects until } is found or the end of the stream is reached
        while(c>=0) {
            // Skip leading whitespace
            c = p.skipWhitespace();
            // Return on terminator
            if(c==t|| c<0) break;
            String key;
            if(c==f) {
                // Skip first quote
                p.next();
                key = readString(p, f);
                c=p.last();
                if(c!=f) throw new IOException("Unterminated field at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
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
            if(c!=k) throw new IOException("Unexpected bare object in map at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
            else if(c<0) throw new IOException("Unterminated map at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
            // Skip key separator
            p.next();
            // Skip intermediary whitespace
            p.skipWhitespace();
            Object val = readObject(p);
            map.add(key, val);
            // Skip trailing whitespace
            c = p.skipWhitespace();
            // Return on terminator
            if(c==t|| c<0) break;
            // Validate and skip separator
            else if(c==r) c = p.next();
            else if(c<0) throw new IOException("Unterminated map at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
            else throw new IOException("Unexpected bare object in map at ln:"+p.ln+",cn:"+p.cn+" near "+(char)c);
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
        return c==',' || c==':' || c=='[' || c==']' || c=='{' || c=='}' || c=='\'' || c=='"';
    }


}
