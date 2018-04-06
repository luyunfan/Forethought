package com.yunfan.forethought.enums;

/**
 * Shuffle操作的类型
 */
public enum ShuffleType {

    /**
     * 相互reduce类型
     */
    AGGREGATE,

    /**
     * 排序类型
     */
    SORTED,

    /**
     * 合并类型
     */
    COMBINE
}
