package com.fesine.juc.lock.aqs;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @description: 自己实现重入锁
 * @author: fesine
 * @createTime:2020/4/9
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/9
 */
public class MyLock implements Lock {

    /**
     * 持有锁的线程
     */
    volatile AtomicReference<Thread> own = new AtomicReference<>();

    /**
     * 阻塞队列，存放未获取到锁的线程
     */
    volatile Queue<Thread> waiters = new LinkedBlockingQueue<>();

    @Override
    public boolean tryLock() {
        //cas操作，期望空值，赋值当前操作线程
        return own.compareAndSet(null,Thread.currentThread());
    }

    @Override
    public void lock() {
        boolean add = true;
        //只要没获取到锁，就添加到队列
        while (!tryLock()){
            if (add){
                waiters.offer(Thread.currentThread());
                add = false;
            }else{
                //阻塞，挂起当前线程
                LockSupport.park();
            }
        }
        waiters.remove(Thread.currentThread());
    }

    @Override
    public void unlock() {
        if(own.compareAndSet(Thread.currentThread(), null)){
            //唤醒其他线程
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()) {
                Thread next = iterator.next();
                LockSupport.unpark(next);
            }
        }
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public Condition newCondition() {
        return null;
    }
}
