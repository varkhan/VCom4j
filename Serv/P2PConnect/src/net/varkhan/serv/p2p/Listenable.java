package net.varkhan.serv.p2p;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/15/15
 * @time 3:10 PM
 */
public interface Listenable {

    public <T extends Listenable> void addListener(UpdateListener<T> list);

    public interface UpdateListener<T extends Listenable> extends Listener {

        public void update(T obj);

    }
}
