package com.yunfan.forethought.type;

import com.sun.istack.internal.NotNull;

/**
 * 元组类型，代表Key Value键值对
 *
 * @param <K> Key类型
 * @param <V> Value类型
 */
public class Tuple<K, V> {

    /**
     * 代表键值对中的键
     */
    private K key;

    /**
     * 代表键值对中的值
     */
    private final V value;

    /**
     * 构造函数，初始化元组数据
     *
     * @param key   键
     * @param value 值
     */
    public Tuple(@NotNull K key, @NotNull V value) {
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
    public static <K, V> Tuple create(K key, V value) {
        return new Tuple<>(key, value);
    }

    /**
     * 重写equals，判断两个对象是否相等
     *
     * @param other 需要和本对象判断的其他对象
     * @return 两个对象是否相等
     */
    @Override
    public boolean equals(Object other) {
        return other instanceof Tuple && ((Tuple) other).key == this.key && ((Tuple) other).value == this.value;
    }

    /**
     * 重写的toString方法，将元祖转化为(key,value)的形式
     *
     * @return (key, value)的字符串
     */
    @Override
    public String toString() {
        return "(" + key + "," + value + ")";
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
}
