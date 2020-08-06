package net.varkhan.base.conversion.character;

import net.varkhan.base.conversion.AbstractDecoder;
import net.varkhan.base.conversion.Decoder;
import net.varkhan.base.conversion.serializer.DecodingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b>An hexadecimal CharSequence decoder</b>.
 * <p/>
 * Decodes a series of bytes into their hexadecimal representation.
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 5:42 AM
 */
public class BinaryDecoder<C> extends AbstractDecoder<String,C> implements Decoder<String,C> {

    public static final BinaryDecoder<Object> BINARY     = new BinaryDecoder<Object>("0","1");
    public static final BinaryDecoder<Object> OCTAL      = new BinaryDecoder<Object>("0","1","2","3","4","5","6","7","8");
    public static final BinaryDecoder<Object> HEX_UPPER  = new BinaryDecoder<Object>("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F");
    public static final BinaryDecoder<Object> HEX_LOWER  = new BinaryDecoder<Object>("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");

    protected final String[] codes;

    public BinaryDecoder() {
        this("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F");
    }

    public BinaryDecoder(String codes) {
        this(codes.split(":"));
    }

    public BinaryDecoder(String... codes) {
        this.codes = codes;
    }

    public String decode(InputStream stm, C ctx) {
        try {
            return _decode(new StringBuilder(), stm, codes, false).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public String decode(ByteBuffer buf, C ctx) {
        try {
            return _decode(new StringBuilder(), buf, codes, false).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public String decode(byte[] dat, long pos, long len, C ctx) {
        try {
            return _decode(new StringBuilder(), dat, pos, len, codes, false).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }


    public static <A extends Appendable> A _decode(A out, InputStream stm, String[] codes, boolean reverse) throws IOException {
        if(codes==null || codes.length<2) throw new IllegalArgumentException("Invalid binary code array");
        final int base = codes.length;
        final int maxq = maxq(base);
        int b=stm.read();
        while(b>=0) {
            try {
                if(reverse) {
                    int q=1;
                    while(q<256) {
                        out.append(codes[b % base]);
                        b /= base;
                        q *= base;
                    }
                }
                else{
                    int q = maxq;
                    while(q>base) {
                        q /= base;
                        out.append(codes[(b/q) % base]);
                    }
                    out.append(codes[b % base]);
                }
                b=stm.read();
            }
            catch(IOException e) {
                throw new DecodingException(e);
            }
        }
        return out;
    }

    public static <A extends Appendable> A _decode(A out, ByteBuffer buf, String[] codes, boolean reverse) throws IOException {
        if(codes==null || codes.length<2) throw new IllegalArgumentException("Invalid binary code array");
        final int base = codes.length;
        final int maxq = maxq(base);
        while(buf.position()<buf.limit()) {
            try {
                int b=0xFF&buf.get();
                if(reverse) {
                    int q=1;
                    while(q<256) {
                        out.append(codes[b % base]);
                        b /= base;
                        q *= base;
                    }
                }
                else{
                    int q = maxq;
                    while(q>base) {
                        q /= base;
                        out.append(codes[(b/q) % base]);
                    }
                    out.append(codes[b % base]);
                }
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

    public static <A extends Appendable> A _decode(A out, byte[] dat, long pos, long len, String[] codes, boolean reverse) throws IOException {
        if(codes==null || codes.length<2) throw new IllegalArgumentException("Invalid binary code array");
        final int base = codes.length;
        final int maxq = maxq(base);
        int p = (int)pos;
        while(p<len) {
            try {
                int b=0xFF&dat[p++];
                if(reverse) {
                    int q=1;
                    while(q<256) {
                        out.append(codes[b % base]);
                        b /= base;
                        q *= base;
                    }
                }
                else{
                    int q=maxq;
                    while(q>base) {
                        q /= base;
                        out.append(codes[(b/q) % base]);
                    }
                    out.append(codes[b % base]);
                }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException(e);
            }
        }
        return out;
    }

    private static int maxq(int base) {
        int maxq = 1;
        while (maxq < 256) maxq *= base;
        return maxq;
    }

}
