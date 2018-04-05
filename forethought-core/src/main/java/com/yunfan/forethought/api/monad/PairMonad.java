package com.yunfan.forethought.api.monad;

import com.yunfan.forethought.api.impls.PairNilMonadImpl;
import com.yunfan.forethought.enums.MonadType;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Comparator;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

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
    <T> CommonMonad<T> mapToNormal(@NotNull Function<? super Tuple<K, V>, ? extends T> mapFunc);

    /**
     * @return 当前PairMonad的所有Key组成的Monad
     */
    default CommonMonad<K> keys() {
        return this.mapToNormal(Tuple::key);
    }

    /**
     * @return 当前PairMonad的所有Value组成的Monad
     */
    default CommonMonad<V> values() {
        return this.mapToNormal(Tuple::value);
    }

    /**
     * 对同一Key的元素进行reduce归并操作，得到归并后的PairMonad
     *
     * @param reduceFunc 进行归并的函数，输入参数为两两归并的两个参数，返回同类型归并后的结果
     * @return 归并后的PairMonad
     */
    PairMonad<K, V> reduceByKey(@NotNull BiFunction<V, V, V> reduceFunc);

    /**
     * 根据Key进行排序
     *
     * @return 根据Key排好序的集合
     */
    PairMonad<K, V> sortByKey();

    /**
     * 计算同一Key的元素数量，得到(Key,Number)键值对集合
     *
     * @return 一个包含(键, 数量)的Map对象
     */
    PairMonad<K, Long> countByKey();

    /**
     * 取出集合第一个元素
     *
     * @return 第一个元素组成的Tuple
     */
    @Override
    Tuple<K, V> first();

    /**
     * 取出集合一些元素组成Map返回
     *
     * @param takeNum 取出的元素数量
     * @return 取出的元素组成的Map对象
     */
    PairMonad<K, V> take(int takeNum);

    /**
     * 对本集合每一个元素进行映射操作，将元素映射成不同的类型
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的元素
     * @param <K1>    变换后生成的元素Key类型
     * @param <V1>    变换后生成的元素Value类型
     * @return 被变换后的元素集合
     */
    <K1, V1> PairMonad<K1, V1> map(@NotNull Function<? super Tuple<K, V>, ? extends Tuple<K1, V1>> mapFunc);

    /**
     * 对本集合中每一个元素执行映射操作，映射后为同样的集合，本方法将会把这些类似的集合压扁，将元素取出，整合为同一个集合返回
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的包含元素的Monad对象
     * @param <K1>    变换后生成的元素Key类型
     * @param <V1>    变换后生成的元素Value类型
     * @return 被变换后的元素集合
     */
    <K1, V1> PairMonad<K1, V1> flatMap(@NotNull Function<? super Tuple<K, V>, ? extends PairMonad<? extends K1, ? extends V1>> mapFunc);

    /**
     * 过滤本数据元素，返回被过滤后的元素集合
     *
     * @param predicate 对每个元素执行的过滤函数，如果函数返回true，则保留该元素，否则放弃该元素
     * @return 被过滤后的元素集合
     */
    PairMonad<K, V> filter(@NotNull Predicate<? super Tuple<K, V>> predicate);

    /**
     * 对本集合进行排序操作，自定义元素排序方法
     *
     * @param comparator 自定义comparator
     * @return 排好序的集合
     */
    PairMonad<K, V> sortWith(@NotNull Comparator<Tuple<K, V>> comparator);

    /**
     * 取出集合元素，直到func成立时
     *
     * @param func 取出元素直到成立的验证函数
     * @return 取出的元素组成的Monad对象
     */
    PairMonad<K, V> takeWhile(Predicate<Tuple<K, V>> func);

    /**
     * 从集合起始端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    PairMonad<K, V> drop();

    /**
     * 从集合起始端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    PairMonad<K, V> drop(int dropNum);

    /**
     * 从集合末端取出一个元素组成集合
     *
     * @return 取出一个元素后的元素集合
     */
    PairMonad<K, V> dropRight();

    /**
     * 从集合末端取出一些元素组成集合
     *
     * @param dropNum 需要取出的元素数量
     * @return 取出一些元素后的元素集合
     */
    PairMonad<K, V> dropRight(int dropNum);

    /**
     * 对其它Key相同的PairMonad执行内连接操作，得到连接后的PairMonad对象
     *
     * @param otherPairMonad 相同Key的其它PairMonad对象
     * @param <NK>           其它对象的Value类型
     * @return 连接后的(Key, Tuple) PairMonad对象
     */
    <NK> PairMonad<K, Tuple<V, NK>> join(@NotNull PairMonad<K, NK> otherPairMonad);

    /**
     * 对本集合进行去重操作
     *
     * @return 去重后的元素集合
     */
    PairMonad<K, V> distinct();

    /**
     * 对同一Key的元素执行group分组操作，得到分组后的PairMonad
     *
     * @return 进行分组后的(Key, Collection)类型的PairMonad对象
     */
    PairMonad<K, Collection<V>> groupByKey();

    /**
     * 对其它同类型数据集合相连接，连接后将合并所有数据到一个新数据集合中
     *
     * @param other 其它同类型数据集合
     * @return 合并后的数据集合
     */
    PairMonad<? extends K, ? extends V> union(@NotNull PairMonad<? extends K, ? extends V> other);

    /**
     * 将集合中所有元素添加到数组中，返回数组
     *
     * @return 包含集合中所有数据的数组
     */
    Tuple<K, V>[] toArray();

    /**
     * @return 返回当前Monad的类型
     */
    @Override
    default MonadType monadType() {
        return MonadType.PAIR;
    }

    /**
     * 获取一个空Monad
     *
     * @return 一个空的Monad
     */
    default PairMonad<K, V> empty() {
        return PairNilMonadImpl.get();
    }
}
