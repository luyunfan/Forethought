package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.PairMonadImpl;

/**
 * 代表Shuffle操作的Monad实现
 */
public class ShuffleImpl<K, V> extends PairMonadImpl<K, V> {

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    protected ShuffleImpl(Dependency<?> father) {
        super(father);
    }
}
