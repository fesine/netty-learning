package com.fesine.juc.lock.aqs.cdl;

/**
 * @description: 类描述
 * @author: fesine
 * @createTime:2020/4/10
 * @update:修改内容
 * @author: fesine
 * @updateTime:2020/4/10
 */
public class MyCountDownLatchDemo {

    public static void main(String[] args) {
        MyCountDownLatch countDownLatch = new MyCountDownLatch(10);
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            new Thread(()->{
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("我是线程："+Thread.currentThread()+",我执行接口-"+finalI+"调用了。");
                countDownLatch.countDown();
            }).start();
        }
        countDownLatch.await();
        System.out.println("全部线程都执行结束了。耗时："+(System.currentTimeMillis()-start)+"毫秒");
    }
}
