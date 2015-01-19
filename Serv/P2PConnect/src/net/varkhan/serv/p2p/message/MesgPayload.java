package net.varkhan.serv.p2p.message;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/23/11
 * @time 11:00 PM
 */
public interface MesgPayload {


    /**********************************************************************************
     **  Message flags
     **/

    /**
     * Gets the message flags array.
     *
     * @return an integer whose bits reflect the state of the message flags
     */
    public long flags();

    /**
     * Gets the values of a message flag.
     *
     * @param f the flag to retrieve
     *
     * @return {@code true} if the bit of order {@code f.ordinal()} in the flag array is on,
     * {@code false} if that bit is not set
     */
    public boolean getFlag(int f);

    /**
     * Set the message flags
     *
     * @param flags an integer whose bits will set the corresponding message flags in the same state
     */
    public void setFlags(long flags);

    /**
     * Set the message flags on and off
     *
     * @param on  an integer whose <b>on</b> bits will set the corresponding flags <b>on</b>
     * @param off an integer whose <b>on</b> bits will set the corresponding flags <b>off</b>
     */
    public void setFlags(long on, long off);

    /**
     * Set the value of one or several message flags
     *
     * @param set   if {@code true}, the designated flags will be set <b>on</b>; if {@code false}, the designated flags will be set <b>off</b>
     * @param flags the positions of the flags to set
     */
    public void setFlags(boolean set, int ... flags);



    /**********************************************************************************
     **  Message data
     **/

//    /**
//     * Set the message data.
//     *
//     * @param data a byte array containing the message data
//     */
//    public void setData(byte[] data);
//
//    /**
//     * Set the message data.
//     *
//     * @param data a byte array containing the message data
//     * @param beg  the start position of the data segment in the array
//     * @param len  the length of the data segment
//     */
//    public void setData(byte[] data, int beg, int len);
//
//    /**
//     * Set the message data.
//     *
//     * @param data a byte buffer, whose data between {@link ByteBuffer#position()}
//     * and {@link ByteBuffer#limit()} will be transferred to the message
//     */
//    public void setData(ByteBuffer data);
//
//    /**
//     * Set the message data.
//     *
//     * @param data an input stream containing the message data
//     */
//    public void setData(InputStream data);

    /**
     * Get the total length of the message.
     *
     * @return the length of the byte array that would be returned by {@link #getDataAsArray()}
     * @throws java.io.IOException if the underlying data source produced this exception
     */
    public long getLength() throws IOException ;

    /**
     * Return the whole message data, as an array.
     *
     * @return a byte array containing the message data
     * @throws java.io.IOException if the underlying data source produced this exception
     */
    public byte[] getDataAsArray() throws IOException;

    /**
     * Read a portion of the message data.
     *
     * @param beg the start position of the data segment
     * @param buf a byte array that will receive the message data
     * @param pos the position in the byte array to start writing at
     * @param len the length of the data segment
     *
     * @return the number of bytes read, less than or equal to {@code len}, or {@code -1} if an error occurred
     * @throws IOException if the underlying data source produced this exception
     */
    public long readData(int beg, byte[] buf, int pos, int len) throws IOException;

    /**
     * Read a portion of the message data.
     *
     * @param beg the start position of the data segment
     * @param stm an output stream to write to
     * @param len the length of the data segment
     *
     * @return the number of bytes read, less than or equal to {@code len}, or {@code -1} if an error occurred
     * @throws IOException if the underlying data source produced this exception
     */
    public long readData(int beg, OutputStream stm, int len) throws IOException;

    /**
     * Return the message data, as a stream.
     *
     * @return data an intput stream containing the message data
     * @throws IOException if the underlying data source produced this exception
     */
    public InputStream getDataAsStream() throws IOException;


}
