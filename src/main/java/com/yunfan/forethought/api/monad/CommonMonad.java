package com.yunfan.forethought.api.monad;

import com.yunfan.forethought.type.Tuple;

import java.util.List;
import java.util.function.Function;

/**
 * 代表普通单元素Monad
 */
public interface CommonMonad<T> extends Monad<T> {
    /**
     * 将Monad映射为键值对Monad结构
     *
     * @param mapFunc 对每个元素执行的映射变换函数，必须映射为Tuple类型
     * @param <K>     Tuple的Key类型
     * @param <V>     Tuple的Value类型
     * @return 被变换后的PairMonad集合
     */
    <K, V> PairMonad<K, V> mapToPair(Function<? super T, ? extends Tuple<K, V>> mapFunc);

    /**
     * 取出集合第一个元素
     *
     * @return 第一个元素
     */
    T first();

    /**
     * 取出集合一些元素组成List返回
     *
     * @param takeNum 取出的元素数量
     * @return 取出的元素组成的List对象
     */
    List<T> take(int takeNum);

    /**
     * 对其它同类型数据集合相连接，连接后将合并所有数据到一个新数据集合中
     *
     * @param other 其它同类型数据集合
     * @return 合并后的数据集合
     */
    CommonMonad<? extends T> union(CommonMonad<? extends T> other);

    /**
     * 对本集合进行排序操作
     *
     * @return 排序后的集合
     */
    CommonMonad<T> sort();

    /**
     * 将集合中所有元素添加到数组中，返回数组
     *
     * @param arrayType 代表数组中元素类型
     * @return 包含集合中所有数据的数组
     */
    T[] toArray(Class<T> arrayType);
}
