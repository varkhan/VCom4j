package net.varkhan.base.extensions.type;

import net.varkhan.base.extensions.Kinded;
import net.varkhan.base.extensions.Named;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of a Kind for structured values containing distinct fields.
 *
 * @param <T> the Java type of the value
 * @param <F> the type of metadata used to represent each field
 */
public abstract class StructKind<T, F extends Named & Kinded<?>> extends Kind.BaseKind<T> {
    protected final Map<String,Integer> names;
    protected final Kinded<?>[] fields;

    public StructKind(String name, List<? extends F> fields) {
        super(name+"<"+
                fields.stream().map(f -> f.name()+":"+f.kind().name()).collect(Collectors.joining(","))
                +">");
        // Preserving the order of the fields is important, so we index them
        this.names = new LinkedHashMap<>(fields.size());
        this.fields = new Kinded[fields.size()];
        int i=0;
        for(F f : fields) {
            this.fields[i] = f;
            this.names.put(f.name(), i);
            i++;
        }
    }

    public StructKind(List<F> fields) { this("struct",fields); }

    @SafeVarargs
    public StructKind(String name, F... fields) { this(name,Arrays.asList(fields)); }

    @SafeVarargs
    public StructKind(F... fields) { this(Arrays.asList(fields)); }

    @Override
    public <X> boolean isAssignableFrom(Kind<X> from) {
        if(!(from instanceof StructKind)) return  false;
        for(Kinded<?> f: fields) {
            Kinded<?> ff = ((StructKind<?,?>) from).getField(((Named)f).name());
            if(ff==null) {
                if(!f.kind().isNullable()) return false;
            }
            else {
                if(!f.kind().isAssignableFrom(ff.kind())) return false;
            }
        }
        // Any fields in <from> that is not present in this target will simply be ignored silently
        return true;
    }

    @Override
    public <X> Caster<X, T> assignFrom(Kind<X> from) {
        // This method in MOST CASES will need to be overridden by implementors
        return new BaseCaster<X, T>(from,this) {
            @SuppressWarnings("unchecked")
            @Override public T apply(X from) { return (T) from; }
        };
    }


    public F getField(String name) {
        Integer i = names.get(name);
        return i==null?null:getField(i);
    }

    @SuppressWarnings("unchecked")
    public F getField(int i) { return i<0||i>fields.length ? null : (F)fields[i]; }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public List<F> fields() { return Collections.unmodifiableList((List)Arrays.asList(fields)); }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof StructKind)) return false;
        StructKind<?, ?> st = (StructKind<?, ?>) obj;
        return names.equals(st.names) && Arrays.equals(fields, st.fields);
    }

    @Override
    public String toString() { return name(); }

}
