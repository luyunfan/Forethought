package com.yunfan.forethought.enums;

/**
 * 中间转换层操作类型
 */
public enum TransformationalType {

    /**
     * 过滤操作
     */
    FILTER,

    /**
     * flatMap操作
     */
    FLATMAP,

    /**
     * map映射操作
     */
    MAP,

    /**
     * 排序操作
     */
    SORT,

    /**
     * 删除元素操作
     */
    DROP,

    /**
     * 取元素操作
     */
    TAKE,

    /**
     * 合并Monad操作
     */
    UNION,

    /**
     * Pair的一般Shuffle操作
     */
    SHUFFLE
}
