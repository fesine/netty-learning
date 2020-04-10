package com.fesine.juc.lock.aqs.lock;

import com.fesine.juc.lock.aqs.MyAqs;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * @description: 自己实现重入锁
 * @author: fesine
 * @createTime:2020/4/9
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/9
 */
public class MyAqsLock implements Lock {

    private MyAqs aqs = new MyAqs(){
        @Override
        public boolean tryAcquire() {
            return owner.compareAndSet(null, Thread.currentThread());
        }

        @Override
        public boolean tryRelease() {
            return owner.compareAndSet(Thread.currentThread(),null);
        }
    };


    @Override
    public boolean tryLock() {
        return aqs.tryAcquire();
    }

    @Override
    public void lock() {
        aqs.acquire();
    }

    @Override
    public void unlock() {
        aqs.release();
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
