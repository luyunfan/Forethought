package com.yunfan.forethought.api.task;

import com.yunfan.forethought.api.impls.action.Action;
import com.yunfan.forethought.api.monad.Monad;
import com.yunfan.forethought.dag.Graph;
import com.yunfan.forethought.enums.ExecutorType;
import com.yunfan.forethought.exception.DependencyClassNotFoundException;
import com.yunfan.forethought.model.Job;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * 执行引擎定义接口
 */
public interface Executor {

    /**
     * @return 执行引擎类别
     */
    ExecutorType type();

    /**
     * 执行任务
     *
     * @param dag    描述任务的DAG
     * @param action 代表最终Action操作的对象
     * @param <R>    最终操作返回值类型
     * @param <F>    Action操作附带的函数类型
     */
    <R, F> R execute(Graph<Monad> dag, Action<R, F> action);

    /**
     * 获取执行引擎的静态方法，执行引擎实现通过spi注入实现
     *
     * @return 执行引擎实现
     */
    static Executor getExecutor() {
        ServiceLoader<Executor> serviceLoader = ServiceLoader.load(Executor.class);
        List<Executor> implList = new ArrayList<>();
        serviceLoader.iterator().forEachRemaining(implList::add);
        if (implList.isEmpty()) { //完全没有执行引擎依赖
            throw new DependencyClassNotFoundException("缺少Executor的依赖！请检查classpath中是否存在执行引擎的实现！");
        }
        if (implList.size() == 1) { //只有一种实现就直接返回
            return implList.get(0);
        } else { //如果有多种实现，则有线返回第一种并行实现
            for (Executor value : implList) {
                if (value.type() == ExecutorType.PARALLEL) {
                    return value;
                }
            }
            return implList.get(0); //如果不存在并行实现，则返回第一个实现
        }
    }
}
