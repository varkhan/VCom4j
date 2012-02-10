package net.varkhan.base.conversion.character;

import net.varkhan.base.conversion.Decoder;
import net.varkhan.base.conversion.serializer.DecodingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b>An ASCII CharSequence decoder</b>.
 * <p/>
 * Decodes a series of ASCII 7-bit characters as a Java String (UTF-16).
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 5:42 AM
 */
public class AsciiDecoder<C> implements Decoder<String,C> {


    public String decode(InputStream stm, C ctx) {
        try {
            return _decode(new StringBuilder(),stm).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public String decode(ByteBuffer buf, C ctx) {
        try {
            return _decode(new StringBuilder(),buf).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public String decode(byte[] dat, long pos, long len, C ctx) {
        try {
            return _decode(new StringBuilder(),dat, pos, len).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }


    public static <A extends Appendable> A _decode(A out, InputStream stm) throws IOException {
        int r=stm.read();
        while(r>=0) {
            try {
                int b=0xFF&r;
                if(b<0x80) {
                    out.append((char) (b&0x7F));
                }
                else throw new DecodingException("Invalid ASCII character");
            }
            catch(IOException e) {
                throw new DecodingException(e);
            }
        }
        return out;
    }

    public static <A extends Appendable> A _decode(A out, ByteBuffer buf) throws IOException {
        while(buf.position()<buf.limit()) {
            try {
                int b=0xFF&buf.get();
                if(b<0x80) {
                    out.append((char) (b&0x7F));
                }
                else throw new DecodingException("Invalid ASCII character");
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
        int p = (int)pos;
        while(p<len) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    out.append((char) (b&0x7F));
                }
                else throw new DecodingException("Invalid ASCII character");
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException(e);
            }
        }
        return out;
    }

}
