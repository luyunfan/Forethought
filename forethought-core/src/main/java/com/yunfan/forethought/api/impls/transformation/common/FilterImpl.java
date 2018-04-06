package com.yunfan.forethought.api.impls.transformation.common;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * 代表进行filter转换操作的Monad
 *
 * @param <T> Monad的类型
 */
public class FilterImpl<T> extends CommonMonadImpl<T> implements Transformation {

    /**
     * 实际filter执行的函数
     */
    private final Predicate<? super T> filterFunc;

    /**
     * 上层依赖对象
     */
    private final Dependency<T> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     * @param f      实际执行的filter函数内容
     */
    public FilterImpl(@NotNull Dependency<T> father, @NotNull Predicate<? super T> f) {
        super(father);
        this.father = father;
        this.filterFunc = f;
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    @Override
    protected Predicate<? super T> getTransformationalFunction() {
        return filterFunc;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.FILTER;
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return "FilterCommandMonad:father dependency is" + father;
    }
}