package com.yunfan.forethought.api.impls.action;

import com.yunfan.forethought.enums.ActionType;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiFunction;

/**
 * reduce操作的Action记录对象
 *
 * @param <T> Monad的元素类型
 */
public class ReduceImpl<T> implements Action<T, BiFunction<T, T, T>> {

    /**
     * 最终执行合并操作的函数
     */
    private final BiFunction<T, T, T> actionFunc;

    /**
     * 构造方法
     *
     * @param actionFunc 最终执行合并操作的函数
     */
    public ReduceImpl(@NotNull BiFunction<T, T, T> actionFunc) {
        this.actionFunc = actionFunc;
    }

    /**
     * @return Action操作的返回值类型（返回null，Java泛型无法获取真实Class）
     */
    @Override
    public Class<T> resultType() {
        return null;
    }

    /**
     * @return Action操作本身的类型
     */
    @Override
    public ActionType type() {
        return ActionType.REDUCE;
    }

    /**
     * @return Action操作附带执行的终端函数
     */
    @Override
    public BiFunction<T, T, T> actionFunc() {
        return actionFunc;
    }
}
