package com.yunfan.forethought.type;

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
    public Tuple(K key, V value) {
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
}
