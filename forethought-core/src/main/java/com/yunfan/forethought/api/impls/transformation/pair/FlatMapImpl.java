package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.PairMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.enums.TransformationalType;

import java.util.function.Function;

/**
 * flatMap的中间转换操作
 *
 * @param <IN> 转换前的类型
 * @param <K>  转换后的Key类型
 * @param <V>  转换后的Value类型
 */
public class FlatMapImpl<IN, K, V> extends PairMonadImpl<K, V> implements Transformation {

    /**
     * 中间转换的函数
     */
    private Function<? super IN, ? extends PairMonad<? extends K, ? extends V>> mapFunc;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    public FlatMapImpl(Dependency<?> father, Function<? super IN, ? extends PairMonad<? extends K, ? extends V>> f) {
        super(father);
        mapFunc = f;
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    @Override
    protected Function<? super IN, ? extends PairMonad<? extends K, ? extends V>> getTransformationalFunction() {
        return mapFunc;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.FLATMAP;
    }
}