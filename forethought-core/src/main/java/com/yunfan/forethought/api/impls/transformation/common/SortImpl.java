package com.yunfan.forethought.api.impls.transformation.common;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;

import java.util.Comparator;
import java.util.Optional;

/**
 * 代表进行排序转换的Monad
 *
 * @param <T> Monad的元素类型
 */
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class SortImpl<T> extends CommonMonadImpl<T> implements Transformation {

    /**
     * 排序规则
     */
    private Optional<Comparator<T>> sortRule;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    private SortImpl(Dependency<T> father) {
        super(father);
    }

    /**
     * 注入上层依赖的构造函数
     *
     * @param father     上层依赖对象
     * @param comparator 排序规则
     */
    public SortImpl(Dependency<T> father, Optional<Comparator<T>> comparator) {
        this(father);
        sortRule = comparator;
    }

    /**
     * 获取排序规则的方法
     *
     * @return 排序规则
     */
    public Optional<Comparator<T>> getSortRule() {
        return sortRule;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.SORT;
    }
}