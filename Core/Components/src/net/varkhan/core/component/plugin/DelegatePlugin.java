package net.varkhan.core.component.plugin;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/12
 * @time 8:07 PM
 */
public class DelegatePlugin<T> implements Plugin<T> {
    protected final Plugin<T> plugin;

    public DelegatePlugin(Plugin<T> plugin) {
        this.plugin=plugin;
    }

    @Override
    public Class<T> target() { return plugin.target(); }

    @Override
    public T value() { return plugin.value(); }

    @Override
    public Throwable failure() { return plugin.failure(); }

    @Override
    public boolean isUpdated() { return plugin.isUpdated(); }

    @Override
    public boolean isAvailable() { return plugin.isAvailable(); }

    @Override
    public void update() { plugin.update(); }

    @Override
    public void updateIfAvailable() { plugin.updateIfAvailable(); }

}
