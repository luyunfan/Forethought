package com.yunfan.forethougth.test;

import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.api.impls.SingleMonadFactory;
import com.yunfan.forethought.api.monad.CommonMonad;
import org.junit.Test;



/**
 * Monad测试类
 */
public class MonadTest {
    @Test
    public void testMonadFactory() {
        SingleMonadFactory factory = MonadFactory.createSingleMonadFactory();
        Integer[] testArray = {1, 2, 3, 4, 5};
        CommonMonad<Integer> monad = factory.from(testArray);
        System.out.println();
    }
}