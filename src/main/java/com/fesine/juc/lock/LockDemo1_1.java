package com.fesine.juc.lock;

import java.util.concurrent.TimeUnit;

/**
 * @description: 使用两个线程实现对变量i的自增操作
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class LockDemo1_1 {
    /**
     * 比较消耗性能方法是直接加锁synchronized
     */
    private  int i;

    public  void add(){
        synchronized (this){
            i++;
        }
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo1_1 demo = new LockDemo1_1();
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
