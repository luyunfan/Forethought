import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.type.Tuple;
import org.junit.Test;

public class TestExecutor {

    @Test
    public void testExecutor() {

        MonadFactory factory = MonadFactory.createMonadFactory();

        CommonMonad<Integer> monad = factory.from(new Integer[]{1, 2, 3, 4, 5});
        Graph<Monad<?>> dag = monad.filter(i -> i == 5)
                .distinct()
                .union(factory.from(new Integer[]{6, 7, 8, 9, 10}))
                .mapToPair(item -> new Tuple<>(item, 1))
                .reduceByKey((a, b) -> a + b)
                .createDAG();

        System.out.println();
    }
}
