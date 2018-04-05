package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 表示空列表
 */
public class CommonNilMonadImpl<T> implements CommonMonad<T> {

    /**
     * 单例空CommonMonad
     */
    private static final CommonNilMonadImpl<?> EMPTY = new CommonNilMonadImpl<>();

    /**
     * 获取单例空Monad
     *
     * @param <T> 获取Monad的类型
     * @return 一个空的Monad
     */
    @SuppressWarnings("unchecked")
    public static <T> CommonNilMonadImpl<T> get() {
        return (CommonNilMonadImpl<T>) EMPTY;
    }

    /**
     * 单例模式私有构造方法
     */
    private CommonNilMonadImpl() {
    }

    @Override
    public CommonMonad<T> filter(@NotNull Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public boolean all(@NotNull Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public boolean any(@NotNull Predicate<? super T> predicate) {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> CommonMonad<R> map(@NotNull Function<? super T, ? extends R> mapFunc) {
        return (CommonMonad<R>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> CommonMonad<R> flatMap(@NotNull Function<? super T, ? extends Monad<? extends R>> mapFunc) {
        return (CommonMonad<R>) this;
    }

    @Override
    public CommonMonad<T> distinct() {
        return this;
    }

    @Override
    public CommonMonad<T> sortWith(@NotNull Comparator<T> comparator) {
        return this;
    }

    @Override
    public CommonMonad<T> drop() {
        return this;
    }

    @Override
    public CommonMonad<T> drop(int dropNum) {
        return this;
    }

    @Override
    public CommonMonad<T> dropRight() {
        return this;
    }

    @Override
    public CommonMonad<T> dropRight(int dropNum) {
        return this;
    }

    @Override
    public T reduce(@NotNull BiFunction<T, T, T> reduceFunc) {
        throw new UnsupportedOperationException("空集合");
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] toArray(@NotNull Class<T> arrayType) {
        return (T[]) Array.newInstance(arrayType, 0);
    }

    @Override
    public List<T> toList() {
        return new ArrayList<>(0);
    }

    @Override
    public Set<T> toSet() {
        return new HashSet<>(0);
    }

    @Override
    public Iterator<T> toIterator() {
        return Collections.emptyIterator();
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void foreach(@NotNull Consumer<? super T> forFunc) {
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
     * @return 返回本Monad的上层依赖
     */
    @Override
    public Optional<Dependency<?>> getFatherDependency() {
        return Optional.empty();
    }

    @Override
    public <K, V> PairMonad<K, V> mapToPair(@NotNull Function<? super T, ? extends Tuple<K, V>> mapFunc) {
        return PairNilMonadImpl.get();
    }

    @Override
    public T first() {
        throw new UnsupportedOperationException("空集合");
    }

    @Override
    public CommonMonad<T> take(int takeNum) {
        return this;
    }

    /**
     * 取出集合元素，直到func成立时
     *
     * @param func 取出元素直到成立的验证函数
     * @return 取出的元素组成的Monad对象
     */
    @Override
    public CommonMonad<T> takeWhile(@NotNull Predicate<T> func) {
        return this;
    }

    @Override
    public CommonMonad<? extends T> union(@NotNull CommonMonad<? extends T> other) {
        return other;
    }

    @Override
    public CommonMonad<T> sort() {
        return this;
    }
}
