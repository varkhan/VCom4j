package net.varkhan.base.management.state;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/5/11
 * @time 4:05 AM
 */
public interface StateAware<L extends Level,S extends State<L,S>> {

    public S state();

}
