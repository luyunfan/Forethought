package com.yunfan.forethought.task;

import com.yunfan.forethought.api.impls.action.Action;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.api.task.Executor;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.enums.ExecutorType;

import java.util.List;

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
     * @param dag    描述任务的DAG
     * @param action 代表最终Action操作的对象
     * @param <R>    最终操作返回值类型
     * @param <F>    Action操作附带的函数类型
     */
    @Override
    public <R, F> R execute(Graph<Monad<?>> dag, Action<R, F> action) {
        List<Monad<?>> vertexes = dag.topologicalSort();
        TaskQueue<R> taskQueue = null;
        for (Monad<?> item : vertexes) {
            if ((!item.getFatherDependency().isPresent()) &&
                    item.isDataSource() && item.getDataSource().isPresent()
            ) {
                taskQueue = new TaskQueue<>(item.getDataSource().get(), action);
            } else if (item.getFatherDependency().isPresent() && taskQueue != null) { //添加中间转换操作
                taskQueue.addTask(item);
            } else {
                throw new IllegalStateException("Monad既不是数据源，也没有上层依赖，执行引擎无法执行！");
            }
        }
        if (taskQueue != null)
            return taskQueue.run();
        else
            throw new IllegalStateException("执行引擎中taskQueue未被正确初始化！");
    }


}
