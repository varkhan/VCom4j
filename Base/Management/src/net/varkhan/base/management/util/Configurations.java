package net.varkhan.base.management.util;

import net.varkhan.base.management.config.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
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

    /**********************************************************************************
     **  Globally accessible config and contexts
     **/

    /**
     * The system environment, properties and settings as a Configuration.
     * <p/>
     * The contexts names for each subsets are, respectively:
     * <li><em>environment</em></li>
     * <li><em>properties</em></li>
     *
     * @return a settable configuration containing all system parameters
     */
    public static CompoundConfiguration<SettableConfiguration.Context> sysconf() { return systemConfiguration; }

    public static final String SYS_CTX_ENVIRONMENT="environment";
    public static final String SYS_CTX_PROPERTIES="properties";


    /** The system environment, properties and settings as a settable configuration. */
    protected static final CompoundConfiguration<SettableConfiguration.Context> systemConfiguration;
    /** The system environment variables, as a settable context */
    protected static final SettableConfiguration.Context systemEnvironment;
    /** The system properties, as a settable context */
    protected static final Properties systemProperties;


    static {
        SettableConfiguration.Context env;
        try {
            env=new Environment(SYS_CTX_ENVIRONMENT);
        }
        catch(Exception e) {
            try {
                env=new Environment2(SYS_CTX_ENVIRONMENT);
            }
            catch(Exception e2) {
                env=new MapContext(SYS_CTX_ENVIRONMENT, new HashMap<String,Object>());
            }
        }
        systemEnvironment= env;
        systemProperties=new Properties(SYS_CTX_PROPERTIES);
        systemConfiguration=new CompoundConfiguration<SettableConfiguration.Context>(
                    systemEnvironment,
                    systemProperties
            );
    }



    /**********************************************************************************
     **  System environment context magic
     **/


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


    /**********************************************************************************
     **  System properties context wrapper
     **/

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


    /**********************************************************************************
     **  Generic properties loader
     **/

    /**
     * Load configuration mappings from a character stream.
     * <p/>
     * Configuration streams are line-oriented. Each line can contain one of:
     * <li><em>a comment</em>: a hash sign '#',
     *      followed by any number of characters,
     *      and a newline character</li>
     * <li><em>a context</em>: a opening square bracket sign ']',
     *      the context name (any number of characters, except for closing square brackets and newlines),
     *      a closing square bracket ']',
     *      any number of non-newline characters (ignored)
     *      and a newline character</li>
     * <li><em>a configuration mapping</em>: a configuration key
     *      (any number of non-newline characters, where '\', '=' and '#' are escaped by a '\' character),
     *      followed by an '=' sign,
     *      a configuration value (where '\' and newline characters are escaped by a '\' character)
     *      and a newline character</li>
     * <p/>
     * A physical line equals a logical line, except for configuration mappings
     * where physical lines can be merged by escaping with a '\' (backslash) the
     * final newline character of each physical line.
     * <p/>
     * Each configuration mapping is set within the context defined by the last
     * seen context line. If no context line is specified, the default context
     * will be used. A context name equal to the empty string or a single '*'
     * character will switch back to the default context.
     * <p/>
     * An example configuration file:
     * <pre>
     *     # [] implicit default context
     *     # Set default context properties
     *     com.example.property1=value1
     *
     *     [internal] the rest of this line is ignored
     *     com.example.property1=internal_value1
     *
     *     [validation]
     *     com.example.property1=validation_value1
     *     com.example.property2=validation_value2_line1\
     *     validation_value2_line2\
     *     validation_value2_line3\
     *
     *     [*] (equivalent to [])
     *     # Override default context values
     *     com.example.property1=override_value1
     * </pre>
     *
     *
     * @param cfg the configuration to load mappings into
     * @param rdr the reader to read mappings from
     * @return the number of configuration mapping reads (including duplicates)
     * @throws IOException if an error occurred while reading from the stream
     */
    public static int loadConfig(SettableConfiguration cfg, Reader rdr) throws IOException {
        BufferedReader lrd = (rdr instanceof BufferedReader)?(BufferedReader)rdr:new BufferedReader(rdr);
        StringBuilder buf = new StringBuilder();
        // Start with null (default) context
        String ctx=null;
        String line;
        int count = 0;
        while((line=lrd.readLine())!=null) {
            // Skip whitespace at the beginning
            int p=0;
            char c = '\0';
            while(p<line.length()) {
                c = line.charAt(p);
                if(c!=' '&&c!='\t'&&c!='\f') break;
                p ++;
            }
            // Empty/blank line
            if(p>=line.length() || c=='\n') continue;
            if(c=='#') continue;
            else if(c=='[') {
                p ++;
                int q = line.indexOf(']',p);
                // Err on the safe side: non-terminated context
                if(q<0) throw new IOException("Malformed context switch at \""+line+"\"");
                // A context switch of [] or [*] resets to the default context
                if(q==p || (q==p+1 && line.charAt(p)=='*')) ctx = null;
                else ctx = line.substring(p,q);
            }
            else {
                boolean esc = false;
                int q=p;
                buf.setLength(0);
                while(q<line.length()) {
                    c = line.charAt(q);
                    if(c=='=' && !esc) break;
                    if(!esc && c=='\\') esc = true;
                    else {
                        esc = false;
                        buf.append(c);
                    }
                    q ++;
                }
                // Err on the safe side: malformed config definition
                if(esc||c!='=') throw new IOException("Malformed config definition at \""+line+"\"");
                String key = buf.toString();
                String val;
                p = ++q;
                if(p>=line.length()) {
                    val = "";
                }
                // Handle multi-line values (partial lines terminated by a lone \ character)
                else if(line.charAt(line.length()-1)=='\\') {
                    buf.setLength(0);
                    while(line!=null) {
                        esc=false;
                        while(q<line.length()) {
                            c=line.charAt(q++);
                            if(!esc && c=='\\') esc = true;
                            else {
                                esc = false;
                                buf.append(c);
                            }
                        }
                        if(esc) {
                            buf.append('\n');
                            line=lrd.readLine();
                            q = 0;
                        }
                        else {
                            break;
                        }
                    }
                    val = buf.toString();
                }
                else val = line.substring(p);
                cfg.add(ctx,key,val);
                count++;
            }
        }
        return count;
    }

    /**
     * Save configuration mappings to a character stream.
     * <p/>
     * See {@link  #loadConfig(SettableConfiguration, Reader) loadConfig}
     * for a description of the format.
     *
     * @param cfg the configuration
     * @param wrt the writer to write mappings to
     * @return the number of configuration mappings written
     * @throws IOException if an error occurred while writing to the stream
     * @see #loadConfig(SettableConfiguration, Reader)
     */
    public static int saveConfig(Configuration cfg, Writer wrt) throws IOException {
        int count=0;
        for(String ctx: cfg.contexts()) {
            wrt.append('[').append(ctx).append(']').append('\n');
            for(Configuration.Entry var: cfg.context(ctx)) {
                if(var.key().startsWith("#")) {
                    if(var.value()!=null) wrt.append('\\');
                }
                wrt.append(var.key().replace("\\", "\\\\").replace("=", "\\=")).append('=');
                if(var.value()!=null) wrt.append(var.value().toString().replace("\\","\\\\").replace("\n", "\\\n"));
                wrt.append('\n');
                count++;
            }
            wrt.append('\n');
        }
        return count;
    }

}
