package com.fesine.juc.lock.aqs;

import java.util.Iterator;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;

/**
 * @description: 自定义实现aqs abstract queue synchronizer
 * @author: fesine
 * @createTime:2020/4/10
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/10
 */
// 抽象队列同步器
// state， owner， waiters
public class MyAqs {

    // acquire、 acquireShared ： 定义了资源争用的逻辑，如果没拿到，则等待。
    // tryAcquire、 tryAcquireShared ： 实际执行占用资源的操作，如何判定一个由使用者具体去实现。
    // release、 releaseShared ： 定义释放资源的逻辑，释放之后，通知后续节点进行争抢。
    // tryRelease、 tryReleaseShared： 实际执行资源释放的操作，具体的AQS使用者去实现。

    /**
     * 如何判断一个资源的拥有者
     */
    protected volatile AtomicReference<Thread> owner = new AtomicReference<>();

    /**
     * 没有获取到资源的线程 添加到队列中
     */
    public volatile Queue<Thread> waiters = new LinkedBlockingQueue<>();

    /**
     * 记录资源状态
     */
    public volatile AtomicInteger state = new AtomicInteger(0);

    /**
     * 独占式获取资源
     */
    public void acquire() {
        boolean add = true;
        //只要没获取到锁，就添加到队列
        while (!tryAcquire()) {
            if (add) {
                waiters.offer(Thread.currentThread());
                add = false;
            } else {
                //阻塞，挂起当前线程
                LockSupport.park();
            }
        }
        waiters.remove(Thread.currentThread());
    }

    /**
     * 独占式释放资源
     * 定义了释放资源后要做的动作
     */
    public void release() {
        if (tryRelease()) {
            //唤醒其他线程
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()) {
                Thread next = iterator.next();
                LockSupport.unpark(next);
            }
        }
    }

    /**
     * 共享式获取资源
     */
    public void acquireShared() {
        boolean add = true;
        //只要没获取到锁，就添加到队列
        while (tryAcquireShared() < 0) {
            if (add) {
                waiters.offer(Thread.currentThread());
                add = false;
            } else {
                //阻塞，挂起当前线程
                LockSupport.park();
            }
        }
        waiters.remove(Thread.currentThread());
    }

    /**
     * 共享式释放资源
     * 定义了释放资源后要做的动作
     */
    public void releaseShared() {
        if (tryReleaseShared()) {
            //唤醒其他线程
            Iterator<Thread> iterator = waiters.iterator();
            while (iterator.hasNext()) {
                Thread next = iterator.next();
                LockSupport.unpark(next);
            }
        }
    }


    /**
     * 独占式，交给使用者实现，模板方法
     * @return
     */
    public boolean tryAcquire(){
        throw new UnsupportedOperationException();
    }

    public boolean tryRelease() {
        throw new UnsupportedOperationException();
    }

    /**
     * 共享式，交给使用者实现，模板方法
     * @return
     */
    public int tryAcquireShared(){
        throw new UnsupportedOperationException();
    }

    public boolean tryReleaseShared() {
        throw new UnsupportedOperationException();
    }


    public void setState(AtomicInteger state) {
        this.state = state;
    }

    public AtomicInteger getState() {
        return state;
    }
}
