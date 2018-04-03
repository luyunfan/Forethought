package com.yunfan.forethought.api.impls.transformation;

import com.yunfan.forethought.enums.TransformationalType;

/**
 * 中间转换类操作通用接口
 */
public interface Transformation {

    /**
     * @return 操作类型
     */
    TransformationalType type();
}
