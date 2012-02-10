package net.varkhan.base.management.state;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:49 PM
 */
public class SimpleStateReport<L,S extends State<L,S>> implements StateReport<L,S> {

    protected final Collection<StateCheck<L,S>> checks = new ArrayList<StateCheck<L,S>>();
    protected final S initial;

    public SimpleStateReport(S initial) { this.initial=initial; }

    public void clear() { checks.clear(); }
    public void add(StateCheck<L,S> hc) { checks.add(hc); }

    public S state() {
        S state = initial;
        for(StateCheck<L,S> hc: checks) {
            hc.update();
            S s = hc.state();
            if(s==null) continue;
            if(state==null) state = s;
            else state = state.aggregate(s, hc.level());
        }
        return state;
    }

    public Collection<StateCheck<L,S>> checks() { return Collections.unmodifiableCollection(checks); }

    public void update() { for(StateCheck<L,S> hc: checks) hc.update(); }

}
