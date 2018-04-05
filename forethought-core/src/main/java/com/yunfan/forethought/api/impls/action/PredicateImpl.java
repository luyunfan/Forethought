package com.yunfan.forethought.api.impls.action;

import com.yunfan.forethought.enums.ActionType;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

/**
 * 布尔判断类类的Action操作
 *
 * @param <R> Monad元素类型
 */
public class PredicateImpl<R> implements Action<Boolean, Predicate<? super R>> {

    /**
     * 判断用的自定义函数
     */
    private final Predicate<? super R> predicateFunc;

    /**
     * 任意一个通过检查就返回true
     */
    private final boolean isAny;

    /**
     * 构造方法
     *
     * @param predicateFunc 自定义函数
     * @param isAny         任意一个通过检查就返回true
     */
    public PredicateImpl(@NotNull Predicate<? super R> predicateFunc, boolean isAny) {
        this.predicateFunc = predicateFunc;
        this.isAny = isAny;
    }

    /**
     * @return Action操作的返回值类型
     */
    @Override
    public Class<Boolean> resultType() {
        return Boolean.class;
    }

    /**
     * @return Action操作本身的类型
     */
    @Override
    public ActionType type() {
        return ActionType.PREDICATE;
    }

    /**
     * @return Action操作附带执行的终端函数
     */
    @Override
    public Predicate<? super R> actionFunc() {
        return predicateFunc;
    }

    /**
     * @return 任意一个通过检查就返回true
     */
    public boolean isAny() {
        return isAny;
    }
}
