package com.yunfan.forethought.model;

/**
 * 代表一个触发了Monad计算的任务
 */
public class Job {

    /**
     * 任务ID
     */
    private final int id;

    public Job(int jobId) {
        this.id = jobId;
    }

    /**
     * 获得任务id的方法
     *
     * @return 任务id
     */
    public int id() {
        return this.id;
    }
}
