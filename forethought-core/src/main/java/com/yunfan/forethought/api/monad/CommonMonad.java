package com.yunfan.forethought.api.monad;

import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.enums.MonadType;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

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
    <K, V> PairMonad<K, V> mapToPair(@NotNull Function<? super T, ? extends Tuple<K, V>> mapFunc);

    /**
     * 取出集合第一个元素
     *
     * @return 第一个元素
     */
    @Override
    T first();

    /**
     * 取出集合一些元素组成List返回
     *
     * @param takeNum 取出的元素数量
     * @return 取出的元素组成的Monad对象
     */
    List<T> take(int takeNum);

    /**
     * 对本集合每一个元素进行映射操作，将元素映射成不同的类型
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的元素
     * @param <R>     变换后生成的元素类型
     * @return 被变换后的元素集合
     */
    <R> CommonMonad<R> map(@NotNull Function<? super T, ? extends R> mapFunc);

    /**
     * 过滤本数据元素，返回被过滤后的元素集合
     *
     * @param predicate 对每个元素执行的过滤函数，如果函数返回true，则保留该元素，否则放弃该元素
     * @return 被过滤后的元素集合
     */
    CommonMonad<T> filter(@NotNull Predicate<? super T> predicate);

    /**
     * 对本集合进行去重操作
     *
     * @return 去重后的元素集合
     */
    CommonMonad<T> distinct();

    /**
     * 对本集合中每一个元素执行映射操作，映射后为同样的集合，本方法将会把这些类似的集合压扁，将元素取出，整合为同一个集合返回
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的包含元素的Monad对象
     * @param <R>     被转换后生成的元素类型
     * @return 被变换后的元素集合
     */
    <R> CommonMonad<R> flatMap(@NotNull Function<? super T, ? extends Monad<? extends R>> mapFunc);

    /**
     * 取出集合元素，直到func成立时
     *
     * @param func 取出元素直到成立的验证函数
     * @return 取出的元素组成的Monad对象
     */
    List<T> takeWhile(Predicate<T> func);

    /**
     * 对其它同类型数据集合相连接，连接后将合并所有数据到一个新数据集合中
     *
     * @param other 其它同类型数据集合
     * @return 合并后的数据集合
     */
    CommonMonad<? extends T> union(@NotNull CommonMonad<? extends T> other);

    /**
     * 对本集合进行排序操作，自定义元素排序方法
     *
     * @param comparator 自定义comparator
     * @return 排好序的集合
     */
    CommonMonad<T> sortWith(@NotNull Comparator<T> comparator);

    /**
     * 从集合起始端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    List<T> drop();

    /**
     * 从集合起始端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    List<T> drop(long dropNum);

    /**
     * 从集合末端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    List<T> dropRight();

    /**
     * 从集合末端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    List<T> dropRight(long dropNum);

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
    T[] toArray(@NotNull Class<T> arrayType);

    /**
     * 获取一个空Monad
     *
     * @return 一个空的Monad
     */
    default CommonMonad<T> empty() {
        return MonadFactory.createMonadFactory().commonNil();
    }

    /**
     * @return 返回当前Monad的类型
     */
    @Override
    default MonadType monadType() {
        return MonadType.COMMON;
    }
}
