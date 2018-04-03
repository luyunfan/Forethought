package com.yunfan.forethought.api.task;

import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.enums.ExecutorType;

/**
 * 发布任务的单例工具类
 */
public enum JobSubmitter {
    INSTANCE;

    private final Executor executor = Executor.getExecutor();

    /**
     * 发布任务
     *
     * @param dag 包含任务描述的有向无环图
     * @param <T> 包含的Monad的元素类型
     */
    public <T> void submitTask(Graph<Monad<T>> dag) {
        executor.execute(dag);
    }

    /**
     * @return 执行引擎的类型
     */
    public ExecutorType executorType() {
        return executor.type();
    }
}
