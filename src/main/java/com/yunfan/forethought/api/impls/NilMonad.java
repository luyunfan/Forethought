package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.monad.Monad;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 表示空列表
 */
public class NilMonad<T> implements Monad<T> {
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
    public <R> Monad<R> map(Function<? super T, ? extends R> mapFunc) {
        return (Monad<R>) this;
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
        return null;
    }

    @Override
    public T[] toArray() {
        return (T[]) new Object[0];
    }

    @Override
    public List<T> toList() {
        return null;
    }

    @Override
    public Set<T> toSet() {
        return null;
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
