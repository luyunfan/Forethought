package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.enums.ShuffleType;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * 代表Combine的Shuffle操作
 *
 * @param <K> PairMonad Key元素类型
 * @param <V> PairMonad Value元素类型
 */
public class CombineShuffleImpl<K, V> extends ShuffleMonad<K, Collection<V>> {

    /**
     * 合并函数
     */
    private final BiFunction<Collection<V>, V, Collection<V>> combineFunc;

    /**
     * 创建Collection的函数
     */
    private final Consumer<Collection<V>> collectionCreator;

    /**
     * 上层依赖对象
     */
    private final Dependency<?> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father            上层依赖对象
     * @param combineFunc       合并函数
     * @param collectionCreator 创建Collection的函数
     */
    public CombineShuffleImpl(@NotNull Dependency<?> father,
                              @NotNull BiFunction<Collection<V>, V, Collection<V>> combineFunc,
                              @NotNull Consumer<Collection<V>> collectionCreator) {
        super(father);
        this.father = father;
        this.combineFunc = combineFunc;
        this.collectionCreator = collectionCreator;
    }

    /**
     * @return 当前Shuffle操作的类型
     */
    @Override
    public ShuffleType shuffleType() {
        return ShuffleType.COMBINE;
    }

    /**
     * 获取中间转换的函数
     *
     * @return 中间转换操作函数对象
     */
    @Override
    public Tuple<BiFunction<Collection<V>, V, Collection<V>>, Consumer<Collection<V>>> getTransformationalFunction() {
        return new Tuple<>(combineFunc, collectionCreator);
    }

    /**
     * 重写toString方法，方便Debug时观察Monad类型
     *
     * @return {Monad类型}:father dependency is {父依赖Monad字符串}
     */
    @Override
    public String toString() {
        return String.format("CombinePairMonad:father dependency is %s%s", father, System.getProperty("line.separator"));
    }

}
