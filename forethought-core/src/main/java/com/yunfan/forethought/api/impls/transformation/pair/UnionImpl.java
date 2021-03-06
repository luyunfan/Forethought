package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.PairMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.enums.TransformationalType;
import org.jetbrains.annotations.NotNull;


/**
 * 代表进行转换的Union操作
 *
 * @param <K> PairMonad Key元素类型
 * @param <V> PairMonad Value元素类型
 */
public class UnionImpl<K, V> extends PairMonadImpl<K, V> implements Transformation {

    /**
     * 和本Monad进行Union的Monad对象
     */
    private final PairMonad<K, V> other;

    /**
     * 上层依赖对象
     */
    private final Dependency<?> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    public UnionImpl(@NotNull Dependency<?> father, @NotNull PairMonad<K, V> other) {
        super(father);
        this.father = father;
        this.other = other;
    }

    /**
     * @return 和本Monad进行Union的Monad对象
     */
    public PairMonad<K, V> other() {
        return other;
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.UNION;
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return String.format("UnionPairMonad:father dependency is %s%s", father, System.getProperty("line.separator"));
    }
}
