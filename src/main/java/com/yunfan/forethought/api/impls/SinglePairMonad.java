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
 * Pair单线程Monad
 */
class SinglePairMonad<K, V> implements PairMonad<K, V> {
    @Override
    public Monad<Tuple<K, V>> filter(Predicate<? super Tuple<K, V>> predicate) {
        return null;
    }

    @Override
    public boolean all(Predicate<? super Tuple<K, V>> predicate) {
        return false;
    }

    @Override
    public boolean any(Predicate<? super Tuple<K, V>> predicate) {
        return false;
    }

    @Override
    public <R> Monad<R> map(Function<? super Tuple<K, V>, ? extends R> mapFunc) {
        return null;
    }

    @Override
    public <R> Monad<R> flatMap(Function<? super Tuple<K, V>, ? extends Monad<? extends R>> mapFunc) {
        return null;
    }

    @Override
    public Monad<Tuple<K, V>> distinct() {
        return null;
    }

    @Override
    public Monad<Tuple<K, V>> sortWith(Comparator<Tuple<K, V>> comparator) {
        return null;
    }

    @Override
    public Monad<Tuple<K, V>> drop() {
        return null;
    }

    @Override
    public Monad<Tuple<K, V>> drop(int dropNum) {
        return null;
    }

    @Override
    public Monad<Tuple<K, V>> dropRight() {
        return null;
    }

    @Override
    public Monad<Tuple<K, V>> dropRight(int dropNum) {
        return null;
    }

    @Override
    public Tuple<K, V> reduce(BiFunction<Tuple<K, V>, Tuple<K, V>, Tuple<K, V>> reduceFunc) {
        return null;
    }

    @Override
    public Tuple<K, V>[] toArray() {
        return new Tuple[0];
    }

    @Override
    public List<Tuple<K, V>> toList() {
        return null;
    }

    @Override
    public Set<Tuple<K, V>> toSet() {
        return null;
    }

    @Override
    public Iterator<Tuple<K, V>> toIterator() {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void foreach(Consumer<? super Tuple<K, V>> forFunc) {

    }

    @Override
    public <T> CommonMonad<T> mapToNormal(Function<? super Tuple<K, V>, ? extends T> mapFunc) {
        return null;
    }

    @Override
    public PairMonad<K, V> reduceByKey(BiFunction<V, V, V> reduceFunc) {
        return null;
    }

    @Override
    public PairMonad<K, V> sortByKey() {
        return null;
    }

    @Override
    public <T> PairMonad<K, V> sortBy(Function<? super Tuple<K, V>, ? extends T> sortFunc) {
        return null;
    }

    @Override
    public Map<K, Long> countByKey() {
        return null;
    }

    @Override
    public Tuple<K, V> first() {
        return null;
    }

    @Override
    public Map<K, V> take(int takeNum) {
        return null;
    }

    @Override
    public <NK> PairMonad<K, Tuple<V, NK>> join(PairMonad<K, NK> otherPairMonad) {
        return null;
    }

    @Override
    public PairMonad<K, Collection<V>> groupByKey() {
        return null;
    }

    @Override
    public PairMonad<K, V> union(PairMonad<? extends K, ? extends V> other) {
        return null;
    }
}
