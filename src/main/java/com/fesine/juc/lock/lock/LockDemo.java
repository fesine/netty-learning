package com.fesine.juc.lock.lock;

import java.util.concurrent.TimeUnit;

/**
 * @description: 使用两个线程实现对变量i的自增操作，此处无法得到正确的结果，变量因为cpu缓存无法保证数据一致
 * @author: fesine
 * @createTime:2020/3/18
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/3/18
 */
public class LockDemo {

    private int i;

    public  void add(){
        i++;
    }

    public static void main(String[] args) throws InterruptedException {
        LockDemo demo = new LockDemo();
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
