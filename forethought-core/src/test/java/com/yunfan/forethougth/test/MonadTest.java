package com.yunfan.forethougth.test;

import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;
import org.junit.Assert;
import org.junit.Test;


/**
 * Monad测试类
 */
public class MonadTest {

    @Test
    public void testMonadFactory() throws Exception {

        MonadFactory factory = MonadFactory.createMonadFactory();
        Integer[] testArray = {1, 2, 3, 4, 5};
        CommonMonad<Integer> monad = factory.from(testArray);
        monad.filter(i -> i == 5)
                .distinct()
                .mapToPair(item -> new Tuple<>(item + "A", 1))
                .reduceByKey((a, b) -> a + b)
                .createDAG()
                .forEachByDFS(System.out::println);

    }
}