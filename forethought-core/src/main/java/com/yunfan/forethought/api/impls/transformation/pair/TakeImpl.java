package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.PairMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;
import com.yunfan.forethought.type.Tuple;

import java.util.function.Predicate;

/**
 * 取部分元素的转换操作实现
 *
 * @param <K> PairMonad中的Key元素类型
 * @param <V> PairMonad中的Value元素类型
 */
public class TakeImpl<K, V> extends PairMonadImpl<K, V> implements Transformation {

    /**
     * 取的元素的函数
     */
    private final Predicate<Tuple<K, V>> takeFunc;

    /**
     * 上层依赖对象
     */
    private final Dependency<?> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father   上层依赖对象
     * @param takeFunc 取的元素的函数
     */
    public TakeImpl(Dependency<?> father, Predicate<Tuple<K, V>> takeFunc) {
        super(father);
        this.father = father;
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
    protected Predicate<Tuple<K, V>> getTransformationalFunction() {
        return takeFunc;
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return "TakePairMonad:father dependency is" + father;
    }
}
