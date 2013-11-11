package net.varkhan.data.ling.tokenize;

import net.varkhan.base.functor.Expander;

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <b></b>.
 * <p/>
 * @author varkhan
 * @date 11/5/13
 * @time 3:46 PM
 */
public class SentenceTokenizer<C> implements Expander<String,Reader,C> {

    @Override
    public Iterable<String> invoke(Reader src, C ctx) {
        return new SentenceTokens(src);
    }

    protected static class SentenceTokens implements Iterable<String> {
        private final Reader src;

        public SentenceTokens(Reader src) {
            this.src = src;
        }

        @Override
        public Iterator<String> iterator() {
            return new SentenceIterator(src);
        }

    }

    protected static class SentenceIterator implements Iterator<String> {
        private final Reader src;
        private volatile int nchr = -1;
        private volatile String next = null;

        public SentenceIterator(Reader src) {
            this.src = src;
        }

        @Override
        public boolean hasNext() {
            if(next!=null) return true;
            try {
                StringBuilder buf = new StringBuilder();
                // Do we have a last read-ahead
                if(nchr>=0) {
                    buf.append((char)nchr);
                    nchr = -1;
                }
                int c;
                characters: while((c=src.read())>=0) {
                    buf.append((char)c);
                    if(isTerminator(c)) {
                        terminators: while((c=src.read())>=0) {
                            if(isTerminator(c)) buf.append((char)c);
                            else if(isWhitespace(c)||isLowercase(c)) {
                                buf.append((char)c);
                                break terminators;
                            }
                            else {
                                nchr = c;
                                break characters;
                            }
                        }
                    }
                }
                if(c<0 && buf.length()==0) return false;
                next = buf.toString();
                return true;
            }
            catch (IOException e) {
                return false;
            }
        }

        @Override
        public String next() {
            if(!hasNext()) throw new NoSuchElementException();
            String next = this.next;
            this.next = null;
            return next;
        }

        @Override
        public void remove() { }
    }

    protected static boolean isTerminator(int c) {
        switch(c) {
            case '.':
            case '?':
            case '!':
            case ';':
                return true;
            default:
                return false;
        }
    }

    protected static boolean isWhitespace(int c) {
        switch(c) {
            case ' ':
            case '\n':
            case '\t':
            case '~':
                return true;
            default:
                return false;
        }
    }

    protected static boolean isLowercase(int c) {
        return ('a'<=c && c<='z') || ('0'<=c && c<='9');
    }

}
