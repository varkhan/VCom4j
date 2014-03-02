package net.varkhan.data.learn.decision;

import junit.framework.TestCase;
import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.curry.Pair;
import net.varkhan.base.functor.generator.GaussianNumberGenerator;
import net.varkhan.base.functor.generator.UniformPRNGDef;
import net.varkhan.data.learn.stats.InformationGain;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 3/2/14
 * @time 12:26 PM
 */
public class DiscretePartitionFactoryTest extends TestCase {



    protected static DiscretePartitionFactory<String,Long,Double,Object> createSplitPartition(final int s) {
        return new DiscretePartitionFactory<String,Long,Double,Object>(
                new Mapper<Long,Double,Object>() {
                    @Override
                    public Long invoke(Double arg, Object ctx) {
                        return arg==null ? null : arg>s ? Long.valueOf(+1) : arg<s ? Long.valueOf(-1) : Long.valueOf(0);
                    }

                    @Override
                    public String toString() {
                        return "<"+s+">($)";
                    }
                },
                3,
                new InformationGain<String,Object>()
        );
    }

    protected static Mapper<String,Double,Object> bucket=new Mapper<String,Double,Object>() {
        @Override
        public String invoke(Double arg, Object ctx) {
            if(arg==null) return null;
            double v = arg;
            if(v<2) return "-";
            if(v>2) return "+";
            return "0";
//            if(v<0) return "]...0]";
//            if(v<1) return "[0..1]";
//            if(v<2) return "[1..2]";
//            if(v<3) return "[2..3]";
//            if(v<4) return "[3..4]";
//            return "[4...[";
        }
    };

    protected static Iterable<Pair<Double,String>> sample(final Random rand, final int num, final double noise) {
        return new Iterable<Pair<Double,String>>() {
            @Override
            public Iterator<Pair<Double,String>> iterator() {
                final GaussianNumberGenerator<Object> rng=new GaussianNumberGenerator<Object>(new UniformPRNGDef(rand), 0, noise);
                return new Iterator<Pair<Double,String>>() {
                    protected volatile int cnt=0;
                    @Override
                    public boolean hasNext() {
                        return cnt<num;
                    }

                    @Override
                    public Pair<Double,String> next() {
                        cnt ++;
                        double v = rand.nextDouble()*10-3;
                        double b = v + rng.invoke(null);
                        String s = bucket.invoke(b,null);
//                        System.err.println(v+"\t-> "+s);
                        return new Pair.Value<Double,String>(v, s);
                    }

                    @Override
                    public void remove() { }
                };
            }
        };
    }

    public void testFair() throws Exception {
        List<Pair<Double,String>> smpl = new ArrayList<Pair<Double,String>>();
        for(Pair<Double,String> s: sample(new Random(), 10000, 0.1)) smpl.add(s);
        for(int i=0; i<=4; i++) {
            DiscretePartitionFactory<String,Long,Double,Object> fact = createSplitPartition(i);
            Partition<Long,Double,Object> part = fact.invoke(smpl, null);
            System.err.println(i+"\t"+part);
        }
    }

}
