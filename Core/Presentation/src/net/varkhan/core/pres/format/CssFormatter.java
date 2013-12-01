package net.varkhan.core.pres.format;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 10:10 PM
 */
public class CssFormatter extends CheckedFormatter {

    public CssFormatter(Appendable out) {
        super(out);
    }

    public CssFormatter style(String sel, Object... atr) throws IOException {
        append(sel).append("{\n");
        writeAttr(this,atr);
        append("}\n");
        return this;
    }

    public static void writeAttr(Appendable out, Object[]... atr) throws IOException, NullPointerException, IllegalArgumentException {
        if(atr!=null) for(Object[] at : atr) {
            if(at!=null) {
                // Check that we have matched name = value pairs
                if((at.length&1)!=0)
                    throw new IllegalArgumentException("Attribute list must contain an even number of elements, as { name, value } pairs");
                for(int i=0;i+1<at.length;i+=2) {
                    Object atn=at[i];
                    // Specifically trap the null case
                    if(atn==null) throw new NullPointerException("Attribute names must not be null");
                    // Must be a CharSequence (this also traps the null case)
                    if(!(atn instanceof CharSequence))
                        throw new IllegalArgumentException("Attribute names must be assignable to CharSequence");
                    // Suppress attributes with null values
                    Object ato=at[i+1];
                    if(ato!=null) {
                        out.append('\t').append((CharSequence) atn).append(':');
                        String atv=ato.toString();
                        // Suppress equal sign for empty attributes
                        if(atv!=null&&!atv.isEmpty()) {
                            out.append(' ').append(atv).append(';').append('\n');
                        }
                    }
                }
            }
        }
    }

}
