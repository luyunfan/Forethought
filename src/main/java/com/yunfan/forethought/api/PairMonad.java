package com.yunfan.forethought.api;

import com.yunfan.forethought.type.Tuple;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 代表键值对(Key, Value)的Monad结构
 */
public interface PairMonad<K, V> extends Monad<Tuple<K, V>> {

    /**
     * 将PairMonad转换为普通的Monad结构
     *
     * @param mapFunc 进行转换映射的函数，输入参数为Tuple，返回值为单一类型的值
     * @param <T>     Monad结构的数据类型
     * @return 返回包含映射函数返回值的Monad对象
     */
    <T> SingleMonad<T> mapToNormal(Function<? super Tuple<K, V>, ? extends T> mapFunc);

    /**
     * 对同一Key的元素进行reduce归并操作，得到归并后的PairMonad
     *
     * @param reduceFunc 进行归并的函数，输入参数为两两归并的两个参数，返回同类型归并后的结果
     * @return 归并后的PairMonad
     */
    PairMonad<K, V> reduceByKey(BiFunction<V, V, V> reduceFunc);

    /**
     * 计算同一Key的元素数量，得到(Key,Number)键值对集合
     *
     * @return 一个包含(键, 数量)的Map对象
     */
    Map<K, Long> countByKey();

    /**
     * 对其它Key相同的PairMonad执行内连接操作，得到连接后的PairMonad对象
     *
     * @param otherPairMonad 相同Key的其它PairMonad对象
     * @param <NK>           其它对象的Value类型
     * @return 连接后的(Key, Tuple) PairMonad对象
     */
    <NK> PairMonad<K, Tuple<V, NK>> join(PairMonad<K, NK> otherPairMonad);

    /**
     * 对同一Key的元素执行group分组操作，得到分组后的PairMonad
     *
     * @return 进行分组后的(Key, Collection)类型的PairMonad对象
     */
    PairMonad<K, Collection<V>> groupByKey();
}
