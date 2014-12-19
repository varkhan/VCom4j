package net.varkhan.core.component.plugin;

import java.io.File;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/12
 * @time 8:09 PM
 */
public class ProxyPlugin<T> extends JarPlugin<T> implements Plugin<T> {
    public ProxyPlugin(Class<T> target, File source, String name) {
        super(target, source, name);
    }

    @SuppressWarnings("unchecked")
    public T value() {
        return (T) java.lang.reflect.Proxy.newProxyInstance(ClassLoader.getSystemClassLoader(), new Class[]{target}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                final Update<T> pl = ref.get();
                if(pl.failure !=null) throw pl.failure;
                return method.invoke(pl.instance, args);
            }
        });
    }


}
