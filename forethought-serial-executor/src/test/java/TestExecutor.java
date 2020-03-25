import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.type.Tuple;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class TestExecutor {

    @Test
    public void testExecutor() throws IOException {

//        TestExecutor[] test = new TestExecutor[100];
//
        List<Tuple<String, Integer>> result =
                MonadFactory.createMonadFactory()
                        .fromText("src/test/resources/word count.txt", " ", StandardCharsets.UTF_8)
                        .mapToPair(text -> new Tuple<>(text, 1))
                        .reduceByKey(Integer::sum)
                        .toList();
        //.foreach(System.out::println);
        System.out.println(result);
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
