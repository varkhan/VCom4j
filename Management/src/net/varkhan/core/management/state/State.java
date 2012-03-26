package net.varkhan.core.management.state;



/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/22/11
 * @time 10:35 PM
 */
public interface State<L extends Level, S extends State<L,S>> {

    public String name();

    public S aggregate(S state, L level);

    public S[] transition(L level);

}
