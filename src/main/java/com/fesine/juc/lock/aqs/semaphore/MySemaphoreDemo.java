package com.fesine.juc.lock.aqs.semaphore;

import java.util.Random;

/**
 * @description: 自定义信号量实现demo
 * @author: fesine
 * @createTime:2020/4/10
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/10
 */
public class MySemaphoreDemo {

    public static void main(String[] args) {

        MySemaphoreDemo mySemaphoreDemo = new MySemaphoreDemo();
        MySemaphore semaphoreTest = new MySemaphore(5);
        for (int i = 0; i < 10; i++) {
            String no = i+"";
            new Thread(()->{
                //获取令牌
                semaphoreTest.acquire();
                //执行业务
                mySemaphoreDemo.service(no);
                //释放令牌
                semaphoreTest.release();

            }).start();
        }

    }

    private void service(String n){
        System.out.println("----->线程编号："+n+"进入，。。。");
        try {
            Thread.sleep(new Random().nextInt(3000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("------>线程离开，线程编号："+n);
    }
}
