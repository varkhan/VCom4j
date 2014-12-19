package net.varkhan.core.component.plugin;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/12
 * @time 8:07 PM
 */
public class StaticPlugin<T> implements Plugin<T> {
    protected final Class<T> target;
    protected final T        value;

    public StaticPlugin(Class<T> target, T value) {
        this.target=target;
        this.value=value;
    }

    @Override
    public Class<T> target() { return target; }

    @Override
    public T value() { return value; }

    @Override
    public Throwable failure() { return null; }

    @Override
    public boolean isUpdated() { return true; }

    @Override
    public boolean isAvailable() { return true; }

    @Override
    public void update() { }

    @Override
    public void updateIfAvailable() { }

}
