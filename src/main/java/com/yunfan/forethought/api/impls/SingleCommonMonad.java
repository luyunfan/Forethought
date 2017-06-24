package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.type.Tuple;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 普通的单线程Monad
 */
class SingleCommonMonad<T> implements CommonMonad<T> {

    /**
     * 代表内部元素类型
     */
    private Class<T> elementClass;

    /**
     * 代表第一个元素
     */
    private T head;

    /**
     * 创建单个元素的Monad
     *
     * @param clazz 元素类型
     * @param first 首元素
     */
    SingleCommonMonad(Class<T> clazz, T first) {
        elementClass = clazz;
        head = first;
    }

    private boolean isEmpty() {
        return true;
    }

    @Override
    public <K, V> PairMonad<K, V> mapToPair(Function<? super T, ? extends Tuple<K, V>> mapFunc) {
        return null;
    }

    @Override
    public T first() {
        return head;
    }

    @Override
    public List<T> take(int takeNum) {
        return null;
    }

    @Override
    public CommonMonad<T> union(Monad<? extends T> other) {
        return null;
    }

    @Override
    public CommonMonad<T> sort() {
        return null;
    }

    @Override
    public Monad<T> filter(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public boolean all(Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public boolean any(Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public <R> Monad<R> map(Function<? super T, ? extends R> mapFunc) {
        return null;
    }

    @Override
    public <R> Monad<R> flatMap(Function<? super T, ? extends Monad<? extends R>> mapFunc) {
        return null;
    }

    @Override
    public Monad<T> distinct() {
        return null;
    }

    @Override
    public Monad<T> sortWith(Comparator<T> comparator) {
        return null;
    }

    @Override
    public Monad<T> drop() {
        return null;
    }

    @Override
    public Monad<T> drop(int dropNum) {
        return null;
    }

    @Override
    public Monad<T> dropRight() {
        return null;
    }

    @Override
    public Monad<T> dropRight(int dropNum) {
        return null;
    }

    @Override
    public T reduce(BiFunction<T, T, T> reduceFunc) {
        return null;
    }

    /**
     * 将Monad元素转换为数组
     *
     * @return 包含所有元素的数组
     * @throws ArithmeticException 如果内部存储数据太大，Array无法承载将抛出ArithmeticException异常
     */
    @SuppressWarnings("unchecked")
    @Override
    public T[] toArray() {
        return (T[]) Array.newInstance(elementClass, Math.toIntExact(count()));
    }

    @Override
    public List<T> toList() {
        List<T> list = new ArrayList<>();
        toIterator().forEachRemaining(list::add);
        return list;
    }

    @Override
    public Set<T> toSet() {
        Set<T> set = new HashSet<>();
        toIterator().forEachRemaining(set::add);
        return set;
    }

    @Override
    public Iterator<T> toIterator() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void foreach(Consumer<? super T> forFunc) {

    }
}