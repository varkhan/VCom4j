package net.varkhan.core.geo.map;

import net.varkhan.base.containers.Container;
import net.varkhan.base.containers.Iterator;
import net.varkhan.base.containers.map.ArrayOpenHashMap;
import net.varkhan.base.containers.map.EmptyMap;
import net.varkhan.base.containers.map.Map;
import net.varkhan.core.geo.shape.Point;
import net.varkhan.core.geo.shape.RectD;
import net.varkhan.core.geo.shape.Shape;

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
public class RTreeMapD<K extends Shape,V> implements MetricMap<K,V> {

    protected final int maxNodeSize;
    protected Node<K,V> root = null;
    protected long size = 0;

    public RTreeMapD(int maxNodeSize) {
        this.maxNodeSize = maxNodeSize;
    }

    public RTreeMapD() {
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
        return has(key.cctr(), 0);
    }

    @Override
    public boolean has(K key, double rad) {
        return has(key.cctr(),rad);
    }

    public boolean has(Point p, double r) {
        return root!=null && root.has(p.cctr(), r*r);
    }

    public boolean has(double[] c, double r) {
        return root!=null && root.has(c, r*r);
    }

    @Override
    public V get(K key) {
        return get(key.cctr(),0);
    }

    @Override
    public V get(K key, double rad) {
        return get(key.cctr(),rad);
    }

    @Override
    public V get(Point p) {
        return get(p.cctr(),0);
    }

    @Override
    public V get(Point p, double r) {
        return get(p.cctr(),r);
    }

    @Override
    public V get(double[] c) {
        return get(c,0);
    }

    @Override
    public V get(double[] c, double r) {
        if(root==null) return null;
        final Node<K,V> f = root.get(c, r*r);
        return f==null?null:f.val;
    }

    @Override
    public Map<K,V> getAll(K key, double rad) {
        return getAll(key.cctr(),rad);
    }

    @Override
    public Map<K,V> getAll(Point p, double r) {
        return getAll(p.cctr(),r);
    }

    public Map<K,V> getAll(double[] c, double r) {
        if(root==null) return new EmptyMap<K,V>();
        Map<K,V> map = new ArrayOpenHashMap<K,V>();
        root.getAll(map, c, r*r);
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

    protected static class Iter<K extends Shape,V> implements Iterator<Node<K,V>> {
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
                return RTreeMapD.this.size();
            }

            @Override
            public boolean isEmpty() {
                return RTreeMapD.this.isEmpty();
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
                return RTreeMapD.this.visit(new Visitor<Entry<K,V>,Par>() {
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
                return RTreeMapD.this.size();
            }

            @Override
            public boolean isEmpty() {
                return RTreeMapD.this.isEmpty();
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
                return RTreeMapD.this.visit(new Visitor<Entry<K,V>,Par>() {
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
        return root.visit(vis,key.cctr(),rad*rad,par);
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

    protected static class Node<K extends Shape,V> extends RectD implements Entry<K,V> {
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

        public boolean contains(double... c) {
            if(key!=null) return key.contains(c);
            return super.contains(c);
        }

        @Override
        public double dmin(double... c) {
            if(key!=null) return key.dmin(c);
            return super.dmin(c);
        }

        @Override
        public double dmin2(double... c) {
            if(key!=null) return key.dmin2(c);
            return super.dmin2(c);
        }

        @Override
        public double dmax(double... c) {
            if(key!=null) return key.dmax(c);
            return super.dmax(c);
        }

        @Override
        public double dmax2(double... c) {
            if(key!=null) return key.dmax2(c);
            return super.dmax2(c);
        }

        /**************************
         **  Sub-node addition and balancing
         **/

        private double ext(double[] gmin, double[] gmax) {
            double d1 = 1.0;
            double d2 = 1.0;
            for(int d=0; d<dim; d++) {
                d1 *= cmax[d]-cmin[d];
                double min = cmin[d];
                double max = cmax[d];
                if(min>gmin[d]) min=gmin[d];
                if(max<gmax[d]) max=gmax[d];
                d2 *= max-min;
            }
            return d2-d1;
        }


        @SuppressWarnings("unchecked")
        public boolean add(K key, V val) {
            final double[] gmin = key.cmin();
            final double[] gmax = key.cmax();
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
                double din = Double.MAX_VALUE;
                Node<K,V> nin = null;
                double dls = 0;
                for(Node<K,V> n: nodes) {
                    double d = dls = n.ext(gmin,gmax);
                    if(d<=din) { din=d; nin=n; }
                }
                if(nin==null) throw new RuntimeException("No matching segment found within "+din+" of "+new RectD(cmin,cmax).toString()+"\n\tfor "+key+"\n\tfrom "+Arrays.toString(nodes));
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
            for(int d=0; d<dim; d++) {
                if(cmin[d]>gmin[d]) cmin[d]=gmin[d];
                if(cmax[d]<gmax[d]) cmax[d]=gmax[d];
            }
            return true;
        }


        /**************************
         **  Sub-node search
         **/

        public boolean has(double[] c, double r2) {
            if(dmin2(c)>r2) return false;
            if(key!=null) {
                double d = key.dmin2(c);
                if(d<=r2) return true;
            }
            if(nodes!=null) {
                for(Node<K,V> n: nodes) {
//                    double d = n.dmin2(x, y);
//                    if(d>r2) continue;
                    if(n.has(c,r2)) return true;
                }
            }
            return false;
        }

        public Node<K,V> get(double[] c, double r2) {
            if(dmin2(c)>r2) return null;
            double dm = r2;
            Node<K,V> f = null;
            if(key!=null) {
                double d = key.dmin2(c);
                if(d<=dm) {
                    dm = d;
                    f = this;
                }
            }
            if(nodes!=null) {
                for(Node<K,V> n: nodes) {
//                    double d = n.dmin2(x, y);
//                    if(d>dm) continue;
                    Node<K,V> v = n.get(c, dm);
                    if(v!=null) {
                        double d = v.dmin2(c);
                        if(d<=dm) {
                            dm = d;
                            f = v;
                        }
                    }
                }
            }
            return f;
        }

        public void getAll(Map<K,V> map, double[] c, double r2) {
            if(dmin2(c)>r2) return;
            if(key!=null && key.dmin2(c)<=r2) map.add(key, val);
            if(nodes!=null) {
                for(Node<K,V> n: nodes) n.getAll(map, c, r2);
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

        protected boolean overlap(K k, Node<K,V> n) {return n.contains(k.cctr());}

        public void box() {
            if(key!=null) {
                for(int d=0; d<dim; d++) {
                    cmin[d] = key.cmin(d);
                    cmax[d] = key.cmax(d);
                }
            }
            else {
                for(int d=0; d<dim; d++) {
                    cmin[d] = +Double.MAX_VALUE;
                    cmax[d] = -Double.MAX_VALUE;
                }
            }
            for(Node<K,V> n: nodes) {
                for(int d=0; d<dim; d++) {
                    if(cmin[d]>n.cmin[d]) cmin[d]=n.cmin[d];
                    if(cmax[d]<n.cmax[d]) cmax[d]=n.cmax[d];
                }
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

        public <Par> long visit(Visitor<Entry<K,V>,Par> vis, double[] c, double r2, Par par) {
            if(dmin2(c)>r2) return 0;
            long s = 0;
            if(key!=null && key.dmin2(c)<=r2) {
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
            buf.append('\n').append(pre).append("= ");
            boolean f=true;
            for(int d=0; d<dim; d++) {
                if(f) f=false;
                else buf.append(" x ");
                buf.append('[').append(cmin[d]).append(':').append(cmax[d]).append(']');
            }
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
