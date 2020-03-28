import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.type.Tuple;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestApiExecute {

    private MonadFactory factory = MonadFactory.createMonadFactory();

    @Test
    public void testFilter() {

        List<Integer> list = factory.from(1, 2, 3, 4, 5)
                .filter(i -> i > 3)
                .toList();
        List<Integer> rightList = Arrays.asList(4, 5);
        Assert.assertEquals(list, rightList);

        List<Tuple<Integer, Integer>> pairList = factory.from(1, 2, 3, 4, 5)
                .mapToPair(num -> new Tuple<>(num, 1))
                .filter(tuple -> tuple.key() > 2)
                .toList();

        List<Tuple<Integer, Integer>> rightPairList = Arrays.asList(
                new Tuple<>(3, 1),
                new Tuple<>(4, 1),
                new Tuple<>(5, 1)
        );
        Assert.assertEquals(pairList, rightPairList);
    }

    @Test
    public void testFlatMap() {
        List<Integer> list = factory.from(
                factory.from(1, 2, 3),
                factory.from(4, 5, 6),
                factory.from(7, 8, 9)
        ).flatMap(monad -> monad)
                .toList();

        List<Integer> rightList = Arrays.asList(
                1, 2, 3,
                4, 5, 6,
                7, 8, 9
        );
        Assert.assertEquals(list, rightList);

        factory.from(
                factory.from(new Tuple<>(1, 1), new Tuple<>(2, 2)),
                factory.from(new Tuple<>(3, 3), new Tuple<>(4, 4))
        )
                .flatMap(monad -> monad)
                .mapToPair(tuple -> tuple)
                .toList();
        //TODO PairMonad的FlatMap
    }

    @Test
    public void testMap() {
        List<Integer> list = factory.from(1, 2, 3)
                .map(num -> num + 1)
                .toList();

        List<Integer> rightList = Arrays.asList(2, 3, 4);
        Assert.assertEquals(list, rightList);

        List<Tuple<Integer, Integer>> pairList = factory.from(1, 2, 3)
                .mapToPair(num -> new Tuple<>(num, num))
                .map(tuple -> new Tuple<>(tuple.key() + 1, tuple.value() + 1))
                .toList();

        List<Tuple<Integer, Integer>> rightPairList = Arrays.asList(
                new Tuple<>(2, 2),
                new Tuple<>(3, 3),
                new Tuple<>(4, 4)
        );
        Assert.assertEquals(pairList, rightPairList);
    }

    @Test
    public void testSort() {
        List<Integer> list = factory.from(3, 2, 1)
                .sort()
                .toList();

        List<Integer> rightList = new ArrayList<>(Arrays.asList(1, 2, 3));
        Assert.assertEquals(list, rightList);

        list = factory.from(list)
                .sortWith(Collections.reverseOrder())
                .toList();
        Collections.reverse(rightList);
        Assert.assertEquals(list, rightList);

        List<Tuple<Integer, Integer>> pairList = factory.from(3, 2, 1)
                .mapToPair(num -> new Tuple<>(num, num))
                .sortByKey()
                .toList();

        List<Tuple<Integer, Integer>> rightPairList = Arrays.asList(
                new Tuple<>(1, 1),
                new Tuple<>(2, 2),
                new Tuple<>(3, 3)
        );
        Assert.assertEquals(pairList, rightPairList);

        pairList = factory.from(1, 2, 3)
                .mapToPair(num -> new Tuple<>(num, num))
                .sortWith(Collections.reverseOrder())
                .toList();

        rightPairList = Arrays.asList(
                new Tuple<>(3, 3),
                new Tuple<>(2, 2),
                new Tuple<>(1, 1)
        );
        Assert.assertEquals(pairList, rightPairList);
    }

    @Test
    public void testUnion() {
        List<Integer> list = factory.from(1, 2, 3)
                .union(factory.from(4, 5, 6))
                .toList();

        List<Integer> rightList = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        Assert.assertEquals(list, rightList);


        List<Tuple<Integer, Integer>> pairList = factory.from(1, 2, 3)
                .mapToPair(num -> new Tuple<>(num, num))
                .union(factory.from(4, 5, 6)
                        .mapToPair(num -> new Tuple<>(num, num)))
                .toList();

        List<Tuple<Integer, Integer>> rightPairList = Arrays.asList(
                new Tuple<>(1, 1),
                new Tuple<>(2, 2),
                new Tuple<>(3, 3),
                new Tuple<>(4, 4),
                new Tuple<>(5, 5),
                new Tuple<>(6, 6)
        );
        Assert.assertEquals(pairList, rightPairList);
    }
}
