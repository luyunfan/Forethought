package com.yunfan.forethought.test.type;

import com.yunfan.forethought.type.Tuple;
import org.junit.Test;

/**
 * 测试Tuple的测试类
 */
public class TupleTest {

    @Test
    public void testTuple() {
        Tuple<String, Integer> tuple = new Tuple<>("A", 1);
    }
}
