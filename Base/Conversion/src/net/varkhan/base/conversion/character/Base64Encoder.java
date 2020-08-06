package net.varkhan.base.conversion.character;

import net.varkhan.base.conversion.AbstractEncoder;
import net.varkhan.base.conversion.Encoder;
import net.varkhan.base.conversion.serializer.EncodingException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 2:04 PM
 */
public class Base64Encoder<C> extends AbstractEncoder<CharSequence,C> implements Encoder<CharSequence,C> {

    public static final Base64Encoder<?> UTF7=new Base64Encoder<Object>(Base64Decoder.CHARMAP_UTF7, '=');
    public static final Base64Encoder<?> URL =new Base64Encoder<Object>(Base64Decoder.CHARMAP_URL, Base64Decoder.NO_PADDING);
    public static final Base64Encoder<?> XML =new Base64Encoder<Object>(Base64Decoder.CHARMAP_XML, Base64Decoder.NO_PADDING);

//    protected final char[] map;
    protected final byte[] pam;
    protected final char   pad;

    public Base64Encoder() {
        this(Base64Decoder.CHARMAP_UTF7, '=');
    }

    public Base64Encoder(char[] map) {
        this(map, '=');
    }

    public Base64Encoder(char[] map, char pad) {
//        this.map=map;
        this.pad=pad;
        this.pam=_mapping(map);
    }

//    public char[] getCharacterMap() { return map.clone(); }

    public long encode(CharSequence obj, OutputStream stm, C ctx) { return _encode(pam, pad, obj, stm); }

    public long encode(CharSequence obj, ByteBuffer buf, C ctx) { return _encode(pam, pad, obj, buf); }

    public long encode(CharSequence obj, byte[] dat, long pos, long len, C ctx) {
        return _encode(pam, pad, obj, dat, pos, len);
    }

    public long length(CharSequence obj, C ctx) { return _length(obj, pad); }


    public static long _encode(final byte[] pam, char pad, CharSequence obj, OutputStream stm) {
        try {
            long len=0;
            byte b0=0, b1=0, b2=0;
            int i=0;
            while(i<obj.length()) {
                char c=obj.charAt(i);
                // If we have padding characters, exit loop
                if(pad!=Base64Decoder.NO_PADDING&&c==pad) break;
                if(c>=pam.length) throw new EncodingException("Invalid character '"+c+"'");
                int v = pam[c]-1;
                if(v<0) throw new EncodingException("Invalid character '"+c+"'");
                switch(i%4) {
                    case 0:
                        // ******.. ........ ........
                        b0 |= (0x3F&v)<<2;
                        break;
                    case 1:
                        // ======** ****.... ........
                        b0 |= (0x30&v)>>>4;
                        b1 |= (0x0F&v)<<4;
                        stm.write(b0);
                        len++;
                        break;
                    case 2:
                        // ________ ====**** **......
                        b1 |= (0x3C&v)>>>2;
                        b2 |= (0x03&v)<<6;
                        stm.write(b1);
                        len++;
                        break;
                    case 3:
                        // ________ ________ ==******
                        b2 |=  0x3F&v;
                        stm.write(b2);
                        b0=b1=b2=0;
                        len++;
                        break;
                }
                i++;
            }
            return len;
        }
        catch(IOException e) {
            throw new EncodingException(e);
        }
    }

    public static long _encode(final byte[] pam, char pad, CharSequence obj, ByteBuffer buf) {
        try {
            final int l = obj.length();
            long len=0;
            byte b0=0, b1=0, b2=0;
            int i=0;
            while(i<l) {
                char c=obj.charAt(i);
                // If we have padding characters, exit loop
                if(pad!=Base64Decoder.NO_PADDING&&c==pad) break;
                if(c>=pam.length) throw new EncodingException("Invalid character '"+c+"'");
                int v = pam[c]-1;
                if(v<0) throw new EncodingException("Invalid character '"+c+"'");
                switch(i%4) {
                    case 0:
                        // ******.. ........ ........
                        b0 |= (0x3F&v)<<2;
                        break;
                    case 1:
                        // ======** ****.... ........
                        b0 |= (0x30&v)>>>4;
                        b1 |= (0x0F&v)<<4;
                        buf.put(b0);
                        len++;
                        break;
                    case 2:
                        // ________ ====**** **......
                        b1 |= (0x3C&v)>>>2;
                        b2 |= (0x03&v)<<6;
                        buf.put(b1);
                        len++;
                        break;
                    case 3:
                        // ________ ________ ==******
                        b2 |=  0x3F&v;
                        buf.put(b2);
                        b0=b1=b2=0;
                        len++;
                        break;
                }
                i++;
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

    public static long _encode(final byte[] pam, char pad, CharSequence obj, byte[] dat, long pos, long len) {
        try {
            final int l = obj.length();
            int p=(int) pos;
            byte b0=0, b1=0, b2=0;
            int i=0;
            while(i<l && p<pos+len) {
                char c=obj.charAt(i);
                // If we have padding characters, exit loop
                if(pad!=Base64Decoder.NO_PADDING&&c==pad) break;
                if(c>=pam.length) throw new EncodingException("Invalid character '"+c+"'");
                int v = pam[c]-1;
                if(v<0) throw new EncodingException("Invalid character '"+c+"'");
                switch(i%4) {
                    case 0:
                        // ******.. ........ ........
                        b0 |= (0x3F&v)<<2;
                        break;
                    case 1:
                        // ======** ****.... ........
                        b0 |= (0x30&v)>>>4;
                        b1 |= (0x0F&v)<<4;
                        dat[p++] = b0;
                        break;
                    case 2:
                        // ________ ====**** **......
                        b1 |= (0x3C&v)>>>2;
                        b2 |= (0x03&v)<<6;
                        dat[p++] = b1;
                        break;
                    case 3:
                        // ________ ________ ==******
                        b2 |=  0x3F&v;
                        dat[p++] = b2;
                        b0=b1=b2=0;
                        break;
                }
                i++;
            }
            return p-pos;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new EncodingException(e);
        }
    }

    public static long _length(CharSequence obj, char pad) {
        final int l = obj.length();
        int i=0;
        int len=0;
        while(i<l) {
            char c=obj.charAt(i);
            // If we have padding characters, exit loop
            if(pad!=Base64Decoder.NO_PADDING&&c==pad) break;
            if(i%4 > 0) len++;
            i++;
        }
        return len;
    }

    public static byte[] _mapping(char[] map) {
        if(map.length!=64) throw new IllegalArgumentException("Character map should have exactly 64 characters");
        int max=0;
        for(char c: map) if(max<c) max=c;
        byte[] pam = new byte[max+1];
        for(int i=0;i<64;) {
            pam[map[i]]=(byte)++i;
        }
        return pam;
    }

}
