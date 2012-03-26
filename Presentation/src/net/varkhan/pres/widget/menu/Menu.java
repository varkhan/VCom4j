package net.varkhan.pres.widget.menu;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.base.containers.list.ArrayList;
import net.varkhan.base.containers.list.List;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/10/11
 * @time 5:28 AM
 */
public class Menu<I> {

    protected static final String[] NO_LOC=new String[0];
    protected final List<Item<I>> items = new ArrayList<Item<I>>();

    public static class Item<I> extends Menu<I> {
        protected final String id;
        protected final I it;
        protected Item(String id, I it) { this.id = id; this.it=it; }
        public String id() { return id; }
        public I it() { return it; }
    }

    /**
     * Returns the menu element described by a given location path.
     * <p/>
     * If the location path is null or empty, {@literal this} is returned.
     * <p/>
     * If the location has one element, the menu element that has that String for id within the children of the node is returned.
     * <p/>
     * Otherwise, is returned the result of invoking the getItem method on the item that has the first element of the
     * location for id, with the remaining of the location as argument.
     *
     * @param loc the location path
     * @return the menu element at that location, or {@literal null} if not found
     */
    public Menu<I> getMenu(String[] loc) {
        if(loc==null || loc.length==0) return this;
        String id = loc[0];
        Item<I> it = findItem(id);
        if(it==null) return null;
        if(loc.length>1) return it.getMenu(Arrays.subarray(loc, 1, loc.length));
        return it;
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
    public I getItem(String... loc) {
        if(loc==null || loc.length==0) return null;
        String id = loc[0];
        Item<I> it = findItem(id);
        if(it==null) return null;
        if(loc.length>1) return it.getItem(Arrays.subarray(loc,1,loc.length));
        return it.it;
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
     * @param id   the first element of the location path
     * @param loc  the remainder of the location path
     * @return {@literal true} if the item has been added
     */
    public boolean addItem(I item, String id, String... loc) {
        if(loc==null || loc.length==0) {
            return items.add(new Item<I>(id,item));
        }
        Item<I> it = findItem(id);
        if(it==null) return false;
        // Shortcut to avoid useless 0-length arrays
        if(loc.length==1) return it.items.add(new Item<I>(loc[0],item));
        return it.addItem(item,loc[0],Arrays.subarray(loc,1,loc.length));
    }

    @SuppressWarnings({ "unchecked" })
    private Item<I> findItem(String id) {
        if(id==null) return null;
        for(Item<I> it : (Iterable<Item<I>>) items) {
            if(id.equals(it.id)) return it;
        }
        return null;
    }

    public Container<Item<I>> items() {
        return items;
    }


}
