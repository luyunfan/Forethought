package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.monad.PairMonad;
import com.yunfan.forethought.enums.ShuffleType;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

/**
 * 代表Join操作的PairMonad
 *
 * @param <K>  PairMonad Key元素类型
 * @param <V>  PairMonad Value元素类型
 * @param <NV> Join的PairMonad的Value元素类型
 */
public class JoinImpl<K, V, NV> extends ShuffleMonad<K, Tuple<V, NV>> implements Transformation {

    /**
     * 和本PairMonad进行Join的PairMonad
     */
    private final PairMonad<K, NV> joinPair;

    /**
     * 上层依赖对象
     */
    private final Dependency<?> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father   上层依赖对象
     * @param joinPair 和本PairMonad进行Join的PairMonad
     */
    public JoinImpl(@NotNull Dependency<?> father, PairMonad<K, NV> joinPair) {
        super(father);
        this.father = father;
        this.joinPair = joinPair;
    }

    /**
     * @return 当前Shuffle操作的类型
     */
    @Override
    public ShuffleType shuffleType() {
        return ShuffleType.JOIN;
    }

    /**
     * @return 和本PairMonad进行Join的PairMonad
     */
    public PairMonad<K, NV> joinPair() {
        return joinPair;
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return String.format("JoinPairMonad:father dependency is %s%s", father, System.getProperty("line.separator"));
    }
}
