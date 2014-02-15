package net.varkhan.data.learn.decision;

import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.Ordinal;
import net.varkhan.base.functor.mapper.ConstMapper;
import net.varkhan.base.functor.ordinal.ConstOrdinal;
import net.varkhan.data.learn.Classifier;
import net.varkhan.data.learn.SingletonClassifier;

import java.util.*;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/26/13
 * @time 7:32 PM
 */
public class DecisionTree<K,T,C> implements Decision.Tree<K,T,C> {
    protected Node<K,?,T,C> tree;

    public Node<K,?,T,C> tree() { return tree; }

    public K invoke(T obs, C ctx) { return tree.invoke(obs, ctx); }
    public Decision<K,T,C> decision(T obs, C ctx) {return tree.decision(obs, ctx);}
    public double confidence(K key, T obs, C ctx) { return tree.confidence(key, obs, ctx); }
    public Ordinal<T,C> partition() { return tree.partition(); }
    public List<? extends Classifier<K,T,C>> classes() { return tree.classes(); }
    public Collection<? extends Mapper<?,T,C>> attributes() { return tree.attributes(); }

//    public Node<K,?,T,C> get(long... s) {
//        Node<K,?,T,C> t = tree;
//        if(s!=null) for(long i: s) {
//            if(t.isLeaf()) throw new IllegalArgumentException("No node at "+LongArrays.toString(s));
//            if(i>=t.partition().cardinal()) throw new IllegalArgumentException("No node at "+LongArrays.toString(s));
//            t = ((Root<K,?,T,C>)t).get(i);
//        }
//        return t;
//    }
//
//    public void set(Node<K,?,T,C> n, long... s) {
//        Root<K,?,T,C> p = null;
//        long c = -1;
//        Node<K,?,T,C> t = tree;
//        if(s!=null) for(long i: s) {
//            if(t.isLeaf()) throw new IllegalArgumentException("No node at "+LongArrays.toString(s));
//            if(i>=t.partition().cardinal()) throw new IllegalArgumentException("No node at "+LongArrays.toString(s));
//            p = (Root<K,?,T,C>)t;
//            c = i;
//            t = p.get(i);
//        }
//        if(p==null) {
//            tree = n;
//        }
//        else {
//            p.set(n,c);
//        }
//    }

    public static interface Node<K,A,T,C> extends Decision.Tree<K,T,C> {
        public K invoke(T obs, C ctx);
        public double confidence(K key, T obs, C ctx);
        public Collection<? extends Mapper<?,T,C>> attributes();
        public Node<K,?,T,C> decision(T obs, C ctx);
        public Partition<A,T,C> partition();
        public boolean isLeaf();
        public List<? extends Node<K,?,T,C>> children();
        StringBuilder toString(StringBuilder buf, String ind, String tab, String sep);
    }

    public static class Leaf<K,A,T,C> extends SingletonClassifier<K,T,C> implements Node<K,A,T,C> {
        protected final Partition<A,T,C> part;
        protected Leaf(K key) { this(key,1); }
        protected Leaf(K key, double cnf) {
            super(key, cnf);
            part = new Partition<A,T,C>((Mapper<A,T,C>) ConstMapper.NULL(), (Ordinal<A,C>) ConstOrdinal.UNITY(), cnf);
        }

        public Collection<? extends Mapper<?,T,C>> attributes() { return Collections.emptySet(); }
        public Node<K,?,T,C> decision(T obs, C ctx) { return this; }
        public Partition<A,T,C> partition() { return part; }
        public List<? extends Node<K,?,T,C>> classes() { return Arrays.asList((Node<K, ?, T, C>) this); }
        public boolean isLeaf() { return true; }
        public List<Node<K,?,T,C>> children() { return Arrays.asList(); }

        @Override
        public StringBuilder toString(StringBuilder buf, String ind, String tab, String sep) {
            return buf.append(ind).append("*: ").append(key).append(sep);
        }

        @Override
        public String toString() {
            return toString(new StringBuilder(), "","\t","\n").toString();
        }
    }

    public static class Root<K,A,T,C> implements Node<K,A,T,C> {
        protected final Partition<A,T,C> part;
        protected final Node<K,?,T,C>[] child;

        @SuppressWarnings("unchecked")
        protected Root(Partition<A,T,C> part) {
            this.part = part;
            this.child = new Node[(int)part.cardinal()];
        }

        protected void set(Node<K,?,T,C> n, long c) {
            child[(int)c] = n;
        }

        protected Node<K,?,T,C> get(long c) {
            return child[(int)c];
        }

        public Node<K,?,T,C> decision(T obs, C ctx) {
            long s = part.invoke(obs, ctx);
            if(s>=child.length) return null;
            return s<child.length?child[(int)s]:null;
        }

        public K invoke(T obs, C ctx) {
            Node<K,?,T,C> d = decision(obs, ctx);
            return d==null?null:d.invoke(obs, ctx);
        }

        public double confidence(K key, T obs, C ctx) {
            long s = part.invoke(obs, ctx);
            if(s>=child.length) return 0;
            Node<K,?,T,C> d = child[(int)s];
            double c = d.confidence(key, obs, ctx);
            for(Node<K,?,T,C> u: child) {
                if(u!=d) c *= 1-u.confidence(key, obs, ctx);
            }
            return c;
        }

        public Collection<? extends Mapper<?,T,C>> attributes() {
            Set<Mapper<?,T,C>> at = new HashSet<Mapper<?,T,C>>();
            at.add(part.attribute());
            for(Node<K,?,T,C> u: child) at.addAll(u.attributes());
            return at;
        }
        public Partition<A,T,C> partition() { return part; }
        public List<Node<K,?,T,C>> classes() { return Arrays.asList(child); }
        public boolean isLeaf() { return false; }
        public List<Node<K,?,T,C>> children() { return Arrays.asList(child); }

        @Override
        public StringBuilder toString(StringBuilder buf, String ind, String tab, String sep) {
            buf.append(ind).append(part.toString()).append(sep);
            for(Node<K,?,T,C> u: child) {
                u.toString(buf,ind+tab,tab,sep);
            }
            return buf;
        }
        @Override
        public String toString() {
            return toString(new StringBuilder(), "","\t","\n").toString();
        }
    }

    @Override
    public String toString() {
        return tree.toString(new StringBuilder(), "","\t","\n").toString();
    }
}
