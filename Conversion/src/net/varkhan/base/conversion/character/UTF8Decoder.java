package net.varkhan.base.conversion.character;

import net.varkhan.base.conversion.Decoder;
import net.varkhan.base.conversion.serializer.DecodingException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b>An UTF-8 CharSequence decoder</b>.
 * <p/>
 * Decodes a series of UTF-8 variable-width code points (on 1 to 4 bytes,
 * depending on the code point) as a Java String (UTF-16).
 * <p/>
 *
 * @author varkhan
 * @date 1/30/11
 * @time 5:42 AM
 */
public class UTF8Decoder<C> implements Decoder<String,C> {


    public String decode(InputStream stm, C ctx) {
        StringBuilder buf = new StringBuilder();
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
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    r=stm.read();
                    if(r<0) throw new DecodingException("Incomplete Unicode sequence");
                    int b2=0xFF&r;
                    out.append((char) (((b&0x1F)<<6)|(b2&0x3F)));
                }
                else if(b<0xF0) {
                    r=stm.read();
                    if(r<0) throw new DecodingException("Incomplete Unicode sequence");
                    int b2=0xFF&r;
                    r=stm.read();
                    if(r<0) throw new DecodingException("Incomplete Unicode sequence");
                    int b3=0xFF&r;
                    out.append((char) (((b&0x0F)<<12)|((b2&0x3F)<<6)|(b3&0x3F)));
                }
                else if(b<0xF8) {
                    r=stm.read();
                    if(r<0) throw new DecodingException("Incomplete Unicode sequence");
                    int b2=0xFF&r;
                    r=stm.read();
                    if(r<0) throw new DecodingException("Incomplete Unicode sequence");
                    int b3=0xFF&r;
                    r=stm.read();
                    if(r<0) throw new DecodingException("Incomplete Unicode sequence");
                    int b4=0xFF&r;
                    out.append((char) (((b&0x07)<<18)|((b2&0x3F)<<12)|((b3&0x3F)<<6)|(b4&0x3F)));
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
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
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    out.append((char) (((b&0x1F)<<6)|(buf.get()&0x3F)));
                }
                else if(b<0xF0) {
                    out.append((char) (((b&0x0F)<<12)|((buf.get()&0x3F)<<6)|(buf.get()&0x3F)));
                }
                else if(b<0xF8) {
                    out.append((char) (((b&0x07)<<18)|((buf.get()&0x3F)<<12)|((buf.get()&0x3F)<<6)|(buf.get()&0x3F)));
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
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
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    out.append((char) (((b&0x1F)<<6)|(dat[p++]&0x3F)));
                }
                else if(b<0xF0) {
                    out.append((char) (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F)));
                }
                else if(b<0xF8) {
                    out.append((char) (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F)));
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException(e);
            }
        }
        return out;
    }

}
