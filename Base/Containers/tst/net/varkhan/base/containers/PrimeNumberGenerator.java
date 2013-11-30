/**
 *
 */
package net.varkhan.base.containers;

/**
 * @author varkhan
 * @date Feb 10, 2010
 * @time 6:26:42 AM
 */
public class PrimeNumberGenerator {


    private static boolean isPrime(long[] primes, int len, long num) {
        for(int i=0;i<len;i++) {
            long p=primes[i];
            if(p*p>num) return true;
            if(num%p==0) return false;
        }
        // We should never get there, but if we do it means num is too big, so we can't be sure
        return false;
    }

    private static long[] getPrimes(int len) {
        int prog=(1<<(int) (Math.log(len/1000)/Math.log(2)))-1;
        if(len<1) return new long[0];
        long[] primes=new long[len];
        primes[0]=2;
        int pos=1;
        long num=3;
        while(pos<len) {
            if(isPrime(primes, pos, num)) {
                primes[pos++]=num;
                if((pos&prog)==0) System.err.println("Prime no "+pos+" = "+num);
            }
            num+=2;
        }
        return primes;
    }


    public static void computePrimes(int num, double step) {
        long[] primes=getPrimes(num);
        int i=0;
        double r=1;
        for(long p : primes) {
            if(p>r) {
                if(i%20==0) System.out.print("\n        ");
                System.out.printf("%8d, ", p);
                i++;
                r*=step;
            }
        }
        System.out.println();
    }

    public static void main(String[] args) {
        computePrimes(200000,1.2);
    }

}
