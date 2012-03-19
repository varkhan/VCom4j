/**
 *
 */
package net.varkhan.core.management.logging;

import java.io.IOException;


/**
 * <b>LogMapper output formatting utilities.</b>
 * <p/>
 *
 * @author varkhan
 * @date Nov 25, 2010
 * @time 9:57:21 PM
 */
@SuppressWarnings( { "UnusedDeclaration" })
public class LogFormat {

    /**
     * Protected constructor to enforce the singleton pattern
     */
    protected LogFormat() { }


    /**
     * Formats a log message into a buffer.
     * <p/>
     * The escapes sequences start with %, followed by an optional padding character,
     * zero or more digits specifying a display width, and a field specifier character.
     * Each escape sequence is replaced by the specified field of the message, formatted
     * according to the padding and width. The field specifiers are:
     * <li/> <b>'%'</b>: no field is displayed, instead a literal '%' character is added
     * <li/> <b>'c'</b>: the context name
     * <li/> <b>'k'</b>: the filter key
     * <li/> <b>'l'</b>: the severity level (for this field the default padding is '.', and the default width is 3)
     * <li/> <b>'s'</b>: the number of seconds in the time-stamp
     * <li/> <b>'u'</b>: the number of microseconds in the time-stamp
     * <li/> <b>'v'</b>: the floating point event weight
     * <li/> <b>'m'</b>: the message content (converted to a String, using {@link java.lang.Object#toString()})
     *
     * @param out the output buffer
     * @param fmt the format defining how the message should be written
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param tms the message time-stamp (in milliseconds)
     * @param val the event weight
     * @param msg the message content
     * @param <T> the type of message to be formatted
     *
     * @throws IllegalArgumentException if the format string contains invalid directives
     * @throws IOException              if appending to the buffer produced an I/O error
     */
    public static <T> void format(Appendable out, String fmt, String ctx, String key, int lev, long tms, double val, T msg) throws IllegalArgumentException, IOException {
        final int n=fmt.length();
        final char[] nbf=new char[11];
        for(int pos=0;pos<n;pos++) {
            char chr=fmt.charAt(pos);
            if(chr!='%') {
                out.append(chr);
                continue;
            }
            if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
            chr=fmt.charAt(pos);
            char pad='\0';
            // Look for a pad char -> 'c'
            if(((chr<='0')||(chr>'9'))
               &&((chr<'a')||(chr>'z'))
               &&((chr<'A')||(chr>'Z'))
               &&(chr!='%')) {
                pad=chr;
                if(++pos>=n) throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                chr=fmt.charAt(pos);
            }
            if(chr=='\0') break;
            // Look for display width -> 'l'
            int dsp=-1;
            if((chr>'0')&&(chr<='9')) {
                dsp=0;
                while((chr>='0')&&(chr<='9')) {
                    dsp*=10;
                    dsp+=chr-'0';
                    if(++pos>=n)
                        throw new IllegalArgumentException("Invalid format sequence in '"+fmt+"' at char "+pos);
                    chr=fmt.charAt(pos);
                }
            }
            switch(chr) {
                case '%': {     // Literal %
                    out.append('%');
                }
                break;
                case 'c': {     // Context Name value
                    // Pad with '.' by default
                    if(pad=='\0') pad='.';
                    dsp-=ctx.length();
                    out.append(ctx);
                    while(dsp>0) {
                        out.append(pad);
                        dsp--;
                    }
                }
                break;
                case 'k': {     // Filter Key value
                    // Pad with '.' by default
                    if(pad=='\0') pad='.';
                    dsp-=key.length();
                    out.append(key);
                    while(dsp>0) {
                        out.append(pad);
                        dsp--;
                    }
                }
                break;
                case 'l': {     // Debug Level
                    // Pad with '0' by default
                    if(pad=='\0') pad='0';
                    // Display aligned on 3 chars by default
                    if(dsp<0) dsp=3;
                    while(dsp>3) {
                        out.append(pad);
                        dsp--;
                    }
                    if(dsp>2) out.append((char) ('0'+lev%10));
                    if(dsp>1) out.append((char) ('0'+(lev/10)%10));
                    if(dsp>0) out.append((char) ('0'+(lev/100)%10));
                }
                break;
                case 's': {     // Seconds
                    // Pad with '0' by default
                    if(pad=='\0') pad='0';
                    int j=0;
                    long u=tms/1000;
                    while(u!=0&&j<nbf.length) {
                        nbf[j++]=(char) (u%10+'0');
                        u/=10;
                    }
                    if(dsp<0) while(j>0) out.append(nbf[--j]);
                    else {
                        while(dsp>j) {
                            out.append(pad);
                            dsp--;
                        }
                        while(dsp>0) out.append(nbf[--dsp]);
                    }
                }
                break;
                case 'u': {     // Micro seconds
                    // Pad with '0' by default
                    if(pad=='\0') pad='0';
                    int j=0;
                    long u=1000*(tms%1000);
                    while(u!=0&&j<nbf.length) {
                        nbf[j++]=(char) (u%10+'0');
                        u/=10;
                    }
                    if(dsp<0) while(j>0) out.append(nbf[--j]);
                    else {
                        while(dsp>j) {
                            out.append(pad);
                            dsp--;
                        }
                        while(dsp>0) out.append(nbf[--dsp]);
                    }
                }
                break;
                case 'v': {    // Message
                    // Pad with ' ' by default
                    if(pad=='\0') pad=' ';
                    String s=Double.toString(val);
                    dsp-=s.length();
                    out.append(s);
                    while(dsp>0) {
                        out.append(pad);
                        dsp--;
                    }
                }
                break;
                case 'm': {    // Message
                    // Pad with ' ' by default
                    if(pad=='\0') pad=' ';
                    String s=msg.toString();
                    dsp-=s.length();
                    out.append(s);
                    while(dsp>0) {
                        out.append(pad);
                        dsp--;
                    }
                }
                break;
                default:
                    // Ignore unrecognized types
            }
        }
    }

    /**
     * Formats a log message into a buffer.
     *
     * @param buf the output buffer
     * @param fmt the format defining how the message should be written
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param tms the message time-stamp (in milliseconds)
     * @param val the event weight
     * @param msg the message content
     * @param <T> the type of message to be formatted
     *
     * @throws IllegalArgumentException if the format string contains invalid directives
     * @see #format(Appendable, String, String, String, int, long, double, Object)  for a complete specification of the escape sequences
     */
    public static <T> void format(StringBuilder buf, String fmt, String ctx, String key, int lev, long tms, double val, T msg) {
        try {
            format((Appendable) buf, fmt, ctx, key, lev, tms, val, msg);
        }
        catch(IOException e) {
            /* That exception is never thrown by StringBuilder */
        }
    }

    /**
     * Writes a text representation of a log message into a buffer.
     * <p/>
     * This is equivalent to {@code format(buf, "&lt;%03l&gt;[%k:%c] %v %m", ...)}.
     *
     * @param buf the output buffer
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param tms the message time-stamp (in milliseconds)
     * @param val the event weight
     * @param msg the message content
     * @param <T> the type of message to be formatted
     *
     * @throws IllegalArgumentException if the format string contains invalid directives
     * @see #format(Appendable, String, String, String, int, long, double, Object) for a complete specification of the escape sequences
     */
    public static <T> void formatAsText(StringBuilder buf, String ctx, String key, int lev, long tms, double val, T msg) {
        buf.append('<').append((char) ('0'+((lev/100)%10))).append((char) ('0'+((lev/10)%10))).append((char) ('0'+(lev%10))).append('>').append('[').append(key).append(':').append(ctx).append(']').append(' ').append(val).append(' ').append(msg);
    }

    /**
     * Writes a text representation of a log message into a buffer.
     * <p/>
     * This is equivalent to {@code format(buf, "&lt;%03l&gt;[%k:%c] %v %m", ...)}.
     *
     * @param buf the output buffer
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param tms the message time-stamp (in milliseconds)
     * @param val the event weight
     * @param msg the message content
     * @param <T> the type of message to be formatted
     *
     * @throws IllegalArgumentException if the format string contains invalid directives
     * @throws IOException              if appending to the buffer produced an I/O error
     * @see #format(Appendable, String, String, String, int, long, double, Object) for a complete specification of the escape sequences
     */
    public static <T> void formatAsText(Appendable buf, String ctx, String key, int lev, long tms, double val, T msg) throws IOException {
        buf.append('<').append((char) ('0'+((lev/100)%10))).append((char) ('0'+((lev/10)%10))).append((char) ('0'+(lev%10))).append('>').append('[').append(key).append(':').append(ctx).append(']').append(' ').append(Double.toString(val)).append(' ').append(msg.toString());
    }

    /**
     * Writes an XML representation of a log message into a buffer.
     * <p/>
     * This is equivalent to {@code format(buf, "&lt;"+elt+" ctx=\"%c\" key=\"%k\" lev=\"%03l\" val=\"%v\" "+att+">%m</"+elt+"&gt;", ...)}.
     *
     * @param buf the output buffer
     * @param elt the XML element name
     * @param att supplemental XML element attributes
     * @param ctx a context name
     * @param key a filter key
     * @param lev a severity level
     * @param tms the message time-stamp (in milliseconds)
     * @param val the event weight
     * @param msg the message content
     * @param <T> the type of message to be formatted
     *
     * @throws IllegalArgumentException if the format string contains invalid directives
     * @throws IOException              if appending to the buffer produced an I/O error
     * @see #format(Appendable, String, String, String, int, long, double, Object) for a complete specification of the escape sequences
     */
    public static <T> void formatAsXML(Appendable buf, String elt, String att, String ctx, String key, int lev, long tms, double val, T msg) throws IOException {
        buf.append('<').append(elt);
        buf.append(" ctx=\"").append(ctx).append('\"');
        buf.append(" key=\"").append(key).append('\"');
        buf.append(" lev=\"").append((char) ('0'+((lev/100)%10))).append((char) ('0'+((lev/10)%10))).append((char) ('0'+(lev%10))).append('\"');
        buf.append(" val=\"").append(Double.toString(val)).append('\"');
        if(att!=null) buf.append(' ').append(att);
        buf.append('>');
        buf.append(msg.toString());
        buf.append('<').append('/').append(elt).append('>');
    }

}
