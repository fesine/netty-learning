package com.fesine.juc.lock.aqs.cyclicbarrier;

import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @description: 栅栏示例
 * @author: fesine
 * @createTime:2020/4/11
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/11
 */
public class CyclicBarrierTest {



    public static void main(String[] args) {
        LinkedBlockingQueue<String> sqls = new LinkedBlockingQueue<>();

        //创建一个栅栏，只要有4个await就触发barrierAction线程执行
        CyclicBarrier barrier = new CyclicBarrier(4,()->{
            System.out.println("有4个线程执行了，" + Thread.currentThread());
            for (int i = 0;i<4;i++){
                System.out.println(sqls.poll());
            }
        });

        for (int i = 0; i < 10; i++) {
            new Thread(()->{
                try {
                    sqls.add("data-" + Thread.currentThread());
                    Thread.sleep(1000);
                    barrier.await();
                    System.out.println(Thread.currentThread()+"插入完毕");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }

    }


}
