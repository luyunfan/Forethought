package com.yunfan.forethought.api.impls.action;

import com.yunfan.forethought.enums.ActionType;

import java.util.List;

/**
 * Drop的中间转换操作
 *
 * @param <T> Monad元素类型
 */
public class DropImpl<T> implements Action<List<T>, Object> {

    /**
     * drop操作删除的数量
     */
    private final long dropNumber;

    /**
     * 是否从起始端抛弃
     */
    private final boolean isStartWithLeft;

    /**
     * 注入上层依赖的构造函数
     *
     * @param isStartWithLeft 是否从起始端抛弃
     */
    public DropImpl(long dropNumber, boolean isStartWithLeft) {
        this.dropNumber = dropNumber;
        this.isStartWithLeft = isStartWithLeft;
    }

    /**
     * @return drop操作删除的数量
     */
    public long dropNumber() {
        return dropNumber;
    }

    /**
     * @return 是否从起始端抛弃
     */
    public boolean isStartWithLeft() {
        return isStartWithLeft;
    }

    /**
     * @return Action操作的返回值类型
     */
    @SuppressWarnings("unchecked")
    @Override
    public Class<List<T>> resultType() {
        return (Class) List.class;
    }

    /**
     * @return Action操作本身的类型
     */
    @Override
    public ActionType type() {
        return ActionType.DROP;
    }

    /**
     * @return Action操作附带执行的终端函数
     */
    @Override
    public Object actionFunc() {
        throw new UnsupportedOperationException("Drop类型的Action操作没有actionFunc！");
    }
}