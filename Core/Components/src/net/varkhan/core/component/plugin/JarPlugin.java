package net.varkhan.core.component.plugin;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/12
 * @time 8:10 PM
 */
public class JarPlugin<T> extends CachedPlugin<T> {

    protected final File     source;
    protected final String   name;

    public JarPlugin(Class<T> target, File source, String classname) {
        super(target);
        this.source=source;
        this.name=classname;
        Update<T> result;
        try { result=fetch(); }
        catch (Exception e) { result=new Update<T>(null, e, System.currentTimeMillis()); }
        this.ref.set(result);
    }


    @Override
    protected long modified() {
        return source.lastModified();
    }

    protected Update<T> fetch() throws Exception {
        return new Update<T>(loadFromJar(target, source, name),null, source.lastModified());
    }

    @SuppressWarnings("unchecked")
    public static <T> T loadFromJar(Class<T> target, File source, String name) throws Exception {
        // We need to copy the jar file to a temp file with a random name,
        // otherwise the JVM will continue referencing the old file in the class loader hierarchy
        File temp = File.createTempFile("tmp--" + target.getCanonicalName() + "--" + System.nanoTime(), null);
        copyTo(source, temp);
        Object instance;
        try {
            ClassLoader classLoader = new URLClassLoader(new URL[] {source.toURI().toURL()});
            Class clazz = classLoader.loadClass(name);
            instance = clazz.newInstance();
        }
        catch (MalformedURLException x) {
            throw new IllegalArgumentException("Invalid JAR file",x);
        }
        catch (InstantiationException e) {
            throw new ClassNotFoundException("Failed class instantiation",e);
        }
        catch (IllegalAccessException e) {
            throw new ClassNotFoundException("Illegal class reference",e);
        }
        finally {
            temp.deleteOnExit();
            temp.delete();
        }
        // Check type before casting
        if (target.isAssignableFrom(instance.getClass())) return (T) instance;
        throw new ClassCastException("Invalid class type "+instance.getClass().getName());
    }

    private static void copyTo(File src, File dst) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(src);
            os = new FileOutputStream(dst);
            byte[] buf = new byte[8192];
            int r = is.read(buf, 0, buf.length);
            while (r >= 0) {
                os.write(buf, 0, r);
                r = is.read(buf, 0, buf.length);
            }
        } finally {
            if (is != null) is.close();
            if (os != null) os.close();
        }
    }

}
