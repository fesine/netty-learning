package com.fesine.semaphore;

import java.util.concurrent.TimeUnit;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2019/12/13
 * @update:修改内容
 * @author: fesine
 * @updateTime:2019/12/13
 */
public class Join {

    public static void main(String[] args) throws InterruptedException {
        Thread previous = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(new DemoJoin(previous), String.valueOf(i));
            thread.start();
            previous=thread;
        }
        TimeUnit.SECONDS.sleep(5);
        System.out.println(Thread.currentThread().getName() + " terminate.");
    }


    static class DemoJoin implements Runnable{

        private Thread thread;

        public DemoJoin(Thread thread) {
            this.thread = thread;
        }

        @Override
        public void run() {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName()+" terminate1.");

        }
    }
}
