package net.varkhan.core.concurrent.executor;

import junit.framework.TestCase;
import net.varkhan.base.containers.array.Arrays;
import net.varkhan.core.concurrent.Task;


/**
 * <b></b>.
 * <p/>
 *
 * @author varkhan
 * @date 11/30/13
 * @time 6:06 PM
 */
public class SerialExecutorTest extends TestCase {
    public void testExecute() throws Exception {
        class tt implements Task {
            private int[] i;
            private int f;

            tt(int[] i, int f) {
                this.i=i;
                this.f=f;
            }

            @Override
            public void invoke() {
                i[0] += f;
            }
        }
        Executor<tt> x = new SerialExecutor<tt>();
        int[] i1 = new int[]{0};
        x.execute(Arrays.asList(new tt(i1,1),new tt(i1,2),new tt(i1,3),new tt(i1,4)));
        assertEquals(10,i1[0]);
    }
}
