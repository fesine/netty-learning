package com.fesine.juc.lock.readWriteLock;

/**
 * @description: 不用读写锁实现多线程读写
 * @author: fesine
 * @createTime:2020/4/15
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/15
 */
public class ReentrantReadWriteLockDemo1 {
    public static void main(String[] args) {
        ReentrantReadWriteLockDemo1 demo1 = new ReentrantReadWriteLockDemo1();
        new Thread(()->{
            demo1.read(Thread.currentThread());
        }).start();
        new Thread(()->{
            demo1.write(Thread.currentThread());
        }).start();
        new Thread(()->{
            demo1.read(Thread.currentThread());
        }).start();


    }

    public synchronized void read(Thread thread) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis()-start <=1){
            System.out.println(thread.getName()+" 正在进行[读]操作");
        }
        System.out.println(thread.getName() + " [读]操作完毕");
    }

    public synchronized void write(Thread thread) {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis()-start <=1){
            System.out.println(thread.getName()+" 正在进行[写]操作");
        }
        System.out.println(thread.getName() + " [写]操作完毕");
    }

}
