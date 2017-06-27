package com.yunfan.forethougth.test;

import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import org.junit.Test;


/**
 * Monad测试类
 */
public class MonadTest {
    @Test
    public void testMonadFactory() {
        MonadFactory factory = MonadFactory.createSingleMonadFactory();
        CommonMonad<MonadTest> commonNil = factory.commonNil(MonadTest.class);
        Monad<String> test = commonNil.map(Object::toString);
        System.out.println();
    }
}