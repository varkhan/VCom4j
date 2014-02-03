package net.varkhan.base.functor.generator;

import junit.framework.TestCase;

import java.io.PrintStream;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 2/2/14
 * @time 3:35 PM
 */
public abstract class AbstractUniformRandomGeneratorTest extends TestCase {

    public void testLong(UniformRandomGenerator rand, long count, double pre) throws Exception {
        long[] bins01 = new long[16];
        long[] bins02 = new long[8];
        long[] bins04 = new long[4];
        long[] bins08 = new long[4];
        long[] bins10 = new long[2];
        long[] bins20 = new long[2];
        long[] bins40 = new long[2];
        for(long i=0; i<count; i++) {
            long r = rand.nextLong();
            if((r&0x0000000000000001L)==0) bins01[0]++;
            if((r&0x0000000000000010L)==0) bins01[1]++;
            if((r&0x0000000000000100L)==0) bins01[2]++;
            if((r&0x0000000000001000L)==0) bins01[3]++;
            if((r&0x0000000000010000L)==0) bins01[4]++;
            if((r&0x0000000000100000L)==0) bins01[5]++;
            if((r&0x0000000001000000L)==0) bins01[6]++;
            if((r&0x0000000010000000L)==0) bins01[7]++;
            if((r&0x0000000100000000L)==0) bins01[8]++;
            if((r&0x0000001000000000L)==0) bins01[9]++;
            if((r&0x0000010000000000L)==0) bins01[10]++;
            if((r&0x0000100000000000L)==0) bins01[11]++;
            if((r&0x0001000000000000L)==0) bins01[12]++;
            if((r&0x0010000000000000L)==0) bins01[13]++;
            if((r&0x0100000000000000L)==0) bins01[14]++;
            if((r&0x1000000000000000L)==0) bins01[15]++;

            if((r&0x0000000000000022L)==0) bins02[0]++;
            if((r&0x0000000000002200L)==0) bins02[1]++;
            if((r&0x0000000000220000L)==0) bins02[2]++;
            if((r&0x0000000022000000L)==0) bins02[3]++;
            if((r&0x0000002200000000L)==0) bins02[4]++;
            if((r&0x0000220000000000L)==0) bins02[5]++;
            if((r&0x0022000000000000L)==0) bins02[6]++;
            if((r&0x2200000000000000L)==0) bins02[7]++;

            if((r&0x0000000000004444L)==0) bins04[0]++;
            if((r&0x0000000044440000L)==0) bins04[1]++;
            if((r&0x0000444400000000L)==0) bins04[2]++;
            if((r&0x4444000000000000L)==0) bins04[3]++;

            if((r&0x0000000000009999L)==0) bins08[0]++;
            if((r&0x0000000099990000L)==0) bins08[1]++;
            if((r&0x0000999900000000L)==0) bins08[2]++;
            if((r&0x9999000000000000L)==0) bins08[3]++;

            if((r&0x00000000AAAAAAAAL)==0) bins10[0]++;
            if((r&0xAAAAAAAA00000000L)==0) bins10[1]++;

            if((r&0x000000000F0F0F0FL)==0) bins20[0]++;
            if((r&0xF0F0F0F000000000L)==0) bins20[1]++;

            if((r&0x00000000FFFFFFFFL)==0) bins40[0]++;
            if((r&0xFFFFFFFF00000000L)==0) bins40[1]++;
        }

        long t0 = System.currentTimeMillis();
        long t = 0;
        for(long i=0; i<count; i++) {
            long r = rand.nextLong();
            if((r&0x0000000000000001L)==0) t++;
        }
        long t1 = System.currentTimeMillis();
        assertEquals("ratio01 ~ 0.5",0.5,((double)t)/count,1.0/Math.sqrt(count));
        System.err.println("Computed "+count+" rands in "+((t1-t0)/1000)+"s, "+(1.0e6*(t1-t0)/count)+"ns/call, with "+((double)t)/count+" mix, +/-"+(1.0/Math.sqrt(count)));

        for(int i=0; i<bins01.length; i++) {
            printDeltas(System.err, "ratio01", i, 0.50000, ((double) bins01[i])/count, pre*1.0/Math.sqrt(count));
        }

        for(int i=0; i<bins02.length; i++) {
            printDeltas(System.err, "ratio02", i, 0.25000, ((double)bins02[i])/count,(pre*2.0/Math.sqrt(count)));
        }

        for(int i=0; i<bins04.length; i++) {
            printDeltas(System.err, "ratio04", i, 0.06250, ((double)bins04[i])/count,(pre*4.0/Math.sqrt(count)));
        }

        for(int i=0; i<bins08.length; i++) {
            printDeltas(System.err, "ratio08", i, 0.003905, ((double)bins08[i])/count,(pre*8.0/Math.sqrt(count)));
        }

        for(int i=0; i<bins10.length; i++) {
            printDeltas(System.err, "ratio10", i, 0.000015625, ((double)bins10[i])/count,(pre*16.0/Math.sqrt(count)));
        }

        for(int i=0; i<bins20.length; i++) {
            printDeltas(System.err, "ratio20", i, 0.000015625, ((double)bins20[i])/count,(pre*32.0/Math.sqrt(count)));
        }

        for(int i=0; i<bins40.length; i++) {
            printDeltas(System.err, "ratio40", i, 0.00000078125, ((double)bins40[i])/count,(pre*64.0/Math.sqrt(count)));
        }

        for(int i=0; i<bins01.length; i++) {
            assertEquals("ratio01 "+i+" ~ 0.50000",0.50000,((double)bins01[i])/count,pre*1.0/Math.sqrt(count));
        }

        for(int i=0; i<bins02.length; i++) {
            assertEquals("ratio02 "+i+" ~ 0.25000",0.25000,((double)bins02[i])/count,pre*2.0/Math.sqrt(count));
        }

        for(int i=0; i<bins04.length; i++) {
            assertEquals("ratio04 "+i+" ~ 0.06250",0.06250,((double)bins04[i])/count,pre*4.0/Math.sqrt(count));
        }

        for(int i=0; i<bins08.length; i++) {
            // TODO understand why this is not in line with other values
//            assertEquals("ratio08 "+i+" ~ 0.003125",0.003125,((double)bins08[i])/count,pre*8.0/Math.sqrt(count));
            assertEquals("ratio08 "+i+" ~ 0.003905",0.003905,((double)bins08[i])/count,pre*8.0/Math.sqrt(count));
        }

        for(int i=0; i<bins10.length; i++) {
            assertEquals("ratio10 "+i+" ~ 0.003125",0.000015625,((double)bins10[i])/count,pre*16.0/Math.sqrt(count));
        }

        for(int i=0; i<bins20.length; i++) {
            assertEquals("ratio20 "+i+" ~ 0.000015625",0.000015625,((double)bins20[i])/count,pre*32.0/Math.sqrt(count));
        }

        for(int i=0; i<bins40.length; i++) {
            assertEquals("ratio40 "+i+" ~ 0.00000078125",0.00000078125,((double)bins40[i])/count,pre*64.0/Math.sqrt(count));
        }
    }

    /**
     *
     * @param p the output
     * @param r the name of the test
     * @param i the index of the test bin
     * @param t the target value
     * @param v the actual value
     * @param e the expected error
     */
    protected static void printDeltas(PrintStream p, final String r, int i, double t, double v, double e) {
        double d = t-v;
        if(d<0) d=-d;
        p.printf(r+" %02d ~ %f = %f +/- %f (%f)\n", i, t, v, d, e);
    }

}
