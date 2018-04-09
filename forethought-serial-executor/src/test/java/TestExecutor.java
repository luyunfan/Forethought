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

        CommonMonad<Integer> monad = factory.from(new Integer[]{2, 2, 1, 3, 4, 5});
        monad.mapToPair(item -> new Tuple<>(item + "A", 1))
                .reduceByKey((a, b) -> a + b)
                .foreach(System.out::println);


        //.foreach(System.out::println);
        System.out.println();
    }
}
