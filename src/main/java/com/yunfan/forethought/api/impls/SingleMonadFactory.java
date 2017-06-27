package com.yunfan.forethought.api.impls;

import com.yunfan.forethought.api.MonadFactory;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.type.Tuple;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 创建单线程Monad的工厂类
 */
public final class SingleMonadFactory implements MonadFactory {

    public SingleMonadFactory() {
    }

    @Override
    public <T> CommonMonad<T> commonNil() {
        return NilMonad.get();
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Monad<T> from(Collection<T> collection) {
        return from((T[]) collection.toArray());
    }

    @Override
    public <T> CommonMonad<T> from(T[] array) {
        CommonMonad<T> newMonad = commonNil();  //最后的Monad为空
        for (int i = array.length - 1; i >= 0; i--)
            newMonad = new SingleCommonMonad<>(array[i], newMonad);
        return newMonad;
    }

    @Override
    public <T> Monad<T> from(Stream<T> stream) {
        return from(stream.collect(Collectors.toList()));
    }

    @Override
    public <T> Monad<T> from(Iterator<T> iterator) {
        return null;
    }

    @Override
    public <T> Monad<T> fromFile(String fileName, Function<InputStream, T> creator) {
        return null;
    }

    @Override
    public Monad<String> fromText(String fileName) {
        return null;
    }

    @Override
    public <K, V> PairMonad<K, V> fromPairFile(String fileName, Function<InputStream, Tuple<K, V>> creator) {
        return null;
    }

    @Override
    public <K, V> PairMonad<K, V> from(Map<K, V> map) {
        return null;
    }

    @Override
    public <T> Monad<T> of(T value) {
        return new SingleCommonMonad<>(value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Monad<T> of(T... value) {
        return from(value);
    }

    @Override
    public <T> Monad<T> of() {
        return NilMonad.get();
    }
}
