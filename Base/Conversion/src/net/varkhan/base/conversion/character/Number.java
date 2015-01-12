package net.varkhan.base.conversion.character;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/11/15
 * @time 11:01 PM
 */
public class Number {

    public static long parseLong(String str, int i, int l) {
        long val=0;
        boolean neg;
        l+=i;
        char c=str.charAt(i);
        while(i<l) {
            if(!Character.isWhitespace(c)) break;
            c=str.charAt(++i);
        }
        if(c=='+') {
            c=str.charAt(++i);
            neg = false;
        }
        else if(c=='-') {
            c=str.charAt(++i);
            neg = true;
        }
        else neg = false;
        while(i<l) {
            if(c==' ') break;
            else if('0'<=c&&c<='9') {
                val = val*10+(c-'0');
            }
            else throw new NumberFormatException("Illegal integer character '"+c+"' in \""+str+"\"");
            if(++i>=l) break;
            c=str.charAt(i);
        }
        return neg?-val:val;
    }


    public static double parseDouble(String str, int i, int l) {
        long man=0;
        long fpo=0;
        long exp=0;
        int sman;
        int sexp;
        l+=i;
        char c=str.charAt(i);
        while(i<l) {
            if(!Character.isWhitespace(c)) break;
            c=str.charAt(++i);
        }
        if(c=='+') {
            c=str.charAt(++i);
            sman=+1;
        }
        else if(c=='-') {
            c=str.charAt(++i);
            sman=-1;
        }
        else sman=+1;
        while(i<l) {
            if(c==' '||c=='.'||c=='e'||c=='E') break;
            else if('0'<=c&&c<='9') {
                man = man*10+(c-'0');
            }
            else throw new NumberFormatException("Illegal number character '"+c+"' in \""+str+"\"");
            if(++i>=l) break;
            c=str.charAt(i);
        }
        if(c=='.') {
            c=str.charAt(++i);
            while(i<l) {
                if(c==' '||c=='e'||c=='E') break;
                else if('0'<=c&&c<='9') {
                    man = man*10+(c-'0');
                    fpo ++;
                }
                else throw new NumberFormatException("Illegal number character '"+c+"' in \""+str+"\"");
                if(++i>=l) break;
                c=str.charAt(i);
            }
        }
        if(c=='e'||c=='E') {
            c=str.charAt(++i);
            if(c=='+') {
                c=str.charAt(++i);
                sexp=+1;
            }
            else if(c=='-') {
                c=str.charAt(++i);
                sexp=-1;
            }
            else sexp=+1;
            while(i<l) {
                if(c==' ') break;
                else if('0'<=c&&c<='9') {
                    exp = exp*10+(c-'0');
                }
                else throw new NumberFormatException("Illegal number character '"+c+"' in \""+str+"\"");
                if(++i>=l) break;
                c=str.charAt(i);
            }
            long e=sexp*exp-fpo;
            if(e==0) return sman*man;
            return sman*man*pow10(e);
        }
        if(fpo==0) return sman*man;
        return sman*man*pow10(-fpo);
    }

    protected static final double[] p10 = new double[256];
    static {
        for(int i=0; i<p10.length; i++) p10[i] = Math.pow(10,i);
    }
    protected static final double[] n10 = new double[256];
    static {
        for(int i=0; i<n10.length; i++) n10[i] = Math.pow(10,-i);
    }
    protected static double pow10(long i) {
        if(i>0) {
            if(i<p10.length) return p10[(int)i];
            else return Math.pow(10,i);
        }
        else if(i<0) {
            if(-i<n10.length) return n10[(int)-i];
            else return Math.pow(10,i);
        }
        else return 1;
    }

}
