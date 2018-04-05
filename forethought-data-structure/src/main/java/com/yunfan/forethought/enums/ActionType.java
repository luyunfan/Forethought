package com.yunfan.forethought.enums;

/**
 * Action操作本身的类型枚举
 */
public enum ActionType {

    /**
     * 布尔断类型
     */
    PREDICATE,

    /**
     * 收集数据到本地的类型
     */
    COLLECT,

    /**
     * 互相合并计算的类型
     */
    REDUCE
}
