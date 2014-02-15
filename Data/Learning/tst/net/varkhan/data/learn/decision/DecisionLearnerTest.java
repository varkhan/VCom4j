package net.varkhan.data.learn.decision;

import junit.framework.TestCase;
import net.varkhan.base.functor.Mapper;
import net.varkhan.base.functor.curry.Pair;
import net.varkhan.base.functor.generator.GaussianNumberGenerator;
import net.varkhan.base.functor.generator.UniformPRNGDef;
import net.varkhan.data.learn.stats.InformationGain;

import java.util.Iterator;
import java.util.Random;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 1/25/14
 * @time 10:11 PM
 */
public class DecisionLearnerTest extends TestCase {

    public void testTrain() throws Exception {
        DecisionLearner<String,Double,Object> learner = new DecisionLearner<String,Double,Object>(
                0.7,    // Min confidence
                10,     // Max depth
                createSplitPartition(0),
                createSplitPartition(1),
                createSplitPartition(2),
                createSplitPartition(3),
                createSplitPartition(4)
        );
        for(Pair<Double,String> obs: sample(new Random(), 50, 0.5)) {
            learner.train(obs.lvalue(),obs.rvalue(), null);
            System.out.println("Tree:\n"+learner.model().toString());
        }
        System.out.println("Tree:\n"+learner.model().toString());
    }


    protected static Mapper<String,Double,Object> bucket=new Mapper<String,Double,Object>() {
        @Override
        public String invoke(Double arg, Object ctx) {
            if(arg==null) return null;
            double v = arg;
            if(v<0) return "]..0]";
            if(v<1) return "[0..1]";
            if(v<2) return "[1..2]";
            if(v<3) return "[2..3]";
            if(v<4) return "[3..4]";
            return "[4..[";
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
                        double v = rand.nextDouble()*10-2.5;
                        double b = v + rng.invoke(null);
                        String s = bucket.invoke(b,null);
                        return new Pair.Value<Double,String>(v, s);
                    }

                    @Override
                    public void remove() { }
                };
            }
        };
    }


    protected static DiscretePartitionFactory<String,Long,Double,Object> createSplitPartition(final int s) {
        return new DiscretePartitionFactory<String,Long,Double,Object>(
                new Mapper<Long,Double,Object>() {
                    @Override
                    public Long invoke(Double arg, Object ctx) {
                        return arg==null ? null : arg>s ? Long.valueOf(+1) : arg<s ? Long.valueOf(-1) : Long.valueOf(0);
                    }

                    @Override
                    public String toString() {
                        return "<"+s+">";
                    }
                },
                3,
                new InformationGain<String,Object>()
        );
    }

    public void testTrainPartial() throws Exception {

    }

}
