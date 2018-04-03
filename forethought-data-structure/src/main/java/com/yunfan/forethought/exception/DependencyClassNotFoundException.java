package com.yunfan.forethought.exception;

/**
 * 缺少依赖的异常
 */
public class DependencyClassNotFoundException extends RuntimeException {

    /**
     * 异常的构造方法
     *
     * @param info 异常信息
     */
    public DependencyClassNotFoundException(String info) {
        super(info);
    }
}
