package com.fesine.concurrent;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/13
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/13
 */
public class Test {
    public static void main(String[] args) {
        //创建生产者和消费者的共享对象
        ShareResource resource = new ShareResource();

        new Thread(new Producer(resource)).start();
        new Thread(new Consumer(resource)).start();
    }
}
