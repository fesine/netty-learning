package com.fesine.juc.forkjoin;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/4/19
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/19
 */
//jdk的本质就是利用一些底层API，为开发人员提供便利
public class MyFutureTask<T> implements Runnable, Future {
    //1、
    Callable<T> callable;
    T result = null;

    /**
     * 记录线程状态
     */
    volatile String state = "NEW";

    LinkedBlockingQueue<Thread> waiters = new LinkedBlockingQueue<>();

    public MyFutureTask(Callable<T> callable) {
        this.callable = callable;
    }



    /**
     * 返回结果,线程需要阻塞
     * @return
     */
    @Override
    public T get(){
        if("END".equals(state)){
            return result;
        }
        waiters.offer(Thread.currentThread());
        while (!"END".equals(state)){
            LockSupport.park();
        }
        return result;
    }

    @Override
    public Object get(long timeout, TimeUnit unit) {
        return null;
    }

    @Override
    public void run() {
        try {
            result = callable.call();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            state="END";
        }
        //唤醒等待者
        Thread waiter = waiters.poll();
        while (waiter != null){
            LockSupport.unpark(waiter);
            waiter = waiters.poll();
        }
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }
}
