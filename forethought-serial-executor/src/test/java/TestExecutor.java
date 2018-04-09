import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.type.Tuple;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class TestExecutor {

    @Test
    public void testExecutor() {

        MonadFactory factory = MonadFactory.createMonadFactory();

        CommonMonad<Integer> monad = factory.from(new Integer[]{2, 1, 3, 4, 5});
        monad.filter(i -> i < 4)
                //.distinct()
                .union(factory.from(new Integer[]{6, 7, 8, 9, 10}))
                //.mapToPair(item -> new Tuple<>(item, 1))
                //.reduceByKey((a, b) -> a + b)
                //.map(i -> i + "")

                .sort()
                .drop(1).forEach(System.out::println);

        int i = factory.from(new Integer[]{2, 1, 3, 4, 5}).reduce((a, b) -> a + b);

        //.foreach(System.out::println);
        System.out.println();
    }
}
