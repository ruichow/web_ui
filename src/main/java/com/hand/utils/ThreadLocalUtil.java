package com.hand.utils;

/**
 * @author：关河九州
 * @date：2019/11/14 17:18
 * @version：1.0
 */
public class ThreadLocalUtil<T> {
    //设置当前线程变量
    public void setThreadValue(ThreadLocal<T> threadLocal,T value){
        if (threadLocal.get()==null){
            threadLocal.set(value);
        }
    }
    //获得当前线程变量的值
    public T getThreadValue(ThreadLocal<T> threadLocal){
        return threadLocal.get();
    }
}
