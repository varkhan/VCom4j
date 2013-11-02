package net.varkhan.base.containers;

import junit.framework.TestCase;

import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/1/13
 * @time 7:13 PM
 */
public class HashesTest extends TestCase {

    private static int csb(int b) {
        if(b==0) return 0;
        int c=0;
        while(b!=0) {
            c+=b&0x1;
            b>>>=1;
        }
        return c;
    }

    private static int csb(long b) {
        if(b==0) return 0;
        int c=0;
        while(b!=0) {
            c+=b&0x1L;
            b>>>=1;
        }
        return c;
    }
    public void testMix32() throws Exception {
        Random r = new Random();
        long n = 10000;
        long c = 0;
        for(int i=0; i<n; i++) {
            int p = r.nextInt();
            int m1 = Hashes.mix(p);
            int b = r.nextInt(32);
            int m2 = Hashes.mix(p^(1<<b));
//            System.out.println("mix32: "+p+" "+b+" "+csb(m1^m2));
            c += csb(m1^m2);
        }
        System.out.println("mix32: "+(c/(32.0*n)));
        assertTrue(c>15.5*n);
        assertTrue(c<16.5*n);
    }

    public void testMix64() throws Exception {
        Random r = new Random();
        long n= 10000;
        long c = 0;
        for(int i=0; i<n; i++) {
            long p = r.nextLong();
            long m1 = Hashes.mix(p);
            int b = r.nextInt(64);
            long m2 = Hashes.mix(p^(1<<b));
//            System.out.println("mix64: "+p+" "+b+" "+csb(m1^m2));
            c += csb(m1^m2);
        }
        System.out.println("mix64: "+(c/(64.0*n)));
        assertTrue(c>31.5*n);
        assertTrue(c<32.5*n);
    }

    public void testPrime() throws Exception {
        int len = 1000;
        for(int i=0;i<len;i++) {
            long num = Hashes.prime(i);
            for(int j=0;j<i;j++) {
                long p=Hashes.prime(j);
                assertFalse(""+num+" / "+p,num%p==0);
            }
        }
    }
}
