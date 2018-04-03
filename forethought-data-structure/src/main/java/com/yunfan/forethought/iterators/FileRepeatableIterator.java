package com.yunfan.forethought.iterators;

import org.jetbrains.annotations.NotNull;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 基于文件的可重复迭代对象
 *
 * @param <T> 包含的数据类型
 */
public class FileRepeatableIterator<T> implements RepeatableIterator<T> {

    /**
     * 从来自内存中的对象生成的可重复迭代的对象，本对象逻辑由此实现
     */
    private final MemoryRepeatableIterator<T> source;

    /**
     * 构造方法，构建一个文件可重复迭代对象
     *
     * @param fileName    文件名
     * @param creator     将文件流转化成数据的函数
     * @param canBeRepeat 能否重复迭代
     * @throws FileNotFoundException 如果路径上的文件没有找到，则触发这个异常
     */
    public FileRepeatableIterator(@NotNull String fileName, @NotNull Function<InputStream, T> creator, boolean canBeRepeat) throws FileNotFoundException {
        Iterator<T> iterator = new FileIterator<>(new FileInputStream(fileName), creator);
        source = new MemoryRepeatableIterator<>(iterator, canBeRepeat);
    }

    /**
     * 产生一个新的迭代器对象，迭代位置从头开始
     *
     * @return 和本对象内容一致的从头开始迭代的新迭代器对象
     */
    @Override
    public RepeatableIterator<T> backToStarting() {
        return source.backToStarting();
    }

    /**
     * 获取对象第一个元素
     *
     * @return 首个元素
     */
    @Override
    public T firstElement() {
        return source.firstElement();
    }

    /**
     * 迭代器是否真的可以从头开始，如果返回false，则不能调用backToStarting()
     *
     * @return 迭代器是否可以从头开始（数据是否是一次性的）
     */
    @Override
    public boolean canBeRepeat() {
        return source.canBeRepeat();
    }

    /**
     * Returns {@code true} if the iteration has more elements.
     * (In other words, returns {@code true} if {@link #next} would
     * return an element rather than throwing an exception.)
     *
     * @return {@code true} if the iteration has more elements
     */
    @Override
    public boolean hasNext() {
        return source.hasNext();
    }

    /**
     * Returns the next element in the iteration.
     *
     * @return the next element in the iteration
     */
    @Override
    public T next() {
        return source.next();
    }

    /**
     * Performs the given action for each remaining element until all elements
     * have been processed or the action throws an exception.  Actions are
     * performed in the order of iteration, if that order is specified.
     * Exceptions thrown by the action are relayed to the caller.
     *
     * @param action The action to be performed for each element
     * @throws NullPointerException if the specified action is null
     * @implSpec <p>The default implementation behaves as if:
     * <pre>{@code
     *     while (hasNext())
     *         action.accept(next());
     * }</pre>
     * @since 1.8
     */
    @Override
    public void forEachRemaining(Consumer<? super T> action) {
        source.forEachRemaining(action);
    }

    /**
     * 包装的文件迭代器实现
     *
     * @param <T> 迭代器类型
     */
    private static class FileIterator<T> implements Iterator<T> {

        /**
         * 代表数据源的文件输入流
         */
        private final FileInputStream source;

        /**
         * 数据源转换函数
         */
        private final Function<InputStream, T> creator;

        /**
         * 当前正在迭代的对象
         */
        private T item;

        /**
         * 构造方法，初始化本迭代器
         *
         * @param source  数据源输入流
         * @param creator 数据处理函数
         */
        FileIterator(FileInputStream source, Function<InputStream, T> creator) {
            this.source = source;
            this.creator = creator;
            item = creator.apply(source);
        }

        /**
         * Returns {@code true} if the iteration has more elements.
         * (In other words, returns {@code true} if {@link #next} would
         * return an element rather than throwing an exception.)
         *
         * @return {@code true} if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return item != null;
        }

        /**
         * Returns the next element in the iteration.
         *
         * @return the next element in the iteration
         */
        @Override
        public T next() {
            if (item == null) { //没有数据next直接抛异常
                throw new NoSuchElementException("已经不能再调用next了！");
            }
            T result = item; //记录当前返回值
            item = creator.apply(source); //将item推向下一个位置
            return result;
        }
    }
}
