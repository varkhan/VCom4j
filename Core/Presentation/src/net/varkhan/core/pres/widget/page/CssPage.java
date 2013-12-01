package net.varkhan.core.pres.widget.page;

import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.base.containers.map.Map;
import net.varkhan.core.pres.format.CssFormatter;
import net.varkhan.core.pres.render.Renderable;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 10:40 PM
 */
public class CssPage<F extends CssFormatter,P> implements Renderable<F,String[],P> {

    protected final Map<String, String[]> defs = new ArrayOpenHashMap<String,String[]>();

    @Override
    public void render(F fmt, String[] loc, P par) throws IOException {
        Iterable<Map.Entry<String,String[]>> itd=(Iterable<Map.Entry<String,String[]>>) defs;
        for(Map.Entry<String,String[]> def : itd) {
            fmt.style(def.getKey(),def.getValue());
        }
    }

    public void addCss(String spec, String[] atr) { defs.add(spec,atr); }

}
