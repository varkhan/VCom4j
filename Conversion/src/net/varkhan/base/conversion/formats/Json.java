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
     * @throws IOException if the output Appendable generated an exception
     */
    @SuppressWarnings("unchecked")
    public static void writeObject(Appendable out, Object obj) throws IOException {
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
    }

    /**
     * Writes a map to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param map the map to write
     * @throws IOException if the output Appendable generated an exception
     */
    public static void writeMap(Appendable out, java.util.Map<? extends CharSequence,Object> map) throws IOException {
        @SuppressWarnings("unchecked")
        java.util.Iterator<java.util.Map.Entry<CharSequence, Object>> it = ((java.util.Map<CharSequence,Object>) map).entrySet().iterator();
        while(it.hasNext()) {
            java.util.Map.Entry<CharSequence, Object> x = it.next();
            writeField(out, x.getKey().toString(), x.getValue());
            if(it.hasNext()) out.append(',');
        }
    }

    /**
     * Writes a map to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param map the map to write
     * @throws IOException if the output Appendable generated an exception
     */
    public static void writeMap(Appendable out, Map<? extends CharSequence,Object> map) throws IOException {
        @SuppressWarnings("unchecked")
        Iterator<? extends Map.Entry<CharSequence,Object>> it = ((Map<CharSequence,Object>) map).iterator();
        while(it.hasNext()) {
            java.util.Map.Entry<CharSequence, Object> x = it.next();
            writeField(out, x.getKey().toString(), x.getValue());
            if(it.hasNext()) out.append(',');
        }
    }

    /**
     * Writes a list to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param lst the list to write
     * @throws IOException if the output Appendable generated an exception
     */
    public static void writeList(Appendable out, java.util.List<Object> lst) throws IOException {
        java.util.Iterator<Object> it = lst.iterator();
        while(it.hasNext()) {
            writeObject(out, it.next());
            if(it.hasNext()) out.append(',');
        }
    }

    /**
     * Writes a list to an {@link Appendable}.
     *
     * @param out the output Appendable
     * @param cnt the list to write
     * @throws IOException if the output Appendable generated an exception
     */
    public static void writeList(Appendable out, List<Object> cnt) throws IOException {
        Iterator<? extends Object> it = cnt.iterator();
        while(it.hasNext()) {
            writeObject(out, it.next());
            if(it.hasNext()) out.append(',');
        }
    }

    public static void writeField(Appendable out, CharSequence key, Object val) throws IOException {
        out.append('"');
        writeName(out, key);
        out.append('"').append(':');
        writeObject(out, val);
    }

    public static void writeName(Appendable out, CharSequence str) throws IOException {
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
    }

    public static void writeString(Appendable out, CharSequence str) throws IOException {
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
    }

    public static void writeNumber(Appendable out, Number n) throws IOException {
        if(n.longValue()==n.doubleValue()) {
            out.append(Long.toString(n.longValue()));
        }
        else {
            out.append(Double.toString(n.doubleValue()));
        }
    }

    public static void writeBoolean(Appendable out, boolean b) throws IOException {
        if(b) out.append(LITERAL_TRUE);
        else out.append(LITERAL_FALSE);
    }

    public static void writeNull(Appendable out) throws IOException {
        out.append(LITERAL_NULL);
    }


    /*********************************************************************************
     **  JSON reading
     **/

    public static Object readObject(Reader in) throws IOException {
        int c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        return readObject(in,c);
    }

    private static Object readObject(Reader in, int c) throws IOException {
        switch(c) {
            case '{':
                return readMap(in,c);
            case '[':
                return readList(in,c);
            case '"':
                return readString(in,c,c);
            default:
                return readLiteral(in,c);
        }
    }

    public static Number readNumber(Reader in) throws IOException {
        int c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        return readNumber(in,c);
    }

    private static Number readNumber(Reader in, int c) throws IOException {
        StringBuilder buf = new StringBuilder();
        boolean isInteger = true;
        boolean isFloat = true;
        while(c>=0 && !isWhiteSpace(c)) {
            isInteger &= isValidIntegerChar(c);
            isFloat &= isValidNumberChar(c);
            buf.append((char)c);
            c = in.read();
        }
        if(isInteger) try {
            return Long.parseLong(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new IOException("Invalid number format",e);
        }
        if(isFloat) try {
            return Double.parseDouble(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new IOException("Invalid number format",e);
        }
        throw new IOException("Invalid number "+buf);
    }

    public static Boolean readBoolean(Reader in) throws IOException {
        int c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        return readBoolean(in, c);
    }

    private static Boolean readBoolean(Reader in, int c) throws IOException {
        StringBuilder buf = new StringBuilder();
        boolean isInteger = true;
        boolean isFloat = true;
        while(c>=0 && !isWhiteSpace(c)) {
            isInteger &= isValidIntegerChar(c);
            isFloat &= isValidNumberChar(c);
            buf.append((char)c);
            c = in.read();
        }
        if(CharArrays.equals(buf,LITERAL_FALSE)) return false;
        if(CharArrays.equals(buf,LITERAL_TRUE)) return true;
        throw new IOException("Invalid boolean "+buf);
    }

    private static Object readLiteral(Reader in, int c) throws IOException {
        StringBuilder buf = new StringBuilder();
        boolean isInteger = true;
        boolean isFloat = true;
        while(c>=0 && !isWhiteSpace(c)) {
            isInteger &= isValidIntegerChar(c);
            isFloat &= isValidNumberChar(c);
            buf.append((char)c);
            c = in.read();
        }
        if(CharArrays.equals(buf,LITERAL_NULL)) return null;
        if(CharArrays.equals(buf,LITERAL_FALSE)) return false;
        if(CharArrays.equals(buf,LITERAL_TRUE)) return true;
        if(isInteger) try {
            return Long.parseLong(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new IOException("Invalid number format",e);
        }
        if(isFloat) try {
            return Double.parseDouble(buf.toString());
        }
        catch(NumberFormatException e) {
            throw new IOException("Invalid number format",e);
        }
        throw new IOException("Invalid literal "+buf);
    }

    public static String readString(Reader in) throws IOException {
        int c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        if(c!='"') throw new IOException("Invalid character sequence format");
        // Skip leading "
        return readString(in,in.read(),c);
    }

    private static String readString(Reader in, int c, int t) throws IOException {
        StringBuilder buf = new StringBuilder();
        // Read all characters until an unescaped terminator is found
        while(c>=0 && c!=t) {
            // Decode escape sequences
            if(c=='\\') {
                c = in.read();
                if(c<=0) throw new IOException("Unterminated string literal");
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
                        c = in.read();
                        if(c<0) throw new IOException("Unterminated unicode escape");
                        int d = asHexDigit(c);
                        if(d<0) throw new IOException("Invalid unicode escape character "+(char)c);
                        x |= d<<12;
                        c = in.read();
                        if(c<0) throw new IOException("Unterminated unicode escape");
                        d = asHexDigit(c);
                        if(d<0) throw new IOException("Invalid unicode escape character "+(char)c);
                        x |= d<<8;
                        c = in.read();
                        if(c<0) throw new IOException("Unterminated unicode escape");
                        d = asHexDigit(c);
                        if(d<0) throw new IOException("Invalid unicode escape character "+(char)c);
                        x |= d<<4;
                        c = in.read();
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
            c = in.read();
        }
        return buf.toString();
    }

    public static List<Object> readList(Reader in) throws IOException {
        int c = in.read();
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        if(c!='[') throw new IOException("Invalid list format");
        // Skip leading [
        return readList(in,in.read());
    }

    private static List<Object> readList(Reader in, int c) throws IOException {
        List<Object> lst = new ArrayList<Object>();
        // Read all objects until ] is found or the end of the stream is reached
        while(c>=0) {
            // Skip leading whitespace
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            if(c==']') break;
            Object val = readObject(in, c);
            lst.add(val);
            // Skip trailing whitespace
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            // Return on terminator
            if(c==']') break;
            // Validate and skip separator
            else if(c==',') c = in.read();
            else if(c<0) throw new IOException("Unterminated list");
            else throw new IOException("Unexpected bare object in list");
        }
        return lst;
    }

    public static Map<CharSequence,Object> readMap(Reader in) throws IOException {
        int c = in.read();
        if(c!='{') throw new IOException("Invalid map format");
        while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
        return readMap(in,c);
    }

    private static Map<CharSequence,Object> readMap(Reader in, int c) throws IOException {
        Map<CharSequence,Object> map = new ArrayOpenHashMap<CharSequence,Object>();
        // Read all objects until } is found or the end of the stream is reached
        while(c>=0) {
            // Skip leading whitespace
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            if(c=='}') break;
            String key;
            if(c=='"') key = readString(in, in.read(), c);
            else {
                // Parse in raw field: sequence of non-whitespace, non-':' chars
                StringBuilder buf = new StringBuilder();
                while(c>=0 && !isWhiteSpace(c) && c!=':') { buf.append(c); c = in.read(); }
                key = buf.toString();
            }
            // Skip intermediary whitespace
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            if(c!=':') throw new IOException("Unexpected bare object in map");
            else if(c<0) throw new IOException("Unterminated map");
            // Skip intermediary whitespace
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            Object val = readObject(in,c);
            map.add(key, val);
            // Skip trailing whitespace
            while(c>=0 && isWhiteSpace(c)) { c = in.read(); }
            // Return on terminator
            if(c=='}') break;
            // Validate and skip separator
            else if(c==',') c = in.read();
            else if(c<0) throw new IOException("Unterminated map");
            else throw new IOException("Unexpected bare object in map");
        }
        return map;
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

    /**
     * Checks whether a character is a decimal digit.
     * <p/>
     *
     * @param c the character to check
     * @return {@literal true} if the character is a decimal digit
     */
    private static int asDecDigit(int c) {
        if(c>='0' && c<='9') return c-'0';
        return -1;
    }

    /**
     * Checks whether a character is an hexadecimal digit.
     * <p/>
     *
     * @param c the character to check
     * @return {@literal true} if the character is an hexadecimal digit
     */
    private static int asHexDigit(int c) {
        if(c>='0' && c<='9') return c-'0';
        if(c>='A' && c<='F') return c-'A'+10;
        if(c>='a' && c<='f') return c-'a'+10;
        return -1;
    }

    /**
     * Checks whether a character is legal in the character representation of an integer number.
     * <p/>
     *
     * @param c the character to check
     * @return {@literal true} if the character is legal in an integer number
     */
    public static boolean isValidIntegerChar(int c) {
        return (c>='0' && c<='9') || c=='-' || c=='+';
    }

    /**
     * Checks whether a character is legal in the character representation of a floating point number.
     * <p/>
     *
     * @param c the character to check
     * @return {@literal true} if the character is legal in a floating point number
     */
    public static boolean isValidNumberChar(int c) {
        return (c>='0' && c<='9') || c=='-'  || c=='+' || c=='.' || c=='e' || c=='E';
    }

}
