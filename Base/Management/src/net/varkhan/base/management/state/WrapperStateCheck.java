package net.varkhan.base.management.state;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:43 PM
 */
public abstract class WrapperStateCheck<L extends Level,S extends State<L,S>> implements StateCheck<L,S> {
    protected final StateCheck<L,S> check;

    protected WrapperStateCheck(StateCheck<L,S> check) { this.check=check; }

    public String name() { return check.name(); }

    public String desc() { return check.desc(); }

    public L level() { return check.level(); }

    public abstract S state();

    public abstract String reason();

    public abstract void update();

}
