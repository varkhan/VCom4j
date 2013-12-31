package net.varkhan.core.pres.format;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/29/13
 * @time 5:47 PM
 */
public class RowFormatter extends CheckedFormatter {
    protected final char[] ors;
    protected final char[] crs;

    public RowFormatter(Appendable out, char[] crs, char[] ors) {
        super(out);
        this.crs=crs;
        this.ors=ors;
    }

    public char[] getOpenRow() {
        return ors.clone();
    }

    public char[] getCloseRow() {
        return crs.clone();
    }

    public RowFormatter openRow() throws IOException { append(ors); return this; }

    public RowFormatter closeRow() throws IOException { append(crs); return this; }

    public RowFormatter row(char[] row) throws IOException { openRow(); append(row); closeRow(); return this; }

    public RowFormatter row(CharSequence row) throws IOException { openRow(); append(row); closeRow(); return this; }

    public CheckedFormatter rows(CharSequence... rows) throws IOException {
        if(rows!=null) for(CharSequence s: rows) if(s!=null) { openRow(); append(s); closeRow(); }
        return this;
    }

    public CheckedFormatter rows(CharSequence[]... rows) throws IOException {
        if(rows!=null) for(CharSequence[] ss: rows) if(ss!=null) for(CharSequence s: ss) if(s!=null) { openRow(); append(s); closeRow(); }
        return this;
    }
}
