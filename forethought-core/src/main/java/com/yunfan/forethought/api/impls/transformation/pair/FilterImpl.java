package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.PairMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * 代表进行filter转换操作的Monad
 *
 * @param <K> PairMonad的Key
 * @param <V> PairMonad的Value
 */
public class FilterImpl<K, V> extends PairMonadImpl<K, V> implements Transformation {

    /**
     * 实际filter执行的函数
     */
    private Predicate<? super Tuple<K, V>> filterFunc;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    private FilterImpl(@NotNull Dependency<Tuple<K, V>> father) {
        super(father);
    }

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     * @param f      实际执行的filter函数内容
     */
    public FilterImpl(@NotNull Dependency<Tuple<K, V>> father, @NotNull Predicate<? super Tuple<K, V>> f) {
        this(father);
        filterFunc = f;
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    @Override
    protected Predicate<? super Tuple<K, V>> getTransformationalFunction() {
        return filterFunc;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.FILTER;
    }
}