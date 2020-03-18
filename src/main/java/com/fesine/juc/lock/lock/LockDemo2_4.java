package com.fesine.juc.lock.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description: 使用两个线程实现对变量i的自增操作，
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class LockDemo2_4 {
    /**
     * 使用并发原子类
     * 其内部也是使用unsafe的cas实现
     */
    AtomicInteger i = new AtomicInteger(0);


    public  void add(){
        i.incrementAndGet();
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo2_4 demo = new LockDemo2_4();
        for (int i = 0; i < 2; i++) {
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
