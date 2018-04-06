package com.yunfan.forethought.api.impls.transformation.pair;

import com.yunfan.forethought.api.dependency.Dependency;
import com.yunfan.forethought.api.impls.PairMonadImpl;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.enums.ShuffleType;
import com.yunfan.forethought.enums.TransformationalType;
import org.jetbrains.annotations.NotNull;

/**
 * 代表Shuffle操作的Monad实现
 *
 * @param <K> PairMonad的Key类型
 * @param <V> PairMonad的Value类型
 */
public abstract class ShuffleMonad<K, V> extends PairMonadImpl<K, V> implements Transformation {

    /**
     * 注入上层依赖的构造函数
     *
     * @param father 上层依赖对象
     */
    public ShuffleMonad(@NotNull Dependency<?> father) {
        super(father);
    }

    /**
     * @return 操作类型
     */
    @Override
    public TransformationalType type() {
        return TransformationalType.SHUFFLE;
    }

    /**
     * @return 当前Shuffle操作的类型
     */
    public abstract ShuffleType shuffleType();

}
