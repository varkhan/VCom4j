package net.varkhan.base.management.state;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:43 PM
 */
public abstract class WrapperStateCheck<L,S extends State<L,S>> implements StateCheck<L,S> {
    protected final StateCheck<L,S> hc;

    protected WrapperStateCheck(StateCheck<L,S> hc) { this.hc=hc; }

    public String name() { return hc.name(); }

    public String desc() { return hc.desc(); }

    public L level() { return hc.level(); }

    public abstract S state();

    public abstract String reason();

    public abstract void update();

}
