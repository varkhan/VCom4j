package net.varkhan.base.management.state;

import java.util.Collection;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:40 PM
 */
public interface StateReport<L extends Level,S extends State<L,S>> extends StateAware<L,S> {

    public S state();

    public Collection<StateCheck<L,S>> checks();

    public void update();

}
