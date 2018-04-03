package com.yunfan.forethought.api.dependency;

import com.yunfan.forethought.api.monad.Monad;

import java.io.Serializable;

/**
 * 表示某一个Monad的向上一层的依赖Monad
 *
 * @param <T> 依赖的Monad类型
 */
public abstract class Dependency<T> implements Serializable {

    /**
     * 获取上层依赖Monad对象
     *
     * @return 上层依赖Monad对象
     */
    public abstract Monad<T> get();
}
