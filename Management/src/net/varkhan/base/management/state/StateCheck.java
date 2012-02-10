package net.varkhan.base.management.state;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:34 PM
 */
public interface StateCheck<L,S extends State<L,S>> extends StateMonitor<L,S> {

    public String name();

    public String desc();

    public L level();

    public S state();

    public String reason();

    public void update();

}
