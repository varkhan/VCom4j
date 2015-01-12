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
public class UTF8String implements CharSequence, java.io.Serializable {
    protected final byte[] data;

    public UTF8String(byte[] data, int start, int end) {
        // Use charLen to validate the data
        idxAt(data, start, end);
        this.data=new byte[end-start];
        System.arraycopy(data, start, this.data, 0, end-start);
    }

    public UTF8String(byte[] data) {
        this(data, 0, data.length);
    }

    public UTF8String(char[] chars, int start, int end) {
        data= new byte[(int)bytesLen(chars, start, end)];
        bytes(chars, start, end, data, 0, data.length);
    }

    public UTF8String(CharSequence str, int start, int end) {
        data= new byte[(int)bytesLen(str, start, end)];
        bytes(str, start, end, data, 0, data.length);
    }

    @Override
    public int length() {
        return (int) idxAt(data, 0, data.length);
    }

    @Override
    public char charAt(int index) {
        return (char) charAt(data, posAt(data, 0, index));
    }

    @Override
    public UTF8String subSequence(int start, int end) {
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
            return compare(data, 0, data.length, that)==0;
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
        char[] c = new char[(int) idxAt(data, 0, data.length)];
        chars(c, 0, c.length, data, 0, data.length);
        return new String(c);
    }

    public byte[] getBytes() {
        return data.clone();
    }

    public int indexOf(int chr) {
        return indexOf(chr,0);
    }

    public int indexOf(int chr, int pos) {
        long beg=posAt(data, 0, pos);
        long idx=indexOf(data, beg, data.length, chr);
        return idx<0 ? -1 : (int) idx+pos;
    }

    public int indexOf(CharSequence str) {
        return indexOf(str, 0);
    }

    public int indexOf(CharSequence str, int pos) {
        long beg=posAt(data, 0, pos);
        long idx=indexOf(data, beg, data.length, str);
        return idx<0 ? -1 : (int) idx+pos;
    }



    /**********************************************************************************
     **  Static methods operating on UTF8 byte arrays
     **/

    public static long idxAt(byte[] dat, long beg, long end) {
        int p = (int)beg;
        int i=0;
        while(p<end) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    i ++;
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    i ++;
                    p ++;
                }
                else if(b<0xF0) {
                    i ++;
                    p += 2;
                }
                else if(b<0xF8) {
                    i ++;
                    p += 3;
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException("Character index "+i+" out of data bounds "+p);
            }
        }
        return i;
    }

    public static long posAt(byte[] dat, long beg, long idx) {
        int p=(int) beg;
        long i=0;
        while(i<idx) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    i ++;
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    i ++;
                    p ++;
                }
                else if(b<0xF0) {
                    i ++;
                    p += 2;
                }
                else if(b<0xF8) {
                    i ++;
                    p += 3;
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException("Character index "+idx+" out of data bounds "+p);
            }
        }
        return p;
    }

    public static int charAt(byte[] dat, long beg) {
        int p=(int) beg;
        try {
            int b=0xFF&dat[p++];
            if(b<0x80) {
                return b&0x7F;
            }
            else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
            else if(b<0xE0) {
                return ((b&0x1F)<<6)|(dat[p++]&0x3F);
            }
            else if(b<0xF0) {
                return ((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F);
            }
            else if(b<0xF8) {
                return ((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F);
            }
            else { throw new DecodingException("Incomplete Unicode sequence"); }
        }
        catch(ArrayIndexOutOfBoundsException e) {
            throw new DecodingException("Incomplete Unicode sequence");
        }
    }

    public static long indexOf(byte[] dat, long beg, long end, int chr) {
        int p=(int) beg;
        long i=0;
        while(p<end) {
            try {
                int b=0xFF&dat[p++];
                if(b<0x80) {
                    if(chr == (b&0x7F)) return i;
                    i ++;
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    if(chr == (((b&0x1F)<<6)|(dat[p++]&0x3F))) return i;
                    i ++;
                }
                else if(b<0xF0) {
                    if(chr == (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F))) return i;
                    i ++;
                }
                else if(b<0xF8) {
                    if(chr == (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F))) return i;
                    i ++;
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new IndexOutOfBoundsException("Character index out of data bounds "+p);
            }
        }
        return -1L;
    }


//    public static long compare(byte[] dat, long pos, long len, char[] str, long cpos, long clen) {
//        int p = (int) pos;
//        int q = (int) cpos;
//        clen += cpos;
//        while(p<len && q<clen) {
//            try {
//                int b=0xFF&dat[p++];
//                if(b<0x80) {
//                    int d = str[q++] - (b&0x7F);
//                    if(d!=0) return d;
//                }
//                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
//                else if(b<0xE0) {
//                    int d = str[q++] - (((b&0x1F)<<6)|(dat[p++]&0x3F));
//                    if(d!=0) return d;
//                }
//                else if(b<0xF0) {
//                    int d = str[q++] - (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
//                    if(d!=0) return d;
//                }
//                else if(b<0xF8) {
//                    int d = str[q++] - (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
//                    if(d!=0) return d;
//                }
//                else { throw new DecodingException("Incomplete Unicode sequence"); }
//            }
//            catch(ArrayIndexOutOfBoundsException e) {
//                throw new DecodingException("Incomplete Unicode sequence")
//            }
//        }
//        return (clen-q) - (len-p);
//    }

    public static long compare(byte[] dat, long pos, long len, CharSequence str) {
        int p = (int) pos;
        int q = 0;
        int clen=str.length();
        while(p<len && q<clen) {
            try {
                int b=0xFF&dat[p++];
                int d=0;
                int c=str.charAt(q++);
                if(b<0x80) {
                    d = c - (b&0x7F);
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    d = c - (((b&0x1F)<<6)|(dat[p++]&0x3F));
                }
                else if(b<0xF0) {
                    d = c - (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                }
                else if(b<0xF8) {
                    d = c - (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
                if(d!=0) return d;
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException("Incomplete Unicode sequence");
            }
        }
        return (clen-q) - (len-p);
    }

    public static long indexOf(byte[] dat, long beg, long end, CharSequence str) {
        int p=(int) beg;
        long i=0;
        int clen=str.length();
        match: while(p<end) {
            int q=0;
            int c=str.charAt(q++);
            try {
                int b=0xFF&dat[p++];
                int d=0;
                if(b<0x80) {
                    d = c - (b&0x7F);
                }
                else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                else if(b<0xE0) {
                    d = c - (((b&0x1F)<<6)|(dat[p++]&0x3F));
                }
                else if(b<0xF0) {
                    d = c - (((b&0x0F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                }
                else if(b<0xF8) {
                    d = c - (((b&0x07)<<18)|((dat[p++]&0x3F)<<12)|((dat[p++]&0x3F)<<6)|(dat[p++]&0x3F));
                }
                else { throw new DecodingException("Incomplete Unicode sequence"); }
                if(d!=0) { i++; continue match; }
            }
            catch(ArrayIndexOutOfBoundsException e) {
                throw new DecodingException("Incomplete Unicode sequence");
            }
            int pq = p;
            while(pq<clen) {
                // Too few chars remaining? not found
                if(pq>=end) return -1L;
                c = str.charAt(q++);
                try {
                    int b=0xFF&dat[pq++];
                    int d=0;
                    if(b<0x80) {
                        d = c - (b&0x7F);
                    }
                    else if(b<0xC0) { throw new DecodingException("Incomplete Unicode sequence"); }
                    else if(b<0xE0) {
                        d = c - (((b&0x1F)<<6)|(dat[pq++]&0x3F));
                    }
                    else if(b<0xF0) {
                        d = c - (((b&0x0F)<<12)|((dat[pq++]&0x3F)<<6)|(dat[pq++]&0x3F));
                    }
                    else if(b<0xF8) {
                        d = c - (((b&0x07)<<18)|((dat[pq++]&0x3F)<<12)|((dat[pq++]&0x3F)<<6)|(dat[pq++]&0x3F));
                    }
                    else { throw new DecodingException("Incomplete Unicode sequence"); }
                    if(d!=0) { i++; continue match; }
                }
                catch(ArrayIndexOutOfBoundsException e) {
                    throw new DecodingException("Incomplete Unicode sequence");
                }
            }
            return i;
        }
        return -1L;
    }


    /**********************************************************************************
     **  Static methods operating on char arrays
     **/

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

    public static long bytesLen(CharSequence obj, long cpos, long clen) {
        int len=0;
        for(int i=(int)cpos;i<clen;i++) {
            char c=obj.charAt(i);
            if(c<0x80) len++;
            else if(c<0x800) len+=2;
            else if(c<0x10000) len+=3;
            else clen+=4;
        }
        return len;
    }

    public static long bytes(CharSequence obj, long cpos, long clen, byte[] dat, long pos, long len) {
        try {
            int p=(int) pos;
            int i=(int) cpos;
            while(i<clen) {
                if(p-pos>=len) return p-pos;
                char c=obj.charAt(i++);
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

}
