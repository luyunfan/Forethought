package com.yunfan.forethought.type;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * 元组类型，代表Key Value键值对
 *
 * @param <K> Key类型
 * @param <V> Value类型
 */
public class Tuple<K, V> implements Comparable<Tuple<K, V>> {

    /**
     * 代表键值对中的键
     */
    private K key;

    /**
     * 代表键值对中的值
     */
    private final V value;

    /**
     * 对Key进行自定义排序的排序器
     */
    private Comparator<K> keyComparator = null;

    /**
     * 构造函数，初始化元组数据
     *
     * @param key   键
     * @param value 值
     */
    public Tuple(@NotNull K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * 获取键
     *
     * @return 键数据
     */
    public K key() {
        return key;
    }

    /**
     * 获取值
     *
     * @return 值数据
     */
    public V value() {
        return value;
    }

    /**
     * 通过静态方法创建Tuple
     *
     * @param <K> Key类型
     * @param <V> Value类型
     * @return 一个新的Tuple元祖
     */
    public static <K, V> Tuple<K, V> create(K key, V value) {
        return new Tuple<>(key, value);
    }

    /**
     * 重写equals，判断两个对象是否相等
     *
     * @param other 需要和本对象判断的其他对象
     * @return 两个对象是否相等
     */
    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object other) {
        return other instanceof Tuple &&
                ((Tuple) other).key == this.key &&
                ((Tuple) other).value == this.value;
    }

    /**
     * 重写的toString方法，将元祖转化为(key,value)的形式
     *
     * @return (key, value)的字符串
     */
    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }

    /**
     * 重写的hashCode方法，获取哈希值
     *
     * @return 本对象的hashCode
     */
    @Override
    public int hashCode() {
        return 31 * key.hashCode() + (value.hashCode());
    }

    /**
     * 为当前Tuple的Key值加入自定义排序功能，优先级最高，如果加入Comparator后，则原Key的Comparable失效
     *
     * @param keyComparator 对Key进行自定义排序
     * @return this Tuple对象，用于方便调用
     */
    public Tuple<K, V> withKeyComparator(Comparator<K> keyComparator) {
        this.keyComparator = keyComparator;
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(@NotNull Tuple<K, V> o) {
        if (this.keyComparator != null) {
            return this.keyComparator.compare(key, o.key);
        } else {
            if (key instanceof Comparable) {
                return ((Comparable<K>) key).compareTo(o.key);
            } else {
                throw new IllegalStateException("对Tuple进行排序必须对Key值类型实现Comparable或调用withKeyComparator()实现对Key进行排序的逻辑！");
            }
        }
    }
}
