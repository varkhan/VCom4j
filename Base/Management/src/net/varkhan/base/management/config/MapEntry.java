package net.varkhan.base.management.config;

import java.util.Map;


/**
* <b></b>.
* <p/>
*
* @author varkhan
* @date 9/20/14
* @time 8:56 PM
*/
public class MapEntry implements Configuration.Entry {
    protected final String                   ctx;
    protected final Map.Entry<String,Object> ent;

    public MapEntry(String ctx, Map.Entry<String,Object> ent) {
        this.ctx=ctx;
        this.ent=ent;
    }

    public String ctx() { return ctx; }

    public String key() { return ent.getKey(); }

    public Object value() { return ent.getValue(); }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Configuration.Entry)) return false;
        Configuration.Entry that = (Configuration.Entry) obj;
        if(ctx==null || that.ctx()==null) return false;
        if(!ctx.equals(that.ctx())) return false;
        String key=ent.getKey();
        if(key==null) return that.key()==null;
        return key.equals(that.key());
    }

    @Override
    public int hashCode() {
        return 31 * (ctx==null?0:ctx.hashCode()) + (ent.getKey()==null?0:ent.getKey().hashCode());
    }

    @Override
    public String toString() {
        return (ctx==null?"":ctx)+":"+(ent.getKey()==null?"":ent.getKey())+"="+(ent.getValue()==null?"":ent.getValue());
    }
}
