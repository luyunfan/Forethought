package com.yunfan.forethought.task;

import com.yunfan.forethought.api.impls.action.Action;
import com.yunfan.forethought.api.impls.transformation.Transformation;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.iterators.RepeatableIterator;

import java.util.*;

/**
 * 执行任务的队列
 *
 * @param <R> 执行任务最终返回值类型
 */
public class TaskQueue<R> {

    /**
     * 包含数据源的队列
     */
    private final List<Monad<?>> tasksQueue = new LinkedList<>();

    /**
     * 数据源迭代器
     */
    private final RepeatableIterator<?> dataSource;

    /**
     * Action操作
     */
    private final Action<R, ?> action;

    /**
     * 构造方法，需要传入数据源
     *
     * @param dataSource 数据源迭代器
     */
    TaskQueue(RepeatableIterator<?> dataSource, Action<R, ?> action) {
        this.dataSource = dataSource;
        this.action = action;
    }

    /**
     * 任务队列执行前需要添加先任务
     *
     * @param task 添加的任务
     */
    void addTask(Monad<?> task) {
        this.tasksQueue.add(task);
    }

    /**
     * 执行任务
     */
    R run() {
        TransformationProcessor<R> process = new TransformationProcessor<>(action);
        for (Monad<?> trans : tasksQueue) {
            if (trans instanceof Transformation) {
                process.add((Transformation) trans);
            } else {
                throw new IllegalStateException("执行引擎输入的中间操作不是Transformation操作");
            }
        }
        return process.process(dataSource);
    }


}
