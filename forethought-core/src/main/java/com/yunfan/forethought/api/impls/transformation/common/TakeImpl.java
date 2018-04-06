package com.yunfan.forethought.api.impls.transformation.common;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;

import java.util.function.Predicate;

/**
 * 取部分元素的转换操作实现
 *
 * @param <T> PairMonad中的元素类型
 */
public class TakeImpl<T> extends CommonMonadImpl<T> implements Transformation {

    /**
     * 取的元素程度的函数
     */
    private final Predicate<T> takeFunc;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father   上层依赖对象
     * @param takeFunc 取的元素程度的函数
     */
    public TakeImpl(Dependency<?> father, Predicate<T> takeFunc) {
        super(father);
        this.takeFunc = takeFunc;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.TAKE;
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    @Override
    protected Predicate<T> getTransformationalFunction() {
        return takeFunc;
    }
}
