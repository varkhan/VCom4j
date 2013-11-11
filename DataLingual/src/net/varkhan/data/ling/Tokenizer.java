package net.varkhan.data.ling;

import net.varkhan.base.functor.Mapper;


/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 3:47 PM
 */
public interface Tokenizer<T, S, C> extends Mapper<Tokenizer.Tokens<T>,S,C> {

    public static interface Tokens<T> extends Iterable<T> {

    }

    public Tokens<T> invoke(S src, C ctx);

}
