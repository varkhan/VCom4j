package net.varkhan.pres.render;

import net.varkhan.pres.format.Formatter;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 6:38 PM
 */
public class TrivialRenderer<F extends Formatter, L, I extends Renderable<F,L,P>, P> implements Renderer<F,L,I,P> {
    @Override
    public void render(F fmt, L loc, I obj, P par) throws IOException {
        obj.render(fmt, loc, par);
    }
}
