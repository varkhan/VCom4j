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

    protected static Mapper<String,Double,Object> bucket=new Mapper<String,Double,Object>() {
        @Override
        public String invoke(Double arg, Object ctx) {
            if(arg==null) return null;
            double v = arg;
            if(v<0) return "]...0]";
            if(v<1) return "[0..1]";
            if(v<2) return "[1..2]";
            if(v<3) return "[2..3]";
            if(v<4) return "[3..4]";
            return "[4...[";
        }
    };

    protected static Iterable<Pair<Double,String>> sample(final Random rand, final int num, final double err) {
        return new Iterable<Pair<Double,String>>() {
            @Override
            public Iterator<Pair<Double,String>> iterator() {
                final GaussianNumberGenerator<Object> rng=new GaussianNumberGenerator<Object>(new UniformPRNGDef(rand), 0, err);
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
                        return "<"+s+">($)";
                    }
                },
                3,
                new InformationGain<String,Object>()
        );
    }

    public void testTrain100() throws Exception {
        Random rand=new Random();

        testTrain(rand, 100, 100, 0.03, 0.05);
        testTrain(rand, 100, 100, 0.05, 0.10);
        testTrain(rand, 100, 100, 0.10, 0.15);
        testTrain(rand, 100, 100, 0.20, 0.20);
        testTrain(rand, 100, 100, 0.30, 0.25);
        testTrain(rand, 100, 100, 0.40, 0.30);

    }

    public void testTrain300() throws Exception {
        Random rand=new Random();

        testTrain(rand, 30,  300, 0.03, 0.02);
        testTrain(rand, 30,  300, 0.05, 0.03);
        testTrain(rand, 30,  300, 0.10, 0.10);
        testTrain(rand, 30,  300, 0.20, 0.20);
        testTrain(rand, 30,  300, 0.30, 0.25);
        testTrain(rand, 30,  300, 0.40, 0.30);

    }

    public void testTrain1000() throws Exception {
        Random rand=new Random();

        testTrain(rand, 10, 1000, 0.10, 0.05);
        testTrain(rand, 10, 1000, 0.20, 0.15);
        testTrain(rand, 10, 1000, 0.30, 0.25);
        testTrain(rand, 10, 1000, 0.40, 0.30);

    }

    public void testTrain3000() throws Exception {
        Random rand=new Random();

        // Not much improvement is to be expected at these noise levels by just increasing the # of samples
        testTrain(rand, 10, 3000, 0.20, 0.15);
        testTrain(rand, 10, 3000, 0.30, 0.25);
        testTrain(rand, 10, 3000, 0.40, 0.30);
    }

    public void testTrain(Random rand, int run, int num, double err, double max) throws Exception {
        double inv = 0;
        for(int i=0; i<run; i++) {
            inv += runTrain(rand, num, err);
        }
        inv /= run;
        System.out.printf("Inv for %3d runs of %5d +/-%4f = %5f / %5f\n",run,num,err,inv,max);
        assertTrue("error rate "+inv+" / "+max,inv<max);
    }

    public double runTrain(Random rand, int num, double err) throws Exception {
        DecisionLearner<String,Double,Object> learner = new DecisionLearner<String,Double,Object>(
                0.7,    // Min confidence
                10,     // Max depth
                createSplitPartition(0),
                createSplitPartition(1),
                createSplitPartition(2),
                createSplitPartition(3),
                createSplitPartition(4)
        );
        for(Pair<Double,String> obs: sample(rand, num, err)) {
            learner.train(obs.lvalue(),obs.rvalue(), null);
//            System.out.println("Tree:\n"+learner.model().toString());
        }
        DecisionTree<String,Double,Object> model=learner.model();
//        System.out.println("Tree for "+num+" +/-"+err+":\n"+model.toString());
        int tst = 0;
        int inv = 0;
        for(double v=-3;v<7;v+=0.1) {
            if(!bucket.invoke(v,null).equals(model.invoke(v,null))) inv++;
//            System.out.printf("%4f:\t%s\t%s\n",v,bucket.invoke(v,null),model.invoke(v,null));
            tst ++;
        }
        return (double)inv/(double)tst;
    }

//    public void testTrainPartial() throws Exception {
//
//    }

}
