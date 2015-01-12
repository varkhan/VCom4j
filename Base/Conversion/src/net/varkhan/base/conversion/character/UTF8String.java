package net.varkhan.base.conversion.character;

import net.varkhan.base.conversion.serializer.DecodingException;
import net.varkhan.base.conversion.serializer.EncodingException;

import java.util.Arrays;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/11/15
 * @time 4:05 PM
 */
public class UTF8String implements CharSequence {
    protected final byte[] data;

    public UTF8String(byte[] data, int start, int end) {
        // Use charLen to validate the data
        charLen(data,start,end);
        this.data=new byte[end-start];
        System.arraycopy(data, start, this.data, 0, end-start);
    }

    public UTF8String(byte[] data) {
        this(data, 0, data.length);
    }

    public UTF8String(char[] chars, int start, int end) {
        data= new byte[(int)bytesLen(chars, start, end)];
        bytes(chars,start,end,data,0,data.length);
    }

    @Override
    public int length() {
        return (int)charLen(data, 0, data.length);
    }

    @Override
    public char charAt(int index) {
        return (char) charAt(data, 0, index);
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        long bs = posAt(data,0,start);
        long be = posAt(data,bs,end-start);
        return new UTF8String(data, (int)bs, (int)be);
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof CharSequence)) return false;
        if(o instanceof UTF8String) {
            UTF8String that=(UTF8String) o;
            return Arrays.equals(this.data,that.data);
        }
        else {
            CharSequence that=(CharSequence) o;
            return compare(that, data, 0, data.length)==0;
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        for(byte c: data) h=31*h+c;
        return h;
    }

    @Override
    public String toString() {
        char[] c = new char[(int)charLen(data, 0, data.length)];
        chars(c, 0, c.length, data, 0, data.length);
        return new String(c);
    }

    public static long charLen(byte[] dat, long pos, long len) {
        int p = (int)pos;
        int l=0;
        while(p<len) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    l ++;
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    l ++;
                    p ++;
                }
                else if(b<0xF0) {
                    l ++;
                    p += 2;
                }
                else if(b<0xF8) {
                    l ++;
                    p += 3;
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException(e);
            }
        }
        return l;
    }

    public static int charAt(byte[] dat, long pos, long idx) {
        int p=(int) posAt(dat, (int) pos, idx);
        try {
            int b=0xFF&dat[p++];
            if(b<0x80) {
                return (char) (b&0x7F);
            }
            else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
            else if(b<0xE0) {
                return (char) (((b&0x1F)<<6)|(dat[p]&0x3F));
            }
            else if(b<0xF0) {
                return (char) (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p]&0x3F));
            }
            else if(b<0xF8) {
                return (char) (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p]&0x3F));
            }
            else { throw new DecodingException("Incomplete Unicode sequence"); }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new IndexOutOfBoundsException("Character index "+idx+" out of data bounds "+idx);
        }
    }

    public static long posAt(byte[] dat, long pos, long idx) {
        int p=(int) pos;
        long l=0;
        while(l<idx) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    l ++;
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    l ++;
                    p ++;
                }
                else if(b<0xF0) {
                    l ++;
                    p += 2;
                }
                else if(b<0xF8) {
                    l ++;
                    p += 3;
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException("Character index "+idx+" out of data bounds "+l);
            }
        }
        return p;
    }


    public static long bytesLen(char[] obj, long cpos, long clen) {
        int len=0;
        for(int i=(int)cpos;i<clen;i++) {
            char c=obj[i];
            if(c<0x80) len++;
            else if(c<0x800) len+=2;
            else if(c<0x10000) len+=3;
            else clen+=4;
        }
        return len;
    }

    public static long bytes(char[] obj, long cpos, long clen, byte[] dat, long pos, long len) {
        try {
            int p=(int) pos;
            int i=(int) cpos;
            while(i<clen) {
                if(p-pos>=len) return p-pos;
                char c=obj[i++];
                if(c<0x80) {
                    dat[p++]=(byte) (0x7F&c);
                }
                else if(c<0x800) {
                    dat[p++]=(byte) (0xC0|(0x1F&(c>>>6)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&c));
                }
                else if(c<0x10000) {
                    dat[p++]=(byte) (0xE0|(0x0F&(c>>>12)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&(c>>>6)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&c));
                }
                else {
                    dat[p++]=(byte) (0xF0|(0x07&(c>>>18)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&(c>>>12)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&(c>>>6)));
                    if(p-pos>=len) throw new EncodingException();
                    dat[p++]=(byte) (0x80|(0x3F&c));
                }
            }
            return p-pos;
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new EncodingException(e);
        }
    }

    public static long chars(char[] out, long cpos, long clen, byte[] dat, long pos, long len) {
        int p = (int)pos;
        int q = (int) cpos;
        clen += cpos;
        while(p<len && q<clen) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    out[q++]=(char) (b&0x7F);
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    out[q++]=(char) (((b&0x1F)<<6)|(dat[p++]&0x3F));
                }
                else if(b<0xF0) {
                    out[q++]=(char) (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                }
                else if(b<0xF8) {
                    out[q++]=(char) (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException(e);
            }
        }
        return q-cpos;
    }

    public static long compare(char[] str, long cpos, long clen, byte[] dat, long pos, long len) {
        int p = (int) pos;
        int q = (int) cpos;
        clen += cpos;
        while(p<len && q<clen) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    int d = str[q++] - (b&0x7F);
                    if(d!=0) return d;
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    int d = str[q++] - (((b&0x1F)<<6)|(dat[p++]&0x3F));
                    if(d!=0) return d;
                }
                else if(b<0xF0) {
                    int d = str[q++] - (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                    if(d!=0) return d;
                }
                else if(b<0xF8) {
                    int d = str[q++] - (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                    if(d!=0) return d;
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException(e);
            }
        }
        return (clen-q) - (len-p);
    }

    public static long compare(CharSequence str, byte[] dat, long pos, long len) {
        int p = (int) pos;
        int q = 0;
        int clen=str.length();
        while(p<len && q<clen) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    int d = str.charAt(q++) - (b&0x7F);
                    if(d!=0) return d;
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    int d = str.charAt(q++) - (((b&0x1F)<<6)|(dat[p++]&0x3F));
                    if(d!=0) return d;
                }
                else if(b<0xF0) {
                    int d = str.charAt(q++) - (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                    if(d!=0) return d;
                }
                else if(b<0xF8) {
                    int d = str.charAt(q++) - (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                    if(d!=0) return d;
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException(e);
            }
        }
        return (clen-q) - (len-p);
    }

}
