package com.fesine.juc.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: condition实现队列线程安全
 * @author: fesine
 * @createTime:2020/4/14
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/14
 */
public class QueueDemo {
    final Lock lock = new ReentrantLock();
    /* 指定条件的等待 - 等待有空位 */
    final Condition notFull = lock.newCondition();

    /* 指定条件的等待 - 等待不为空 */
    final Condition notEmpty = lock.newCondition();

    /* 定义存放数据的数组 */
    final Object[] items = new Object[100];
    int putptr,takeptr,count;

    /**
     * 写入数据的线程，写进数据
     * @param x
     */
    public void put(Object x) throws InterruptedException {
        lock.lock();
        try {
            //数据写满了,写入线程进入阻塞
            while (count == items.length){
                notFull.await();
            }
            items[putptr] = x;
            if(++putptr == items.length){
                putptr = 0;
            }
            ++count;
            //唤醒指定的读取线程
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    /**
     * 读取数据
     * @return
     * @throws InterruptedException
     */
    public Object take() throws InterruptedException {
        lock.lock();
        try {
            //当数据为空时，读取线程进入阻塞
            while (count == 0){
                notEmpty.await();
            }
            Object x = items[takeptr];
            if(++takeptr == items.length){
                takeptr = 0;
            }
            --count;
            //通知写入线程写入数据
            notFull.signal();
            return x;

        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        QueueDemo demo = new QueueDemo();
        for (int i = 0; i < 200; i++) {
            new Thread(() -> {
                try {
                    demo.put(new Object());
                    System.out.println("---->生产数据成功，items中有元素个数："+ demo.count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ).start();
        }
        for (int i = 0; i < 200; i++) {
            new Thread(() -> {
                try {
                    demo.take();
                    System.out.println("---->消费数据成功，items中有元素个数："+ demo.count);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ).start();
        }
    }


}
