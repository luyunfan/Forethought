package com.yunfan.forethought.api.impls.transformation.common;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * 代表进行map转换后的Monad
 *
 * @param <IN>  转换前的类型
 * @param <OUT> 转换后的类型
 */
public class MapImpl<IN, OUT> extends CommonMonadImpl<OUT> implements Transformation {

    /**
     * 实际map执行的函数
     */
    private final Function<? super IN, ? extends OUT> mapFunc;

    /**
     * 上层依赖对象
     */
    private final Dependency<IN> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     * @param f      实际执行的map函数内容
     */
    public MapImpl(@NotNull Dependency<IN> father, @NotNull Function<? super IN, ? extends OUT> f) {
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
    protected Function<? super IN, ? extends OUT> getTransformationalFunction() {
        return mapFunc;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.MAP;
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return String.format("MapCommandMonad:father dependency is %s%s", father, System.getProperty("line.separator"));
    }
}