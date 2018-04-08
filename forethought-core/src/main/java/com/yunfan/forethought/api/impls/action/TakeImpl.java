package com.yunfan.forethought.api.impls.action;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.ActionType;
import com.yunfan.forethought.enums.TransformationalType;

import java.util.List;
import java.util.function.Predicate;

/**
 * 取部分元素的转换操作实现
 *
 * @param <T> PairMonad中的元素类型
 */
public class TakeImpl<T> implements Action<List<T>, Predicate<T>> {

    /**
     * 取的元素程度的函数
     */
    private final Predicate<T> takeFunc;


    /**
     * 注入上层依赖的构造函数
     *
     * @param takeFunc 取的元素程度的函数
     */
    public TakeImpl(Predicate<T> takeFunc) {
        this.takeFunc = takeFunc;
    }

    /**
     * @return Action操作的返回值类型
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<List<T>> resultType() {
        return (Class) List.class;
    }

    /**
     * @return Action操作本身的类型
     */
    @Override
    public ActionType type() {
        return ActionType.TAKE;
    }

    /**
     * @return Action操作附带执行的终端函数
     */
    @Override
    public Predicate<T> actionFunc() {
        return takeFunc;
    }
}
