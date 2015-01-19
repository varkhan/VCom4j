package net.varkhan.serv.p2p.message.protocol;

import net.varkhan.serv.p2p.message.MesgPayload;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/24/11
 * @time 1:06 AM
 */
public class BinaryEnvelope {


    protected byte type;
    protected long flags=0;
    protected String srcId;
    protected String dstId;
    protected String method;

    protected long sequence=-1;

    protected MesgPayload message;

    protected InputStream dataIS=null;
    //    protected OutputStream dataOS = null;
    protected byte[]      data  =null;
    protected int dataBeg, dataLen;

    private static final int msg_seqId_len=8;
    private static final int msg_flags_len=8;


    public static final byte CALL=1;
    public static final byte REPL=2;
    public static final byte PING=8;


    public BinaryEnvelope(byte[] data) throws IOException {
        this(data, 0, data.length);
    }

    public BinaryEnvelope(byte type, String src, String dst, String method, long sequence, MesgPayload message) {
        this.type = type;
        this.srcId = src;
        this.dstId = dst;
        this.method = method;
        this.sequence = sequence;
        this.message=message;
    }

    public BinaryEnvelope(byte[] data, int beg, int len) throws IOException {
        int hdr = readHeader(data, beg, len);
        BinaryPayload message = new BinaryPayload();
        message.setData(data,beg+hdr,len-hdr);
        this.message = message;
    }

    public BinaryEnvelope(InputStream data) throws IOException {
        readHeader(data);
        BinaryPayload message = new BinaryPayload();
        message.setData(data);
        this.message = message;
    }

    public byte type() {
        return type;
    }

    public void send(OutputStream out) throws IOException {
        writeHeader(out);
//    private void writeData(OutputStream data) throws IOException {
//        if(this.data!=null) {
//            data.write(this.data,this.dataBeg,this.dataLen);
//        }
//        if(this.dataIS!=null) {
//            byte[] buf = new byte[4096];
//            int len;
//            while((len=this.dataIS.read(buf)) != -1) {
//                data.write(buf,0,len);
//            }
//        }
//    }
        message.readData(0,out,Integer.MAX_VALUE);
    }


    public String method() { return method; }
    public long sequence() { return sequence; }
    public MesgPayload message() { return message; }
    public String srcId() { return srcId; }
    public String dstId() { return dstId; }


    /**********************************************************************************
     **  Message format parsing
     **/

    /**
     * Initializes a message from a data stream
     *
     * @param data the input stream containing the complete message data
     * @throws java.io.IOException on a data read error or an invalid message format
     */
    private void readHeader(InputStream data) throws IOException {
        byte[] buf = new byte[256];
        char[] str = new char[256];
        int cnt = 0;
        int b;
        // Read message type
        cnt = data.read();
        if(cnt==-1) throw new IOException("Invalid message format (type)");
        type = (byte)cnt;
        // Read message source
        cnt = data.read();
        if(cnt==-1) throw new IOException("Invalid message format (src)");
        b = data.read(buf,0,cnt);
        if(b<cnt) throw new IOException("Invalid message format (src)");
        for(int i=0; i<cnt; i++) str[i] = (char) (buf[i]&0x7F);
        this.srcId = String.valueOf(str,0,cnt);
        // Read message destination
        cnt = data.read();
        if(cnt==-1) throw new IOException("Invalid message format (dst)");
        b = data.read(buf,0,cnt);
        if(b<cnt) throw new IOException("Invalid message format (dst)");
        for(int i=0; i<cnt; i++) str[i] = (char) (buf[i]&0x7F);
        this.dstId = String.valueOf(str,0,cnt);
        // Read procedure id
        cnt = data.read();
        if(cnt==-1) throw new IOException("Invalid message format (method)");
        b = data.read(buf,0,cnt);
        if(b<cnt) throw new IOException("Invalid message format (method)");
        for(int i=0; i<cnt; i++) str[i] = (char) (buf[i]&0x7F);
        this.method= String.valueOf(str,0,cnt);
        // Read sequence id
        b = data.read(buf,0,msg_seqId_len);
        if(b<msg_seqId_len) throw new IOException("Invalid message format (sequence)");
        // Read N bytes, big-endian
        long seqId = 0;
        for(int i=(msg_seqId_len-1); i>=0; i--) seqId += (buf[i]&0x7F)<<(i<<3);
        this.sequence= seqId;
        // Read flags array
        b = data.read(buf,0,msg_seqId_len);
        if(b<msg_flags_len) throw new IOException("Invalid message format (sequence)");
        // Read N bytes, big-endian
        long flags = 0;
        for(int i=(msg_flags_len-1); i>=0; i--) flags += (buf[i]&0xFF)<<(i<<3);
        this.flags = flags;
    }


    /**
     * Initializes a message from a byte array
     *
     * @param data the byte array containing the message data
     * @param beg  the start position of message data in the array
     * @param len  the length (number of bytes) of message data available
     * @return the number of bytes read from the byte array
     * @throws java.io.IOException on a data read error or an invalid message format
     */
    private int readHeader(byte[] data, int beg, int len) throws IOException {
        char[] str = new char[256];
        int pos = beg;
        int end = len + beg;
        int cnt;
        // Read message type
        if(pos>=end) throw new IOException("Invalid message format (type)");
        cnt = data[pos++]&0xFF;
        type = (byte)cnt;
        // Read message source
        if(pos>=end) throw new IOException("Invalid message format (src)");
        cnt = data[pos++]&0xFF;
        if(pos+cnt>end) throw new IOException("Invalid message format (src)");
        for(int i=0; i<cnt; i++, pos++) str[i] = (char) (data[pos]&0x7F);
        this.srcId = String.valueOf(str,0,cnt);
        // Read message destination
        if(pos>=end) throw new IOException("Invalid message format (dst)");
        cnt = data[pos++]&0xFF;
        if(pos+cnt>end) throw new IOException("Invalid message format (dst)");
        for(int i=0; i<cnt; i++, pos++) str[i] = (char) (data[pos]&0x7F);
        this.dstId = String.valueOf(str,0,cnt);
        // Read procedure id
        if(pos>=end) throw new IOException("Invalid message format (method)");
        cnt = data[pos++]&0xFF;
        if(pos+cnt>end) throw new IOException("Invalid message format (method)");
        for(int i=0; i<cnt; i++, pos++) str[i] = (char) (data[pos]&0x7F);
        this.method= String.valueOf(str,0,cnt);
        // Read sequence id
        if(pos+msg_seqId_len>end) throw new IOException("Invalid message format (sequence)");
        // Read N bytes, big-endian
        long seqId = 0;
        for(int i=(msg_seqId_len-1); i>=0; i--) seqId += (data[pos+i]&0xFF)<<(i<<3);
        this.sequence= seqId;
        pos += msg_seqId_len;
        // Read flags array
        if(pos+msg_seqId_len>end) throw new IOException("Invalid message format (sequence)");
        // Read N bytes, big-endian
        long flags = 0;
        for(int i=(msg_flags_len-1); i>=0; i--) flags += (data[pos+i]&0xFF)<<(i<<3);

        this.flags = flags;
        pos += msg_flags_len;
        return pos-beg;
    }

    private int headerLength() {
        return 1+1+srcId.length()+1+dstId.length()+1+method.length()+msg_seqId_len+msg_flags_len;
    }

    private void writeHeader(OutputStream data) throws IOException {
        byte[] buf = new byte[headerLength()];
        int pos = 0;
        // Write message type
        buf[pos++] = type;
        // Write message source
        buf[pos++] = (byte) srcId.length();
        for(int i=0; i<srcId.length(); i++, pos++) buf[pos] = (byte) (0x7F & srcId.charAt(i));
        // Write message destination
        buf[pos++] = (byte) dstId.length();
        for(int i=0; i<dstId.length(); i++, pos++) buf[pos] = (byte) (0x7F & dstId.charAt(i));
        // Write procedure id
        buf[pos++] = (byte) method.length();
        for(int i=0; i<method.length(); i++, pos++) buf[pos] = (byte) (0x7F & method.charAt(i));
        // Write sequence id, big-endian
        for(int i=0; i<msg_seqId_len; i++) buf[pos+i] = (byte) ((this.sequence>>>(i<<3))&0xFF);
        pos += msg_seqId_len;
        // Write flags array, big-endian
        for(int i=0; i<msg_flags_len; i++) buf[pos+i] = (byte) ((this.flags>>>(i<<3))&0xFF);
        pos += msg_flags_len;
        data.write(buf,0,pos);
    }

//    private int writeHeader(byte[] buf, int beg, int len) throws IOException {
//    	int pos = beg;
//    	// Write message type
//    	buf[pos++] = (byte) type.ordinal();
//    	// Write message source
//    	buf[pos++] = (byte) srcId.length();
//    	for(int i=0; i<srcId.length(); i++, pos++) buf[pos] = (byte) (0x7F & srcId.charAt(i));
//    	// Write message destination
//    	buf[pos++] = (byte) dstId.length();
//    	for(int i=0; i<dstId.length(); i++, pos++) buf[pos] = (byte) (0x7F & dstId.charAt(i));
//    	// Write procedure id
//    	buf[pos++] = (byte) rpcId.length();
//    	for(int i=0; i<rpcId.length(); i++, pos++) buf[pos] = (byte) (0x7F & rpcId.charAt(i));
//    	// Write sequence id, big-endian
//        for(int i=0; i<msg_seqId_len; i++) buf[pos+i] = (byte) ((this.seqId>>>(i<<3))&0xFF);
//        pos += msg_seqId_len;
//    	// Write flags array, big-endian
//        for(int i=0; i<msg_flags_len; i++) buf[pos+i] = (byte) ((this.flags>>>(i<<3))&0xFF);
//        pos += msg_flags_len;
//        return pos-beg;
//    }

//    private void writeData(OutputStream data) throws IOException {
//        if(this.data!=null) {
//            data.write(this.data,this.dataBeg,this.dataLen);
//        }
//        if(this.dataIS!=null) {
//            byte[] buf = new byte[4096];
//            int len;
//            while((len=this.dataIS.read(buf)) != -1) {
//                data.write(buf,0,len);
//            }
//        }
//    }

//    private int writeData(byte[] buf, int beg, int len) throws IOException {
//    	int pos = beg;
//    	if(this.data!=null) {
//    		System.arraycopy(data,dataBeg,buf,pos,dataLen);
//    		pos += dataLen;
//    	}
//        if(this.dataIS!=null) {
//            int cnt;
//            while((cnt=this.dataIS.read(buf,pos,len)) != -1) {
//                pos += cnt;
//            }
//        }
//        return pos-beg;
//    }


}
