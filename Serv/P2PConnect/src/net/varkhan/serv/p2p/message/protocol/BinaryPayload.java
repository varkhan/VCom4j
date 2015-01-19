/**
 *
 */
package net.varkhan.serv.p2p.message.protocol;

import net.varkhan.serv.p2p.message.MesgPayload;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Arrays;


/**
 * <b>.</b>
 * <p/>
 * @author varkhan
 * @date Nov 12, 2009
 * @time 4:15:44 AM
 */
public class BinaryPayload implements MesgPayload {

    protected long flags;

    protected InputStream dataIS = null;
    //    protected OutputStream dataOS = null;
    protected byte[] data = null;
    protected int dataBeg, dataLen;


    public BinaryPayload() { }


    /**********************************************************************************
     **  Transport information
     **/

    public final long flags() { return flags; }
    public final boolean getFlag(int f) { return (this.flags & (1<<f)) != 0; }

    public final void setFlags(long flags) { this.flags = flags; }

    public final void setFlags(long on, long off) { this.flags = (flags | on) & ~off; }

    public final void setFlags(boolean set, int ... flags) {
        if(set) for(int f: flags) {
            this.flags |= 1<<f;
        }
        else for(int f: flags) {
            this.flags &= ~(1<<f);
        }
    }

    public void setData(byte[] data) {
        setData(data,0,data.length);
    }

    public void setData(byte[] data, int beg, int len) {
        this.dataIS = null;
        this.data = Arrays.copyOfRange(data,beg,beg+len);
        this.dataBeg = 0;
        this.dataLen = len;
    }

    public void setData(InputStream data) {
        this.data = null;
        this.dataIS = data;
    }

    public void setData(ByteBuffer data) {
        this.dataIS = null;
        this.dataBeg = 0;
        this.dataLen = data.limit()-data.position();
        this.data = new byte[this.dataLen];
        data.get(this.data);
    }

    /**
     * Read data in a memory buffer, if not already done
     *
     * @throws java.io.IOException on a data read error
     */
    private void flushInput() throws IOException {
        if(this.dataIS!=null) {
            ByteArrayOutputStream data = new ByteArrayOutputStream();
            if(this.data!=null) data.write(this.data,this.dataBeg,this.dataLen);
            byte[] buf = new byte[4096];
            int len;
            while((len=this.dataIS.read(buf)) != -1) {
                data.write(buf,0,len);
            }
            this.data = data.toByteArray();
            this.dataBeg = 0;
            this.dataLen = data.size();
            this.dataIS = null;
        }
    }

    public long getLength() throws IOException { flushInput(); return dataLen; }

    public byte[] getDataAsArray() throws IOException { flushInput(); return Arrays.copyOfRange(data,dataBeg,dataBeg+dataLen); }

    public long readData(int beg, byte[] buf, int pos, int len) throws IOException {
        flushInput();
        if(beg+len>dataLen) len=dataLen-beg;
        System.arraycopy(data,dataBeg+beg,buf,pos,len);
        return len;
    }

    public long readData(int beg, OutputStream out, int len) throws IOException {
        int l = 0;
        if(this.data!=null) {
            l = len;
            if(beg+l>dataLen) l=dataLen-beg;
            out.write(this.data,this.dataBeg+beg,l);
        }
        if(this.dataIS!=null) {
            byte[] buf = new byte[4096];
            int r;
            while(l<len && (r=this.dataIS.read(buf)) != -1) {
                out.write(buf,0,r);
                l += r;
            }
        }
        return l;
    }

    public InputStream getDataAsStream() throws IOException {
        if(this.dataIS!=null && data==null) return this.dataIS;
        return new ByteArrayInputStream(data,dataBeg,dataLen);
    }



}
