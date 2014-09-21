package net.varkhan.core.geo.geomaps;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.base.containers.map.EmptyMap;
import net.varkhan.base.containers.map.Map;
import net.varkhan.core.geo.geometry.plane.RectF2D;
import net.varkhan.core.geo.geometry.plane.Shape2D;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.NoSuchElementException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/9/12
 * @time 1:33 PM
 */
public class RTreeMapF2D<K extends Shape2D,V> implements MetricMap<K,V> {

    protected final int maxNodeSize;
    protected Node<K,V> root = null;
    protected long size = 0;

    public RTreeMapF2D(int maxNodeSize) {
        this.maxNodeSize = maxNodeSize;
    }

    public RTreeMapF2D() {
        this(12);
    }

    /**********************************************************************************
     **  R-Tree accessors
     **/

    @Override
    public void clear() {
        root = null;
    }

    @Override
    public long size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size==0;
    }

    @Override
    public boolean has(K key) {
        return has(key.xctr(), key.yctr(), 0);
    }

    @Override
    public boolean has(K key, double rad) {
        return has(key.xctr(),key.yctr(),rad);
    }

    public boolean has(double x, double y, double r) {
        return root!=null && root.has((float)x, (float)y, (float)(r*r));
    }

    @Override
    public V get(K key) {
        return get(key.xctr(),key.yctr(),0);
    }

    @Override
    public V get(K key, double rad) {
        return get(key.xctr(),key.yctr(),rad);
    }

    @Override
    public V get(double x, double y) {
        return get(x,y,0);
    }

    @Override
    public V get(double x, double y, double r) {
        if(root==null) return null;
        final Node<K,V> f = root.get((float)x, (float)y, (float)(r*r));
        return f==null?null:f.val;
    }

    @Override
    public Map<K,V> getAll(K key, double rad) {
        return getAll(key.xctr(),key.yctr(),rad);
    }

    public Map<K,V> getAll(double x, double y, double r) {
        if(root==null) return new EmptyMap<K,V>();
        Map<K,V> map = new ArrayOpenHashMap<K,V>();
        root.getAll(map, (float)x, (float)y, (float)(r*r));
        return map;
    }

    @Override
    public boolean add(Entry<K,V> item) {
        return add(item.getKey(),item.getValue());
    }

    @Override
    public boolean add(K key, V val) {
        if(root==null) {
            root = new Node<K,V>(maxNodeSize, key, val);
            size ++;
            return true;
        }
        else {
            if(root.add(key, val)) {
                size ++;
                return true;
            }
            return false;
        }
    }

    @Override
    public boolean del(K k) {
        if(root==null) return false;
        if(root.del(k)) {
            if(root.isVoid()) root=null;
            return true;
        }
        return false;
    }

    /**************************
     **  R-Tree traverasl and iterators
     **/

    protected static class Iter<K extends Shape2D,V> implements Iterator<Node<K,V>> {
        protected final Deque<Node<K,V>> next = new ArrayDeque<Node<K,V>>();
        protected volatile Node<K,V> curr = null;

        public Iter(Node<K,V> root) {
            if(root!=null) next.add(root);
        }

        @Override
        public boolean hasNext() {
            return !next.isEmpty();
        }

        @Override
        public Node<K,V> next() {
            while(!next.isEmpty()) {
                curr = next.pollLast();
                if(curr.nodes!=null) {
                    for(Node<K,V> n: curr.nodes) if(n!=null) next.addLast(n);
                }
                if(curr.key!=null) return curr;
            }
            throw new NoSuchElementException();
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public Iterator<Entry<K,V>> iterator() {
        return (Iterator) new Iter(root);
    }

    @Override
    public Container<K> keys() {
        return new Container<K>() {
            @Override
            public long size() {
                return RTreeMapF2D.this.size();
            }

            @Override
            public boolean isEmpty() {
                return RTreeMapF2D.this.isEmpty();
            }

            @Override
            public Iterator<? extends K> iterator() {
                return new Iterator<K>() {
                    final Iter<K,V> it = new Iter<K,V>(root);
                    public boolean hasNext() { return it.hasNext(); }
                    public K next() { return it.next().getKey(); }
                    public void remove() { it.remove(); }
                };
            }

            @Override
            public <Par> long visit(final Visitor<K,Par> vis, final Par par) {
                return RTreeMapF2D.this.visit(new Visitor<Entry<K,V>,Par>() {
                    public long invoke(Entry<K,V> obj, Par par) {
                        return vis.invoke(obj.getKey(), par);
                    }
                }, par);
            }
        };
    }

    @Override
    public Container<V> values() {
        return new Container<V>() {
            @Override
            public long size() {
                return RTreeMapF2D.this.size();
            }

            @Override
            public boolean isEmpty() {
                return RTreeMapF2D.this.isEmpty();
            }

            @Override
            public Iterator<? extends V> iterator() {
                return new Iterator<V>() {
                    final Iter<K,V> it = new Iter<K,V>(root);
                    public boolean hasNext() { return it.hasNext(); }
                    public V next() { return it.next().getValue(); }
                    public void remove() { it.remove(); }
                };
            }

            @Override
            public <Par> long visit(final Visitor<V,Par> vis, final Par par) {
                return RTreeMapF2D.this.visit(new Visitor<Entry<K,V>,Par>() {
                    public long invoke(Entry<K,V> obj, Par par) {
                        return vis.invoke(obj.getValue(), par);
                    }
                }, par);
            }
        };
    }

    @Override
    public <Par> long visit(Visitor<Entry<K,V>,Par> vis, Par par) {
        if(root==null) return 0;
        return root.visit(vis,par);
    }

    @Override
    public <Par> long visit(final MapVisitor<K,V,Par> vis, Par par) {
        return visit(new Visitor<Entry<K,V>,Par>() {
            public long invoke(Entry<K,V> obj, Par par) {
                return vis.invoke(obj.getKey(),obj.getValue(),par);
            }
        }, par);
    }

    @Override
    public <Par> long visit(Visitor<Entry<K,V>,Par> vis, K key, double rad, Par par) {
        if(root==null) return 0;
        return root.visit(vis,(float)key.xctr(),(float)key.yctr(),(float)(rad*rad),par);
    }

    @Override
    public <Par> long visit(final MapVisitor<K,V,Par> vis, K key, double rad, Par par) {
        return visit(new Visitor<Entry<K,V>,Par>() {
            public long invoke(Entry<K,V> obj, Par par) {
                return vis.invoke(obj.getKey(),obj.getValue(),par);
            }
        }, key, rad, par);
    }


    @Override
    public String toString() {
        return root.toString();
    }


    /**********************************************************************************
     **  R-Node implementation
     **/

    protected static class Node<K extends Shape2D,V> extends RectF2D implements Entry<K,V> {
        protected K key= null;
        protected V val = null;
        protected int maxNodes;

        protected Node<K,V>[] nodes = null;

        /**************************
         **  Node accessors
         **/

        public boolean isVoid() {
            return key==null && (nodes==null || nodes.length==0);
        }

        public boolean isEmpty() {
            if(key!=null) return false;
            if(nodes!=null) for(Node<K,V> n: nodes) if(!n.isEmpty()) return false;
            return true;
        }

        public long size() {
            long s=key==null?0:1;
            if(nodes!=null) for(Node<K,V> n: nodes) s+=n.size();
            return s;
        }

        @Override
        public K getKey() {
            return key;
        }

        @Override
        public V getValue() {
            return val;
        }

        @Override
        public V setValue(V val) {
            V old = this.val;
            this.val = val;
            return old;
        }

        public boolean contains(double x, double y) {
            if(key!=null) return key.contains(x, y);
            return super.contains(x, y);
        }

        @Override
        public double dmin(double x, double y) {
            if(key!=null) return key.dmin(x, y);
            return super.dmin(x, y);
        }

        @Override
        public double dmin2(double x, double y) {
            if(key!=null) return key.dmin2(x, y);
            return super.dmin2(x, y);
        }

        @Override
        public double dmax(double x, double y) {
            if(key!=null) return key.dmax(x, y);
            return super.dmax(x, y);
        }

        @Override
        public double dmax2(double x, double y) {
            if(key!=null) return key.dmax2(x, y);
            return super.dmax2(x, y);
        }

        public boolean contains(double... coords) {
            final double x = coords[0];
            final double y = coords[1];
            if(key!=null) return key.contains(x, y);
            return super.contains(coords);
        }

        /**************************
         **  Sub-node addition and balancing
         **/

        private float ext(float gxmin, float gxmax, float gymin, float gymax) {
            float xmin = this.xmin;
            float xmax = this.xmax;
            float ymin = this.ymin;
            float ymax = this.ymax;
            float d1 = (xmax-xmin)*(ymax-ymin);
            if(xmin>gxmin) xmin=gxmin;
            if(xmax<gxmax) xmax=gxmax;
            if(ymin>gymin) ymin=gymin;
            if(ymax<gymax) ymax=gymax;
            float d2 = (xmax-xmin)*(ymax-ymin);
            return d2-d1;
        }


        @SuppressWarnings("unchecked")
        public boolean add(K key, V val) {
            final float gxmin =(float) key.xmin();
            final float gxmax =(float) key.xmax();
            final float gymin =(float) key.ymin();
            final float gymax =(float) key.ymax();
            if(nodes==null) {
                if(this.key!=null) {
                    if(this.key.equals(key)) {
                        this.val=val;
                        return false;
                    }
                    Node<K,V> sub = create(this.key, this.val);
                    this.key=null;
                    this.val=null;
                    nodes = new Node[] { sub, create(key, val) };
                }
                else {
                    nodes = new Node[] { create(key, val) };
                }
            }
            else {
                float din = Float.MAX_VALUE;
                Node<K,V> nin = null;
                for(Node<K,V> n: nodes) {
                    float d = n.ext(gxmin,gxmax,gymin,gymax);
                    if(d<din) { din=d; nin=n; }
                }
                if(nin==null) throw new RuntimeException("Whoops! cannot find a matching segment");
                // Should we add to an existing node?
                if(din==0 || nodes.length>=maxNodes) {
                    if(!nin.add(key,val)) return false;
                }
                else {
                    Node<K,V>[] copy = new Node[nodes.length+1];
                    System.arraycopy(nodes,0,copy,0,nodes.length);
                    copy[nodes.length] = create(key, val);
                    nodes = copy;
                }
            }
            if(xmin>gxmin) xmin=gxmin;
            if(xmax<gxmax) xmax=gxmax;
            if(ymin>gymin) ymin=gymin;
            if(ymax<gymax) ymax=gymax;
            return true;
        }


        /**************************
         **  Sub-node search
         **/

        public boolean has(float x, float y, float r2) {
            if(dmin2(x, y)>r2) return false;
            if(key!=null) {
                float d = (float) key.dmin2(x, y);
                if(d<=r2) return true;
            }
            if(nodes!=null) {
                for(Node<K,V> n: nodes) {
//                    float d = (float)n.dmin2(x, y);
//                    if(d>r2) continue;
                    if(n.has(x,y,r2)) return true;
                }
            }
            return false;
        }

        public Node<K,V> get(float x, float y, float r2) {
            if(dmin2(x, y)>r2) return null;
            float dm = r2;
            Node<K,V> f = null;
            if(key!=null) {
                float d = (float) key.dmin2(x, y);
                if(d<=dm) {
                    dm = d;
                    f = this;
                }
            }
            if(nodes!=null) {
                for(Node<K,V> n: nodes) {
//                    float d = (float)n.dmin2(x, y);
//                    if(d>dm) continue;
                    Node<K,V> v = n.get(x, y, dm);
                    if(v!=null) {
                        float d = (float) v.dmin2(x, y);
                        if(d<=dm) {
                            dm = d;
                            f = v;
                        }
                    }
                }
            }
            return f;
        }

        public void getAll(Map<K,V> map, float x, float y, float r2) {
            if(dmin2(x, y)>r2) return;
            if(key!=null && key.dmin2(x, y)<=r2) map.add(key, val);
            if(nodes!=null) {
                for(Node<K,V> n: nodes) n.getAll(map, x, y, r2);
            }
        }

        protected Node(int maxNodes, K key, V val) {
            super(key);
            this.maxNodes=maxNodes;
            this.key=key;
            this.val = val;
        }

        protected Node<K,V> create(K key, V val) {
            return new Node<K,V>(maxNodes, key,val);
        }

        public boolean del(K k) {
            if(key==k || key!=null && key.equals(k)) {
                key = null;
                val = null;
                box();
                return true;
            }
            if(nodes!=null) {
                for(int i=0; i<nodes.length;i++) {
                    Node<K,V> n=nodes[i];
                    // Check that, at the very least, there is a chance of overlap
                    if(overlap(k, n)) {
                        if(n.del(k)) {
                            if(n.isVoid()) {
                                if(nodes.length==1) nodes = null;
                                else if(nodes.length==2) {
                                    n = nodes[1-i];
                                    key = n.key;
                                    val = n.val;
                                    nodes = null;
                                }
                                else {
                                    if(i+1<nodes.length) System.arraycopy(nodes,i+1,nodes,i,nodes.length-i-1);
                                    nodes = Arrays.copyOf(nodes,nodes.length-1);
                                }
                            }
                            box();
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        protected boolean overlap(K k, Node<K,V> n) {return n.contains(k.xctr(),k.yctr());}

        public void box() {
            xmin = +Float.MAX_VALUE;
            xmax = -Float.MAX_VALUE;
            ymin = +Float.MAX_VALUE;
            ymax = -Float.MAX_VALUE;
            if(key!=null) {
                if(xmin>key.xmin()) xmin=(float) key.xmin();
                if(xmax<key.xmax()) xmax=(float) key.xmax();
                if(ymin>key.ymin()) xmin=(float) key.ymin();
                if(ymax<key.ymax()) xmin=(float) key.ymax();
            }
            for(Node<K,V> n: nodes) {
                if(xmin>n.xmin) xmin=n.xmin;
                if(xmax<n.xmax) xmax=n.xmax;
                if(ymin>n.ymin) xmin=n.ymin;
                if(ymax<n.ymax) xmin=n.ymax;
            }
        }


        /**************************
         **  Sub-node traversal
         **/

        public <Par> long visit(Visitor<Entry<K,V>,Par> vis, Par par) {
            long s=0;
            if(key!=null) {
                long r=vis.invoke(this,par);
                if(r<0) return s;
                s += r;
            }
            if(nodes!=null) for(Node<K,V> n: nodes) {
                long r=vis.invoke(n,par);
                if(r<0) return s;
                s += r;
            }
            return s;
        }

        public <Par> long visit(Visitor<Entry<K,V>,Par> vis, float x, float y, float r2, Par par) {
            if(dmin2(x, y)>r2) return 0;
            long s = 0;
            if(key!=null && key.dmin2(x, y)<=r2) {
                long r=vis.invoke(this,par);
                if(r<0) return s;
                s += r;
            }
            if(nodes!=null) {
                for(Node<K,V> n: nodes) {
                    long r=vis.invoke(n,par);
                    if(r<0) return s;
                    s += r;
                }
            }
            return s;
        }

        @Override
        public String toString() {
            StringBuilder buf = new StringBuilder();
            toString("",buf);
            return buf.toString();
        }

        protected void toString(String pre, StringBuilder buf) {
            buf.append('\n').append(pre).append("= [").append(xmin).append(':').append(xmax).append("] [").append(ymin).append(':').append(xmax).append(']');
            if(key!=null) {
                buf.append('\n').append(pre).append("  { ").append(key).append(" => ").append(val).append(" }");
            }
            if(nodes!=null) {
                for(Node<K,V> n: nodes) {
                    n.toString(pre+"|\t",buf);
                }
                buf.append('\n').append(pre);
            }
        }
    }

}
