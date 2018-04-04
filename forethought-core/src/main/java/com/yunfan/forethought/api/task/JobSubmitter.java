package com.yunfan.forethought.api.task;

import com.yunfan.forethought.api.impls.action.Action;
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
     * @param <R> 最终操作返回值类型
     * @param <F> Action操作附带的函数类型
     */
    public <T, R, F> R submitTask(Graph<Monad<T>> dag, Action<R, F> action) {
        return executor.execute(dag, action);
    }

    /**
     * @return 执行引擎的类型
     */
    public ExecutorType executorType() {
        return executor.type();
    }
}
