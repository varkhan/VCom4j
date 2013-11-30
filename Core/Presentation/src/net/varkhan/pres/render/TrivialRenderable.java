package net.varkhan.pres.render;

import net.varkhan.pres.format.Formatter;

import java.io.IOException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/24/12
 * @time 6:44 PM
 */
public class TrivialRenderable<F extends Formatter, L, P> implements Renderable<F,L,P> {

    protected Renderer<F,L,Renderable<F,L,P>,P> renderer;

    protected TrivialRenderable() {
    }

    public TrivialRenderable(Renderer<F,L,Renderable<F,L,P>,P> renderer) {
        this.renderer=renderer;
    }

    @Override
    public void render(F fmt, L loc, P par) throws IOException {
        renderer.render(fmt, loc, this, par);
    }
}
