package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.enums.ShuffleType;
import org.jetbrains.annotations.NotNull;

/**
 * 代表排序的PairMonad
 *
 * @param <K> PairMonad Key元素类型
 * @param <V> PairMonad Value元素类型
 */
public class SortedShuffleImpl<K, V> extends ShuffleMonad<K, V> {

    /**
     * 上层依赖对象
     */
    private final Dependency<?> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    public SortedShuffleImpl(@NotNull Dependency<?> father) {
        super(father);
        this.father = father;
    }

    /**
     * @return 当前Shuffle操作的类型
     */
    @Override
    public ShuffleType shuffleType() {
        return ShuffleType.SORTED;
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return String.format("SortedPairMonad:father dependency is %s%s", father, System.getProperty("line.separator"));
    }
}
