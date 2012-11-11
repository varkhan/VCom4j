package net.varkhan.base.functor.reducer;

import net.varkhan.base.functor.Inspector;
import net.varkhan.base.functor.Reducer;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/21/12
 * @time 11:42 PM
 */
public class InspectorReducer<A extends Inspector<A,C>,C> implements Reducer<A,C> {

    public A invoke(A larg, A rarg, C ctx) {
        larg.invoke(rarg, ctx);
        return larg;
    }

}
