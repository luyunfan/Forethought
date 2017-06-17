package com.yunfan.forethought.api;

import com.yunfan.forethought.type.Tuple;

import java.util.Collection;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * 代表键值对(Key, Value)的Data结构
 */
public interface PairData<K, V> extends Data {

    /**
     * 将PairData转换为普通的Data结构
     *
     * @param mapFunc 进行转换映射的函数，输入参数为Tuple，返回值为单一类型的值
     * @param <T>     Data结构的数据类型
     * @return 返回包含映射函数返回值的Data对象
     */
    <T> Data<T> mapToNormal(Function<? super Tuple<K, V>, ? extends T> mapFunc);

    /**
     * 对同一Key的元素进行reduce归并操作，得到归并后的PairData
     *
     * @param reduceFunc 进行归并的函数，输入参数为两两归并的两个参数，返回同类型归并后的结果
     * @return 归并后的PairData
     */
    PairData<K, V> reduceByKey(BiFunction<V, V, V> reduceFunc);

    /**
     * 计算同一Key的元素数量，得到(Key,Number)键值对集合
     *
     * @return 一个包含(键, 数量)的Map对象
     */
    Map<K, Long> countByKey();

    /**
     * 对其它Key相同的PairData执行内连接操作，得到连接后的PairData对象
     *
     * @param otherPairData 相同Key的其它PairData对象
     * @param <NK>          其它对象的Value类型
     * @return 连接后的(Key, Tuple) PairData对象
     */
    <NK> PairData<K, Tuple<V, NK>> join(PairData<K, NK> otherPairData);

    /**
     * 对同一Key的元素执行group分组操作，得到分组后的PairData
     *
     * @return 进行分组后的(Key, Collection)类型的PairData对象
     */
    PairData<K, Collection<V>> groupByKey();
}
