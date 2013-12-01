package net.varkhan.data.ling.tokenize;

import net.varkhan.base.functor.Expander;

import java.io.Reader;
import java.io.StringReader;

/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 4:12 PM
 */
public class StringReaderTokenizer<T,C> implements Expander<T,String,C> {
    protected final Expander<T,Reader,C> tkz;

    public StringReaderTokenizer(Expander<T, Reader, C> tkz) {
        this.tkz = tkz;
    }

    @Override
    public Iterable<T> invoke(String src, C ctx) {
        Reader rdr = new StringReader(src);
        return tkz.invoke(rdr, ctx);
    }

}
