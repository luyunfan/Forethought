package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.PairMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

/**
 * 代表进行map转换后的Monad
 *
 * @param <IN> 转换前的数据类型
 * @param <K2> 转换后的Key类型
 * @param <V2> 转换后的Value类型
 */
public class MapImpl<IN, K2, V2> extends PairMonadImpl<K2, V2> implements Transformation {

    /**
     * 实际map执行的函数
     */
    private Function<? super IN, ? extends Tuple<K2, V2>> mapFunc;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     * @param f      实际执行的map函数内容
     */
    public MapImpl(@NotNull Dependency<IN> father, @NotNull Function<? super IN, ? extends Tuple<K2, V2>> f) {
        this(father);
        mapFunc = f;
    }

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    private MapImpl(@NotNull Dependency<IN> father) {
        super(father);
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    @Override
    protected Function<? super IN, ? extends Tuple<K2, V2>> getTransformationalFunction() {
        return mapFunc;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.MAP;
    }
}