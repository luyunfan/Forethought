package com.yunfan.forethought.api.impls.transformation.common;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;

/**
 * Drop的中间转换操作
 *
 * @param <T> Monad元素类型
 */
public class DropImpl<T> extends CommonMonadImpl<T> implements Transformation {

    /**
     * drop操作删除的数量
     */
    private final int dropNumber;

    /**
     * 是否从起始端抛弃
     */
    private final boolean isStartWithLeft;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father          上层依赖对象
     * @param isStartWithLeft 是否从起始端抛弃
     */
    public DropImpl(Dependency<?> father, int dropNumber, boolean isStartWithLeft) {
        super(father);
        this.dropNumber = dropNumber;
        this.isStartWithLeft = isStartWithLeft;
    }

    /**
     * @return drop操作删除的数量
     */
    public int dropNumber() {
        return dropNumber;
    }

    /**
     * @return 是否从起始端抛弃
     */
    public boolean isStartWithLeft() {
        return isStartWithLeft;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.DROP;
    }
}