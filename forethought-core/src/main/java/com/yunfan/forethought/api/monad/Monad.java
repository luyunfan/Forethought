package com.yunfan.forethought.api.monad;

import com.yunfan.forethought.enums.MonadType;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * 代表封装数据的惰性求值数据集合接口
 *
 * @param <T> 封装的数据类型
 */
public interface Monad<T> {

    /**
     * 检测所有元素是否都符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 是否所有元素都能通过检测
     */
    boolean all(@NotNull Predicate<? super T> predicate);

    /**
     * 检测任一元素是否符合输入验证函数的要求
     *
     * @param predicate 验证要求函数，输入当前元素，返回Boolean值
     * @return 只要有元素能通过检测就返回true，否则返回false
     */
    boolean any(@NotNull Predicate<? super T> predicate);

    /**
     * 取出集合第一个元素
     *
     * @return 第一个元素
     */
    T first();

    /**
     * 将集合中的所有数据相互进行归并操作，计算出归并的最终值
     *
     * @param reduceFunc 对每两个元素进行互相归并的函数
     * @return 归并后计算得出的结果
     */
    T reduce(@NotNull BiFunction<T, T, T> reduceFunc);

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
     * 将集合中所有元素转化为迭代器
     *
     * @return 一个 Iterator对象
     */
    Iterator<T> toIterator();

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
    void foreach(@NotNull Consumer<? super T> forFunc);

    /**
     * 代表集合是否为空
     *
     * @return 集合是否为空
     */
    boolean isEmpty();

    /**
     * 代表该Monad是否是数据源Monad
     *
     * @return 是否是包含数据的第一个Monad
     */
    boolean isDataSource();

    /**
     * @return 当前Monad的类型
     */
    MonadType monadType();
}
