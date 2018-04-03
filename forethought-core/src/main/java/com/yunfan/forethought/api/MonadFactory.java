package com.yunfan.forethought.api;

import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.CommonNilMonadImpl;
import com.yunfan.forethought.api.impls.PairMonadImpl;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.api.task.JobSubmitter;
import com.yunfan.forethought.enums.ExecutorType;
import com.yunfan.forethought.iterators.FileRepeatableIterator;
import com.yunfan.forethought.iterators.MemoryRepeatableIterator;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 代表创建Monad结构的工厂抽象类
 */
public class MonadFactory {

    /**
     * 提交任务的提交对象
     */
    private static final JobSubmitter submitter = JobSubmitter.INSTANCE;

    /**
     * 创建Monad工厂对象
     *
     * @return 一个Monad工厂对象
     */
    public static MonadFactory createMonadFactory() {
        return new MonadFactory();
    }

    /**
     * @return 当前执行引擎类型
     */
    public ExecutorType executorType() {
        return submitter.executorType();
    }

    /**
     * 产生一个空的CommonMonad
     *
     * @param <T> Monad类型
     * @return 一个空的CommonMonad
     */
    public <T> CommonMonad<T> commonNil() {
        return CommonNilMonadImpl.get();
    }

    /**
     * 通过Collection集合获取Monad
     *
     * @param collection 输入的Collection集合
     * @param <T>        集合中元素的数据类型
     * @return 包含集合内容的Monad
     */
    public <T> CommonMonad<T> from(@NotNull Collection<T> collection) {
        MemoryRepeatableIterator<T> iterator = new MemoryRepeatableIterator<>(collection.iterator(), true);
        return new CommonMonadImpl<>(iterator);
    }

    /**
     * 通过数组获取Monad
     *
     * @param array 输入的数组
     * @param <T>   数组中元素的数据类型
     * @return 包含数组内容的Monad
     */
    public <T> CommonMonad<T> from(@NotNull T[] array) {
        MemoryRepeatableIterator<T> iterator = new MemoryRepeatableIterator<>(Arrays.asList(array).iterator(), true);
        return new CommonMonadImpl<>(iterator);

    }

    /**
     * 通过Stream接口的对象获取Monad
     *
     * @param stream 输入的Stream对象
     * @param <T>    Stream中对象的数据类型
     * @return 包含Stream中所有内容的Monad
     */
    public <T> CommonMonad<T> from(@NotNull Stream<T> stream) {
        MemoryRepeatableIterator<T> iterator = new MemoryRepeatableIterator<>(stream.iterator(), true);
        return new CommonMonadImpl<>(iterator);
    }

    /**
     * 通过迭代器获取Monad
     *
     * @param iterator 输入的迭代器对象
     * @param <T>      迭代器中对象的数据类型
     * @return 包含迭代器中所有内容的Monad
     */
    public <T> CommonMonad<T> from(@NotNull Iterator<T> iterator) {
        MemoryRepeatableIterator<T> i = new MemoryRepeatableIterator<>(iterator, true);
        return new CommonMonadImpl<>(i);
    }

    /**
     * 通过文件读取获得Monad
     *
     * @param fileName 文件路径
     * @param creator  将文件映射为T类型对象的方法
     * @param <T>      产生的元素类型
     * @return 包含文件中所有内容的Monad
     */
    public <T> CommonMonad<T> fromFile(@NotNull String fileName, @NotNull Function<InputStream, T> creator) throws FileNotFoundException {
        FileRepeatableIterator<T> i = new FileRepeatableIterator<>(fileName, creator, true);
        return new CommonMonadImpl<>(i);
    }

    /**
     * 通过读取文本文件获得Monad，换行分隔符采用当前系统分隔符，编码采用UTF-8
     *
     * @param fileName 文本路径
     * @return 包含所有文本字符串的Monad
     */
    public CommonMonad<String> fromText(@NotNull String fileName) throws FileNotFoundException {
        return fromText(fileName, System.getProperty("line.separator"), Charset.forName("UTF-8"));
    }

    /**
     * 通过读取文本文件获得Monad
     *
     * @param fileName 文本路径
     * @param split    分隔符
     * @return 包含所有文本字符串的Monad
     */
    public CommonMonad<String> fromText(@NotNull String fileName, @NotNull String split, Charset charset) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileInputStream(fileName), charset.name()).useDelimiter(split);
        Function<InputStream, String> creator = source -> {
            try {
                return scanner.next();
            } catch (NoSuchElementException e) {
                return null; //没有了返回null，迭代器就会停止下一次迭代
            }
        };
        FileRepeatableIterator<String> i = new FileRepeatableIterator<>(fileName, creator, true);
        return new CommonMonadImpl<>(i);
    }

    /**
     * 通过文件读取获得PairMonad
     *
     * @param fileName 文件路径
     * @param creator  将文件流映射为Tuple对象的方法
     * @param <K>      Pair的Key类型
     * @param <V>      Pair的Value类型
     * @return 包含所有数据的PairMonad
     */
    public <K, V> PairMonad<K, V> fromPairFile(@NotNull String fileName, @NotNull Function<InputStream, Tuple<K, V>> creator) throws FileNotFoundException {
        FileRepeatableIterator<Tuple<K, V>> i = new FileRepeatableIterator<>(fileName, creator, true);
        return new PairMonadImpl<>(i);
    }

    /**
     * 通过Map获得PairMonad
     *
     * @param map Map对象
     * @param <K> Map和Pair的Key类型
     * @param <V> Map和Pair的Value类型
     * @return 包含Map中所有内容的PairMonad
     */
    public <K, V> PairMonad<K, V> from(@NotNull Map<K, V> map) {
        MemoryRepeatableIterator<Tuple<K, V>> i = new MemoryRepeatableIterator<>(mapToList(map).iterator(), true);
        return new PairMonadImpl<>(i);
    }

    /**
     * 通过单个值产生Monad
     *
     * @param value Monad中包含的值
     * @param <T>   值的类型
     * @return 包含这个值的Monad
     */
    public <T> Monad<T> of(@NotNull T value) {
        return new CommonMonadImpl<>(new MemoryRepeatableIterator<>(value, true));
    }

    /**
     * 通过单个值产生PairMonad
     *
     * @param value PairMonad中包含的值
     * @param <K>   Key的类型
     * @param <V>   Value的类型
     * @return 包含这个值的PairMonad
     */
    public <K, V> PairMonad<K, V> ofPair(@NotNull Tuple<K, V> value) {
        return new PairMonadImpl<>(new MemoryRepeatableIterator<>(value, true));
    }

    /**
     * 通过多个值产生Monad
     *
     * @param value Monad中包含的值
     * @param <T>   值的类型
     * @return 包含这些值的Monad
     */
    @SuppressWarnings("unchecked")
    public <T> CommonMonad<T> of(@NotNull T... value) {
        return this.from(value);
    }


    /**
     * 产生一个空Monad
     *
     * @return 一个空Monad
     */
    public <T> CommonMonad<T> of() {
        return CommonNilMonadImpl.get();
    }

    /**
     * 将Map转换为装有Tuple的List
     *
     * @param map Map对象
     * @param <K> Map中的Key类型
     * @param <V> Map中的Value类型
     * @return 装有Tuple的List对象
     */
    private <K, V> List<Tuple<K, V>> mapToList(@NotNull Map<K, V> map) {
        return map.entrySet()
                .parallelStream()
                .map(entry -> new Tuple<>(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }
}
