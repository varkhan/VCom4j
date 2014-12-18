package net.varkhan.base.conversion.character;

import net.varkhan.base.conversion.AbstractDecoder;
import net.varkhan.base.conversion.Decoder;
import net.varkhan.base.conversion.serializer.DecodingException;
import net.varkhan.base.conversion.serializer.EncodingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/17/14
 * @time 7:40 PM
 */
public class URLDecoder<C> extends AbstractDecoder<String,C> implements Decoder<String,C> {

    public String decode(InputStream stm, C ctx) {
        try {
            return _decode(new StringBuilder(), stm).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public String decode(ByteBuffer buf, C ctx) {
        try {
            return _decode(new StringBuilder(), buf).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public String decode(byte[] dat, long pos, long len, C ctx) {
        try {
            return _decode(new StringBuilder(), dat, pos, len).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public static <A extends Appendable> A _decode(A out, InputStream stm) throws IOException {
        int r=stm.read();
        int b1, b2;
        while(r>=0) {
            try {
                if(r=='+') out.append(' ');
                else if(r=='%') {
                    // Read 2 hex characters for code
                    b1=stm.read();
                    if(b1<0) throw new DecodingException("Incomplete escape sequence");
                    b2=stm.read();
                    if(b2<0) throw new DecodingException("Incomplete escape sequence");
                    int c=toHex(b1)<<4|toHex(b2);
                    if(c<0x80) {
                        // no need to mask byte
                    }
                    else if(c<0xE0) {
                        c=(31&c)<<6;
                        r=stm.read();
                        if(r<0) throw new DecodingException("Incomplete escape sequence");
                        if(r!='%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char) c+"' instead ");
                        b1=stm.read();
                        if(b1<0) throw new DecodingException("Incomplete escape sequence");
                        b2=stm.read();
                        if(b2<0) throw new DecodingException("Incomplete escape sequence");
                        c|=(63&(toHex(b1)<<4|toHex(b2)));
                    }
                    else if(c<0xF0) {
                        c=(15&c)<<12;
                        r=stm.read();
                        if(r<0) throw new DecodingException("Incomplete escape sequence");
                        if(r!='%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char) c+"' instead ");
                        b1=stm.read();
                        if(b1<0) throw new DecodingException("Incomplete escape sequence");
                        b2=stm.read();
                        if(b2<0) throw new DecodingException("Incomplete escape sequence");
                        c|=(63&(toHex(b1)<<4|toHex(b2)))<<6;
                        r=stm.read();
                        if(r<0) throw new DecodingException("Incomplete escape sequence");
                        if(r!='%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char) c+"' instead ");
                        b1=stm.read();
                        if(b1<0) throw new DecodingException("Incomplete escape sequence");
                        b2=stm.read();
                        if(b2<0) throw new DecodingException("Incomplete escape sequence");
                        c|=(63&(toHex(b1)<<4|toHex(b2)));
                    }
                    else {
                        c=(7&c)<<18;
                        r=stm.read();
                        if(r<0) throw new DecodingException("Incomplete escape sequence");
                        if(r!='%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char) c+"' instead ");
                        b1=stm.read();
                        if(b1<0) throw new DecodingException("Incomplete escape sequence");
                        b2=stm.read();
                        if(b2<0) throw new DecodingException("Incomplete escape sequence");
                        c|=(63&(toHex(b1)<<4|toHex(b2)))<<12;
                        r=stm.read();
                        if(r<0) throw new DecodingException("Incomplete escape sequence");
                        if(r!='%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char) c+"' instead ");
                        b1=stm.read();
                        if(b1<0) throw new DecodingException("Incomplete escape sequence");
                        b2=stm.read();
                        if(b2<0) throw new DecodingException("Incomplete escape sequence");
                        c|=(63&(toHex(b1)<<4|toHex(b2)))<<6;
                        r=stm.read();
                        if(r<0) throw new DecodingException("Incomplete escape sequence");
                        if(r!='%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char) c+"' instead ");
                        b1=stm.read();
                        if(b1<0) throw new DecodingException("Incomplete escape sequence");
                        b2=stm.read();
                        if(b2<0) throw new DecodingException("Incomplete escape sequence");
                        c|=(63&(toHex(b1)<<4|toHex(b2)));
                    }
                    out.append((char) c);
                }
                else out.append((char) r);
            }
            catch(IOException e){
                throw new DecodingException(e);
            }
        }
        return out;
    }

    public static <A extends Appendable> A _decode(A out, ByteBuffer buf) throws IOException {
        while(buf.position()<buf.limit()) {
            try {
            byte b = buf.get();
            if (b == '+') out.append(' ');
            else if (b == '%') {
                // Read 2 hex characters for code
                int c = toHex(buf.get()) << 4 | toHex(buf.get());
                if (c < 0x80) {
                    // no need to mask byte
                } else if (c < 0xE0) {
                    c = (31 & c) << 6;
                    b = buf.get();
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.get()) << 4 | toHex(buf.get())));
                } else if (c < 0xF0) {
                    c = (15 & c) << 12;
                    b = buf.get();
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.get()) << 4 | toHex(buf.get()))) << 6;
                    b = buf.get();
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 3rd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.get()) << 4 | toHex(buf.get())));
                } else {
                    c = (7 & c) << 18;
                    b = buf.get();
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.get()) << 4 | toHex(buf.get()))) << 12;
                    b = buf.get();
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 3rd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.get()) << 4 | toHex(buf.get()))) << 6;
                    b = buf.get();
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 4th byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.get()) << 4 | toHex(buf.get())));
                }
                out.append((char) c);
            } else out.append((char) b);
            }
            catch(BufferOverflowException e) {
                throw new DecodingException(e);
            }
            catch(ReadOnlyBufferException e) {
                throw new DecodingException(e);
            }
        }
        return out;
    }

    public static <A extends Appendable> A _decode(A out, CharSequence buf) throws IOException {
        for(int i=0; i<buf.length(); i++) {
            try {
            char b = buf.charAt(i);
            if (b == '+') out.append(' ');
            else if (b == '%') {
                // Read 2 hex characters for code
                int c = toHex(buf.charAt(++i)) << 4 | toHex(buf.charAt(++i));
                if (c < 0x80) {
                    // no need to mask byte
                } else if (c < 0xE0) {
                    c = (31 & c) << 6;
                    b = buf.charAt(++i);
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.charAt(++i)) << 4 | toHex(buf.charAt(++i))));
                } else if (c < 0xF0) {
                    c = (15 & c) << 12;
                    b = buf.charAt(++i);
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.charAt(++i)) << 4 | toHex(buf.charAt(++i)))) << 6;
                    b = buf.charAt(++i);
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 3rd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.charAt(++i)) << 4 | toHex(buf.charAt(++i))));
                } else {
                    c = (7 & c) << 18;
                    b = buf.charAt(++i);
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.charAt(++i)) << 4 | toHex(buf.charAt(++i)))) << 12;
                    b = buf.charAt(++i);
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 3rd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.charAt(++i)) << 4 | toHex(buf.charAt(++i)))) << 6;
                    b = buf.charAt(++i);
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 4th byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(buf.charAt(++i)) << 4 | toHex(buf.charAt(++i))));
                }
                out.append((char) c);
            } else out.append((char) b);
            }
            catch(BufferOverflowException e) {
                throw new DecodingException(e);
            }
            catch(ReadOnlyBufferException e) {
                throw new DecodingException(e);
            }
        }
        return out;
    }

    public static <A extends Appendable> A _decode(A out, byte[] dat, long pos, long len) throws IOException {
        for (int i = 0; i < len; i++) {
            byte b = dat[(int)(pos+i)];
            if (b == '+') out.append(' ');
            else if (b == '%') {
                // Read 2 hex characters for code
                int c = toHex(dat[(int)(pos+(++i))]) << 4 | toHex(dat[(int)(pos+(++i))]);
                if (c < 0x80) {
                    // no need to mask byte
                } else if (c < 0xE0) {
                    c = (31 & c) << 6;
                    b = dat[(int)(pos+(++i))];
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(dat[(int)(pos+(++i))]) << 4 | toHex(dat[(int)(pos+(++i))])));
                } else if (c < 0xF0) {
                    c = (15 & c) << 12;
                    b = dat[(int)(pos+(++i))];
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(dat[(int)(pos+(++i))]) << 4 | toHex(dat[(int)(pos+(++i))]))) << 6;
                    b = dat[(int)(pos+(++i))];
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 3rd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(dat[(int)(pos+(++i))]) << 4 | toHex(dat[(int)(pos+(++i))])));
                } else {
                    c = (7 & c) << 18;
                    b = dat[(int)(pos+(++i))];
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 2nd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(dat[(int)(pos+(++i))]) << 4 | toHex(dat[(int)(pos+(++i))]))) << 12;
                    b = dat[(int)(pos+(++i))];
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 3rd byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(dat[(int)(pos+(++i))]) << 4 | toHex(dat[(int)(pos+(++i))]))) << 6;
                    b = dat[(int)(pos+(++i))];
                    if (b != '%') throw new EncodingException("Invalid encoding, expecting % escape for 4th byte, got '"+(char)c+"' instead ");
                    c |= (63 & (toHex(dat[(int)(pos+(++i))]) << 4 | toHex(dat[(int)(pos+(++i))])));
                }
                out.append((char) c);
            } else out.append((char) b);
        }
        return out;
    }

    private static int toHex(int c) {
        if (c >= '0' && c <= '9') return c - '0';
        if (c >= 'a' && c <= 'f') return c - 'a' + 10;
        if (c >= 'A' && c <= 'F') return c - 'A' + 10;
        throw new IllegalArgumentException("Invalid character '" + (char)c + "' in hex escape");
    }

}
