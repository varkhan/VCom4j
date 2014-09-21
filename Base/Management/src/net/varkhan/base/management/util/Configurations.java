package net.varkhan.base.management.util;

import net.varkhan.base.management.config.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 9/20/14
 * @time 1:24 PM
 */
public class Configurations {

    /** Private constructor to enforce static method collection status */
    protected Configurations() { }

    public static final SettableConfiguration.Context environment;
    static {
        SettableConfiguration.Context env;
        try {
            env = new Environment("environment");
        }
        catch(Exception e) {
            try {
                env = new Environment2("environment");
            }
            catch(Exception e2) {
                env = new MapContext("environment",new HashMap<String,Object>());
            }
        }
        environment = env;
    }

    public static final Properties properties=new Properties("properties");

    protected static final CompoundConfiguration<SettableConfiguration.Context> configs=new CompoundConfiguration<SettableConfiguration.Context>(
            environment,
            properties
    );

    public static CompoundConfiguration<SettableConfiguration.Context> configs() {
        return configs;
    }


    /**
     * <b>A modifiable, settable implementation of the Environment context</b>.
     * <p/>
     */
    protected static class Environment implements SettableConfiguration.Context {

        protected final String             ctx;
        protected final Method             varConv;
        protected final Method             valConv;
        protected final Map<String,String> envMapUm;
        protected final Map<Object,Object> envMapCi;
        protected final Map<Object,Object> envMap;

        @SuppressWarnings("unchecked")
        protected Environment(String name) throws ClassNotFoundException, NoSuchFieldException, NoSuchMethodException, IllegalAccessException {
            ctx=name;
            envMapUm=System.getenv();
            Class<?> peClass=Class.forName("java.lang.ProcessEnvironment");
            varConv=Class.forName("java.lang.ProcessEnvironment$Variable").getMethod("valueOf", String.class);
            varConv.setAccessible(true);
            valConv=Class.forName("java.lang.ProcessEnvironment$Value").getMethod("valueOf", String.class);
            valConv.setAccessible(true);
            Field varsField;
            try {
                varsField=peClass.getDeclaredField("theEnvironment");
                varsField.setAccessible(true);
            }
            catch(NoSuchFieldException e) {
                varsField=null;
            }
            envMap=varsField==null ? null : (Map<Object,Object>) varsField.get(null);
            Field varsFieldCi;
            try {
                varsFieldCi=peClass.getDeclaredField("theCaseInsensitiveEnvironment");
                varsFieldCi.setAccessible(true);
            }
            catch(NoSuchFieldException e) {
                varsFieldCi=null;
            }
            envMapCi=varsFieldCi==null?null:(Map<Object,Object>) varsFieldCi.get(null);
            if(varsField==null&& varsFieldCi==null) throw new ClassNotFoundException("java.lang.ProcessEnvironment");
        }

        @Override
        public String name() {
            return ctx;
        }

        @Override
        public synchronized boolean has(String key) {
            return java.lang.System.getenv().containsKey(key);
        }

        @Override
        public synchronized String get(String var) {
            return java.lang.System.getenv().get(var);
        }

        @Override
        public synchronized Map<String, String> get() {
            return java.lang.System.getenv();
        }

        @Override
        public Iterator<Configuration.Entry> iterator() {
            return new Iterator<Configuration.Entry>() {
                Iterator<Map.Entry<Object,Object>> it = envMap.entrySet().iterator();
                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public Configuration.Entry next() {
                    final Map.Entry<Object,Object> e;
                    synchronized(Environment.this) { e = it.next(); }
                    return new Configuration.Entry() {
                        @Override
                        public String ctx() {
                            return name();
                        }

                        @Override
                        public String key() {
                            synchronized(Environment.this) { return e.getKey().toString(); }
                        }

                        @Override
                        public Object value() {
                            synchronized(Environment.this) { return e.getValue(); }
                        }
                    };
                }

                @Override
                public void remove() {
                    it.remove();
                }
            };
        }

        @Override
        public synchronized boolean add(String var, Object val) {
            if(var==null || val==null) return false;
            try {
                Object k=varConv.invoke(null, var);
                Object v=valConv.invoke(null, val);
                if(envMap!=null) envMap.put(k, v);
                if(envMapCi!=null) envMapCi.put(k, v);
                return true;
            }
            catch(Exception ex) {
                return false;
            }
        }

        @Override
        public synchronized boolean add(Configuration.Entry cfg) {
            return add(cfg.key(),cfg.value());
        }

        @Override
        public synchronized boolean add(Map<String, ?> env) {
            try {
                for(Map.Entry<String,?> e: env.entrySet()) {
                    Object var=varConv.invoke(null, e.getKey());
                    Object v=e.getValue();
                    if(v==null) {
                        if(envMap!=null) envMap.remove(var);
                        if(envMapCi!=null) envMapCi.remove(var);
                    }
                    else {
                        Object val=valConv.invoke(null, v.toString());
                        if(envMap!=null) envMap.put(var, val);
                        if(envMapCi!=null) envMapCi.put(var, val);
                    }
                }
                return true;
            }
            catch(Exception ex) {
                return false;
            }
        }

        @Override
        public synchronized boolean del(String var) {
            if(var==null) return false;
            try {
                Object k=varConv.invoke(null, var);
                boolean m = false;
                if(envMap!=null) m |= null!=envMapCi.remove(k);
                if(envMapCi!=null)  m |= null!=envMapCi.remove(k);
                return m;
            }
            catch(Exception ex) {
                return false;
            }
        }

    }

    /**
     * <b>A fallback implementation of Environment where the implementation doesn't have java.lang.ProcessEnvironment</b>.
     * <p/>
     */
    protected static class Environment2 implements SettableConfiguration.Context {

        protected final String ctx;
        protected final Field varsField;

        @SuppressWarnings("unchecked")
        protected Environment2(String name) throws ClassNotFoundException, NoSuchFieldException {
            ctx=name;
            varsField=Class.forName("java.util.Collections$UnmodifiableMap").getField("m");
            varsField.setAccessible(true);
        }

        @Override
        public String name() {
            return ctx;
        }

        @Override
        public synchronized boolean has(String key) {
            try {
                Map<String,String> envMapUm=java.lang.System.getenv();
                @SuppressWarnings("unchecked") Map<String,String> envMap=(Map<String,String>) varsField.get(envMapUm);
                return envMap.containsKey(key);
            }
            catch(Exception ex) {
                return false;
            }
        }

        public synchronized String get(String var) {
            return java.lang.System.getenv(var);
        }

        public synchronized Map<String,String> get() {
            return java.lang.System.getenv();
        }

        @Override
        public Iterator<Configuration.Entry> iterator() {
            try {
                Map<String,String> envMapUm=java.lang.System.getenv();
                @SuppressWarnings("unchecked") final Map<String,String> envMap=(Map<String,String>) varsField.get(envMapUm);
                return new Iterator<Configuration.Entry>() {
                    Iterator<Map.Entry<String,String>> it=envMap.entrySet().iterator();

                    @Override
                    public boolean hasNext() {
                        return it.hasNext();
                    }

                    @Override
                    public Configuration.Entry next() {
                        final Map.Entry<String,String> e=it.next();
                        return new Configuration.Entry() {
                            @Override
                            public String ctx() {
                                return name();
                            }

                            @Override
                            public String key() {
                                return e.getKey();
                            }

                            @Override
                            public Object value() {
                                return e.getValue();
                            }
                        };
                    }

                    @Override
                    public void remove() {
                        it.remove();
                    }
                };
            }
            catch(Exception ex) {
                return null;
            }
        }

        @Override
        public synchronized boolean add(String var, Object val) {
            if(var==null||val==null) return false;
            try {
                Map<String,String> envMapUm=java.lang.System.getenv();
                @SuppressWarnings("unchecked") final Map<String,String> envMap=(Map<String,String>) varsField.get(envMapUm);
                envMap.put(var, val.toString());
                return true;
            }
            catch(Exception ex) {
                return false;
            }
        }

        @Override
        public boolean add(Configuration.Entry cfg) {
            return add(cfg.key(), cfg.value());
        }

        @Override
        public synchronized boolean del(String var) {
            if(var==null) return false;
            try {
                Map<String,String> envMapUm=java.lang.System.getenv();
                @SuppressWarnings("unchecked") final Map<String,String> envMap=(Map<String,String>) varsField.get(envMapUm);
                return null!=envMap.remove(var);
            }
            catch(Exception ex) {
                return false;
            }
        }

        public synchronized boolean add(Map<String,?> env) {
            try {
                Map<String,String> envMapUm=java.lang.System.getenv();
                @SuppressWarnings("unchecked") final Map<String,String> envMap=(Map<String,String>) varsField.get(envMapUm);
                for(Map.Entry<String,?> e : env.entrySet()) {
                    String var=e.getKey();
                    Object val=e.getValue();
                    if(val==null) envMap.remove(var);
                    else envMap.put(var, val.toString());
                }
                return true;
            }
            catch(Exception ex) {
                return false;
            }
        }

    }


    /**
     * <b>A wrapper implementation of the system properties</b>.
     * <p/>
     */
    protected static class Properties implements SettableConfiguration.Context {

        protected final String ctx;

        public Properties(String ctx) { this.ctx=ctx;}

        @Override
        public String name() {
            return ctx;
        }

        @Override
        public boolean has(String key) {
            return java.lang.System.getProperties().containsKey(key);
        }

        @Override
        public String get(String name) {
            return java.lang.System.getProperty(name);
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map<String,String> get() {
            return (Hashtable) java.lang.System.getProperties();
        }

        @Override
        public Iterator<Configuration.Entry> iterator() {
            return new Iterator<Configuration.Entry>() {
                Iterator<Map.Entry<Object,Object>> it = java.lang.System.getProperties().entrySet().iterator();
                @Override
                public boolean hasNext() {
                    return it.hasNext();
                }

                @Override
                public Configuration.Entry next() {
                    final Map.Entry<Object,Object> e = it.next();
                    return new Configuration.Entry() {
                        @Override
                        public String ctx() {
                            return name();
                        }

                        @Override
                        public String key() {
                            return e.getKey().toString();
                        }

                        @Override
                        public Object value() {
                            return e.getKey();
                        }
                    };
                }

                @Override
                public void remove() {
                    it.remove();
                }
            };
        }

        public static String set(String name, String prop) {
            return java.lang.System.setProperty(name, prop);
        }

        public static boolean set(Map<String,String> props) {
            java.util.Properties p=java.lang.System.getProperties();
            p.putAll(props);
            java.lang.System.setProperties(p);
            return true;
        }

        @Override
        public boolean add(String key, Object val) {
            if(key==null||val==null) return false;
            java.lang.System.setProperty(key, val.toString());
            return true;
        }

        @Override
        public boolean add(Configuration.Entry cfg) {
            return add(cfg.key(),cfg.value());
        }

        @Override
        public boolean add(Map<String,?> cfgs) {
            java.util.Properties p=java.lang.System.getProperties();
            p.putAll(cfgs);
            java.lang.System.setProperties(p);
            return true;
        }

        @Override
        public boolean del(String key) {
            java.lang.System.clearProperty(key);
            return true;
        }
    }
}
