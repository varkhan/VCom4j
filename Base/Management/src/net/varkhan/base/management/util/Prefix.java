package net.varkhan.base.management.util;

import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/2/15
 * @time 7:58 PM
 */
public class Prefix {

    /** Protected constructor to prevent instantiation */
    protected Prefix() { }

    public static Iterable<String> enumeratePrefixesAsc(final char sep, final String key) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int dot=Integer.MAX_VALUE;
                    @Override
                    public boolean hasNext() {
                        return key!=null && dot>0;
                    }

                    @Override
                    public String next() {
                        if(key==null) throw new NoSuchElementException();
                        if(dot>key.length()) {
                            dot=key.length();
                            return key;
                        }
                        dot=key.lastIndexOf(sep, dot-1);
                        if(dot<0) return "";
                        return key.substring(0, dot);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }

    public static Iterable<String> enumeratePrefixesDes(final char sep, final String key) {
        return new Iterable<String>() {
            @Override
            public Iterator<String> iterator() {
                return new Iterator<String>() {
                    private int dot=-1;
                    @Override
                    public boolean hasNext() {
                        return key!=null && dot<key.length();
                    }

                    @Override
                    public String next() {
                        if(key==null) throw new NoSuchElementException();
                        if(dot<0) {
                            dot = 0;
                            return "";
                        }
                        dot=key.indexOf(sep, dot+1);
                        if(dot<0) {
                            dot=key.length();
                            return key;
                        }
                        return key.substring(0, dot);
                    }

                    @Override
                    public void remove() {
                        throw new UnsupportedOperationException();
                    }
                };
            }
        };
    }
}
