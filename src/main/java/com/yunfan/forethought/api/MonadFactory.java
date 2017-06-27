package com.yunfan.forethought.api;

import com.yunfan.forethought.api.impls.NilMonad;
import com.yunfan.forethought.api.impls.SingleMonadFactory;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.type.Tuple;

import java.io.InputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * 代表创建Monad结构的工厂接口
 */
public interface MonadFactory {

    /**
     * 创建单线程Monad工厂对象
     *
     * @return 一个单线程Monad工厂对象
     */
    static MonadFactory createSingleMonadFactory() {
        return new SingleMonadFactory();
    }

    /**
     * 产生一个空的CommonMonad
     *
     * @param <T> Monad类型
     * @return 一个空的CommonMonad
     */
    <T> CommonMonad<T> commonNil();

    /**
     * 通过Collection集合获取Monad
     *
     * @param collection 输入的Collection集合
     * @param <T>        集合中元素的数据类型
     * @return 包含集合内容的Monad
     */
    <T> Monad<T> from(Collection<T> collection);

    /**
     * 通过数组获取Monad
     *
     * @param array 输入的数组
     * @param <T>   数组中元素的数据类型
     * @return 包含数组内容的Monad
     */
    <T> Monad<T> from(T[] array);

    /**
     * 通过Stream接口的对象获取Monad
     *
     * @param stream 输入的Stream对象
     * @param <T>    Stream中对象的数据类型
     * @return 包含Stream中所有内容的Monad
     */
    <T> Monad<T> from(Stream<T> stream);

    /**
     * 通过迭代器获取Monad
     *
     * @param iterator 输入的迭代器对象
     * @param <T>      迭代器中对象的数据类型
     * @return 包含迭代器中所有内容的Monad
     */
    <T> Monad<T> from(Iterator<T> iterator);

    /**
     * 通过文件读取获得Monad
     *
     * @param fileName 文件路径
     * @param creator  将文件映射为T类型对象的方法
     * @param <T>      产生的元素类型
     * @return 包含文件中所有内容的Monad
     */
    <T> Monad<T> fromFile(String fileName, Function<InputStream, T> creator);

    /**
     * 通过读取文本文件获得Monad
     *
     * @param fileName 文本路径
     * @return 包含所有文本字符串的Monad
     */
    Monad<String> fromText(String fileName);

    /**
     * 通过文件读取获得PairMonad
     *
     * @param fileName 文件路径
     * @param creator  将文件流映射为Tuple对象的方法
     * @param <K>      Pair的Key类型
     * @param <V>      Pair的Value类型
     * @return 包含所有数据的PairMonad
     */
    <K, V> PairMonad<K, V> fromPairFile(String fileName, Function<InputStream, Tuple<K, V>> creator);

    /**
     * 通过Map获得PairMonad
     *
     * @param map Map对象
     * @param <K> Map和Pair的Key类型
     * @param <V> Map和Pair的Value类型
     * @return 包含Map中所有内容的PairMonad
     */
    <K, V> PairMonad<K, V> from(Map<K, V> map);

    /**
     * 通过单个值产生Monad
     *
     * @param value Monad中包含的值
     * @param <T>   值的类型
     * @return 包含这个值的Monad
     */
    <T> Monad<T> of(T value);

    /**
     * 通过多个值产生Monad
     *
     * @param value Monad中包含的值
     * @param <T>   值的类型
     * @return 包含这些值的Monad
     */
    @SuppressWarnings("unchecked")
    <T> Monad<T> of(T... value);


    /**
     * 产生一个空Monad
     *
     * @return 一个空Monad
     */
    <T> Monad<T> of();

//    static MonadFactory createSingleMonadFactory(){
//
//    }
}
