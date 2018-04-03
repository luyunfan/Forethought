package com.yunfan.forethought.api.impls.action;

import com.yunfan.forethought.enums.ActionType;

/**
 * 代表所有终端Action操作的通用接口
 */
public interface Action {

    /**
     * @return Action操作的类型
     */
    ActionType type();
}
