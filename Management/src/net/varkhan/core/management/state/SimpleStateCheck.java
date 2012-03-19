package net.varkhan.core.management.state;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:43 PM
 */
public abstract class SimpleStateCheck<L,S extends State<L,S>> implements StateCheck<L,S> {
    protected final String name;
    protected final String desc;
    protected final L level;

    protected SimpleStateCheck(String name, String desc, L level) { this.name=name; this.desc=desc; this.level=level; }

    public String name() { return name; }

    public String desc() { return desc; }

    public L level() { return level; }

    public abstract S state();

    public abstract String reason();

    public abstract void update();

}
