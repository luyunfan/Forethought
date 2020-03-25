package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.iterators.RepeatableIterator;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 表示空的PairMonad对象
 */
public class PairNilMonadImpl<K, V> implements PairMonad<K, V> {

    /**
     * 单例空PairMonad
     */
    private static final PairMonad<?, ?> EMPTY = new PairNilMonadImpl<>();

    /**
     * 获取单例空PairMonad
     *
     * @param <K> 获取Monad的Key类型
     * @param <V> 获取Monad的Value类型
     * @return 一个空的PairMonad
     */
    @SuppressWarnings("unchecked")
    public static <K, V> PairMonad<K, V> get() {
        return (PairMonad<K, V>) EMPTY;
    }

    /**
     * 单例模式私有构造方法
     */
    private PairNilMonadImpl() {
    }

    @Override
    public <T> CommonMonad<T> mapToNormal(@NotNull Function<? super Tuple<K, V>, ? extends T> mapFunc) {
        return CommonNilMonadImpl.get();
    }

    @Override
    public PairMonad<K, V> reduceByKey(@NotNull BiFunction<V, V, V> reduceFunc) {
        return this;
    }

    @Override
    public PairMonad<K, V> sortByKey() {
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PairMonad<K, Long> countByKey() {
        return (PairMonad<K, Long>) EMPTY;
    }

    @Override
    public Tuple<K, V> first() {
        return null;
    }

    @Override
    public List<Tuple<K, V>> take(int takeNum) {
        if (takeNum < 0) {
            throw new IllegalArgumentException("take函数的参数不能小于0");
        }
        return new ArrayList<>(0);
    }

    /**
     * 对本集合每一个元素进行映射操作，将元素映射成不同的类型
     *
     * @param mapFunc 对每个元素执行的映射变换函数，函数输入参数为当前迭代元素，返回值为变换后的元素
     * @return 被变换后的元素集合
     */
    @SuppressWarnings("unchecked")
    @Override
    public <K1, V1> PairMonad<K1, V1> map(@NotNull Function<? super Tuple<K, V>, ? extends Tuple<K1, V1>> mapFunc) {
        return (PairMonad<K1, V1>) this;
    }

    /**
     * 取出集合元素，直到func成立时
     *
     * @param func 取出元素直到成立的验证函数
     * @return 取出的元素组成的Monad对象
     */
    @Override
    public List<Tuple<K, V>> takeWhile(@NotNull Predicate<Tuple<K, V>> func) {
        return new ArrayList<>(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <NK> PairMonad<K, Tuple<V, NK>> join(@NotNull PairMonad<K, NK> otherPairMonad) {
        return (PairMonad<K, Tuple<V, NK>>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PairMonad<K, Collection<V>> groupByKey() {
        return (PairMonad<K, Collection<V>>) this;
    }

    @Override
    public PairMonad<? extends K, ? extends V> union(@NotNull PairMonad<? extends K, ? extends V> other) {
        return other;
    }

    @Override
    public PairMonad<K, V> filter(@NotNull Predicate<? super Tuple<K, V>> predicate) {
        return this;
    }

    @Override
    public boolean all(@NotNull Predicate<? super Tuple<K, V>> predicate) {
        return false;
    }

    @Override
    public boolean any(@NotNull Predicate<? super Tuple<K, V>> predicate) {
        return false;
    }


    @SuppressWarnings("unchecked")
    @Override
    public <K1, V1> PairMonad<K1, V1> flatMap(@NotNull Function<? super Tuple<K, V>, ? extends PairMonad<? extends K1, ? extends V1>> mapFunc) {
        return (PairMonad<K1, V1>) this;
    }

    @Override
    public PairMonad<K, V> distinct() {
        return this;
    }

    @Override
    public PairMonad<K, V> sortWith(@NotNull Comparator<Tuple<K, V>> comparator) {
        return this;
    }

    @Override
    public List<Tuple<K, V>> drop() {
        return new ArrayList<>(0);
    }

    @Override
    public List<Tuple<K, V>> drop(long dropNum) {
        if (dropNum < 0) {
            throw new IllegalArgumentException("drop函数的参数不能小于0");
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<Tuple<K, V>> dropRight() {
        return new ArrayList<>(0);
    }

    @Override
    public List<Tuple<K, V>> dropRight(long dropNum) {
        if (dropNum < 0) {
            throw new IllegalArgumentException("drop函数的参数不能小于0");
        }
        return new ArrayList<>(0);
    }

    @Override
    public Tuple<K, V> reduce(@NotNull BiFunction<Tuple<K, V>, Tuple<K, V>, Tuple<K, V>> reduceFunc) {
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Tuple<K, V>[] toArray() {
        return (Tuple<K, V>[]) new Tuple[0];
    }

    @Override
    public List<Tuple<K, V>> toList() {
        return new ArrayList<>(0);
    }

    @Override
    public Set<Tuple<K, V>> toSet() {
        return new HashSet<>(0);
    }

    @Override
    public Iterator<Tuple<K, V>> toIterator() {
        return Collections.emptyIterator();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void foreach(@NotNull Consumer<? super Tuple<K, V>> forFunc) {

    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    /**
     * 代表该Monad是否是数据源Monad
     *
     * @return 是否是包含数据的第一个Monad
     */
    @Override
    public boolean isDataSource() {
        return true;
    }

    /**
     * @return Monad的数据源
     */
    @Override
    public Optional<RepeatableIterator<Tuple<K, V>>> getDataSource() {
        return Optional.empty();
    }

    /**
     * @return 返回本Monad的上层依赖
     */
    @Override
    public Optional<Dependency<?>> getFatherDependency() {
        return Optional.empty();
    }

    @Override
    public String toString() {
        return String.format("PairNilMonad%s", System.getProperty("line.separator"));
    }
}
