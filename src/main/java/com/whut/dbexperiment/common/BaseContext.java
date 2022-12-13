package com.whut.dbexperiment.common;

/**
 * 基于 ThreadLocal 封装的工具类，用于保存和获取当前用户的 id 值
 */
public class BaseContext {

    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程的用户 id
     *
     * @param userId 传入的当前用户 id
     */
    public static void setCurrentId(Long userId) {
        threadLocal.set(userId);
    }

    /**
     * 获取当前线程的用户 id
     *
     * @return 当前用户 id
     */
    static Long getCurrentId() {
        return threadLocal.get();
    }
}
