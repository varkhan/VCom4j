package net.varkhan.core.component.command;

/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/31/13
 * @time 1:55 PM
 */
public class Command {

    /**
     * Splits a string into "command-line" fields.
     * <p/>
     * The string is cut in separate substrings at every white-space, except when those
     * spaces are escaped, or are contained within literal sequences.
     * <p/>
     * "Escaped" characters, i.e. characters prefixed by an (unescaped) '\' backslash
     * character, are "unescaped" (the backslash is removed).
     * <p/>
     * "Literal" sequences are sequences starting and ending with the same (unescaped)
     * delimiter (one of '\'' or '\"'), in which all occurrences of that same delimiter
     * are escaped.
     *
     * @param s the String containing the concatenated fields
     *
     * @return an array containing the command-line fields
     */
    public static String[] splitCmd(String s) {
        int l=s.length();
        while(l>0) {
            if(Character.isSpaceChar(s.charAt(l-1))) l--;
            else break;
        }
        int n=1;
        boolean isEscape=false;
        char sepChar=' ';
        for(int i=0;i<l;i++) {
            char c=s.charAt(i);
            if(c=='\\') isEscape=!isEscape;
            else {
                if(isEscape) isEscape=false;
                else {
                    if(sepChar=='\''||sepChar=='\"') {
                        if(c==sepChar) sepChar=' ';
                    }
                    else if(Character.isSpaceChar(c)) {
                        n++;
                    }
                    else if(c=='\''||c=='\"') {
                        sepChar=c;
                    }
                }
            }
        }
        String[] b=new String[n];
        int p=0;
        int j=0;
        isEscape=false;
        sepChar=' ';
        for(int i=0;i<l;i++) {
            char c=s.charAt(i);
            if(c=='\\') isEscape=!isEscape;
            else {
                if(isEscape) {
                    if(b[p]==null) b[p]=s.substring(j, i-1);
                    else b[p]+=s.substring(j, i-1);
                    j=i;
                    isEscape=false;
                }
                else {
                    if(sepChar=='\''||sepChar=='\"') {
                        if(c==sepChar) {
                            sepChar=' ';
                            if(b[p]==null) b[p]=s.substring(j, i);
                            else b[p]+=s.substring(j, i);
                            j=i+1;
                        }
                    }
                    else if(Character.isSpaceChar(c)) {
                        if(i>j) {
                            if(b[p]==null) b[p++]=s.substring(j, i);
                            else b[p++]+=s.substring(j, i);
                        }
                        else if(b[p]!=null) p++;
                        j=i+1;
                    }
                    else if(c=='\''||c=='\"') {
                        if(i>j) {
                            if(b[p]==null) b[p]=s.substring(j, i);
                            else b[p]+=s.substring(j, i);
                        }
                        sepChar=c;
                        j=i+1;
                    }
                }
            }
        }
        if(p<b.length) {
            if(j<l) {
                if(b[p]==null) b[p]=s.substring(j, l);
                else b[p]+=s.substring(j, l);
            }
            else if(b[p]==null) b[p]="";
        }
        return b;
    }

}
