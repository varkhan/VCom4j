package net.varkhan.base.conversion.character;

import junit.framework.TestCase;

import java.util.Random;


public class NumbersTest extends TestCase {

    public void testParseLong() throws Exception {
        Random rand = new Random();
        for(int i=0; i<10000; i++) {
            long p = rand.nextLong();
            String s = Long.toString(p);
            assertEquals("parse("+s+")",p, Numbers.parseLong(s));
        }
    }


    public void testParseDouble() throws Exception {
        Random rand = new Random();
        for(int i=0; i<10000; i++) {
            double p = rand.nextDouble()*Math.pow(10,300*(rand.nextDouble()-0.5));
            String s = Double.toString(p);
            assertEquals("parse("+s+")",p, Numbers.parseDouble(s),p*1e-15);
        }
    }

}