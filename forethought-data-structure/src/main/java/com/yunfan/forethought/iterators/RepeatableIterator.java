package com.yunfan.forethought.iterators;

import java.util.Iterator;

/**
 * 表示一个可重新遍历的迭代器
 *
 * @param <T> 迭代元素类型
 */
public interface RepeatableIterator<T> extends Iterator<T> {

    /**
     * 产生一个新的迭代器对象，迭代位置从头开始
     *
     * @return 和本对象内容一致的从头开始迭代的新迭代器对象
     */
    RepeatableIterator<T> backToStarting();

    /**
     * 获取对象第一个元素
     *
     * @return 首个元素
     */
    T firstElement();

    /**
     * 迭代器是否真的可以从头开始，如果返回false，则不能调用backToStarting()
     *
     * @return 迭代器是否可以从头开始（数据是否是一次性的）
     */
    boolean canBeRepeat();

}
