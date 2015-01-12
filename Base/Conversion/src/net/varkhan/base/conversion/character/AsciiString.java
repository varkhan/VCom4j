package net.varkhan.base.conversion.character;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/11/15
 * @time 4:05 PM
 */
public class AsciiString implements CharSequence, java.io.Serializable {
    protected final byte[] data;

    public AsciiString() {
        data = new byte[0];
    }

    public AsciiString(byte[] data, int start, int end) {
        this(data, start, end, true);
    }

    public AsciiString(byte[] data, int start, int end, boolean squash) {
        this(data, start, end, squash?0x7F:0xFF);
    }

    protected AsciiString(byte[] data, int start, int end, int mask) {
        this.data= new byte[end-start];
        for(int i=start,j=0; i<end; i++, j++) {
            this.data[j] = (byte)(mask&data[i]);
        }
    }

    public AsciiString(byte[] data) {
        this(data, 0, data.length);
    }

    public AsciiString(char[] chars) {
        this(chars, 0, chars.length);
    }

    public AsciiString(char[] chars, int start, int end) {
        this(chars, start, end, true);
    }

    public AsciiString(char[] chars, int start, int end, boolean squash) {
        this(chars, start, end, squash?0x7F:0xFF);
    }

    protected AsciiString(char[] chars, int start, int end, int mask) {
        this.data= new byte[end-start];
        for(int i=start,j=0; i<end; i++, j++) {
            this.data[j] = (byte)(mask&chars[i]);
        }
    }

    public AsciiString(CharSequence str) {
        this(str,0,str.length(),0x7F);
    }

    public AsciiString(CharSequence str, boolean squash) {
        this(str,0,str.length(), squash?0x7F:0xFF);
    }

    protected AsciiString(CharSequence str, int start, int end, int mask) {
        this.data= new byte[end-start];
        for(int i=start,j=0; i<end; i++, j++) {
            this.data[j] = (byte)(mask&str.charAt(i));
        }
    }

    @Override
    public int length() {
        return data.length;
    }

    @Override
    public char charAt(int index) {
        return (char)(0xFF&data[index]);
    }

    @Override
    public AsciiString subSequence(int start, int end) {
        return new AsciiString(data, start, end, 0xFF);
    }

    @Override
    public boolean equals(Object o) {
        if(this==o) return true;
        if(!(o instanceof CharSequence)) return false;
        CharSequence that=(CharSequence) o;
        if(that.length()!=data.length) return false;
        for(int i=0; i<data.length; i++) {
            if(that.charAt(i)!=(0xFF&data[i])) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int h = 0;
        for(byte c: data) h=31*h+c;
        return h;
    }

    @Override
    public String toString() {
        char[] s = new char[data.length];
        for(int i=0; i<data.length; i++) s[i] = (char)(0xFF&data[i]);
        return new String(s);
    }

    public byte[] getBytes() {
        return data.clone();
    }

    public int indexOf(int chr) {
        return indexOf(chr,0);
    }

    public int indexOf(int chr, int pos) {
        for(int i=pos; i<data.length; i++) if((0xFF&data[i])==chr) return i;
        return -1;
    }

    public int indexOf(CharSequence str) {
        return indexOf(str, 0);
    }

    public int indexOf(CharSequence str, int pos) {
        final int clen = str.length();
        match: for(int i=pos; i<data.length; i++) {
            for(int p=0; p<clen; p++) {
                if(i+p>data.length) return -1;
                if((char)(0xFF&data[i+p])!=str.charAt(p)) continue match;
            }
            return i;
        }
        return -1;
    }


}
