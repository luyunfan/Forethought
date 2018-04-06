package com.yunfan.forethought.api.impls.transformation.common;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;
import org.jetbrains.annotations.NotNull;

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
    private final Optional<Comparator<T>> sortRule;

    /**
     * 上层依赖对象
     */
    private final Dependency<T> father;


    /**
     * 注入上层依赖的构造函数
     *
     * @param father     上层依赖对象
     * @param comparator 排序规则
     */
    public SortImpl(@NotNull Dependency<T> father, @NotNull Optional<Comparator<T>> comparator) {
        super(father);
        this.father = father;
        this.sortRule = comparator;
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

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return "SortCommandMonad:father dependency is" + father;
    }
}