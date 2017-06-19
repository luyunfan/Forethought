package com.yunfan.forethought.api;

import com.yunfan.forethought.type.Tuple;

import java.util.function.Function;

/**
 * 代表普通单元素Monad
 */
public interface SingleMonad<T> extends Monad<T> {
    /**
     * 将Monad映射为键值对Monad结构
     *
     * @param mapFunc 对每个元素执行的映射变换函数，必须映射为Tuple类型
     * @param <K>     Tuple的Key类型
     * @param <V>     Tuple的Value类型
     * @return 被变换后的PairMonad集合
     */
    <K, V> PairMonad<K, V> mapToPair(Function<? super T, ? extends Tuple<K, V>> mapFunc);
}
