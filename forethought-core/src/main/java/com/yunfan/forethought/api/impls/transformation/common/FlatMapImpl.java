package com.yunfan.forethought.api.impls.transformation.common;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.enums.TransformationalType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * flatMap的中间转换操作
 *
 * @param <IN>  转换前的类型
 * @param <OUT> 转换后的类型
 */
public class FlatMapImpl<IN, OUT> extends CommonMonadImpl<OUT> implements Transformation {

    /**
     * 中间转换的函数
     */
    private final Function<? super IN, ? extends Monad<? extends OUT>> mapFunc;

    /**
     * 上层依赖对象
     */
    private final Dependency<?> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    public FlatMapImpl(@NotNull Dependency<?> father, @NotNull Function<? super IN, ? extends Monad<? extends OUT>> f) {
        super(father);
        this.father = father;
        this.mapFunc = f;
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    @Override
    public Function<? super IN, ? extends Monad<? extends OUT>> getTransformationalFunction() {
        return mapFunc;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.FLATMAP;
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return String.format("FlatMapCommandMonad:father dependency is %s%s", father, System.getProperty("line.separator"));
    }
}