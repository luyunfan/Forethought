package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.type.Tuple;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 表示空的PairMonad对象
 */
public class PairNilMonad<K, V> implements PairMonad<K, V> {

    @Override
    public <T> CommonMonad<T> mapToNormal(Function<? super Tuple<K, V>, ? extends T> mapFunc) {
        return new NilMonad<>();
    }

    @Override
    public PairMonad<K, V> reduceByKey(BiFunction<V, V, V> reduceFunc) {
        return this;
    }

    @Override
    public PairMonad<K, V> sortByKey() {
        return this;
    }

    @Override
    public <T> PairMonad<K, V> sortBy(Function<? super Tuple<K, V>, ? extends T> sortFunc) {
        return this;
    }

    @Override
    public Map<K, Long> countByKey() {
        return new HashMap<>(0);
    }

    @Override
    public Tuple<K, V> first() {
        throw new UnsupportedOperationException("空集合");
    }

    @Override
    public Map<K, V> take(int takeNum) {
        return new HashMap<>(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <NK> PairMonad<K, Tuple<V, NK>> join(PairMonad<K, NK> otherPairMonad) {
        return (PairMonad<K, Tuple<V, NK>>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public PairMonad<K, Collection<V>> groupByKey() {
        return (PairMonad<K, Collection<V>>) this;
    }

    @Override
    public PairMonad<? extends K, ? extends V> union(PairMonad<? extends K, ? extends V> other) {
        return other;
    }

    @Override
    public Monad<Tuple<K, V>> filter(Predicate<? super Tuple<K, V>> predicate) {
        return this;
    }

    @Override
    public boolean all(Predicate<? super Tuple<K, V>> predicate) {
        return false;
    }

    @Override
    public boolean any(Predicate<? super Tuple<K, V>> predicate) {
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Monad<R> map(Function<? super Tuple<K, V>, ? extends R> mapFunc) {
        return (Monad<R>) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <R> Monad<R> flatMap(Function<? super Tuple<K, V>, ? extends Monad<? extends R>> mapFunc) {
        return (Monad<R>) this;
    }

    @Override
    public Monad<Tuple<K, V>> distinct() {
        return this;
    }

    @Override
    public Monad<Tuple<K, V>> sortWith(Comparator<Tuple<K, V>> comparator) {
        return this;
    }

    @Override
    public Monad<Tuple<K, V>> drop() {
        return this;
    }

    @Override
    public Monad<Tuple<K, V>> drop(int dropNum) {
        return this;
    }

    @Override
    public Monad<Tuple<K, V>> dropRight() {
        return this;
    }

    @Override
    public Monad<Tuple<K, V>> dropRight(int dropNum) {
        return this;
    }

    @Override
    public Tuple<K, V> reduce(BiFunction<Tuple<K, V>, Tuple<K, V>, Tuple<K, V>> reduceFunc) {
        throw new UnsupportedOperationException("空集合");
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
    public void foreach(Consumer<? super Tuple<K, V>> forFunc) {

    }

    @Override
    public boolean isEmpty() {
        return true;
    }
}
