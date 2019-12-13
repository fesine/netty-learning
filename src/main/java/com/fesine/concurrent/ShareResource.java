package com.fesine.concurrent;

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

    /**
     * 模拟生产者向共享资源对象中存储数据
     * @param name
     * @param gender
     */
    synchronized public void push(String name,String gender){
        try {
            while (!isEmpty){
                //当共享资源池不为空的时候，则等待消费者来消费
                //使用同步锁对象来调用，表示当前线程释放同步锁，只能被其他线程唤醒
                this.wait();
            }
            //开始生产
            this.name=name;
            Thread.sleep(10L);
            this.gender=gender;
            //生产结束
            isEmpty = false;
            //唤醒一个消费者来消费
            this.notify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟消费者从共享资源中取出数据
     */
    synchronized public void popup(){
        try {
            while (isEmpty) {
                //为空则等待生产者来生产
                //使用同步锁对象来调用，表示当前线程释放同步锁，进入等待池，只能被其他线程唤醒
                this.wait();
            }
            Thread.sleep(10L);
            isEmpty = true;
            //消费结束，唤醒一个生产者来生产
            this.notify();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(this.name+"-"+this.gender);
    }
}
