package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.enums.ShuffleType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * 合并Shuffle操作
 *
 * @param <K> PairMonad Key元素类型
 * @param <V> PairMonad Value元素类型
 */
public class AggregateShuffleImpl<K, V> extends ShuffleMonad<K, V> {


    /**
     * 元素合并函数
     */
    private final BiFunction<V, V, V> aggregateFunc;

    /**
     * 上层依赖对象
     */
    private final Dependency<?> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father        上层依赖对象
     * @param aggregateFunc 元素合并函数
     */
    public AggregateShuffleImpl(@NotNull Dependency<?> father, @NotNull BiFunction<V, V, V> aggregateFunc) {
        super(father);
        this.father = father;
        this.aggregateFunc = aggregateFunc;
    }

    /**
     * @return 当前Shuffle操作的类型
     */
    @Override
    public ShuffleType shuffleType() {
        return ShuffleType.AGGREGATE;
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    @Override
    protected BiFunction<V, V, V> getTransformationalFunction() {
        return aggregateFunc;
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return String.format("AggregatePairMonad:father dependency is %s%s", father, System.getProperty("line.separator"));
    }
}
