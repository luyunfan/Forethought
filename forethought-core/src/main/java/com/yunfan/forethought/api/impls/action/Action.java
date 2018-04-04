package com.yunfan.forethought.api.impls.action;

import com.yunfan.forethought.enums.ActionType;

/**
 * 代表所有终端Action操作的通用接口
 *
 * @param <T> Action操作最终返回的结果类型
 * @param <F> 操作返回函数类型
 */
public interface Action<T,F> {

    /**
     * @return Action操作的返回值类型
     */
    Class<T> resultType();

    /**
     * @return Action操作本身的类型
     */
    ActionType type();

    /**
     * @return Action操作附带执行的终端函数
     */
    F actionFunc();
}
