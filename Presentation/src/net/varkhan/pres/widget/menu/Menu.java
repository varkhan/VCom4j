package net.varkhan.pres.widget.menu;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.base.containers.map.Map;
import net.varkhan.pres.widget.Widget;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/10/11
 * @time 5:28 AM
 */
public class Menu<W extends Widget> {

    protected final Map<String,Item<W>> items = new ArrayOpenHashMap<String, Item<W>>();

    public static class Item<W extends Widget> extends Menu<W> {
        protected final String id;
        protected final W wd;
        protected Item(String id, W wd) { this.id = id; this.wd = wd; }
        protected Item(W wd) { this(wd.id(),wd); }
        public String id() { return id; }
        public W getWidget() { return wd; }
    }

    /**
     * Returns the menu item described by a given location path.
     * <p/>
     * If the location path is null or empty, {@literal null} is returned.
     * <p/>
     * If the location has one element, the item that has that String for id within the children of the node is returned.
     * <p/>
     * Otherwise, is returned the result of invoking the getItem method on the item that has the first element of the
     * location for id, with the remaining of the location as argument.
     *
     * @param loc the location path
     * @return the item at that location, or {@literal null} if not found, or the location is itself null or empty
     */
    public W getItem(String[] loc) {
        if(loc==null || loc.length==0) return null;
        String id = loc[0];
        Item<W> it = items.get(id);
        if(it==null) return null;
        if(loc.length>1) return it.getItem(Arrays.subarray(loc,1,loc.length));
        return it.wd;
    }

    /**
     * Adds an item as children of the item described by a location path.
     * <p/>
     * If the location path is null or empty, adds the item as a children of this node.
     * <p/>
     * Otherwise, invokes the addItem method on the item that has the first element of the
     * location for id, with the item and the remaining of the location as argument.
     *
     * @param item the item to add
     * @param loc  the location path
     * @return
     */
    public boolean addItem(W item, String... loc) {
        if(loc==null || loc.length==0) {
            return items.add(item.id(),new Item<W>(item));
        }
        String id = loc[0];
        Item<W> it = items.get(id);
        if(it==null) return false;
        if(loc.length>1) return it.addItem(item,Arrays.subarray(loc,1,loc.length));
        return it.addItem(item);
    }

    public Container<Item<W>> items() {
        return items.values();
    }


}
