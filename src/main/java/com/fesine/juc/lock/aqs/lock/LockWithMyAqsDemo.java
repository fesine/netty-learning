package com.fesine.juc.lock.aqs.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * @description: 使用两个线程实现对变量i的自增操作，
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class LockWithMyAqsDemo {
    int i;

    Lock lock = new MyAqsLock();

    /**
     * 使用重入锁实现
     */
    public  void add(){
        lock.lock();
        try {
            i++;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockWithMyAqsDemo demo = new LockWithMyAqsDemo();
        for (int i = 0; i < 4; i++) {
            new Thread(()->{
                for (int j = 0; j < 10000; j++) {
                    demo.add();
                }
            }).start();
        }

        TimeUnit.SECONDS.sleep(2);
        System.out.println(demo.i);

    }
}
