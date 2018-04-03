package com.yunfan.forethought.iterators;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

/**
 * 从来自内存中的对象生成的可重复迭代的对象
 *
 * @param <T> 元素类型
 */
public class MemoryRepeatableIterator<T> implements RepeatableIterator<T> {

    /**
     * 代表数据源的迭代器
     */
    private final Iterator<T> source;

    /**
     * 每次next后都要记录数据，以帮助重新迭代
     */
    private List<T> copy;

    /**
     * 能否重复迭代
     */
    private final boolean canBeRepeat;

    /**
     * 首个元素的缓存
     */
    private T firstElementCache;

    /**
     * 是否调用过一次next
     */
    private boolean isCallNext = false;

    /**
     * 构造方法，需要传入一个代表数据源的迭代器
     *
     * @param source    数据源
     * @param canBeRepeat 是否可以重复迭代
     */
    public MemoryRepeatableIterator(@NotNull Iterator<T> source, boolean canBeRepeat) {
        this.source = source;
        this.canBeRepeat = canBeRepeat;
        if (canBeRepeat) {
            copy = new LinkedList<>();
        }
    }

    /**
     * 构造方法，需要传入一个代表数据源的数据，构建单数据迭代器
     *
     * @param source    数据源
     * @param canBeRepeat 是否可以重复迭代
     */
    public MemoryRepeatableIterator(@NotNull T source, boolean canBeRepeat) {
        this.source = new Iterator<T>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public T next() {
                return source;
            }
        };
        this.canBeRepeat = canBeRepeat;
        if (canBeRepeat) {
            copy = new LinkedList<>();
        }
    }

    /**
     * 产生一个新的迭代器对象，迭代位置从头开始（本迭代器没有遍历完成的数据将全部遍历）
     *
     * @return 和本对象内容一致的从头开始迭代的新迭代器对象
     */
    @Override
    public RepeatableIterator<T> backToStarting() {
        if (canBeRepeat) {
            if (hasNext()) { //如果当前没有遍历完成，则全部遍历
                forEachRemaining(item -> {
                });
            }
            return new MemoryRepeatableIterator<>(copy.iterator(), true);
        } else {
            throw new IllegalStateException("本迭代器初始化不支持可重复迭代！不能调用backToStarting()方法");
        }
    }

    /**
     * 获取对象第一个元素，如果数据源为空的话，返回null
     *
     * @return 首个元素
     */
    @Override
    public T firstElement() {
        if (isCallNext) { //如果调用过next
            return firstElementCache;
        } else { //从来没调用过next
            if (hasNext()) { //判断数据源是否是空的不能next
                firstElementCache = next();
                return firstElementCache;
            } else {
                return null;
            }
        }
    }

    /**
     * 迭代器是否真的可以从头开始，如果返回false，则不能调用backToStarting()
     *
     * @return 迭代器是否可以从头开始（数据是否是一次性的）
     */
    @Override
    public boolean canBeRepeat() {
        return canBeRepeat;
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
        return nextElement();
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
    public void forEachRemaining(@NotNull Consumer<? super T> action) {
        while (hasNext()) {
            action.accept(nextElement());
        }
    }

    /**
     * 获取迭代器下一个元素，且记录目前已有的元素
     *
     * @return 下一个元素
     */
    private T nextElement() {
        if (!isCallNext && firstElementCache != null) { //有没有从来没有next过直接调用了firstElement()
            isCallNext = true;
            return firstElementCache;
        }
        if (!isCallNext) { //如果是第一次调用的话
            isCallNext = true;
            T result = source.next();
            firstElementCache = result; //设置调用缓存
            if (canBeRepeat) {
                copy.add(result);
                return result;
            } else {
                return result;
            }
        } else { //如果不是第一次调用的话
            T result = source.next();
            if (canBeRepeat) {
                copy.add(result);
                return result;
            } else {
                return result;
            }
        }

    }
}
