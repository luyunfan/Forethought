package com.yunfan.forethought.api;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 代表封装数据的惰性求值数据集合接口
 *
 * @param <T> 封装的数据类型
 */
public interface Monad<T> {

    /**
     * 过滤本数据元素，返回被过滤后的元素集合
     *
     * @param predicate 对每个元素执行的过滤函数，如果函数返回true，则保留该元素，否则放弃该元素
     * @return 被过滤后的元素集合
     */
    Monad<T> filter(Predicate<? super T> predicate);

    /**
     * 检测所有元素是否都符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 是否所有元素都能通过检测
     */
    boolean all(Predicate<? super T> predicate);

    /**
     * 检测任一元素是否符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 只要有元素能通过检测就返回true，否则返回false
     */
    boolean any(Predicate<? super T> predicate);

    /**
     * 对本集合每一个元素进行映射操作，将元素映射成不同的类型
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的元素
     * @param <R>     变换后生成的元素类型
     * @return 被变换后的元素集合
     */
    <R> Monad<R> map(Function<? super T, ? extends R> mapFunc);


    /**
     * 对本集合中每一个元素执行映射操作，映射后为同样的集合，本方法将会把这些类似的集合压扁，将元素取出，整合为同一个集合返回
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的包含元素的Monad对象
     * @param <R>     被转换后生成的元素类型
     * @return 被变换后的元素集合
     */
    <R> Monad<R> flatMap(Function<? super T, ? extends Monad<? extends R>> mapFunc);

    /**
     * 对本集合进行去重操作
     *
     * @return 去重后的元素集合
     */
    Monad<T> distinct();

    /**
     * 从集合起始端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    Monad<T> drop();

    /**
     * 从集合起始端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    Monad<T> drop(int dropNum);

    /**
     * 从集合末端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    Monad<T> dropRight();

    /**
     * 从集合末端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    Monad<T> dropRight(int dropNum);

    /**
     * 将集合中的所有数据相互进行归并操作，计算出归并的最终值
     *
     * @param reduceFunc 对每两个元素进行互相归并的函数
     * @return 归并后计算得出的结果
     */
    T reduce(BiFunction<T, T, T> reduceFunc);

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

    /**
     * 遍历数据集合中的所有元素
     *
     * @param forFunc 遍历时执行的操作
     */
    void foreach(Consumer<? super T> forFunc);
}
