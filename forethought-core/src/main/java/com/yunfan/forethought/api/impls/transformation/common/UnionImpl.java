package com.yunfan.forethought.api.impls.transformation.common;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.monad.CommonMonad;
import com.yunfan.forethought.enums.TransformationalType;

/**
 * 代表进行转换的Union操作
 *
 * @param <T> Monad元素类型
 */
public class UnionImpl<T> extends CommonMonadImpl<T> implements Transformation {

    /**
     * 和本Monad进行Union的Monad对象
     */
    private final CommonMonad<? extends T> other;


    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    public UnionImpl(Dependency<?> father, CommonMonad<? extends T> other) {
        super(father);
        this.other = other;
    }

    /**
     * @return 和本Monad进行Union的Monad对象
     */
    public CommonMonad<? extends T> other() {
        return other;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.UNION;
    }
}
