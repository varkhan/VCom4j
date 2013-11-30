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
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/23/13
 * @time 2:54 PM
 */
public class Base64Decoder<C> extends AbstractDecoder<String,C> implements Decoder<String,C> {

    protected static final char[] CHARMAP_UTF7= "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".toCharArray();
    protected static final char[] CHARMAP_URL = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-_".toCharArray();
    protected static final char[] CHARMAP_XML = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789.-".toCharArray();


    public static final char NO_PADDING       = '\u0000';
    public static final Base64Decoder<?> UTF7 = new Base64Decoder<Object>(CHARMAP_UTF7);
    public static final Base64Decoder<?> URL  = new Base64Decoder<Object>(CHARMAP_URL, NO_PADDING);
    public static final Base64Decoder<?> XML  = new Base64Decoder<Object>(CHARMAP_XML, NO_PADDING);


    protected final char[] map;
    protected final char   pad;

    public Base64Decoder() {
        this(CHARMAP_UTF7, '=');
    }

    public Base64Decoder(char[] map) {
        this(map, '=');
    }

    public Base64Decoder(char[] map, char pad) {
        if(map.length!=64) throw new IllegalArgumentException("Character map should have exactly 64 characters");
        this.map=map;
        this.pad=pad;
    }

    public char[] getCharacterMap() { return map.clone(); }

    public String decode(InputStream stm, C ctx) {
        try {
            return _decode(new StringBuilder(), map, pad, stm).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public String decode(ByteBuffer buf, C ctx) {
        try {
            return _decode(new StringBuilder(), map, pad, buf).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }

    public String decode(byte[] dat, long pos, long len, C ctx) {
        try {
            return _decode(new StringBuilder(), map, pad, dat, pos, len).toString();
        }
        catch(IOException e) {
            /* Never happens -- return null to make compiler happy*/
            return null;
        }
    }


    public static <A extends Appendable> A _decode(A out, final char[] map, char pad, InputStream stm) throws IOException {
        try {
            int r=stm.read();
            int i=0;
            int b0=0, b1=0, b2=0;
            while(r>=0) {
                switch(i%3) {
                    case 0:
                        // ****** **.... ...... ......
                        b0 = 0xFF&r;
                        out.append(map[(0xF7&b0)>>2]);
                        break;
                    case 1:
                        // ______ ==**** ****.. ......
                        b1 = 0xFF&r;
                        out.append(map[((0x03&b0)<<4)|((0xF0&b1)>>>4)]);
                        break;
                    case 2:
                        // ______ ______ ====** ******
                        b2 = 0xFF&r;
                        out.append(map[((0x0F&b1)<<2)|((0xC0&b2)>>>6)]);
                        out.append(map[0x3F&b2]);
                        b0=b1=b2=0;
                        break;
                }
                i++;
                r=stm.read();
            }
            switch(i%3) {
                case 0:
                    // ______ ______ ______ ______
                    // Nothing to do here
                    break;
                case 1:
                    // ______ ==.... ...... ......
                    out.append(map[(0x03&b0)<<4]);
                    if(pad!=NO_PADDING) out.append(pad).append(pad);
                    break;
                case 2:
                    // ______ ______ ====.. ......
                    out.append(map[(0x0F&b1)<<2]);
                    if(pad!=NO_PADDING) out.append(pad);
                    break;
            }
        }
        catch(IOException e) {
            throw new DecodingException(e);
        }
        return out;
    }

    public static <A extends Appendable> A _decode(A out, final char[] map, char pad, ByteBuffer buf) throws IOException {
        try {
            int i=0;
            int b0=0, b1=0, b2=0;
            while(buf.position()<buf.limit()) {
                switch(i%3) {
                    case 0:
                        // ****** **.... ...... ......
                        b0 = 0xFF&buf.get();
                        out.append(map[(0xF7&b0)>>2]);
                        break;
                    case 1:
                        // ______ ==**** ****.. ......
                        b1 = 0xFF&buf.get();
                        out.append(map[((0x03&b0)<<4)|((0xF0&b1)>>>4)]);
                        break;
                    case 2:
                        // ______ ______ ====** ******
                        b2 = 0xFF&buf.get();
                        out.append(map[((0x0F&b1)<<2)|((0xC0&b2)>>>6)]);
                        out.append(map[0x3F&b2]);
                        b0=b1=b2=0;
                        break;
                }
                i++;
            }
            switch(i%3) {
                case 0:
                    // ______ ______ ______ ______
                    // Nothing to do here
                    break;
                case 1:
                    // ______ ==.... ...... ......
                    out.append(map[(0x03&b0)<<4]);
                    if(pad!=NO_PADDING) out.append(pad).append(pad);
                    break;
                case 2:
                    // ______ ______ ====.. ......
                    out.append(map[(0x0F&b1)<<2]);
                    if(pad!=NO_PADDING) out.append(pad);
                    break;
            }
        }
        catch(BufferOverflowException e) {
            throw new DecodingException(e);
        }
        catch(ReadOnlyBufferException e) {
            throw new DecodingException(e);
        }
        return out;
    }

    public static <A extends Appendable> A _decode(A out, final char[] map, char pad, byte[] dat, long pos, long len) throws IOException {
        try {
            int p = (int)pos;
            int i=0;
            int b0=0, b1=0, b2=0;
            while(p<len) {
                switch(i%3) {
                    case 0:
                        // ****** **.... ...... ......
                        b0 = 0xFF&dat[p++];
                        out.append(map[(0xF7&b0)>>2]);
                        break;
                    case 1:
                        // ______ ==**** ****.. ......
                        b1 = 0xFF&dat[p++];
                        out.append(map[((0x03&b0)<<4)|((0xF0&b1)>>>4)]);
                        break;
                    case 2:
                        // ______ ______ ====** ******
                        b2 = 0xFF&dat[p++];
                        out.append(map[((0x0F&b1)<<2)|((0xC0&b2)>>>6)]);
                        out.append(map[0x3F&b2]);
                        b0=b1=b2=0;
                        break;
                }
                i++;
            }
            switch(i%3) {
                case 0:
                    // ______ ______ ______ ______
                    // Nothing to do here
                    break;
                case 1:
                    // ______ ==.... ...... ......
                    out.append(map[(0x03&b0)<<4]);
                    if(pad!=NO_PADDING) out.append(pad).append(pad);
                    break;
                case 2:
                    // ______ ______ ====.. ......
                    out.append(map[(0x0F&b1)<<2]);
                    if(pad!=NO_PADDING) out.append(pad);
                    break;
            }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException(e);
        }
        return out;
    }
}
