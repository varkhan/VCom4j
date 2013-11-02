/**
 *
 */
package net.varkhan.base.containers.array;

import java.io.IOException;


/**
 * @author varkhan
 * @date Nov 15, 2010
 * @time 12:09:54 AM
 */
public class StringArrays {


    /**
     * Private empty constructor that forbids instantiation of this class.
     */
    protected StringArrays() {
    }


    /*********************************************************************************
     **  Boolean arrays
     **/

    /**
     * Builds a pretty string representation of a boolean array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the boolean array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, boolean[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(array[i] ? "1" : "0");
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of a boolean array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the boolean array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, boolean[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(array[i] ? "1" : "0");
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of a boolean array.
     *
     * @param array the boolean array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(boolean[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of a boolean array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the boolean array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, boolean[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(array[i] ? "1" : "0");
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(array[i] ? "1" : "0");
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a boolean array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the boolean array to concatenate
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder join(StringBuilder buf, String sep, boolean[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(array[i] ? "1" : "0");
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(array[i] ? "1" : "0");
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a boolean array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the boolean array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static String join(String sep, boolean[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }


    /*********************************************************************************
     **  Byte arrays
     **/

    private static final char[] hexcode={ '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

    /**
     * Builds a pretty string representation of a byte array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the byte array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, byte[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(hexcode[(array[i]>>>8)&0xF]);
            buf.append(hexcode[array[i]&0xF]);
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of a byte array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the byte array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, byte[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(hexcode[(array[i]>>>8)&0xF]);
            buf.append(hexcode[array[i]&0xF]);
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of a byte array.
     *
     * @param array the byte array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(byte[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of a byte array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the byte array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, byte[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(hexcode[(array[i]>>>8)&0xF]);
            buf.append(hexcode[array[i]&0xF]);
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(hexcode[(array[i]>>>8)&0xF]);
            buf.append(hexcode[array[i]&0xF]);
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a byte array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the byte array to concatenate
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder join(StringBuilder buf, String sep, byte[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(hexcode[(array[i]>>>8)&0xF]);
            buf.append(hexcode[array[i]&0xF]);
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(hexcode[(array[i]>>>8)&0xF]);
            buf.append(hexcode[array[i]&0xF]);
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a byte array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the byte array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static String join(String sep, byte[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }


    /*********************************************************************************
     **  Short arrays
     **/

    /**
     * Builds a pretty string representation of a short array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the short array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, short[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Integer.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of a short array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the short array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, short[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Integer.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of a short array.
     *
     * @param array the short array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(short[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of a short array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the short array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, short[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Integer.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Integer.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a short array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the short array to concatenate
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder join(StringBuilder buf, String sep, short[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Integer.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Integer.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a short array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the short array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static String join(String sep, short[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }


    /*********************************************************************************
     **  Int arrays
     **/

    /**
     * Builds a pretty string representation of an int array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the int array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, int[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Integer.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of an int array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the int array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, int[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Integer.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of an int array.
     *
     * @param array the int array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(int[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of an int array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the int array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, int[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Integer.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Integer.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an int array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the int array to concatenate
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder join(StringBuilder buf, String sep, int[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Integer.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Integer.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an int array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the int array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static String join(String sep, int[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }


    /*********************************************************************************
     **  Long arrays
     **/

    /**
     * Builds a pretty string representation of a long array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the long array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, long[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Long.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of a long array.
     *
     * @param buf   the buffer to compose the string into
     * @param array the long array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, long[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(Long.toString(array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of a long array.
     *
     * @param array the long array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(long[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of a long array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the long array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, long[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Long.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Long.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a long array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the long array to concatenate
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder join(StringBuilder buf, String sep, long[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(Long.toString(array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(Long.toString(array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a long array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the long array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static String join(String sep, long[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }


    /*********************************************************************************
     **  Float arrays
     **/

    /**
     * Builds a pretty string representation of a float array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the float array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, float[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(String.format("%f", array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of a float array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the float array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, float[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(String.format("%f", array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of a float array.
     *
     * @param array the float array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(float[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of a float array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the float array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, float[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(String.format("%f", array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(String.format("%f", array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a float array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the float array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static StringBuilder join(StringBuilder buf, String sep, float[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(String.format("%f", array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(String.format("%f", array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a float array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the float array to concatenate
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String join(String sep, float[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }


    /*********************************************************************************
     **  Double arrays
     **/

    /**
     * Builds a pretty string representation of a double array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the double array to stringify
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A toString(A buf, double[] array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(String.format("%f", array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of a double array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the double array to stringify
     *
     * @return the original buffer, for chaining purposes
     */
    public static StringBuilder toString(StringBuilder buf, double[] array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            buf.append(String.format("%f", array[i]));
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of a double array.
     *
     * @param array the double array to stringify
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String toString(double[] array) {
        return toString(new StringBuilder(), array).toString();
    }

    /**
     * Appends as strings the elements of a double array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the double array to concatenate
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <A extends Appendable> A join(A buf, String sep, double[] array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(String.format("%f", array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(String.format("%f", array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a double array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the double array to concatenate
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static StringBuilder join(StringBuilder buf, String sep, double[] array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            buf.append(String.format("%f", array[i]));
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            buf.append(String.format("%f", array[i]));
        }
        return buf;
    }

    /**
     * Appends as strings the elements of a double array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the double array to concatenate
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static String join(String sep, double[] array) {
        return join(new StringBuilder(), sep, array).toString();
    }


    /*********************************************************************************
     **  Generic object arrays
     **/

    /**
     * Builds a pretty string representation of an array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the array to stringify
     * @param <T>   the element type
     * @param <A>   the buffer type
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <T,A extends Appendable> A toString(A buf, T... array) throws IOException {
        buf.append("[").append(Integer.toString(array.length)).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        buf.append("]");
        return buf;
    }

    /**
     * Builds a pretty string representation of an array.
     *
     * @param buf   the buffer to append the composed string to
     * @param array the array to stringify
     * @param <T>   the element type
     *
     * @return the original buffer, for chaining purposes
     */
    public static <T> StringBuilder toString(StringBuilder buf, T... array) {
        buf.append("[").append(array.length).append("|");
        for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(",");
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        buf.append("]");
        return buf;
    }

    /**
     * Returns a pretty string representation of an array.
     *
     * @param array the array to stringify
     * @param <T>   the element type
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static <T> String toString(T... array) {
        StringBuilder buf=new StringBuilder();
        return toString(buf, array).toString();
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the array to concatenate
     * @param <T>   the element type
     * @param <A>   the buffer type
     *
     * @return the original buffer, for chaining purposes
     *
     * @throws IOException if the output buffer raises this exception on {@code append()}
     */
    public static <T,A extends Appendable> A join(A buf, String sep, T... array) throws IOException {
        if(sep==null) for(int i=0;i<array.length;i++) {
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param buf   the buffer to append the composed string to
     * @param sep   the separator to use
     * @param array the array to concatenate
     * @param <T>   the element type
     *
     * @return a concatenation of the elements of the array, as string, and the separator
     */
    public static <T> StringBuilder join(StringBuilder buf, String sep, T... array) {
        if(sep==null) for(int i=0;i<array.length;i++) {
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        else for(int i=0;i<array.length;i++) {
            if(i>0) buf.append(sep);
            T t=array[i];
            if(t!=null) buf.append(t.toString());
        }
        return buf;
    }

    /**
     * Appends as strings the elements of an array, separating them with a given string.
     *
     * @param sep   the separator to use
     * @param array the array to concatenate
     * @param <T>   the element type
     *
     * @return a human-readable string exposing the contents of the array
     */
    public static <T> String join(String sep, T... array) {
        return join(new StringBuilder(), sep, array).toString();
    }

}
