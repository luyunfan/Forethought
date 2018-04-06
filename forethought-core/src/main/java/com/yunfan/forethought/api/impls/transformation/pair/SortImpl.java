package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.CommonMonadImpl;
import com.yunfan.forethought.api.impls.PairMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.TransformationalType;
import com.yunfan.forethought.type.Tuple;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

/**
 * 代表进行排序转换的Monad
 *
 * @param <K> PairMonad Key元素类型
 * @param <V> PairMonad Value元素类型
 */
public class SortImpl<K, V> extends PairMonadImpl<K, V> implements Transformation {

    /**
     * 排序规则
     */
    private final Comparator<Tuple<K, V>> sortRule;

    /**
     * 上层依赖对象
     */
    private final Dependency<Tuple<K, V>> father;

    /**
     * 注入上层依赖的构造函数
     *
     * @param father     上层依赖对象
     * @param comparator 排序规则
     */
    public SortImpl(@NotNull Dependency<Tuple<K, V>> father, @NotNull Comparator<Tuple<K, V>> comparator) {
        super(father);
        this.father = father;
        this.sortRule = comparator;
    }

    /**
     * 获取排序规则的方法
     *
     * @return 排序规则
     */
    public Comparator<Tuple<K, V>> getSortRule() {
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
        return String.format("SortPairMonad:father dependency is %s%s", father, System.getProperty("line.separator"));
    }

}