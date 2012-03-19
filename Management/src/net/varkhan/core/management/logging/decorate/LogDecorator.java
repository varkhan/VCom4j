package net.varkhan.core.management.logging.decorate;

import net.varkhan.base.containers.array.CharArrays;
import net.varkhan.base.functors.Mapper;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/21/12
 * @time 11:10 PM
 */
public class LogDecorator<T> implements Mapper<Object,Object,String> {

    private final ConcurrentMap<Class,Decoration> decors = new ConcurrentHashMap<Class,Decoration>();

    public static class Entry {
        public final AnnotatedElement value;
        public final String name;
        public final String comment;
        public final boolean nullable;

        public Entry(AnnotatedElement e) {
            value = e;
            Loggable.Field f = e.getAnnotation(Loggable.Field.class);
            name = f.name();
            Loggable.Comment c = e.getAnnotation(Loggable.Comment.class);
            if(c==null) comment = "";
            else comment = c.comment();
            Loggable.Nullable n = e.getAnnotation(Loggable.Nullable.class);
            if(n!=null) nullable = n.value();
            else nullable = true;
        }
    }

    public static class Decoration {
        private final Map<String,Entry> fields = new HashMap<String,Entry>();

        public Decoration(Class<?> cls) {
            for(Method m: cls.getMethods()) {
                if(m.getAnnotation(Loggable.Field.class)!=null) {
                    Entry e = new Entry(m);
                    if(!fields.containsKey(e.name)) fields.put(e.name,e);
                }
            }
            for(Field f: cls.getFields()) {
                if(f.getAnnotation(Loggable.Field.class)!=null) {
                    Entry e = new Entry(f);
                    if(!fields.containsKey(e.name)) fields.put(e.name,e);
                }
            }
        }

    }


    public Object invoke(Object arg, String ctx) {
        Class cls = arg.getClass();
        Decoration d = decors.get(cls);
        if(d==null) {
            d = new Decoration(cls);
            decors.putIfAbsent(cls, d);
            d = decors.get(cls);
        }
        StringBuilder buf = new StringBuilder();
        buf.append('{');
        for(Entry e: d.fields.values()) {
            Object v = null;
            try {
                if(e.value instanceof Method) v = ((Method)e.value).invoke(arg);
                else if(e.value instanceof Field) v = ((Field)e.value).get(arg);
            }
            catch(Throwable t) { /*ignore */ }
            String s = v.toString();
            buf.append(e.name).append(':').append('"');
            CharArrays.repl(buf,s,new String[]{"\\","\""},new String[]{"\\\\","\\\""});
            buf.append('"').append(',');
        }
        buf.append('}');
        return buf;
    }

}
