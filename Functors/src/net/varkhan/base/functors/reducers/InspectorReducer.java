package net.varkhan.base.functors.reducers;

import net.varkhan.base.functors.Inspector;
import net.varkhan.base.functors.Reducer;


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
