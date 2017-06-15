package com.yunfan.forethought.api;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 代表封装数据的惰性求值数据集合接口
 *
 * @param <T> 封装的数据类型
 */
public interface Data<T> {

    /**
     * 过滤本数据元素，返回被过滤后的元素集合
     *
     * @param predicate 对每个元素执行的过滤函数，如果函数返回true，则保留该元素，否则放弃该元素
     * @return 被过滤后的元素集合
     */
    Data<T> filter(Predicate<? super T> predicate);

    /**
     * 对本集合每一个元素进行映射操作，将元素映射成不同的类型
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的元素
     * @param <R>     变换后生成的元素类型
     * @return 被变换后的元素集合
     */
    <R> Data<R> map(Function<? super T, ? extends R> mapFunc);


    /**
     * 对本集合进行去重操作
     *
     * @return 去重后的元素集合
     */
    Data<T> distinct();

    /**
     * 从集合起始端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    Data<T> take();

    /**
     * 从集合起始端取出一些元素组成集合
     *
     * @param takeNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    Data<T> take(int takeNum);

    /**
     * 从集合末端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    Data<T> takeRight();

    /**
     * 从集合末端取出一些元素组成集合
     *
     * @param takeNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    Data<T> takeRight(int takeNum);

    /**
     * 对其它同类型数据集合相连接，连接后将合并所有数据到一个新数据集合中
     *
     * @param other 其它同类型数据集合
     * @return 合并后的数据集合
     */
    Data<T> union(Data<? extends T> other);

    /**
     * 将集合中的所有数据相互进行归并操作，计算出归并的最终值
     *
     * @param reduceFunc 对每两个元素进行互相归并的函数
     * @return 归并后计算得出的结果
     */
    T reduce(BinaryOperator<T> reduceFunc);

    /**
     * 将集合中所有元素添加到数组中，返回数组
     *
     * @return 包含集合中所有数据的数组
     */
    T[] toArray();

    /**
     * 将集合中所有元素添加到List中，返回List
     *
     * @return 包含集合中所有数据的List
     */
    List<T> toList();

    /**
     * 将集合中所有元素添加到Set中，返回Set
     *
     * @return 包含集合中所有数据的Set
     */
    Set<T> toSet();

    /**
     * 返回本数据集合中元素数量的方法
     *
     * @return 数据集合中元素数量
     */
    long count();
}
