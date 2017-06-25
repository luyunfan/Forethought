package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.type.Tuple;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 表示空列表
 */
public class NilMonad<T> implements CommonMonad<T> {

    private Class<T> elementType;

    NilMonad(Class<T> elementClass) {
        elementType = elementClass;
    }

    @Override
    public Monad<T> filter(Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public boolean all(Predicate<? super T> predicate) {
        return false;
    }

    @Override
    public boolean any(Predicate<? super T> predicate) {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> CommonMonad<R> map(Function<? super T, ? extends R> mapFunc) {
//        try {
//            return new NilMonad<R>(mapFunc(elementType.newInstance()).getClass());
//        } catch (InstantiationException | IllegalAccessException e) {
//            return (CommonMonad<R>) this;
//            e.printStackTrace();
//        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Monad<R> flatMap(Function<? super T, ? extends Monad<? extends R>> mapFunc) {
        return (Monad<R>) this;
    }

    @Override
    public Monad<T> distinct() {
        return this;
    }

    @Override
    public Monad<T> sortWith(Comparator<T> comparator) {
        return this;
    }

    @Override
    public Monad<T> drop() {
        return this;
    }

    @Override
    public Monad<T> drop(int dropNum) {
        return this;
    }

    @Override
    public Monad<T> dropRight() {
        return this;
    }

    @Override
    public Monad<T> dropRight(int dropNum) {
        return this;
    }

    @Override
    public T reduce(BiFunction<T, T, T> reduceFunc) {
        throw new UnsupportedOperationException("空集合");
    }

    @SuppressWarnings("unchecked")
    @Override
    public T[] toArray() {
        return (T[]) Array.newInstance(elementType, 0);
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
    public void foreach(Consumer<? super T> forFunc) {
    }

    @Override
    public <K, V> PairMonad<K, V> mapToPair(Function<? super T, ? extends Tuple<K, V>> mapFunc) {
        return new PairNilMonad<>();
    }

    @Override
    public T first() {
        throw new UnsupportedOperationException("空集合");
    }

    @Override
    public List<T> take(int takeNum) {
        return new ArrayList<>(0);
    }

    @Override
    public CommonMonad<? extends T> union(CommonMonad<? extends T> other) {
        return other;
    }

    @Override
    public CommonMonad<T> sort() {
        return this;
    }
}
