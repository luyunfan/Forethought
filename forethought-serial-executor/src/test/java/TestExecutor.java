import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.type.Tuple;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TestExecutor {

    @Test
    public void testExecutor() throws IOException {
        MonadFactory factory = MonadFactory.createMonadFactory();
        List<Tuple<String, Integer>> result = factory.fromText("src/test/resources/word count.txt", " ", StandardCharsets.UTF_8)
                .mapToPair(text -> new Tuple<>(text, 1))
                .reduceByKey(Integer::sum)
                .toList();
        System.out.println(result);
        System.out.println();
        factory.fromLine("src/test/resources/word count.txt")
                .mapToPair(text -> new Tuple<>(text, 1L))
                .distinct()
                .reduceByKey(Long::sum)
                .foreach(System.out::println);

//        MonadFactory factory = MonadFactory.createMonadFactory();
//        Integer[] testArray = {1, 2, 3, 4, 5};
//        CommonMonad<Integer> monad = factory.from(testArray);
//        System.out.println(monad.filter(i -> i >= 2)
//                .distinct()
//                .mapToPair(item -> new Tuple<>("A", item))
//                .reduceByKey(Integer::sum)
//                .reduce((t1, t2) -> new Tuple<>("Test", t1.value() + t2.value())));
    }
}
