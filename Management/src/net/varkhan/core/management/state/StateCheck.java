package net.varkhan.core.management.state;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:34 PM
 */
public interface StateCheck<L extends Level,S extends State<L,S>> extends StateAware<L,S> {

    public String name();

    public String desc();

    public L level();

    public S state();

    public String reason();

    public void update();

}
