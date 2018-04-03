package com.yunfan.forethought.task;

import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.task.Executor;
import com.yunfan.forethought.enums.ExecutorType;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.model.Job;

/**
 * 单线程串行执行引擎实现
 */
public class SerialExecutor implements Executor {

    /**
     * @return 执行引擎类别
     */
    @Override
    public ExecutorType type() {
        return ExecutorType.SERIAL;
    }

    /**
     * 执行任务
     *
     * @param dag 描述任务的DAG
     */
    @Override
    public <T> Job execute(Graph<Monad<T>> dag) {
        return null;
    }
}
