package net.varkhan.data.ling.filter;

import net.varkhan.base.functor.Predicate;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/10/13
 * @time 3:40 PM
 */
public class StopWordPredicate<C> implements Predicate<String,C> {
    private final int    minLength;
    private final int    maxDigits;
    private final double frqDigits;
    private final int    maxSymbols;
    private final double frqSymbols;
    private final int    maxRepeats;

    public StopWordPredicate(int minLength, int maxDigits, double frqDigits, int maxSymbols, double frqSymbols, int maxRepeats) {
        this.minLength=minLength;
        this.maxDigits=maxDigits;
        this.frqDigits=frqDigits;
        this.maxSymbols=maxSymbols;
        this.frqSymbols=frqSymbols;
        this.maxRepeats=maxRepeats;
    }

    public boolean invoke(String arg, C ctx) {
        if (arg == null) return false;
        if (arg.length() < minLength) return false;
        int d = countDigits(arg);
        if (d > maxDigits|| d > arg.length() * frqDigits) return false;
        int s = countSymbols(arg);
        if (s > maxSymbols|| s > arg.length() * frqSymbols) return false;
        return countRepeats(arg) < maxRepeats;
    }

    private static final char[] digits = "0123456789".toCharArray();
    private static final char[] symbols = "~`'\"@#$%^&*()-_+={}[]\\|/<>:;,.?!".toCharArray();

    public static int countSymbols(String s) {
        return countChars(symbols, s);
    }

    public static int countDigits(String s) {
        return countChars(digits, s);
    }

    public static int countRepeats(String s) {
        if (s.length() == 0) return 0;
        int n = 0;
        int d = 0;
        char l = s.charAt(0);
        for (int i = 1; i < s.length(); i++) {
            char c = s.charAt(i);
            if (l == c) {
                d++;
                if (n < d) n = d;
            } else {
                d = 0;
                l = c;
            }
        }
        return n;
    }

    private static int countChars(char[] chars, String str) {
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            for (char x : chars)
                if (c == x) { count++; break; }
        }
        return count;
    }

}
