package com.fesine.juc.lock.readWriteLock;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @description: 使用读写锁实现多线程读写
 * @author: fesine
 * @createTime:2020/4/15
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/15
 */
public class ReentrantReadWriteLockDemo2 {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    public static void main(String[] args) {
        ReentrantReadWriteLockDemo2 demo1 = new ReentrantReadWriteLockDemo2();
        new Thread(()->{
            demo1.read(Thread.currentThread());
        }).start();
        new Thread(()->{
            demo1.read(Thread.currentThread());
        }).start();
        new Thread(()->{
            demo1.write(Thread.currentThread());
        }).start();


    }

    public void read(Thread thread) {
        lock.readLock().lock();
        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis()-start <=1){
                System.out.println(thread.getName()+" 正在进行[读]操作");
            }
            System.out.println(thread.getName() + " [读]操作完毕");
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(Thread thread) {
        lock.writeLock().lock();
        try {
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start <= 1) {
                System.out.println(thread.getName() + " 正在进行[写]操作");
            }
            System.out.println(thread.getName() + " [写]操作完毕");
        } finally {
            lock.writeLock().unlock();
        }
    }

}
