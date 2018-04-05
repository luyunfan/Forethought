package com.yunfan.forethought.api.impls.action;

import com.yunfan.forethought.enums.ActionType;

import java.util.Iterator;

/**
 * 将数据集结成本地数据的Action操作
 *
 * @param <T> Monad中元素类型
 */
public class CollectImpl<T> implements Action<Iterator<T>, Object> {

    /**
     * @return Action操作的返回值类型
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<Iterator<T>> resultType() {
        return (Class) Iterator.class;
    }

    /**
     * @return Action操作本身的类型
     */
    @Override
    public ActionType type() {
        return ActionType.COLLECT;
    }

    /**
     * @return Action操作附带执行的终端函数
     */
    @Override
    public Object actionFunc() {
        throw new UnsupportedOperationException("Collect类型的Action操作没有actionFunc！");
    }
}
