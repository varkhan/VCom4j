package net.varkhan.base.conversion.character;

import net.varkhan.base.conversion.AbstractEncoder;
import net.varkhan.base.conversion.Encoder;
import net.varkhan.base.conversion.serializer.EncodingException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import java.util.HashMap;
import java.util.Map;


/**
 * <b>An hexadecimal CharSequence encoder.</b>.
 * <p/>
 * Encodes hexadecimal representations as a series of corresponding bytes.
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 5:42 AM
 */
public class BinaryEncoder<C> extends AbstractEncoder<CharSequence,C> implements Encoder<CharSequence,C> {

    public static final BinaryEncoder<Object> BINARY     = new BinaryEncoder<Object>("0","1");
    public static final BinaryEncoder<Object> OCTAL      = new BinaryEncoder<Object>("0","1","2","3","4","5","6","7","8");
    public static final BinaryEncoder<Object> HEX_UPPER  = new BinaryEncoder<Object>("0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F");
    public static final BinaryEncoder<Object> HEX_LOWER  = new BinaryEncoder<Object>("0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f");

    protected final int step;
    protected final Map<String, Byte> codes;

    public BinaryEncoder() {
        this( "0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F");
    }

    public BinaryEncoder(String codes) {
        this(codes.split(":"));
    }

    public BinaryEncoder(String... codes) {
        this.codes = new HashMap<>(codes.length);
        int step = 0;
        for (int i = 0; i < codes.length; i++) {
            String c = codes[i];
            if(step==0) step=c.length();
            else if(step!=c.length()) throw new IllegalArgumentException("Uneven code lengths");
            this.codes.put(c, (byte) (0xFF & i));
        }
        this.step = step;
    }

    public long encode(CharSequence obj, OutputStream stm, C ctx) { return _encode(obj, stm, step, codes); }

    public long encode(CharSequence obj, ByteBuffer buf, C ctx) { return _encode(obj, buf, step, codes); }

    public long encode(CharSequence obj, byte[] dat, long pos, long len, C ctx) { return _encode(obj, dat, pos, len, step, codes); }

    public long length(CharSequence obj, C ctx) { return _length(obj, step); }


    public static long _encode(CharSequence obj, OutputStream stm, int step, Map<String, Byte> codes) {
        int base = codes.size();
        try {
            long len=0;
            int i=0;
            while(i<obj.length()) {
                int q = 1;
                int b = 0;
                while(q<256) {
                    String c = obj.subSequence(i, i + step).toString();
                    if (!codes.containsKey(c)) throw new EncodingException("Invalid binary sequence '" + c + "'");
                    b = b*base + codes.get(c);
                    i += step;
                    q *= base;
                }
                stm.write((byte) (0xFF&b));
                len++;
            }
            return len;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(CharSequence obj, ByteBuffer buf, int step, Map<String, Byte> codes) {
        int base = codes.size();
        try {
            long len=0;
            int i=0;
            while(i<obj.length()) {
                int q = 1;
                int b = 0;
                while(q<256) {
                    String c = obj.subSequence(i, i + step).toString();
                    if (!codes.containsKey(c)) throw new EncodingException("Invalid binary sequence '" + c + "' in "+codes);
                    b = b*base + codes.get(c);
                    i += step;
                    q *= base;
                }
                buf.put((byte) (0xFF&b));
                len++;
            }
            return len;
        }
        catch(BufferOverflowException e) {
            throw new EncodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(CharSequence obj, byte[] dat, long pos, long len, int step, Map<String, Byte> codes) {
        int base = codes.size();
        try {
            int p=(int) pos;
            int i=0;
            while(i<obj.length() && p<pos+len) {
                int q = 1;
                int b = 0;
                while(q<256) {
                    String c = obj.subSequence(i, i + step).toString();
                    if (!codes.containsKey(c)) throw new EncodingException("Invalid binary sequence '" + c + "' in "+codes);
                    System.out.println("at "+i+": "+obj.subSequence(0,i)+"|"+obj.subSequence(i,obj.length()));
                    System.out.println("'"+c+"' is "+codes.get(c)+" into "+b);
                    b = b*base + codes.get(c);
                    i += step;
                    q *= base;
                }
                System.out.println(" final for "+p+" is "+b);
                dat[p++] = (byte) (0xFF&b);
            }
            return p-pos;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new EncodingException(e);
        }
    }

    public static long _length(CharSequence obj, int step) {
        return obj.length()/step;
    }

}
