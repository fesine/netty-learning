package com.fesine.concurrent;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/13
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/13
 */
public class ShareResource {

    private String name;
    private String gender;

    //增加标志位，判断资源池是否为空，true为空
    private boolean isEmpty = true;

    //使用重入锁实现
    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();
    /**
     * 模拟生产者向共享资源对象中存储数据
     * @param name
     * @param gender
     */
     public void push(String name,String gender){
        lock.lock();
        try {
            while (!isEmpty){
                //当共享资源池不为空的时候，则等待消费者来消费
                //使用同步锁对象来调用，表示当前线程释放同步锁，只能被其他线程唤醒
                //this.wait();
                condition.await();
            }
            //开始生产
            this.name=name;
            Thread.sleep(10L);
            this.gender=gender;
            //生产结束
            isEmpty = false;
            //唤醒一个消费者来消费
            //this.notify();
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }

    }

    /**
     * 模拟消费者从共享资源中取出数据
     */
     public void popup(){
        lock.lock();
        try {
            while (isEmpty) {
                //为空则等待生产者来生产
                //使用同步锁对象来调用，表示当前线程释放同步锁，进入等待池，只能被其他线程唤醒
                //this.wait();
                condition.await();
            }
            Thread.sleep(10L);
            System.out.println(this.name + "-" + this.gender);
            isEmpty = true;
            //消费结束，唤醒一个生产者来生产
            //this.notify();
            condition.signalAll();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}
