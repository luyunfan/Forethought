package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.iterators.RepeatableIterator;
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
    public List<T> drop() {
        return new ArrayList<>(0);
    }

    @Override
    public List<T> drop(long dropNum) {
        if (dropNum < 0) {
            throw new IllegalArgumentException("drop函数的参数不能小于0");
        }
        return new ArrayList<>(0);
    }

    @Override
    public List<T> dropRight() {
        return new ArrayList<>(0);
    }

    @Override
    public List<T> dropRight(long dropNum) {
        if (dropNum < 0) {
            throw new IllegalArgumentException("drop函数的参数不能小于0");
        }
        return new ArrayList<>(0);
    }

    @Override
    public T reduce(@NotNull BiFunction<T, T, T> reduceFunc) {
        return null;
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
     * @return Monad的数据源
     */
    @Override
    public Optional<RepeatableIterator<T>> getDataSource() {
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
    public <K, V> PairMonad<K, V> mapToPair(@NotNull Function<? super T, ? extends Tuple<K, V>> mapFunc) {
        return PairNilMonadImpl.get();
    }

    @Override
    public T first() {
        return null;
    }

    @Override
    public List<T> take(int takeNum) {
        if (takeNum < 0) {
            throw new IllegalArgumentException("take函数的参数不能小于0");
        }
        return new ArrayList<>(0);
    }

    /**
     * 取出集合元素，直到func成立时
     *
     * @param func 取出元素直到成立的验证函数
     * @return 取出的元素组成的Monad对象
     */
    @Override
    public List<T> takeWhile(@NotNull Predicate<T> func) {
        return new ArrayList<>(0);
    }

    @Override
    public CommonMonad<? extends T> union(@NotNull CommonMonad<? extends T> other) {
        return other;
    }

    @Override
    public CommonMonad<T> sort() {
        return this;
    }

    @Override
    public String toString() {
        return String.format("CommonNilMonad%s", System.getProperty("line.separator"));
    }
}
