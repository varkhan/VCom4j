package net.varkhan.core.pres.convert;

import net.varkhan.core.pres.format.RowFormatter;

import java.io.IOException;
import java.util.Arrays;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/29/13
 * @time 11:46 AM
 */
public class DotGraph extends RowFormatter {

    protected final int    dpc;
    protected final int    dpr;
    protected final char[] map;

    protected volatile int[] rows=null;
    protected volatile int   rpos=0;

    public DotGraph(Appendable out, int dpc, int dpr, char[] map, String bor, String eor) {
        this(out,dpc,dpr,map,bor.toCharArray(),eor.toCharArray());
    }

    public DotGraph(Appendable out, int dpc, int dpr, char[] map, char[] bor, char[] eor) {
        super(out, eor, bor);
        if(map.length!=(1<<(dpc*dpr)))
            throw new IllegalArgumentException("Invalid character map: should have "+(1<<(dpc*dpr))+" characters");
        this.dpc = dpc;
        this.dpr = dpr;
        this.map = map;
    }

    public int getDotsPerRow() {
        return dpr;
    }

    public int getDotsPerCol() {
        return dpc;
    }

    public char[] getCharMap() {
        return map.clone();
    }

    public boolean nextRow() throws IOException {
        if(++rpos>=dpr) {
            flushRow();
            return true;
        }
        return false;
    }

    public void flushRow() throws IOException {
        rpos = 0;
        openRow();
        for(int i=0;i<rows.length;i++) {
            int c = rows[i];
            append(map[c]);
            rows[i] = 0;
        }
        closeRow();
    }

    public RowFormatter row(boolean[] row) throws IOException {
        int l=(row.length+dpc-1)/dpc;
        if(rows==null) rows = new int[l];
        else if(rows.length<l) rows = Arrays.copyOf(rows, l);
        for(int p=0, i=0; p<l; p++, i+=dpc) {
            int d = 0;
            for(int j=0; j<dpc && i+j<row.length; j++) {
                if(row[i+j]) d |= 1<<j;
            }
            rows[p] |= d<<(rpos*dpc);
        }
        nextRow();
        return this;
    }

    public RowFormatter row(char dot, char[] row) throws IOException {
        int l=(row.length+dpc-1)/dpc;
        if(rows==null) rows = new int[l];
        else if(rows.length<l) rows = Arrays.copyOf(rows, l);
        for(int p=0, i=0; p<l; p++, i+=dpc) {
            int d = 0;
            for(int j=0; j<dpc && i+j<row.length; j++) {
                if(row[i+j]==dot) d |= 1<<j;
            }
            rows[p] |= d<<(rpos*dpc);
        }
        nextRow();
        return this;
    }

    public RowFormatter rows(boolean[]... rows) throws IOException {
        for(boolean[] row: rows) row(row);
        return this;
    }

    public RowFormatter row(byte[] row) throws IOException {
        int l=(8*row.length+dpc-1)/dpc;
        if(rows==null) rows = new int[l];
        else if(rows.length<l) rows = Arrays.copyOf(rows, l);
        for(int p=0, i=0; p<l; p++, i+=dpc) {
            int d = 0;
            for(int j=0; j<dpc && i+j<8*row.length; j++) {
                if((row[(i+j)>>>3]&(i%8))!=0) d |= 1<<i;
            }
            rows[p] |= d<<(rpos*dpc);
        }
        nextRow();
        return this;
    }


    public RowFormatter dots(int xn, int[] X, int yn, int[] Y) throws IOException {
        if(X.length!=Y.length) throw new IllegalArgumentException();
        yn += dpr - yn%dpr;
        boolean[][] dots = new boolean[yn][xn];
        for(int i=0; i<X.length; i++) {
            int x = X[i];
            int y = Y[i];
            if(0<=x && x<xn && 0<=y && y<yn) dots[y][x] = true;
        }
        rows(dots);
        return this;
    }

    public RowFormatter dots(int xnum, double xmin, double xmax, double[] X, int ynum, double ymin, double ymax, double[] Y) throws IOException {
        return dots(xnum, discretize(xnum, xmin, xmax, X),ynum, discretize(ynum, ymax, ymin, Y));
    }

    public RowFormatter bars(int xn, int[] X, int yn, int[] Y) throws IOException {
        if(X.length!=Y.length) throw new IllegalArgumentException();
        yn += dpr - yn%dpr;
        boolean[][] dots = new boolean[yn][xn];
        for(int i=0; i<X.length; i++) {
            int x = X[i];
            int y = Y[i];
            if(0<=x && x<xn && 0<=y && y<yn) {
                for(int yj=y; yj<yn; yj++) dots[yj][x] = true;
            }
        }
        rows(dots);
        return this;
    }

    public RowFormatter bars(int xnum, double xmin, double xmax, double[] X, int ynum, double ymin, double ymax, double[] Y) throws IOException {
        return bars(xnum, discretize(xnum, xmin, xmax, X), ynum, discretize(ynum, ymax, ymin, Y));
    }

    public static int[] discretize(int num, double min, double max, double[] vals) {
        int[] ints = new int[vals.length];
        double s = (max-min)/num;
        for(int i=0;i<vals.length;i++) {
            double v=vals[i];
            if(min<=v && v<max) {
                ints[i] = (int)((v-min)/s);
            }
            else if(max<=v && v<min) {
                ints[i] = num+(int)((v-max)/s);
            }
            else ints[i] = -1;
        }
        return ints;
    }


    protected static final char[] CHARMAP_2x2 = (
            "| |▘|▝|▀|" +
            "|▖|▌|▞|▛|" +
            "|▗|▚|▐|▜|" +
            "|▄|▙|▟|█|"
    ).replace("|","").toCharArray();
    public static class _2x2 extends DotGraph {
        public _2x2(Appendable out, char[] bor, char[] eor) { super(out, 2, 2, CHARMAP_2x2, bor, eor); }
        public _2x2(Appendable out, String bor, String eor) { super(out, 2, 2, CHARMAP_2x2, bor, eor); }
    }


    protected static final char[] CHARMAP_2x4=(
            "|⠀|⠁|⠈|⠉|"+"|⠂|⠃|⠊|⠋|"+"|⠐|⠑|⠘|⠙|"+"|⠒|⠓|⠚|⠛|"+
            "|⠄|⠅|⠌|⠍|"+"|⠆|⠇|⠎|⠏|"+"|⠔|⠕|⠜|⠝|"+"|⠖|⠗|⠞|⠟|"+
            "|⠠|⠡|⠨|⠩|"+"|⠢|⠣|⠪|⠫|"+"|⠰|⠱|⠸|⠹|"+"|⠲|⠳|⠺|⠻|"+
            "|⠤|⠥|⠬|⠭|"+"|⠦|⠧|⠮|⠯|"+"|⠴|⠵|⠼|⠽|"+"|⠶|⠷|⠾|⠿|"+

            "|⡀|⡁|⡈|⡉|"+"|⡂|⡃|⡊|⡋|"+"|⡐|⡑|⡘|⡙|"+"|⡒|⡓|⡚|⡛|"+
            "|⡄|⡅|⡌|⡉|"+"|⡆|⡇|⡎|⡏|"+"|⡔|⡕|⡜|⡝|"+"|⡖|⡗|⡞|⡟|"+
            "|⡠|⡡|⡨|⡩|"+"|⡢|⡣|⡪|⡫|"+"|⡰|⡱|⡸|⡹|"+"|⡲|⡳|⡺|⡻|"+
            "|⡤|⡥|⡬|⡭|"+"|⡦|⡧|⡮|⡯|"+"|⡴|⡵|⡼|⡽|"+"|⡶|⡷|⡾|⡿|"+

            "|⢀|⢁|⢈|⢉|"+"|⢂|⢃|⢊|⢋|"+"|⢐|⢑|⢘|⢙|"+"|⢒|⢓|⢚|⢛|"+
            "|⢄|⢅|⢌|⢍|"+"|⢆|⢇|⢎|⢏|"+"|⢔|⢕|⢜|⢝|"+"|⢖|⢗|⢞|⢟|"+
            "|⢠|⢨|⢡|⢩|"+"|⢢|⢣|⢪|⢫|"+"|⢰|⢱|⢸|⢹|"+"|⢲|⢳|⢺|⢻|"+
            "|⢤|⢥|⢬|⢭|"+"|⢦|⢧|⢮|⢯|"+"|⢴|⢵|⢼|⢽|"+"|⢶|⢷|⢾|⢿|"+

            "|⣀|⣁|⣈|⣉|"+"|⣂|⣃|⣊|⣋|"+"|⣐|⣑|⣘|⣙|"+"|⣒|⣓|⣚|⣛|"+
            "|⣄|⣅|⣌|⣍|"+"|⣆|⣇|⣎|⣏|"+"|⣔|⣕|⣜|⣝|"+"|⣖|⣗|⣞|⣟|"+
            "|⣠|⣡|⣨|⣩|"+"|⣢|⣣|⣪|⣫|"+"|⣰|⣱|⣸|⣹|"+"|⣲|⣳|⣺|⣻|"+
            "|⣤|⣥|⣬|⣭|"+"|⣦|⣧|⣮|⣯|"+"|⣴|⣵|⣼|⣽|"+"|⣶|⣷|⣾|⣿|"
    ).replace("|","").toCharArray();

    public static class _2x4 extends DotGraph {
        public _2x4(Appendable out, char[] bor, char[] eor) { super(out, 2, 4, CHARMAP_2x4, bor, eor); }
        public _2x4(Appendable out, String bor, String eor) { super(out, 2, 4, CHARMAP_2x4, bor, eor); }
    }


}
