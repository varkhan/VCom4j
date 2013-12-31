package net.varkhan.core.pres.convert;

import junit.framework.TestCase;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 12/29/13
 * @time 12:54 PM
 */
public class DotGraphTest extends TestCase {

    public void testBraille() {
        StringBuilder buf=new StringBuilder();
        for(char c='⠀';c<='⣿';c++) buf.append(c).append('|');
        System.out.println(buf);
    }

    public void testRow22() throws Exception {
        StringBuilder buf = new StringBuilder();
        DotGraph fmt = new DotGraph._2x2(buf, "|", "|\n");
        fmt.open();

        fmt.row('#', "######_#_#__#_".toCharArray());
        fmt.row('#', "#____##_______".toCharArray());
        fmt.row('#', "#____##___##__".toCharArray());
        fmt.row('#', "######_#_####_".toCharArray());

        fmt.flush();
        System.out.println(buf);
    }

    public void testRow24() throws Exception {
        StringBuilder buf = new StringBuilder();
        DotGraph fmt = new DotGraph._2x4(buf, "|", "|\n");
        fmt.open();

        fmt.row('#', "######_#_#__#_".toCharArray());
        fmt.row('#', "#____##_______".toCharArray());
        fmt.row('#', "#____##___##__".toCharArray());
        fmt.row('#', "######_#_####_".toCharArray());


        fmt.flush();
        System.out.println(buf);
    }

    public void testDots22() throws Exception {
        StringBuilder buf = new StringBuilder();
        DotGraph fmt = new DotGraph._2x2(buf, "|", "|\n");
        fmt.open();

        double[] X = new double[200];
        double[] Y = new double[200];
        double xmin = 0, xmax = 10;
        double ymin = -1, ymax = +1;
        for(int i=0; i<X.length; i++) {
            double x = xmin + (i+0.5)*(xmax-xmin)/X.length;
            double y = Math.sin(x);
            X[i] = x;
            Y[i] = y;
        }

        fmt.dots(200, xmin, xmax, X ,25, ymax, ymin, Y);

        fmt.flush();
        System.out.println(buf);
    }

    public void testDots24() throws Exception {
        StringBuilder buf = new StringBuilder();
        DotGraph fmt = new DotGraph._2x4(buf, "|", "|\n");
        fmt.open();

        double[] X = new double[200];
        double[] Y = new double[200];
        double xmin = 0, xmax = 10;
        double ymin = -1, ymax = +1;
        for(int i=0; i<X.length; i++) {
            double x = xmin + (i+0.5)*(xmax-xmin)/X.length;
            double y = Math.sin(x);
            X[i] = x;
            Y[i] = y;
        }

        fmt.dots(200, xmin, xmax, X, 50, ymax, ymin, Y);

        fmt.flush();
        System.out.println(buf);
    }

    public void testBars24() throws Exception {
        StringBuilder buf = new StringBuilder();
        DotGraph fmt = new DotGraph._2x4(buf, "|", "|\n");
        fmt.open();

        double[] X = new double[200];
        double[] Y = new double[200];
        double xmin = 0, xmax = 10;
        double ymin = -1, ymax = +1;
        for(int i=0; i<X.length; i++) {
            double x = xmin + (i+0.5)*(xmax-xmin)/X.length;
            double y = Math.sin(x);
            X[i] = x;
            Y[i] = y;
        }

        fmt.bars(200, xmin, xmax, X, 50, ymax, ymin, Y);

        fmt.flush();
        System.out.println(buf);
    }
}
